package test0326;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

public class UpdateDialog extends JDialog {

	private final int WIDTH = 400;
	private final int HEIGHT = 300;
	private int selectedRow;
	private JTextField currencyField;
	private JTextField cashBuyField;
	private JTextField cashSellField;
	private JTextField spotBuyField;
	private JTextField spotSellField;
	
	public UpdateDialog(JFrame jf, String title, boolean isMode, int selectedRow) {
		super(jf, title, isMode);
		this.selectedRow = selectedRow ;
		
		//設置視窗置中
		int screenWidthSize = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeightSize = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setBounds((screenWidthSize - WIDTH)/2, (screenHeightSize - HEIGHT)/2, WIDTH, HEIGHT);
		
		//輸入框_貨幣種類
		Box currencyBox = Box.createHorizontalBox();
		JLabel currencyLabel = new JLabel("貨幣種類:");
		this.currencyField = new JTextField(10);
		currencyField.setEditable(false);//此輸入框無法變更
		currencyBox.add(currencyLabel);
		currencyBox.add(currencyField);
		
		//輸入框_現金買入
		Box cashBuyBox = Box.createHorizontalBox();
		JLabel cashBuyLabel = new JLabel("現金買入:");
		this.cashBuyField = new JTextField(10);
		cashBuyBox.add(cashBuyLabel);
		cashBuyBox.add(cashBuyField);
		
		//輸入框_現金賣出
		Box cashSellBox = Box.createHorizontalBox();
		JLabel cashSellLabel = new JLabel("現金賣出:");
		this.cashSellField = new JTextField(10);
		cashSellBox.add(cashSellLabel);
		cashSellBox.add(cashSellField);
		
		//輸入框_即期買入
		Box spotBuyBox = Box.createHorizontalBox();
		JLabel spotBuyLabel = new JLabel("即期買入:");
		this.spotBuyField = new JTextField(10);
		spotBuyBox.add(spotBuyLabel);
		spotBuyBox.add(spotBuyField);
		
		//輸入框_即期賣出
		Box spotSellBox = Box.createHorizontalBox();
		JLabel spotSellLabel = new JLabel("即期賣出:");
		this.spotSellField = new JTextField(10);
		spotSellBox.add(spotSellLabel);
		spotSellBox.add(spotSellField);
		
		//按鈕_添加
		Box btnBox = Box.createHorizontalBox();
		JButton updateBtn = new JButton("修改");
		btnBox.add(updateBtn);
		updateBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String currency = currencyField.getText().trim();
				String cashBuyS	= cashBuyField.getText().trim();
				String cashSellS = cashSellField.getText().trim();
				String spotBuyS = spotBuyField.getText().trim();
				String spotSellS = spotSellField.getText().trim();
				
				Double cashBuy = Double.parseDouble(cashBuyS);
				Double cashSell = Double.parseDouble(cashSellS);
				Double spotBuy = Double.parseDouble(spotBuyS);
				Double spotSell = Double.parseDouble(spotSellS);
				
				String sql = "UPDATE test.bank SET cashBuy = "+ cashBuy +",cashSell = "+ cashSell +" ,spotBuy = "+ spotBuy +",spotSell = "+ spotSell + ",recordTime = NOW()" + "WHERE rowNum = "+ selectedRow ;
				JDBCUtils jdbc = new JDBCUtils();
				jdbc.update(sql, null);
				
				JOptionPane.showMessageDialog(jf, "修改完成!!");
			}
			
		});
		
		//將輸入框與按鈕添加進垂直BOX
		Box vBox = Box.createVerticalBox();
		vBox.add(Box.createVerticalStrut(20));
		vBox.add(currencyBox);
		vBox.add(Box.createVerticalStrut(20));
		vBox.add(cashBuyBox);
		vBox.add(Box.createVerticalStrut(20));
		vBox.add(cashSellBox);
		vBox.add(Box.createVerticalStrut(20));
		vBox.add(spotBuyBox);
		vBox.add(Box.createVerticalStrut(20));
		vBox.add(spotSellBox);
		vBox.add(Box.createVerticalStrut(20));
		vBox.add(btnBox);
		
		
		//將vBox放進hBox中,降低vBox的寬度
		Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(50));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(50));
		
		this.add(hBox);
		
		requestData();
		
	}
	
	
	//將選中的數據讀取到到修改的對話框
	public void requestData() {
		JDBCUtils jdbc = new JDBCUtils();
		String sql = "SELECT * FROM test.bank WHERE rowNum = ? ";
		
		List<Currency> list = jdbc.getInstance(Currency.class, sql, selectedRow);
		
		
		this.currencyField.setText(list.get(0).getCurrency().toString());
		this.cashBuyField.setText(list.get(0).getCashBuy().toString());
		this.cashSellField.setText(list.get(0).getCashSell().toString());
		this.spotBuyField.setText(list.get(0).getSpotBuy().toString());
		this.spotSellField.setText(list.get(0).getSpotSell().toString());
	}
}
