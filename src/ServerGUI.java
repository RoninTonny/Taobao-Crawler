import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.FontUIResource;

import com.sun.corba.se.spi.transport.CorbaAcceptor;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.PrivateKeyResolver;

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
	
	private JSeparator separator;
	private JSeparator separator2;
	private JPanel sPanel1;
	private JPanel sPanel2;
	
	private JTextArea textArea;
	private JScrollPane scrollPane;
	
	private JTextField pTextField;
	
	
	public ServerGUI() {
		// TODO Auto-generated constructor stub
		super("分布式爬虫-主机");
		initGF();
		setBounds(500, 500, 550, 460);
		startBtn = new JButton("启动主机");
		startBtn.addActionListener(this);
		startBtn.setBackground(Color.WHITE);
		websiteLabel = new JLabel("     数据包大小：");
		pTextField = new JTextField(5);
		pTextField.setText("300");
		keywordLabel = new JLabel("      关键词：");
		keywordTextField = new JTextField(10);
		inputPanel = new JPanel(new FlowLayout());
		separator = new JSeparator(SwingConstants.HORIZONTAL);
		separator.setPreferredSize(new Dimension(300, 1));
		separator2 = new JSeparator(SwingConstants.HORIZONTAL);
		separator2.setPreferredSize(new Dimension(300, 1));
		slaveNumLabel = new JLabel("上线从机：");
		slaveNum = new JLabel("0");
		slaveNum.setFont(new Font("宋体", 1, 20));
		slaveNum.setForeground(Color.RED);
		slavePanel = new JPanel(new FlowLayout());
		slavePanel.add(slaveNumLabel);
		slavePanel.add(slaveNum);
		inProgressLabel = new JLabel("URL抓取进度：");
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
		outProgressLabel = new JLabel("URL分发进度：");
		outProgressNumLabel = new JLabel("0/99");
		outProgressPanel = new JPanel(new FlowLayout());
		outProgressPanel.add(outProgressLabel);
		outProgressPanel.add(outProgressBar);
		outProgressPanel.add(outProgressNumLabel);
		inputPanel.add(startBtn);
		inputPanel.add(websiteLabel);
		inputPanel.add(pTextField);
		inputPanel.add(keywordLabel);
		inputPanel.add(keywordTextField);
		sPanel1 = new JPanel();
		sPanel2 = new JPanel();
		sPanel1.setPreferredSize(new Dimension(100, 5));
		sPanel2.setPreferredSize(new Dimension(100, 5));
		//sPanel1.add(separator);
		//sPanel2.add(separator2);
		northPanel = new JPanel(new GridLayout(4, 1));
		northPanel.add(inputPanel);
		//northPanel.add(sPanel1);
		northPanel.add(slavePanel);
		//northPanel.add(sPanel2);
		northPanel.add(inProgressPanel);
		northPanel.add(outProgressPanel);
		textArea = new JTextArea();
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.GREEN);
		scrollPane = new JScrollPane(textArea);
		//northPanel.add(scrollPane);
		scrollPane.setBorder(BorderFactory.createMatteBorder(0, 30, 10, 35, getBackground()));
		add(northPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		textArea.setEditable(false);
		System.out.println(getWidth());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
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
	
	public void updateLog(String info) {
		textArea.append("\n> " + info);
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
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
			if(keywordTextField.getText().isEmpty() || pTextField.getText().isEmpty()) {
				try {
					Integer.parseInt(pTextField.getText());
				} catch (NumberFormatException e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(this,"错误");
				}
				JOptionPane.showMessageDialog(this,"错误");
			}
			else {
				startBtn.setEnabled(false);
				keywordTextField.setEditable(false);
				pTextField.setEditable(false);
				UrlCrawlerThreadTest urlCrawlerThreadTest = new UrlCrawlerThreadTest(this.keywordTextField.getText());
				urlCrawlerThreadTest.setViewPort(this);
				urlCrawlerThreadTest.start();
				SocketPortListen socketPortListen = new SocketPortListen(10240);
				socketPortListen.setViewPort(this);
				socketPortListen.setPacketSize(Integer.parseInt(pTextField.getText()));
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ServerGUI();
	}

}
