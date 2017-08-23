import java.awt.TexturePaint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import redis.clients.jedis.Jedis;

public abstract class UrlCrawlerThread extends Thread {
	
	//use this class to get all urls in search-page
	
	protected AtomicInteger nextPage;
	public AtomicBoolean isStoped;
	public AtomicBoolean isPaused;
	private boolean isFail;
	private boolean isFirst;
	private Document searchPageHTML;
	private String searchSort;
	private String searchUrl;
	private CrawlerRule rule;
	private Jedis jedis;
	private int failCount;
	private int maxRetry;
	private ServerGUI serverGUI;
	
	//will cause problem undoubtedly, just write this for future work
	public UrlCrawlerThread() {
		this.nextPage = new AtomicInteger(-1);
		this.searchPageHTML = null;
		this.isFirst = true;
		this.isFail = false;
		this.jedis = new Jedis("localhost");
		this.isStoped = new AtomicBoolean(false);
		this.isPaused = new AtomicBoolean(false);
		this.failCount = 0;
		this.maxRetry = 10;
	}
	
	//need rule.xml, keyword of sort, start position to init
	//searchSort should be utf-8 ! DO NOT USE CHINESE CHARACTER!
	public UrlCrawlerThread(CrawlerRule rule, String searchSort, int startPage) {
		this.rule = rule;
		this.nextPage = new AtomicInteger(-1);
		this.nextPage.set(startPage);
		this.searchSort = searchSort;
		if(this.rule.SEARCH_PAGE_ENCODE_NAME.isEmpty()){
			this.searchUrl = rule.SEARCH_PAGE_FRONT_URL + "?"  + rule.SEARCH_PAGE_SEARCH_NAME + "=" + searchSort + this.rule.SEARCH_PAGE_IGNORE + "&"  + this.rule.SEARCH_PAGE_SORT_NAME + "=" + this.rule.SEARCH_PAGE_SORT + "&" + rule.SEARCH_PAGE_INCREATE_NAME + "=";
		}
		else{
			this.searchUrl = rule.SEARCH_PAGE_FRONT_URL + "?" + rule.SEARCH_PAGE_SEARCH_NAME + "=" + searchSort + this.rule.SEARCH_PAGE_IGNORE + "&" + this.rule.SEARCH_PAGE_SORT_NAME + "=" + this.rule.SEARCH_PAGE_SORT + "&" + rule.SEARCH_PAGE_INCREATE_NAME + "=";
		}
		this.searchPageHTML = null;
		this.isFirst = true;
		this.isFail = false;
		this.jedis = new Jedis("localhost");
		this.isStoped = new AtomicBoolean(false);
		this.isPaused = new AtomicBoolean(false);
		this.failCount = 0;
		this.maxRetry = 10;
	}
	
	//need rule.xml, keyword of sort, start position to init
	//searchSort should be utf-8 ! DO NOT USE CHINESE CHARACTER!
	public UrlCrawlerThread(CrawlerRule rule, String searchSort, AtomicInteger startPage) {
		this.rule = rule;
		this.nextPage = new AtomicInteger(-1);
		this.nextPage.set(startPage.get());
		this.searchSort = searchSort;
		if(this.rule.SEARCH_PAGE_ENCODE_NAME.isEmpty()){
			this.searchUrl = rule.SEARCH_PAGE_FRONT_URL + "?" + rule.SEARCH_PAGE_SEARCH_NAME + "=" + searchSort + this.rule.SEARCH_PAGE_IGNORE + "&"  + "sort=sale-desc&" + rule.SEARCH_PAGE_INCREATE_NAME + "=";
		}
		else{
			this.searchUrl = rule.SEARCH_PAGE_FRONT_URL + "?" + rule.SEARCH_PAGE_SEARCH_NAME + "=" + searchSort + this.rule.SEARCH_PAGE_IGNORE + "&" + "sort=sale-desc&" + rule.SEARCH_PAGE_INCREATE_NAME + "=";
		}
		this.searchPageHTML = null;
		this.isFirst = true;
		this.isFail = false;
		this.jedis = new Jedis("localhost");
		this.isStoped = new AtomicBoolean(false);
		this.isPaused = new AtomicBoolean(false);
		this.failCount = 0;
		this.maxRetry = 10;
	}
	
	public void setViewPort(ServerGUI serverGUI) {
		this.serverGUI = serverGUI;
	}
	
	//local usage , just ignore this
	private String getUrl() {
		System.out.println(this.searchUrl + (this.rule.SEARCH_PAGE_INCREATE_START + this.nextPage.get() * this.rule.SEARCH_PAGE_INCREATE));
		return this.searchUrl + (this.rule.SEARCH_PAGE_INCREATE_START + this.nextPage.get() * this.rule.SEARCH_PAGE_INCREATE);
	}
	
	//analyses html from url, get all links and store into Redis
	private void getResults(Document document) {
		if(this.rule.SEARCH_PAGE_TYPE.equals("urls")) {
			if(this.rule.SEARCH_PAGE_TYPE_METHOD.equals("html-class")) {
				List<Element> resultList;
				resultList = document.getElementsByClass(this.rule.SEARCH_PAGE_TYPE_KEYWORD);
				for(int i = 0; i < resultList.size(); ++i) {
					jedis.lpush("IDS", resultList.get(i).attr(this.rule.SEARCH_PAGE_TYPE_SECOND_KEYWORD));
					/*if(this.rule.INFO_PAGE_ID_NAME.isEmpty()) {
						jedis.lpush("URLS", rule.INFO_PAGE_FRONT_URL + resultList.get(i).attr(this.rule.SEARCH_PAGE_TYPE_SECOND_KEYWORD) + this.rule.INFO_PAGE_BACK_URL);
						//Redis k-v storage, put here for other situation
						//jedis.set(this.nextPage.get() + "-" + i, rule.INFO_PAGE_FRONT_URL + resultList.get(i).attr(this.rule.SEARCH_PAGE_TYPE_SECOND_KEYWORD) + this.rule.INFO_PAGE_BACK_URL);
					}
					else{
						jedis.lpush("URLS", rule.INFO_PAGE_FRONT_URL + "?" + rule.INFO_PAGE_ID_NAME + "=" + resultList.get(i).attr(this.rule.SEARCH_PAGE_TYPE_SECOND_KEYWORD) + this.rule.INFO_PAGE_BACK_URL);
						//Redis k-v storage, put here for other situation
						//jedis.set(this.nextPage.get() + "-" + i, rule.INFO_PAGE_FRONT_URL + "?" + rule.INFO_PAGE_ID_NAME + "=" + resultList.get(i).attr(this.rule.SEARCH_PAGE_TYPE_SECOND_KEYWORD) + this.rule.INFO_PAGE_BACK_URL);
					}	*/		
				}
			}
		}
		if(this.rule.SEARCH_PAGE_TYPE.equals("ids")) {
			if(this.rule.SEARCH_PAGE_TYPE_METHOD.equals("text-array")) {
				String docStr = document.toString();
				int startPos = 0;
				int endPos = 0;	
				startPos = docStr.indexOf(this.rule.SEARCH_PAGE_TYPE_KEYWORD) + 10;
				endPos = docStr.indexOf(this.rule.SEARCH_PAGE_TYPE_SECOND_KEYWORD, startPos);
				String[] strList = docStr.substring(startPos, endPos).split(",");
				for(int i = 0; i < strList.length; ++i) {
					jedis.lpush("IDS", strList[i].substring(1, strList[i].length() - 1));
					if(this.rule.INFO_PAGE_ID_NAME.isEmpty()) {
						//jedis.lpush("URLS", rule.INFO_PAGE_FRONT_URL + "?" + rule.INFO_PAGE_ID_NAME + "=" + strList[i].substring(1, strList[i].length() - 2) + this.rule.INFO_PAGE_BACK_URL);
						//Redis k-v storage, put here for other situation
						//jedis.set(this.nextPage.get() + "-" + i, rule.INFO_PAGE_FRONT_URL + "?" + rule.INFO_PAGE_ID_NAME + "=" + strList[i].substring(1, strList[i].length() - 2) + this.rule.INFO_PAGE_BACK_URL);
					}
					else{
						//jedis.lpush("URLS", rule.INFO_PAGE_FRONT_URL + strList[i].substring(1, strList[i].length() - 2) + this.rule.INFO_PAGE_BACK_URL);
						//Redis k-v storage, put here for other situation
						//jedis.set(this.nextPage.get() + "-" + i, rule.INFO_PAGE_FRONT_URL + strList[i].substring(1, strList[i].length() - 2) + this.rule.INFO_PAGE_BACK_URL);
					}
				}
			}
		}
		
	}
	
	//get the page index that is processing
	public int getCurrPage() {
		return this.nextPage.get();
	}
	
	//set max retry count when get network content fail
	public void setMaxRetry(int max) {
		if(max >= 0) {
			this.maxRetry = max;
		}
	}
	
	//must be override, use this to do max sync and other Java thread stuff,
	//example can be found in class UrlCrawlerThreadTest
	public abstract int getAndIncrement();
	
	//use this to stop this thread, and can not be restarted again
	public void die() {
		this.isStoped.set(true);
	}
	
	//use this to pause this thread, may cause some disturbing multi-thread problem
	//but most time work normally, may be...
	public void pause() {
		this.isPaused.set(true);
	}
	
	//get html and change page automatically,
	//will retry no more than maxRetry when get fail
	//actually Jsoup.connect() throwing "timeout Exception" is a very common situation
	//will throw Exception if still can not get content after maxRetry(default value is 10) times retry
	
	@Override
	public void run() {
		int count = 0;
		while(true) {
			++count;
			if(isFirst) {
				isFirst = false;
				try {
					this.searchPageHTML = Jsoup.connect(this.getUrl()).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64)").timeout(3000).get();
				} catch (IOException e) {
					isFirst = true;
					isFail = true;
					++failCount;
					if(failCount > maxRetry){
						isFail = false;
						isFirst = false;
						failCount = 0;
						e.printStackTrace();
						continue;
					}
					continue;
				}
				getResults(this.searchPageHTML);
				this.searchPageHTML = null;
			}
			else {
				if(!isFail) {
					getAndIncrement();
				}
				if(!isPaused.get() && !isStoped.get()) {
					try {
						this.searchPageHTML = Jsoup.connect(this.getUrl()).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64)").timeout(3000).get();
					} catch (IOException e) {
						isFail = true;
						if(failCount > maxRetry) {
							isFail = false;
							failCount = 0;
							e.printStackTrace();
							continue;
						}
						continue;
					}
					getResults(this.searchPageHTML);
					this.searchPageHTML = null;
				}
				else{
					if(isPaused.get()){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					else if(isStoped.get()){
						return;
					}
				}
				isFail = false;
				failCount = 0;
			}
			if(serverGUI != null) {
				serverGUI.updateInProgress(count, rule.PAGE_COUNT);
			}
		}
	}
}
