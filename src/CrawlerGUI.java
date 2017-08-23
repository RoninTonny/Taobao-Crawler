import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

public class CrawlerGUI extends JFrame implements ActionListener {
	
	public static final int SLAVE_DISCONNECT = 0;
	public static final int SLAVE_CONNECTING = 1;
	public static final int SLAVE_REQUESTING = 2;
	public static final int SLAVE_RECIEVEING = 3;
	public static final int SLAVE_CRAWLING = 4;
	public static final int SLAVE_WAITING = 5;
	public static final int SLAVE_CONNECTED = 6;
	
	private JButton startBtn;
	private JLabel ipLabel;
	private JTextField ipTextField;
	private JLabel threadLabel;
	private JTextField threadTextField;
	private JPanel inputPanel;
	private JLabel statusLabel;
	private JPanel statusPanel;
	private DefaultListModel<String> jListModel;
	private JList<String> infoList;
	private JScrollPane scrollPanel;
	private JPanel northPanel;
	
	public CrawlerGUI() {
		// TODO Auto-generated constructor stub
		super("分布式爬虫-从机");
		setBounds(500, 500, 480, 320);
		startBtn = new JButton("连接主机");
		startBtn.setBackground(Color.GREEN);
		startBtn.addActionListener(this);
		ipLabel = new JLabel("主机IP：");
		ipTextField = new JTextField(8);
		ipTextField.setText("localhost");
		threadLabel = new JLabel("线程数：");
		threadTextField = new JTextField(2);
		inputPanel = new JPanel(new FlowLayout());
		inputPanel.add(startBtn);
		inputPanel.add(ipLabel);
		inputPanel.add(ipTextField);
		inputPanel.add(threadLabel);
		inputPanel.add(threadTextField);
		northPanel = new JPanel(new GridLayout(2, 1));
		northPanel.add(inputPanel);
		statusLabel = new JLabel("连接已断开");
		statusPanel = new JPanel(new FlowLayout());
		statusPanel.setBackground(Color.RED);
		statusPanel.add(statusLabel);
		northPanel.add(statusPanel);
		add(northPanel, BorderLayout.NORTH);
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
		scrollPanel.setViewportView(infoList);
		add(scrollPanel, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == startBtn) {
			if(ipTextField.getText().isEmpty() || threadTextField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this,"错误");
			}
			else {
				startBtn.setEnabled(false);
				UrlReceive urlReceive = new UrlReceive();
				urlReceive.setViewport(this);
				urlReceive.setThreadCount(Integer.parseInt(threadTextField.getText()));
				urlReceive.start();
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
		jListModel.addElement(info);
		scrollPanel.getVerticalScrollBar().setValue(scrollPanel.getVerticalScrollBar().getMaximum());
	}
	
	public synchronized void updateStatus(int SLAVE_STATUS) {
		if(SLAVE_STATUS == SLAVE_DISCONNECT) {
			statusPanel.setBackground(Color.RED);
			statusLabel.setText("连接已断开");
		}
		if(SLAVE_STATUS == SLAVE_CONNECTING) {
			statusPanel.setBackground(Color.ORANGE);
			statusLabel.setText("正在连接……");
		}
		if(SLAVE_STATUS == SLAVE_CONNECTED) {
			statusPanel.setBackground(Color.GREEN);
			statusLabel.setText("已连接");
		}
		if(SLAVE_STATUS == SLAVE_RECIEVEING) {
			statusPanel.setBackground(Color.ORANGE);
			statusLabel.setText("正在接收……");
		}
		if(SLAVE_STATUS == SLAVE_REQUESTING) {
			statusPanel.setBackground(Color.ORANGE);
			statusLabel.setText("正在请求……");
		}
		if(SLAVE_STATUS == SLAVE_CRAWLING) {
			statusPanel.setBackground(Color.GREEN);
			statusLabel.setText("正在抓取……");
		}
		if(SLAVE_STATUS == SLAVE_WAITING) {
			statusPanel.setBackground(Color.ORANGE);
			statusLabel.setText("等待任务……");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new CrawlerGUI();
	}

}
