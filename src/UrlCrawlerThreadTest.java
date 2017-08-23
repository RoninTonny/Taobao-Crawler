import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.atomic.AtomicInteger;

public class UrlCrawlerThreadTest {
	
	//this class is to show that how build a crawler extends UrlCrawlerThread
	//and make a simple to show how use max-sync to get content from url; 
	private AtomicInteger max;
	private Crawler crawler;
	private int dmax;
	
	class Crawler extends UrlCrawlerThread {

		@Override
		public int getAndIncrement() {
			// TODO Auto-generated method stub
			if(max.get() < dmax){
				super.nextPage.set(max.getAndIncrement());
			}
			else{
				super.isStoped.set(true);
			}
			return super.nextPage.get();
		}
		
		public Crawler() {
			// TODO Auto-generated constructor stub
			super();
		}
		
		public Crawler(CrawlerRule rule, String searchSort, int startPage) {
			super(rule, searchSort, startPage);
		}
		
	}

	public UrlCrawlerThreadTest() {
		// TODO Auto-generated constructor stub
		max = new AtomicInteger(1);
		dmax = (new CrawlerRule("taobao.xml")).PAGE_COUNT;
		crawler = new Crawler(new CrawlerRule("taobao.xml"), "%E7%94%B5%E8%84%91", 0);
	}
	
	public UrlCrawlerThreadTest(String keyword) {
		// TODO Auto-generated constructor stub
		max = new AtomicInteger(1);
		dmax = (new CrawlerRule("taobao.xml")).PAGE_COUNT;
		try {
			crawler = new Crawler(new CrawlerRule("taobao.xml"), URLEncoder.encode(keyword, "UTF-8"), 0);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setViewPort(ServerGUI serverGUI) {
		crawler.setViewPort(serverGUI);
	}
	
	public void start() {
		this.crawler.start();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new UrlCrawlerThreadTest().start();
	}

}
