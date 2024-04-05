package test0326;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class AddDialog extends JDialog {
	
	private final int WIDTH = 400;
	private final int HEIGHT = 300;
	
	public AddDialog(JFrame jf, String title, boolean isMode) {
		super(jf, title, isMode);
		
		
		int screenWidthSize = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeightSize = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		this.setBounds((screenWidthSize - WIDTH)/2, (screenHeightSize - HEIGHT)/2, WIDTH, HEIGHT);
		
		//輸入框_貨幣種類
		Box currencyBox = Box.createHorizontalBox();
		JLabel currencyLabel = new JLabel("貨幣種類:");
		JTextField currencyField = new JTextField(10);
		currencyBox.add(currencyLabel);
		currencyBox.add(currencyField);
		
		//輸入框_現金買入
		Box cashBuyBox = Box.createHorizontalBox();
		JLabel cashBuyLabel = new JLabel("現金買入:");
		JTextField cashBuyField = new JTextField(10);
		cashBuyBox.add(cashBuyLabel);
		cashBuyBox.add(cashBuyField);
		
		//輸入框_現金賣出
		Box cashSellBox = Box.createHorizontalBox();
		JLabel cashSellLabel = new JLabel("現金賣出:");
		JTextField cashSellField = new JTextField(10);
		cashSellBox.add(cashSellLabel);
		cashSellBox.add(cashSellField);
		
		//輸入框_即期買入
		Box spotBuyBox = Box.createHorizontalBox();
		JLabel spotBuyLabel = new JLabel("即期買入:");
		JTextField spotBuyField = new JTextField(10);
		spotBuyBox.add(spotBuyLabel);
		spotBuyBox.add(spotBuyField);
		
		//輸入框_即期賣出
		Box spotSellBox = Box.createHorizontalBox();
		JLabel spotSellLabel = new JLabel("即期賣出:");
		JTextField spotSellField = new JTextField(10);
		spotSellBox.add(spotSellLabel);
		spotSellBox.add(spotSellField);
		
		//按鈕_添加
		Box btnBox = Box.createHorizontalBox();
		JButton addBtn = new JButton("添加");
		btnBox.add(addBtn);
		addBtn.addActionListener(new ActionListener() {

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
				
				String sql = "INSERT INTO test.bank(currency, cashBuy, cashSell, spotBuy, spotSell, recordTime) VALUES ('"+currency+"',"+cashBuy+","+cashSell+","+spotBuy+","+spotSell+",Now())";
				JDBCUtils jdbc = new JDBCUtils();
				jdbc.update(sql, null);
				
				
				JOptionPane.showMessageDialog(jf, "添加成功!!");
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
		
		Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(50));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(50));
		
		this.add(hBox);
		
	}
	

}
