package test0326;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreeSelectionModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

public class JDBCUtils {
	
	
	//獲取數據庫連接
	public static Connection getConnection() throws Exception {
		
		
		String user = "root";
        String password = "jay66347";
        String url = "jdbc:mysql://localhost:3306/test";
        String driverClass = "com.mysql.jdbc.Driver";
        
        //加載驅動
        Class.forName(driverClass);

        Connection conn = DriverManager.getConnection(url,user,password);

        return conn;
		
	}
	
	
	//關閉連接數據庫所開啟的流
	public static void closeResource(Connection conn,PreparedStatement ps,ResultSet rs) {
		
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if(ps != null)
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	//獲得sql語句搜尋出來的結果集,clazz:結果集在Java中相對應的類,sql:欲搜尋的語句,args:填充佔位符的數據
	public <T> List<T> getInstance(Class<T> clazz, String sql, Object... args) {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();

			ps = conn.prepareStatement(sql);
			
			//填充佔位符
			if(args != null) {
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i + 1, args[i]);
				}
			}
			rs = ps.executeQuery();
			
			//取的結果集的元數據
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			List<T> list = new ArrayList<>();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				T t = clazz.newInstance();
				for (int i = 0; i < columnCount; i++) {

					Object columnVal = rs.getObject(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1);
					
					
					if (columnVal instanceof BigDecimal) {
			            double doubleVal = ((BigDecimal) columnVal).doubleValue();
			            Field field = clazz.getDeclaredField(columnLabel);
			            field.setAccessible(true);
			            field.set(t, doubleVal);
					}else {
						Field field = clazz.getDeclaredField(columnLabel);
						field.setAccessible(true);
						field.set(t, columnVal);
					}
				}
				list.add(t);

			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResource(conn, ps, rs);
		}

		return null;

	}

	//修改數據庫中的數據
	public void update(String sql,Object ...args){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			if (args != null) {
				for(int i = 0;i < args.length;i++){
					ps.setObject(i + 1, args[i]);
				}
			}
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.closeResource(conn, ps, null);
			
		}
	}
	
	//將sql語句執行後獲取的結果集放進JTable中
	public JTable getTableData(String sql, String currency) {
		
		List<Currency> list;
		if(currency == null) {
			list = getInstance(Currency.class, sql, null);
		}else {
			list = getInstance(Currency.class, sql, currency);
		}
		String[] titleName = {"編號", "幣別", "本行買入(現金)", "本行賣出(現金)", "本行買入(即期)", "本行賣出(即期)", "紀錄時間"};
		Vector<String> columnTitle = new Vector<>();
		for(String s : titleName) {
			columnTitle.add(s);
		}
		
		Vector tableData = new Vector();
		for (Currency currencyData : list) {
		    Vector<Object> row = new Vector<>();
		    row.add(currencyData.getRowNum());
		    row.add(currencyData.getCurrency());
		    row.add(currencyData.getCashBuy());
		    row.add(currencyData.getCashSell());
		    row.add(currencyData.getSpotBuy());
		    row.add(currencyData.getSpotSell());
		    row.add(currencyData.getRecordTime());
		    tableData.add(row);
		}
		
		DefaultTableModel model = new DefaultTableModel(tableData, columnTitle);
		JTable table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		return table;
	}

}

