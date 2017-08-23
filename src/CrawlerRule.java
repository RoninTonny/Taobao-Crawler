import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class CrawlerRule {
	
	//read xml and set all attributes into Java final value
	
	public final String FIRST_PAGE;
	public final String SEARCH_PAGE;
	public final String SEARCH_PAGE_IGNORE;
	public final String SEARCH_PAGE_TYPE;
	public final String SEARCH_PAGE_FRONT_URL;
	public final String SEARCH_PAGE_TYPE_KEYWORD;
	public final String SEARCH_PAGE_TYPE_SECOND_KEYWORD;
	public final String SEARCH_PAGE_TYPE_METHOD;
	public final int	SEARCH_PAGE_INCREATE;
	public final String SEARCH_PAGE_INCREATE_NAME;
	public final int    SEARCH_PAGE_INCREATE_START;
	public final String SEARCH_PAGE_SEARCH_NAME;
	public final String SEARCH_PAGE_ENCODE;
	public final String SEARCH_PAGE_ENCODE_NAME;
	public final String SEARCH_PAGE_SORT;
	public final String SEARCH_PAGE_SORT_NAME;
	public final String INFO_PAGE;
	public final String INFO_PAGE_FRONT_URL;
	public final String INFO_PAGE_ID_NAME;
	public final String INFO_PAGE_BACK_URL;
	public final int	PAGE_COUNT;
	
	private String first_page;
	private String search_page;
	private String search_page_ignore;
	private String search_page_type;
	private String search_page_front_url;
	private String search_page_type_keyword;
	private String search_page_type_second_keyword;
	private String search_page_type_method;
	private int    search_page_increate;
	private String search_page_increate_name;
	private int    search_page_increate_start;
	private String search_page_search_name;
	private String search_page_encode;
	private String search_page_encode_name;
	private String search_page_sort;
	private String search_page_sort_name;
	private String info_page;
	private String info_page_front_url;
	private String info_page_id_name;
	private String info_page_back_url;
	private int    page_count;
	
	private String xmlPath;
	private SAXReader xmlReader;
	private Document xml;
	private Element rootElement;
	
	public CrawlerRule() {
		this.xmlReader = new SAXReader();
		this.xml = null;
		this.FIRST_PAGE	 						= "";
		this.SEARCH_PAGE 						= "";
		this.SEARCH_PAGE_IGNORE 				= "";
		this.SEARCH_PAGE_TYPE	 				= "";
		this.SEARCH_PAGE_FRONT_URL	 			= "";
		this.SEARCH_PAGE_TYPE_KEYWORD   		= "";
		this.SEARCH_PAGE_TYPE_SECOND_KEYWORD 	= "";
		this.SEARCH_PAGE_TYPE_METHOD 			= "";
		this.SEARCH_PAGE_INCREATE	 			=  0;
		this.SEARCH_PAGE_INCREATE_NAME			= "";
		this.SEARCH_PAGE_INCREATE_START			=  0;
		this.SEARCH_PAGE_SEARCH_NAME 			= "";
		this.SEARCH_PAGE_ENCODE 				= "";
		this.SEARCH_PAGE_ENCODE_NAME 			= "";
		this.INFO_PAGE 							= "";
		this.INFO_PAGE_FRONT_URL 				= "";
		this.INFO_PAGE_ID_NAME 					= "";
		this.INFO_PAGE_BACK_URL 				= "";
		this.SEARCH_PAGE_SORT    				= "";
		this.SEARCH_PAGE_SORT_NAME				= "";
		this.PAGE_COUNT							=  0;
	}
	
	//use xml's absolute path to init
	public CrawlerRule(String xmlPath) {
		this.xmlPath = xmlPath;
		this.xmlReader = new SAXReader();
		try {
			this.xml = xmlReader.read(new File(xmlPath));
			rootElement = xml.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		this.init(rootElement);
		this.FIRST_PAGE 						= this.first_page;
		this.SEARCH_PAGE 						= this.search_page;
		this.SEARCH_PAGE_IGNORE		    		= this.search_page_ignore;
		this.SEARCH_PAGE_TYPE					= this.search_page_type;
		this.SEARCH_PAGE_FRONT_URL 				= this.search_page_front_url;
		this.SEARCH_PAGE_TYPE_KEYWORD 			= this.search_page_type_keyword;
		this.SEARCH_PAGE_TYPE_SECOND_KEYWORD 	= this.search_page_type_second_keyword;
		this.SEARCH_PAGE_TYPE_METHOD 			= this.search_page_type_method;
		this.SEARCH_PAGE_INCREATE 				= this.search_page_increate;
		this.SEARCH_PAGE_INCREATE_NAME			= this.search_page_increate_name;
		this.SEARCH_PAGE_INCREATE_START			= this.search_page_increate_start;
		this.SEARCH_PAGE_SEARCH_NAME 			= this.search_page_search_name;
		this.SEARCH_PAGE_ENCODE 				= this.search_page_encode;
		this.SEARCH_PAGE_ENCODE_NAME 			= this.search_page_encode_name;
		this.INFO_PAGE 							= this.info_page;
		this.INFO_PAGE_FRONT_URL 				= this.info_page_front_url;
		this.INFO_PAGE_ID_NAME 					= this.info_page_id_name;
		this.INFO_PAGE_BACK_URL 				= this.info_page_back_url;
		this.SEARCH_PAGE_SORT					= this.search_page_sort;
		this.SEARCH_PAGE_SORT_NAME				= this.search_page_sort_name;
		this.PAGE_COUNT							= this.page_count;
	}
	
	//use this to get current xml's path outside this class
	public String getXmlPath() {
		return this.xmlPath;
	}
	
    private void init(Element element){  
        if(element.getName() == "first-page") {
        	this.first_page = element.getText();
        }
        if(element.getName() == "search-page") {
        	List<Attribute> attrList = element.attributes();  
            for(Attribute attribute : attrList) {  
                if(attribute.getName() == "ignore") {
                	this.search_page_ignore = attribute.getValue();
                }
                if(attribute.getName() == "type") {
                	this.search_page_type = attribute.getValue();
                }
            }
        }
        if(element.getName() == "front-url") {
        	if(element.getParent().getName() == "search-page"){
        		this.search_page_front_url = element.getText();
        	}
        	if(element.getParent().getName() == "info-page"){
        		this.info_page_front_url = element.getText();
        	}
    	}
    	if(element.getName() == "type") {

    	}
		if(element.getName() == "keyword") {
			this.search_page_type_keyword = element.getText();
		}
		if(element.getName() == "second-keyword") {
			this.search_page_type_second_keyword = element.getText();
		}
		if(element.getName() == "method") {
			this.search_page_type_method = element.getText();
		}
    	if(element.getName() == "page-increate") {
    		this.search_page_increate = Integer.parseInt(element.getText());
    	}
    	if(element.getName() == "increate-name") {
    		this.search_page_increate_name = element.getText();
    	}
    	if(element.getName() == "page-start") {
    		this.search_page_increate_start = Integer.parseInt(element.getText());
    	}
    	if(element.getName() == "search-name") {
    		this.search_page_search_name = element.getText();
    	}
    	if(element.getName() == "encode") {
    		this.search_page_encode = element.getText();
    	}
    	if(element.getName() == "encode-name") {
    		this.search_page_encode_name = element.getText();
    	}
        if(element.getName() == "info-page") {
        	this.info_page = element.getText();
        }
        if(element.getName() == "front-url") {
    		this.info_page_front_url = element.getText();
    	}
    	if(element.getName() == "id-name") {
    		this.info_page_id_name = element.getText();
    	}
    	if(element.getName() == "back-url") {
    		this.info_page_back_url= element.getText();
    	}
    	if(element.getName() == "page-count") {
    		this.page_count = Integer.parseInt(element.getText());
    	}
		if(element.getName() == "sort-name") {
			this.search_page_sort_name = element.getText();
		}
		if(element.getName() == "sort") {
			this.search_page_sort = element.getText();
		}
        Iterator<Element> iterator = element.elementIterator();  
        while(iterator.hasNext()){  
            Element e = iterator.next();  
            init(e);
        }
    }  
}
