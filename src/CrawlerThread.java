import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.print.attribute.PrintJobAttributeSet;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.omg.CORBA.BAD_CONTEXT;

import redis.clients.jedis.Jedis;

public class CrawlerThread extends Thread{
	
	private Jedis jedis;
	private String url;
	private String id;
	private CrawlerRule crawlerRule;
	private GetterRule getterRule;
	private Map<String, Object> content;
	private int maxRetry;
	public AtomicBoolean isPasued;
	public AtomicBoolean isStoped;
	private DBOprate dbOprate;
	private CrawlerGUI viewport;
	AtomicBoolean isFinished;
	
	private String setUrl() {
		if(this.crawlerRule.SEARCH_PAGE_TYPE.equals("urls")) {
			if(this.crawlerRule.SEARCH_PAGE_TYPE_METHOD.equals("html-class")) {
				if(this.crawlerRule.INFO_PAGE_ID_NAME.isEmpty()) {
					this.url = crawlerRule.INFO_PAGE_FRONT_URL + this.id + this.crawlerRule.INFO_PAGE_BACK_URL;
				}
				else{
					this.url = crawlerRule.INFO_PAGE_FRONT_URL + "?" + crawlerRule.INFO_PAGE_ID_NAME + "=" + this.id + this.crawlerRule.INFO_PAGE_BACK_URL;
				}
			}
		}
		if(this.crawlerRule.SEARCH_PAGE_TYPE.equals("ids")) {
			if(this.crawlerRule.SEARCH_PAGE_TYPE_METHOD.equals("text-array")) {
				if(this.crawlerRule.INFO_PAGE_ID_NAME.isEmpty()) {
					this.url = crawlerRule.INFO_PAGE_FRONT_URL + this.id + this.crawlerRule.INFO_PAGE_BACK_URL;
				}
				else{
					this.url = crawlerRule.INFO_PAGE_FRONT_URL + "?" + crawlerRule.INFO_PAGE_ID_NAME + "=" + this.id + this.crawlerRule.INFO_PAGE_BACK_URL;
				}
			}
		}
		System.out.println(url);
		return this.url;
	}
	
	private Map<String, Object> getContent(Document document, CrawlerRule crawlerRule, GetterRule getterRule) {
		content.put("name", "");
		content.put("shop", "");
		content.put("price", "");
		content.put("count", "");
		content.put("good", "");
		content.put("normal", "");
		content.put("bad", "");
		content.put("add", "");
		content.put("rate", "");
		content.put("id", this.id);
		if(getterRule.NAME_TYPE.equals("html-class")) {
			content.put("name", document.getElementsByClass(getterRule.NAME_FIRST_KEYWORD).text());
		}
		if(getterRule.SHOP_TYPE.equals("html-class")) {
			content.put("shop", document.getElementsByClass(getterRule.SHOP_FIRST_KEYWORD).text());
		}
		if(getterRule.SHOP_TYPE.equals("html-attr")) {
			content.put("shop", document.getElementsByAttributeValue(getterRule.SHOP_FIRST_KEYWORD, getterRule.SHOP_SECOND_KEYWORD).text());
		}
		if(getterRule.PRICE_TYPE.equals("html-class")) {
			content.put("price", Integer.parseInt(document.getElementsByClass(getterRule.SHOP_FIRST_KEYWORD).text()));
		}
		if(getterRule.PRICE_TYPE.length() > 10 && getterRule.PRICE_TYPE.substring(0, 10).equals("html-id-sub")) {
			content.put("price", Integer.parseInt(document.getElementsByClass(getterRule.PRICE_FIRST_KEYWORD).text().substring(Integer.parseInt(getterRule.PRICE_TYPE.split("-")[3]))));
		}
		if(getterRule.PRICE_TYPE.equals("js")) {
			String fId;
			String nId;
			Connection priceUrl;
			if(getterRule.PRICE_ID_NAME.indexOf("~") != -1) {
				nId = getterRule.PRICE_ID_NAME.split("~")[0];
				fId = getterRule.PRICE_ID_NAME.split("~")[1];
				if(getterRule.PRICE_HREF.indexOf("?") != -1) {
					priceUrl = Jsoup.connect(getterRule.PRICE_HREF + "&" + nId + "=" + fId + this.id);
				}
				else {
					priceUrl = Jsoup.connect(getterRule.PRICE_HREF + "?" + nId + "=" + fId + this.id);
				}
			}
			else {
				if(getterRule.PRICE_HREF.indexOf("?") != -1) {
					priceUrl = Jsoup.connect(getterRule.PRICE_HREF + "&" + getterRule.PRICE_ID_NAME + "=" + this.id);
					//System.out.println(getterRule.PRICE_HREF + "&" + getterRule.PRICE_ID_NAME + "=" + this.id);
				}
				else {
					priceUrl = Jsoup.connect(getterRule.PRICE_HREF + "?" + getterRule.PRICE_ID_NAME + "=" + this.id);
				}
			}
			if(!getterRule.PRICE_REFERRER.isEmpty()) {
				if(getterRule.PRICE_REFERRER.equals("self")) {
					priceUrl.referrer(this.url);
					//System.out.println(this.url);
				}
				if(getterRule.PRICE_REFERRER.equals("first-page")) {
					priceUrl.referrer(crawlerRule.FIRST_PAGE);
				}
			}
			priceUrl.userAgent("Chrom").timeout(3000).ignoreContentType(true);
			String priceJson = new String();
			int failCount = 0;
			while(priceJson.isEmpty()) {
				try {
					priceJson = priceUrl.get().text();
				} catch (IOException e) {
					failCount++;
					if(failCount >= this.maxRetry) {
						break;
					}
					continue;
				}
			}
			//System.out.println(priceJson);
			try {
				content.put("price", Double.parseDouble(priceJson.substring(priceJson.indexOf(getterRule.PRICE_FIRST_KEYWORD) + getterRule.PRICE_OFFSET, priceJson.indexOf(getterRule.PRICE_SECOND_KEYWORD))));
			} catch (NumberFormatException e) {
				content.put("price", Double.parseDouble(priceJson.substring(priceJson.indexOf(getterRule.PRICE_FIRST_KEYWORD) + getterRule.PRICE_OFFSET, priceJson.indexOf(getterRule.PRICE_SECOND_KEYWORD)).split(" - ")[0]));
			}
		}
		if(getterRule.COUNT_TYPE.equals("js")) {
			String fId;
			String nId;
			Connection priceUrl;
			if(getterRule.COUNT_ID_NAME.indexOf("~") != -1) {
				nId = getterRule.COUNT_ID_NAME.split("~")[0];
				fId = getterRule.COUNT_ID_NAME.split("~")[1];
				if(getterRule.COUNT_HREF.indexOf("?") != -1) {
					priceUrl = Jsoup.connect(getterRule.COUNT_HREF + "&" + nId + "=" + fId + this.id);
				}
				else {
					priceUrl = Jsoup.connect(getterRule.COUNT_HREF + "?" + nId + "=" + fId + this.id);
				}
			}
			else {
				if(getterRule.COUNT_HREF.indexOf("?") != -1) {
					priceUrl = Jsoup.connect(getterRule.COUNT_HREF + "&" + getterRule.COUNT_ID_NAME + "=" + this.id);
				}
				else {
					priceUrl = Jsoup.connect(getterRule.COUNT_HREF + "?" + getterRule.COUNT_ID_NAME + "=" + this.id);
				}
			}
			if(!getterRule.COUNT_REFERRER.isEmpty()) {
				if(getterRule.COUNT_REFERRER.equals("self")) {
					priceUrl.referrer(this.url);
				}
				if(getterRule.COUNT_REFERRER.equals("first-page")) {
					priceUrl.referrer(crawlerRule.FIRST_PAGE);
				}
			}
			priceUrl.userAgent("Chrom").timeout(3000).ignoreContentType(true);
			String priceJson = new String();
			int failCount = 0;
			while(priceJson.isEmpty()) {
				try {
					priceJson = priceUrl.get().text();
					if(priceJson == "{\"code\":{\"code\":0,\"message\":\"SUCCESS\"}}") {
						//System.out.println(failCount);
						throw new IOException();
					}
				} catch (IOException e) {
					failCount++;
					if(failCount >= this.maxRetry) {
						break;
					}
					continue;
				}
			}
			//System.out.println(priceJson);
			content.put("count", Integer.parseInt(priceJson.substring(priceJson.indexOf(getterRule.COUNT_FIRST_KEYWORD) + getterRule.COUNT_OFFSET, priceJson.indexOf(getterRule.COUNT_SECOND_KEYWORD))));
		}
		if(getterRule.GOOD_TYPE.equals("js")) {
			String fId;
			String nId;
			Connection priceUrl;
			if(getterRule.GOOD_ID_NAME.indexOf("~") != -1) {
				nId = getterRule.GOOD_ID_NAME.split("~")[0];
				fId = getterRule.GOOD_ID_NAME.split("~")[1];
				if(getterRule.GOOD_HREF.indexOf("?") != -1) {
					priceUrl = Jsoup.connect(getterRule.GOOD_HREF + "&" + nId + "=" + fId + this.id);
				}
				else {
					priceUrl = Jsoup.connect(getterRule.GOOD_HREF + "?" + nId + "=" + fId + this.id);
				}
			}
			else {
				if(getterRule.GOOD_HREF.indexOf("?") != -1) {
					priceUrl = Jsoup.connect(getterRule.GOOD_HREF + "&" + getterRule.GOOD_ID_NAME + "=" + this.id);
					//System.out.println(getterRule.GOOD_HREF + "&" + getterRule.GOOD_ID_NAME + "=" + this.id);
				}
				else {
					priceUrl = Jsoup.connect(getterRule.GOOD_HREF + "?" + getterRule.GOOD_ID_NAME + "=" + this.id);
					//System.out.println(getterRule.GOOD_HREF + "?" + getterRule.GOOD_ID_NAME + "=" + this.id);
				}
			}
			if(!getterRule.GOOD_REFERRER.isEmpty()) {
				if(getterRule.GOOD_REFERRER.equals("self")) {
					priceUrl.referrer(this.url);
				}
				if(getterRule.GOOD_REFERRER.equals("first-page")) {
					priceUrl.referrer(crawlerRule.FIRST_PAGE);
				}
			}
			priceUrl.userAgent("Chrom").timeout(3000).ignoreContentType(true);
			String priceJson = new String();
			int failCount = 0;
			while(priceJson.isEmpty()) {
				try {
					priceJson = priceUrl.get().text();
				} catch (IOException e) {
					failCount++;
					if(failCount >= this.maxRetry) {
						break;
					}
					continue;
				}
			}
			//System.out.println(priceJson);
			content.put("good", Integer.parseInt(priceJson.substring(priceJson.indexOf(getterRule.GOOD_FIRST_KEYWORD) + getterRule.GOOD_OFFSET, priceJson.indexOf(getterRule.GOOD_SECOND_KEYWORD))));

		}
		if(getterRule.NORMAL_TYPE.equals("js")) {
			String fId;
			String nId;
			Connection priceUrl;
			if(getterRule.NORMAL_ID_NAME.indexOf("~") != -1) {
				nId = getterRule.NORMAL_ID_NAME.split("~")[0];
				fId = getterRule.NORMAL_ID_NAME.split("~")[1];
				if(getterRule.NORMAL_HREF.indexOf("?") != -1) {
					priceUrl = Jsoup.connect(getterRule.NORMAL_HREF + "&" + nId + "=" + fId + this.id);
				}
				else {
					priceUrl = Jsoup.connect(getterRule.NORMAL_HREF + "?" + nId + "=" + fId + this.id);
				}
			}
			else {
				if(getterRule.NORMAL_HREF.indexOf("?") != -1) {
					priceUrl = Jsoup.connect(getterRule.NORMAL_HREF + "&" + getterRule.NORMAL_ID_NAME + "=" + this.id);
				}
				else {
					priceUrl = Jsoup.connect(getterRule.NORMAL_HREF + "?" + getterRule.NORMAL_ID_NAME + "=" + this.id);
				}
			}
			if(!getterRule.NORMAL_REFERRER.isEmpty()) {
				if(getterRule.NORMAL_REFERRER.equals("self")) {
					priceUrl.referrer(this.url);
				}
				if(getterRule.NORMAL_REFERRER.equals("first-page")) {
					priceUrl.referrer(crawlerRule.FIRST_PAGE);
				}
			}
			priceUrl.userAgent("Chrom").timeout(3000).ignoreContentType(true);
			String priceJson = new String();
			int failCount = 0;
			while(priceJson.isEmpty()) {
				try {
					priceJson = priceUrl.get().text();
				} catch (IOException e) {
					failCount++;
					if(failCount >= this.maxRetry) {
						break;
					}
					continue;
				}
			}
			content.put("normal", Integer.parseInt(priceJson.substring(priceJson.indexOf(getterRule.NORMAL_FIRST_KEYWORD) + getterRule.NORMAL_OFFSET, priceJson.indexOf(getterRule.NORMAL_SECOND_KEYWORD))));
		}
		if(getterRule.BAD_TYPE.equals("js")) {
			String fId;
			String nId;
			Connection priceUrl;
			if(getterRule.BAD_ID_NAME.indexOf("~") != -1) {
				nId = getterRule.BAD_ID_NAME.split("~")[0];
				fId = getterRule.BAD_ID_NAME.split("~")[1];
				if(getterRule.BAD_HREF.indexOf("?") != -1) {
					priceUrl = Jsoup.connect(getterRule.BAD_HREF + "&" + nId + "=" + fId + this.id);
				}
				else {
					priceUrl = Jsoup.connect(getterRule.BAD_HREF + "?" + nId + "=" + fId + this.id);
				}
			}
			else {
				if(getterRule.BAD_HREF.indexOf("?") != -1) {
					priceUrl = Jsoup.connect(getterRule.BAD_HREF + "&" + getterRule.BAD_ID_NAME + "=" + this.id);
				}
				else {
					priceUrl = Jsoup.connect(getterRule.BAD_HREF + "?" + getterRule.BAD_ID_NAME + "=" + this.id);
				}
			}
			if(!getterRule.BAD_REFERRER.isEmpty()) {
				if(getterRule.BAD_REFERRER.equals("self")) {
					priceUrl.referrer(this.url);
				}
				if(getterRule.BAD_REFERRER.equals("first-page")) {
					priceUrl.referrer(crawlerRule.FIRST_PAGE);
				}
			}
			priceUrl.userAgent("Chrom").timeout(3000).ignoreContentType(true);
			String priceJson = new String();
			int failCount = 0;
			while(priceJson.isEmpty()) {
				try {
					priceJson = priceUrl.get().text();
				} catch (IOException e) {
					failCount++;
					if(failCount >= this.maxRetry) {
						break;
					}
					continue;
				}
			}
			content.put("bad", Integer.parseInt(priceJson.substring(priceJson.indexOf(getterRule.BAD_FIRST_KEYWORD) + getterRule.BAD_OFFSET, priceJson.indexOf(getterRule.BAD_SECOND_KEYWORD))));
		}
		if(getterRule.ADD_TYPE.equals("js")) {
			String fId;
			String nId;
			Connection priceUrl;
			if(getterRule.ADD_ID_NAME.indexOf("~") != -1) {
				nId = getterRule.ADD_ID_NAME.split("~")[0];
				fId = getterRule.ADD_ID_NAME.split("~")[1];
				if(getterRule.ADD_HREF.indexOf("?") != -1) {
					priceUrl = Jsoup.connect(getterRule.ADD_HREF + "&" + nId + "=" + fId + this.id);
				}
				else {
					priceUrl = Jsoup.connect(getterRule.ADD_HREF + "?" + nId + "=" + fId + this.id);
				}
			}
			else {
				if(getterRule.ADD_HREF.indexOf("?") != -1) {
					priceUrl = Jsoup.connect(getterRule.ADD_HREF + "&" + getterRule.ADD_ID_NAME + "=" + this.id);
				}
				else {
					priceUrl = Jsoup.connect(getterRule.ADD_HREF + "?" + getterRule.ADD_ID_NAME + "=" + this.id);
				}
			}
			if(!getterRule.ADD_REFERRER.isEmpty()) {
				if(getterRule.ADD_REFERRER.equals("self")) {
					priceUrl.referrer(this.url);
				}
				if(getterRule.ADD_REFERRER.equals("first-page")) {
					priceUrl.referrer(crawlerRule.FIRST_PAGE);
				}
			}
			priceUrl.userAgent("Chrom").timeout(3000).ignoreContentType(true);
			String priceJson = new String();
			int failCount = 0;
			while(priceJson.isEmpty()) {
				try {
					priceJson = priceUrl.get().text();
				} catch (IOException e) {
					failCount++;
					if(failCount >= this.maxRetry) {
						break;
					}
					continue;
				}
			}
			content.put("add", Integer.parseInt(priceJson.substring(priceJson.indexOf(getterRule.ADD_FIRST_KEYWORD) + getterRule.ADD_OFFSET, priceJson.indexOf(getterRule.ADD_SECOND_KEYWORD))));
		}
		if(getterRule.RATE_TYPE.equals("js")) {
			String fId;
			String nId;
			Connection priceUrl;
			if(getterRule.RATE_ID_NAME.indexOf("~") != -1) {
				nId = getterRule.RATE_ID_NAME.split("~")[0];
				fId = getterRule.RATE_ID_NAME.split("~")[1];
				if(getterRule.RATE_HREF.indexOf("?") != -1) {
					priceUrl = Jsoup.connect(getterRule.RATE_HREF + "&" + nId + "=" + fId + this.id);
				}
				else {
					priceUrl = Jsoup.connect(getterRule.RATE_HREF + "?" + nId + "=" + fId + this.id);
				}
			}
			else {
				if(getterRule.RATE_HREF.indexOf("?") != -1) {
					priceUrl = Jsoup.connect(getterRule.RATE_HREF + "&" + getterRule.RATE_ID_NAME + "=" + this.id);
				}
				else {
					priceUrl = Jsoup.connect(getterRule.RATE_HREF + "?" + getterRule.RATE_ID_NAME + "=" + this.id);
				}
			}
			if(!getterRule.RATE_REFERRER.isEmpty()) {
				if(getterRule.RATE_REFERRER.equals("self")) {
					priceUrl.referrer(this.url);
				}
				if(getterRule.RATE_REFERRER.equals("first-page")) {
					priceUrl.referrer(crawlerRule.FIRST_PAGE);
				}
			}
			priceUrl.userAgent("Chrom").timeout(3000).ignoreContentType(true);
			String priceJson = new String();
			int failCount = 0;
			while(priceJson.isEmpty()) {
				try {
					priceJson = priceUrl.get().text();
				} catch (IOException e) {
					failCount++;
					if(failCount >= this.maxRetry) {
						break;
					}
					continue;
				}
			}
			Double rate = 0.0;
			rate = Double.parseDouble(((priceJson.substring(priceJson.indexOf(getterRule.RATE_FIRST_KEYWORD) + getterRule.RATE_OFFSET, priceJson.indexOf(getterRule.RATE_SECOND_KEYWORD)))));
			content.put("rate", rate);
		}
		return content;
	}
	
	public String getUrlId() {
		return this.id;
	}
	
	public String getUrl() {
		return this.url;
	}

	public CrawlerThread() {
		this.jedis = new Jedis("localhost");
		this.url = null;
		crawlerRule = new CrawlerRule();
		content = new HashMap<String, Object>();
		this.isPasued = new AtomicBoolean(false);
		this.isStoped = new AtomicBoolean(false);
		this.maxRetry = 10;
	}
	
	public CrawlerThread(String crawlerRulePath, String getterRulePath) {
		this.jedis = new Jedis("localhost");
		this.url = null;
		crawlerRule = new CrawlerRule(crawlerRulePath);
		getterRule = new GetterRule(getterRulePath);
		content = new HashMap<String, Object>();
		this.isPasued = new AtomicBoolean(false);
		this.isStoped = new AtomicBoolean(false);
		this.maxRetry = 10;
		this.dbOprate = new DBOprate();
		this.isFinished = new AtomicBoolean(false);
	}
	
	public int getMaxRetry() {
		return this.maxRetry;
	}
	
	public void setMaxRetry(int max) {
		this.maxRetry = max;
	}
	
	public void pause() {
		this.isPasued.set(true);
	}
	
	public void unpause() {
		this.isPasued.set(false);
	}
	
	public void shutdown() {
		this.isStoped.set(true);
	}
	
	@Override
	public void run() {
		com.mysql.jdbc.Connection con = null;
		try {
			con = DBOprate.getConnection(
					"jdbc:mysql://localhost:3306/zr?useUnicode=true&characterEncoding=utf-8", "root", "toor");
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		while(true) {
			if(isPasued.get()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(isStoped.get()) {
				break;
			}
			if(jedis.llen("CIDS") != 0) {
				this.id = jedis.rpop("CIDS");
			}
			else {
				break;
			}
			if(this.id.isEmpty()) {
				continue;
			}
			try {
				getContent(Jsoup.connect(setUrl()).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 UBrowser/6.1.2107.204 Safari/537.36").timeout(3000).get(), crawlerRule, getterRule);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			content.put("rate", 0.0);
			if(viewport != null) {
				viewport.updateInfoList(content.get("name").toString());
			}
			if(CheckIsExist.checkIsExist(content)) {
				;
			}
			else{
				DBOprate.Insert(content, con);
			}
		}
		System.out.println("Over");
		this.isFinished.set(true);
		while(!isStoped.get()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	void setViewport(CrawlerGUI crawlerGUI) {
		this.viewport = crawlerGUI;
	}
	
	public boolean isFinished() {
		return this.isFinished.get();
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub


	}

}
