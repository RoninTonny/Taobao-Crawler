import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import sun.awt.resources.awt;

public class CrawlerGUI extends JFrame implements ActionListener {
	
	public static final int SLAVE_DISCONNECT = 0;
	public static final int SLAVE_CONNECTING = 1;
	public static final int SLAVE_REQUESTING = 2;
	public static final int SLAVE_RECIEVEING = 3;
	public static final int SLAVE_CRAWLING = 4;
	public static final int SLAVE_WAITING = 5;
	public static final int SLAVE_CONNECTED = 6;
	
	public static boolean updateable = true;
	public static boolean isStarted = false;
	
	private JButton startBtn;
	private JLabel ipLabel;
	private JTextField ipTextField;
	private JLabel threadLabel;
	private JTextField threadTextField;
	private JLabel spaceLabel;
	private JPanel inputPanel;
	private JLabel statusLabel;
	private JPanel statusPanel;
	private DefaultListModel<String> jListModel;
	private DefaultTableModel jTableModel;
	private JList<String> infoList;
	private JScrollPane scrollPanel;
	private JPanel northPanel;
	private String infoStr;
	private JTable jTable;
	
	public CrawlerGUI() {
		// TODO Auto-generated constructor stub
		super("分布式爬虫-从机");
		initGF();
		setBounds(500, 500, 1040, 640);
		startBtn = new JButton("连接主机");
		startBtn.setBackground(Color.WHITE);
		startBtn.addActionListener(this);    
		ipLabel = new JLabel("  主机IP：");
		ipTextField = new JTextField(12);
		ipTextField.setText("localhost");
		threadLabel = new JLabel("  线程数：");
		threadTextField = new JTextField(4);
		spaceLabel = new JLabel("  |  ");
		spaceLabel.setFont(new Font("宋体",Font.PLAIN,40));
		inputPanel = new JPanel(new FlowLayout());
		inputPanel.add(startBtn);
		inputPanel.add(ipLabel);
		inputPanel.add(ipTextField);
		inputPanel.add(threadLabel);
		inputPanel.add(threadTextField);
		inputPanel.add(spaceLabel);
		northPanel = new JPanel(new GridLayout(1, 1));
		northPanel.add(inputPanel);
		infoStr = "链接已断开";
		statusLabel = new JLabel("                  " + infoStr + "                   ");
		
		statusLabel.setFont(new Font("宋体", 1, 20));
		
		statusPanel = new JPanel(new FlowLayout());
		statusPanel.setBackground(Color.RED);
		statusPanel.add(statusLabel);
		inputPanel.add(statusPanel);
		add(northPanel, BorderLayout.NORTH);
		String[] tableHead = {"商品名称", "店铺名称", "价格", "销量", "好评", "中评", "差评", "追评",};
		jTableModel = new DefaultTableModel(null, tableHead) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		

		jTable = new JTable(jTableModel);
		this.setTableSelf(jTable, jTableModel);
//		jTable.setTableHeader(new JTableHeader(new Ta));
		
		jListModel = new DefaultListModel<String>();
		infoList = new JList<String>(jListModel);
		infoList.setCellRenderer(new DefaultListCellRenderer() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.red);
                g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        });
		infoList.setFont(new Font("微软雅黑",Font.PLAIN,12));
		scrollPanel = new JScrollPane();
		scrollPanel.setViewportView(jTable);
		add(scrollPanel, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}

	private void setTableSelf(JTable jTable2, DefaultTableModel jTableModel2) {
		// TODO Auto-generated method stub
		jTable2.setRowHeight(25);
		jTable2.getColumnModel().getColumn(0).setPreferredWidth(400);
		jTable2.getColumnModel().getColumn(1).setPreferredWidth(150);
		jTable2.getColumnModel().getColumn(2).setPreferredWidth(75);
		jTable2.getColumnModel().getColumn(3).setPreferredWidth(75);
		jTable2.getColumnModel().getColumn(4).setPreferredWidth(75);
		jTable2.getColumnModel().getColumn(5).setPreferredWidth(75);
		jTable2.getColumnModel().getColumn(6).setPreferredWidth(75);
		jTable2.getColumnModel().getColumn(7).setPreferredWidth(75);
		DefaultTableCellRenderer tcf = new DefaultTableCellRenderer();
		tcf.setHorizontalAlignment(JLabel.CENTER);//居中显示  
		for (int i = 2; i < 8; i++ )
		jTable2.setDefaultRenderer(jTable2.getColumnClass(i), tcf);
		//jTable2.setBackground(new java.awt.Color(175,238,238));//设置背景颜色
		jTable2.getTableHeader().setBackground(new java.awt.Color(192,192,192));
		jTable2.getTableHeader().setForeground(Color.BLACK);
		jTable2.getTableHeader().setFont(new Font("微软雅黑 Light", 1, 15));
		jTable2.setFont(new Font("微软雅黑", 0, 14));
	}
	
	private void initGF() {
		FontUIResource fontUIResource = new FontUIResource(new Font("宋体",Font.PLAIN, 14));
		for(Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements();/**/) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if(value instanceof FontUIResource) {
				UIManager.put(key, fontUIResource);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == startBtn) {
			if(ipTextField.getText().isEmpty() || threadTextField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this,"错误");
			}
			else {
				UrlReceive urlReceive = null;
				if(((JButton) e.getSource()).getText().equals("连接主机")) {
					startBtn.setText("暂停抓取");
					startBtn.setForeground(new Color(175,238,238));
					startBtn.setBackground(new Color(204, 0, 0));
					updateable = true;
					if(!isStarted) {
						urlReceive = new UrlReceive(ipTextField.getText());
						urlReceive.setViewport(this);
						urlReceive.setThreadCount(Integer.parseInt(threadTextField.getText()));
						urlReceive.start();
						isStarted = true;
					}
				}
				else {
					updateable = false;
					startBtn.setText("连接主机");
					startBtn.setForeground(Color.BLACK);
					startBtn.setBackground(Color.WHITE);
				}
			}
		}
	}
	
	public int getThreadCount() {
		if(threadTextField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this,"错误");
			return 0;
		}
		else{
			return Integer.parseInt(threadTextField.getText());
		}
	}
	
	public String getHostIP() {
		if(ipTextField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this,"错误");
			return null;
		}
		else{
			return ipTextField.getText();
		}
	}
	
	public void updateInfoList(String info) {
		if(updateable) {
			jListModel.addElement(info);
			//scrollPanel.getVerticalScrollBar().setValue(scrollPanel.getVerticalScrollBar().getMaximum());
			jTable.setAutoscrolls(true);
		}
	}
	
	public void updateInfoTable(String[] info) {
		if(updateable) {
			jTableModel.addRow(info);
			int x = scrollPanel.getVerticalScrollBar().getMaximum();
			scrollPanel.getVerticalScrollBar().setValue(x);
			jTable.repaint();
		}
	}
	
	public synchronized void updateStatus(int SLAVE_STATUS) {
		if(updateable) {
			if(SLAVE_STATUS == SLAVE_DISCONNECT) {
				statusPanel.setBackground(Color.RED);
				infoStr = "链接已断开";
				statusLabel.setText("                  " + infoStr + "                   ");
			}
			if(SLAVE_STATUS == SLAVE_CONNECTING) {
				infoStr = " 正在链接 ";
				statusPanel.setBackground(Color.ORANGE);
				statusLabel.setText("                  " + infoStr + "                   ");
			}
			if(SLAVE_STATUS == SLAVE_CONNECTED) {
				infoStr = " 已连接  ";
				statusPanel.setBackground(Color.GREEN);
				statusLabel.setText("                  " + infoStr + "                   ");
			}
			if(SLAVE_STATUS == SLAVE_RECIEVEING) {
				infoStr = " 正在接收 ";
				statusPanel.setBackground(Color.ORANGE);
				statusLabel.setText("                  " + infoStr + "                   ");
			}
			if(SLAVE_STATUS == SLAVE_REQUESTING) {
				infoStr = " 正在请求 ";
				statusPanel.setBackground(Color.ORANGE);
				statusLabel.setText("                  " + infoStr + "                   ");
			}
			if(SLAVE_STATUS == SLAVE_CRAWLING) {
				infoStr = " 正在抓取 ";
				statusPanel.setBackground(Color.GREEN);
				statusLabel.setText("                  " + infoStr + "                   ");
			}
			if(SLAVE_STATUS == SLAVE_WAITING) {
				infoStr = " 正在等待 ";
				statusPanel.setBackground(Color.ORANGE);
				statusLabel.setText("                  " + infoStr + "                   ");
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new CrawlerGUI();
	}

}
