import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;

public class UrlReceive extends Thread implements Serializable {
	
	private Socket server;
	private ObjectInputStream urlsFromServer;
	private DataOutputStream msgToServer;
	private List<String> urls;
	private SlaveHBPThread HBP;
	private CrawlerGUI crawlerGUI;
	private Jedis jedis;
	private List<CrawlerThread> crawlerThreads;
	private int threadCount;
	
	private static final int CLIENT_READY = 0;
	private static final int CLIENT_BROKEN = 1;
	private static final int CLIENT_ACCEPTED = 2;
	private static final int CLIENT_RESULT = 3;
	private static final int CLIENT_HBP = 4;

	public UrlReceive(String ip) {
		threadCount = 0;
		urls = new ArrayList<String>();
		jedis = new Jedis("localhost");
		crawlerThreads = new ArrayList<CrawlerThread>();
		try {
			server = new Socket(ip, 10240);
			urlsFromServer = new ObjectInputStream(server.getInputStream());
			msgToServer = new DataOutputStream(server.getOutputStream());
			HBP = new SlaveHBPThread(server);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setThreadCount(int count) {
		this.threadCount = count;
	}
	
	public void setViewport(CrawlerGUI crawlerGUI) {
		this.crawlerGUI = crawlerGUI;
		HBP.setViewPort(crawlerGUI);
	}
	
	public void checkAll() throws IOException {
		msgToServer.writeInt(this.CLIENT_READY);
		//System.out.println("Send ready info");
	}
	
	public void getUrls() throws ClassNotFoundException, IOException {
		updateGUI(CrawlerGUI.SLAVE_RECIEVEING);
		urls = (List<String>) urlsFromServer.readObject();
		System.out.println("get urls : " + urls.size());
		for(String url : urls) {
			jedis.rpush("CIDS", url);
		}
		msgToServer.writeInt(this.CLIENT_ACCEPTED);
		//System.out.println("Send gotten info");
	}
	
	public void startCrawler() throws IOException {
		updateGUI(CrawlerGUI.SLAVE_CRAWLING);
		threadCount = crawlerGUI.getThreadCount();
		for(int i = 0; i < threadCount; ++i) {
			System.out.println(threadCount);
			crawlerThreads.add(new CrawlerThread("crawler-url.xml", "crawler-content.xml"));
			crawlerThreads.get(i).setViewport(crawlerGUI);
			crawlerThreads.get(i).start();
		}
		boolean allOK = false;
		for(;!allOK;) {
			allOK = true;
			for(int i = 0; i < threadCount; ++i) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!crawlerThreads.get(i).isFinished()) {
					System.out.println(crawlerThreads.get(i).isFinished());
					allOK = false;
				}
			}
		}
		System.out.println(crawlerThreads.get(0).isFinished());
		for(int i = 0; i < threadCount; ++i) {
			crawlerThreads.get(i).shutdown();
		}
		crawlerThreads.clear();
		synchronized (server) {
			crawlerGUI.updateStatus(CrawlerGUI.SLAVE_REQUESTING);
			msgToServer.writeInt(this.CLIENT_RESULT);
		}
		System.out.println("Send finish info");
	}
	
	private void updateGUI(int code) {
		if(crawlerGUI != null) {
			crawlerGUI.updateStatus(code);
		}
	}
	
	@Override
	public void run() {
		while(true) {
			boolean isFirst = true;
			for(;;){
				try {
					if(isFirst) {
						updateGUI(CrawlerGUI.SLAVE_CONNECTING);
						checkAll();
						HBP.start();
						isFirst = false;
					}
					updateGUI(CrawlerGUI.SLAVE_REQUESTING);
					getUrls();
					HBP.restart();
					updateGUI(CrawlerGUI.SLAVE_CRAWLING);
					startCrawler();
					HBP.pause();
				} catch (SocketException e) {
					updateGUI(CrawlerGUI.SLAVE_DISCONNECT);
					//System.out.println("Server offline, Slave will be shutdown right now");
				} catch (IOException e) {
					// TODO: handle exception
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new UrlReceive("localhost");
	}

}
