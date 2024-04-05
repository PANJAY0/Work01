package test0326;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlingExchangeRate extends Thread {
	
	private boolean crawling = true;
	
	@Override
	public void run() {

			while (crawling) {
	            Connection conn = null;
	            PreparedStatement ps = null;
	            try {
	            	conn = JDBCUtils.getConnection();
	            	
	            	//將網址內的數據全部取出,並過濾數據後存到list中
	                String net = "https://rate.bot.com.tw/xrt/all/day";
	                String str = webCrawler(net);

	                String regex1 = "(cell\">\\d{1,2}\\.?\\d{1,5})|(cell\">-)";

	                Pattern p = Pattern.compile(regex1);

	                Matcher m = p.matcher(str);
	                
	                ArrayList list = new ArrayList();
	                while (m.find()) {
	                    String r = m.group().replace("cell\">", "");
	                    list.add(r);
	                }
	                
	                //將過濾完的數據家進MySQL數據庫
	                String sql = "INSERT INTO bank(`currency`, `cashBuy`, `cashSell`, `spotBuy`, `spotSell`, `recordTime`)VALUES('USD', ?, ?, ?, ?, NOW()),('HKD', ?, ?, ?, ?, NOW()),('GBP', ?, ?, ?, ?, NOW()),('AUD', ?, ?, ?, ?, NOW()),('CAD', ?, ?, ?, ?, NOW()),('SGD', ?, ?, ?, ?, NOW()),('CHF', ?, ?, ?, ?, NOW()),('JPY', ?, ?, ?, ?, NOW()),('ZAR', ?, ?, ?, ?, NOW()),('SEK', ?, ?, ?, ?, NOW()),('NZD', ?, ?, ?, ?, NOW()),('THB', ?, ?, ?, ?, NOW()),('PHP', ?, ?, ?, ?, NOW()),('IDR', ?, ?, ?, ?, NOW()),('EUR', ?, ?, ?, ?, NOW()),('KRW', ?, ?, ?, ?, NOW()),('VND', ?, ?, ?, ?, NOW()),('MYR', ?, ?, ?, ?, NOW()),('CNY', ?, ?, ?, ?, NOW());";
	                ps = conn.prepareStatement(sql);

	                for (int i = 0; i < list.size(); i++) {
	                    String element = (String) list.get(i);
	                    if (element.equals("-")) {
	                        ps.setObject(i + 1, 0);
	                    } else {
	                        double value = Double.parseDouble(element);
	                        ps.setObject(i + 1, value);
	                    }
	                }

	                ps.execute();
	            } catch (Exception e) {
	                e.printStackTrace();
	            } finally {
	                try {
	                    if (ps != null) {
	                        ps.close();
	                    }
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	                try {
	                    if(conn != null){
	                        conn.close();
	                    }
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            
//	            線程休息時間
				try {
					Thread.sleep(60 * 60 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
	        }
    }
	
	public void EndCrawling() {
		crawling = false;
	}
	
	
	//將net網址中全部的數據轉成字串
	public static String webCrawler(String net) throws IOException {

        StringBuilder sb = new StringBuilder();

        URL url = new URL(net);

        URLConnection conn = url.openConnection();

        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
        int ch;
        while ((ch = isr.read()) != -1) {
            sb.append((char) ch);
        }
        isr.close();
        return sb.toString();
    }
	
	
	public void setCrawling(boolean crawling) {
		this.crawling = crawling;
	}
}
