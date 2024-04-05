package test0326;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class MyTest {
	
	private final int WIDTH = 1000;
	private final int HEIGHT = 600;
	private JFrame jf ;
	private JSplitPane sp ;
	private JDBCUtils jdbc;
	private JTable table ;
	private int selectedRow;

	  
    public void init() {
    	
    	jf = new JFrame("測試");
    	sp = new JSplitPane();
    	jdbc = new JDBCUtils();
    	
    	//初始顯示_START
		
    	String sqlAll = "SELECT * FROM test.bank";
		table = jdbc.getTableData(sqlAll, null);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int result = table.getSelectedRow();
                    
                    if (result != -1) {
                        selectedRow = (int)table.getValueAt(result, 0); 
                    }
                }
            }

        });
		
		
		Box rightBox = Box.createVerticalBox();
		rightBox.add(new JScrollPane(table));
		sp.setRightComponent(rightBox);
		
		//初始顯示_END
		
		
		
		
    	//上層選項區_START
    	JMenuBar menuBar = new JMenuBar();
    	
    	JMenu options = new JMenu("選項");
    	JMenuItem startCrawling = new JMenuItem("開始抓取");
    	JMenuItem endCrawling = new JMenuItem("結束抓取");
    	JMenuItem addData = new JMenuItem("添加");
    	JMenuItem updateData = new JMenuItem("修改");
    	JMenuItem deleteData = new JMenuItem("刪除");
    	
    	options.add(startCrawling);
    	options.add(endCrawling);
    	options.add(addData);
    	options.add(updateData);
    	options.add(deleteData);
    	menuBar.add(options);
    	
    	jf.setJMenuBar(menuBar);
    	
    	
    	CrawlingExchangeRate ceg = new CrawlingExchangeRate();
    	startCrawling.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ceg.setCrawling(true);
				ceg.start();
				
			}
    		
    	});
    	endCrawling.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ceg.EndCrawling();
			}
    		
    	});
    	
    	addData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new AddDialog(jf, "添加", true).setVisible(true);
			}
    		
    	});
    	
    	updateData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String sql = "SELECT * FROM test.bank WHERE rowNum = ? ";
				List<Currency> list = jdbc.getInstance(Currency.class, sql, selectedRow);
				int getRowNum = list.get(0).getRowNum();
				
				
				if(getRowNum < 1) {
					JOptionPane.showMessageDialog(jf, "請選擇要修改的數據!!");
					return;
				}
				new UpdateDialog(jf, "修改", true, getRowNum).setVisible(true);
				
				
			}
			
		});
    	
    	deleteData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				int result = JOptionPane.showConfirmDialog(jf, "確認要刪除此數據嗎?", "確認刪除", JOptionPane.YES_NO_OPTION);
				if(result != JOptionPane.YES_OPTION) {
					return;
				}
				
				String sql = "SELECT * FROM test.bank WHERE rowNum = ? ";
				List<Currency> list = jdbc.getInstance(Currency.class, sql, selectedRow);
				int getRowNum = list.get(0).getRowNum();
				
				System.out.println(getRowNum);
				String deletesql = "DELETE FROM test.bank WHERE rowNum = ?";
				jdbc.update(deletesql, getRowNum);

			}
    		
    	});
    	
    	//上層選項區_END
    	
    	//左_樹狀區_START
		DefaultMutableTreeNode currency = new DefaultMutableTreeNode("幣別");
		String[] s  = {"美金", "港幣", "英鎊", "澳幣", "加拿大幣", "新加坡幣", "瑞士法郎", "日圓", "南非幣", "瑞典幣","紐元", "泰幣", "菲國比索", "印尼幣", "歐元", "韓元", "越南盾", "馬來幣", "人民幣"};
		
		for(String a : s) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(a);
			currency.add(node);
		}
		JTree tree = new JTree(currency);
				//設置只能單選
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
				//設置左邊選項後的顯示動作
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				
				Object lastPathComponent = e.getNewLeadSelectionPath().getLastPathComponent();
				String sql = "SELECT * FROM test.bank WHERE currency = ?";
				
				if("幣別".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sqlAll, null);
					sp.setRightComponent(new JScrollPane(table));
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					
				}
				if("美金".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "USD");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("港幣".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "HKD");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("英鎊".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "GBP");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("澳幣".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "AUD");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("加拿大幣".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "CAD");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("新加坡幣".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "SGD");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("瑞士法郎".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "CHF");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("日圓".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "JPY");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("南非幣".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "ZAR");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("瑞典幣".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "SEK");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("紐元".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "NZD");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("泰幣".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "THB");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("菲國比索".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "PHP");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
				            int result = table.getSelectedRow();
				            
				            if (result != -1) {
				                selectedRow = (int)table.getValueAt(result, 0); 
				            }
				        }
					}
					
				});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("印尼幣".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "IDR");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("歐元".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "EUR");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("韓元".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "KRW");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("越南盾".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "VND");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("馬來幣".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "MYR");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				if("人民幣".equals(lastPathComponent.toString())) {
					table = jdbc.getTableData(sql, "CNY");
					table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							if (!e.getValueIsAdjusting()) {
					            int result = table.getSelectedRow();
					            
					            if (result != -1) {
					                selectedRow = (int)table.getValueAt(result, 0); 
					            }
					        }
						}
						
					});
					sp.setRightComponent(new JScrollPane(table));
				}
				
			}
			
		});
		//左_樹狀區_END
		
		
		
		//視窗_基本設置_START
		sp.setContinuousLayout(true);
        sp.setDividerLocation(100);
        sp.setDividerSize(5);
        sp.setLeftComponent(tree);
        
        		//設置視窗置中
        int screenWidthSize = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeightSize = Toolkit.getDefaultToolkit().getScreenSize().height;
		jf.setLocation((screenWidthSize - WIDTH)/2, (screenHeightSize - HEIGHT)/2);
		
		jf.add(sp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(WIDTH, HEIGHT);
        jf.setVisible(true);
        //視窗_基本設置_END
        
		
    }

    public static void main(String[] args) {

    	new MyTest().init();
    	
    }
}
