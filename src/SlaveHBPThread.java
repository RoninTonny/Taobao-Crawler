import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class SlaveHBPThread extends Thread {
	
	private static final int CLIENT_HBP = 4;
	private DataOutputStream HBPToServer;
	private AtomicBoolean isPause;
	private Socket server;
	private CrawlerGUI crawlerGUI;
	
	public void setViewPort(CrawlerGUI crawlerGUI) {
		this.crawlerGUI = crawlerGUI;
	}
	
	public SlaveHBPThread(Socket socket) {
		if(crawlerGUI != null) {
			crawlerGUI.updateStatus(CrawlerGUI.SLAVE_REQUESTING);
		}
		this.server = socket;
		isPause = new AtomicBoolean(false);
		try {
			this.HBPToServer = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void pause() {
		this.isPause.set(true);
	}
	
	public void restart() {
		this.isPause.set(false);
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(!isPause.get()) {
				try {
					synchronized (server) {
						HBPToServer.writeInt(this.CLIENT_HBP);
					}
					//System.out.println("Sent HBP");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
