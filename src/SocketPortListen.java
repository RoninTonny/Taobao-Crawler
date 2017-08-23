import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketPortListen extends Thread{
	
	private ServerGUI serverGUI;
	private int startPort;
	
	public void setViewPort(ServerGUI serverGUI) {
		this.serverGUI = serverGUI;
	}

	public SocketPortListen() {
		this(10240);
	}
	
	public SocketPortListen(int startPort) {
		this.startPort = startPort;
		start();
	}
	
	@Override
	public void run() {
		System.out.println("Server Online");
		try {
			ServerSocket server = new ServerSocket(startPort);
			while(true) {
				Socket client = server.accept();
				System.out.println("Slave Online : " + client.getInetAddress());
				UrlSendThread urlSendThread = new UrlSendThread(client);
				urlSendThread.setViewport(serverGUI);
				urlSendThread.start();
				//do stuff
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new SocketPortListen(10240);
	}

}
