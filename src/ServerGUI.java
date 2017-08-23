import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class ServerGUI extends JFrame implements ActionListener{
	
	private JPanel inputPanel;
	private JLabel inProgressLabel;
	private JLabel slaveNumLabel;
	private JLabel slaveNum;
	private JPanel slavePanel;
	private JProgressBar inProgressBar;
	private JLabel inProgressNumLabel;
	private JPanel inProgressPanel;
	private JLabel outProgressLabel;
	private JProgressBar outProgressBar;
	private JLabel outProgressNumLabel;
	private JPanel outProgressPanel;
	private JButton startBtn;
	private JLabel websiteLabel;
	private JComboBox<String> websiteComboBox;
	private JLabel keywordLabel;
	private JTextField keywordTextField;
	private JPanel northPanel;
	
	
	public ServerGUI() {
		// TODO Auto-generated constructor stub
		super("分布式爬虫-主机");
		setBounds(500, 500, 480, 180);
		startBtn = new JButton("启动主机");
		startBtn.addActionListener(this);
		startBtn.setBackground(Color.BLUE);
		startBtn.setForeground(Color.WHITE);
		websiteLabel = new JLabel("目标网站：");
		websiteComboBox = new JComboBox<String>();
		websiteComboBox.addItem("淘宝");
		keywordLabel = new JLabel("关键词：");
		keywordTextField = new JTextField(5);
		inputPanel = new JPanel(new FlowLayout());
		slaveNumLabel = new JLabel("上线从机个数：");
		slaveNum = new JLabel("0");
		slaveNum.setForeground(Color.RED);
		slavePanel = new JPanel(new FlowLayout());
		slavePanel.add(slaveNumLabel);
		slavePanel.add(slaveNum);
		inProgressLabel = new JLabel("URL抓取    进度：");
		inProgressBar = new JProgressBar(0, 99);
		inProgressBar.setForeground(Color.GREEN);
		inProgressBar.setPreferredSize(new Dimension(300,15));
		inProgressNumLabel = new JLabel("0/99");
		inProgressPanel = new JPanel(new FlowLayout());
		inProgressPanel.add(inProgressLabel);
		inProgressPanel.add(inProgressBar);
		inProgressPanel.add(inProgressNumLabel);
		outProgressBar = new JProgressBar(0, 99);
		outProgressBar.setForeground(Color.GREEN);
		outProgressBar.setPreferredSize(new Dimension(300,15));
		outProgressLabel = new JLabel("从机取任务进度：");
		outProgressNumLabel = new JLabel("0/99");
		outProgressPanel = new JPanel(new FlowLayout());
		outProgressPanel.add(outProgressLabel);
		outProgressPanel.add(outProgressBar);
		outProgressPanel.add(outProgressNumLabel);
		inputPanel.add(startBtn);
		inputPanel.add(websiteLabel);
		inputPanel.add(websiteComboBox);
		inputPanel.add(keywordLabel);
		inputPanel.add(keywordTextField);
		northPanel = new JPanel(new GridLayout(4, 1));
		northPanel.add(inputPanel);
		northPanel.add(slavePanel);
		northPanel.add(inProgressPanel);
		northPanel.add(outProgressPanel);
		add(northPanel, BorderLayout.NORTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	
	public String getKeyword() {
		String key = keywordTextField.getText();
		if(!key.isEmpty()) {
			return key;
		}
		else {
			JOptionPane.showMessageDialog(this,"错误");
			return null;
		}
	}
	
	public void updateInProgress(int curr, int total) {
		inProgressBar.setMaximum(total);
		inProgressBar.setValue(curr);
		inProgressNumLabel.setText(String.valueOf(curr) + "/" + String.valueOf(total));
	}
	
	public void updateOutProgress(int curr, int total) {
		outProgressBar.setMaximum(total);
		outProgressBar.setValue(curr);
		outProgressNumLabel.setText(String.valueOf(curr) + "/" + String.valueOf(total));
	}
	
	public void updateOutProgress(int curr, long total) {
		outProgressBar.setMaximum(Integer.parseInt(String.valueOf(total)));
		outProgressBar.setValue(curr);
		outProgressNumLabel.setText(String.valueOf(curr) + "/" + String.valueOf(total));
	}
	
	public void updateSlaveNum(int delta) {
		this.slaveNum.setText(String.valueOf(Integer.parseInt(slaveNum.getText()) + delta));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == startBtn) {
			if(keywordTextField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this,"错误");
			}
			else {
				startBtn.setEnabled(false);
				UrlCrawlerThreadTest urlCrawlerThreadTest = new UrlCrawlerThreadTest(this.keywordTextField.getText());
				urlCrawlerThreadTest.setViewPort(this);
				urlCrawlerThreadTest.start();
				SocketPortListen socketPortListen = new SocketPortListen(10240);
				socketPortListen.setViewPort(this);
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ServerGUI();
	}

}
