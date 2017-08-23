
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;

public class UrlSendThread extends Thread implements Serializable{
	
	public int maxRetry;
	public int urlPacketSize;
	
	
	private static final int CLIENT_READY = 0;
	private static final int CLIENT_BROKEN = 1;
	private static final int CLIENT_ACCEPTED = 2;
	private static final int CLIENT_RESULT = 3;
	private static final int CLIENT_HBP = 4;
	
	private ObjectOutputStream IDSToBeSent;
	private OutputStreamWriter msgToBeSent;
	private DataInputStream msgAccepted;
	private List<String> IDS;
	private Jedis jedis;
	private boolean done;
	private String slaveIP;
	private ServerGUI serverGUI;

	public UrlSendThread() {
		// TODO Auto-generated constructor stub
	}
	
	public UrlSendThread(Socket client) {
		this.maxRetry = 3;
		this.urlPacketSize = 30;
		this.done = false;
		this.IDS = new ArrayList<String>();
		this.jedis = new Jedis("localhost");
		try {
			this.slaveIP = client.getInetAddress().toString();
			client.setSoTimeout(10000);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.IDSToBeSent = new ObjectOutputStream(client.getOutputStream());
			this.msgToBeSent = new OutputStreamWriter(client.getOutputStream());
			this.msgAccepted = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setMaxRetry(int max) {
		this.maxRetry = max;
	}
	
	public void setUrlPacketSize(int size) {
		this.urlPacketSize = size;
	}
	
	public void setViewport(ServerGUI sGUI) {
		this.serverGUI = sGUI;
	}
	
	@Override
	public void run() {
		if(serverGUI != null) {
			serverGUI.updateSlaveNum(1);
		}
		boolean isFirst = true;
		System.out.println(this.getName() + " Server send thread start");
		int connectFailCount = 0;
		int msg = -1;
		int sentCount = 0;
		while(true) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(isFirst) {
					msg = msgAccepted.readInt();
					isFirst = false;
				}
				if(msg == this.CLIENT_READY) {
					msg = -1;
					System.out.println(this.getName() + " Slave ready in : " + slaveIP);
					for(int i = 0; i < urlPacketSize && jedis.llen("IDS") != 0; ++i) {
						++sentCount;
						IDS.add(jedis.rpop("IDS"));
					}
					if(IDS.size() == 0) {
						Thread.sleep(3000);
						continue;
					}
					else {
						IDSToBeSent.reset();
						IDSToBeSent.writeObject(IDS);
					}
				}
				if(!isFirst) {
					msg = -1;
					try {
						msg = msgAccepted.readInt();
					} catch (Exception e) {
						// TODO: handle exception
						msg = -1;
					}
				}
				if(msg == this.CLIENT_ACCEPTED) {
					msg = -1;
					System.out.println(this.getName() + " Send Url Packet with " + IDS.size());
					if(serverGUI != null) {
						serverGUI.updateOutProgress(sentCount, jedis.llen("IDS"));
					}
					int HBPFailCount = 0;
					while(true) {
						try {
							//System.out.println(this.getName() + " Waiting HBP");
							msg = msgAccepted.readInt();
							if(msg == this.CLIENT_HBP) {
								msg = -1;
								//System.out.println(this.getName() + " Recieve HBP");
								HBPFailCount = 0;
								continue;
							}
							if(msg == this.CLIENT_RESULT){
								msg = -1;
								System.out.println(this.getName() + " Slave finished work");
								//do sth
								IDS.clear();
								msg = this.CLIENT_READY;
								break;
							}
						} catch (SocketTimeoutException e) {
							System.out.println(this.getName() + " Get HBP fail");
							++HBPFailCount;
							if(HBPFailCount > maxRetry) {
								if(serverGUI != null) {
									serverGUI.updateSlaveNum(-1);
								}
								System.out.println(this.getName() + " Slave offline, Server will be shutdown right now");
								//return;
							}
							continue;
						} catch (SocketException e) {
							if(serverGUI != null) {
								serverGUI.updateSlaveNum(-1);
							}
							System.out.println(this.getName() + " Slave offline, Server will be shutdown right now");
							return;
						}
					}
				}
			} catch (SocketTimeoutException e) {
				System.out.println(this.getName() + " Connect fail");
				++connectFailCount;
				if(connectFailCount > maxRetry) {
					if(done) {
					}
					else {
						for(String url : IDS) {
							jedis.lpush("IDS", url);
						}
					}
					if(serverGUI != null) {
						serverGUI.updateSlaveNum(-1);
					}
					System.out.println(this.getName() + " Slave offline, Server will be shutdown right now");
					return;
				}
			} catch (SocketException e) {
				if(serverGUI != null) {
					serverGUI.updateSlaveNum(-1);
				}
				System.out.println(this.getName() + " Slave offline, Server will be shutdown right now");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
