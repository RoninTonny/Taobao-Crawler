import java.io.File;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class GetterRule {
	
	public final String NAME_FIRST_KEYWORD;
	public final String NAME_SECOND_KEYWORD;
	public final String SHOP_FIRST_KEYWORD;
	public final String SHOP_SECOND_KEYWORD;
	public final String PRICE_REFERRER;
	public final String PRICE_FIRST_KEYWORD;
	public final String PRICE_SECOND_KEYWORD;
	public final String PRICE_HREF;
	public final int 	PRICE_OFFSET;
	public final String COUNT_REFERRER;
	public final String COUNT_FIRST_KEYWORD;
	public final String COUNT_SECOND_KEYWORD;
	public final String COUNT_HREF;
	public final int 	COUNT_OFFSET;
	public final String GOOD_REFERRER;
	public final String GOOD_FIRST_KEYWORD;
	public final String GOOD_SECOND_KEYWORD;
	public final String GOOD_HREF;
	public final int 	GOOD_OFFSET;
	public final String NORMAL_REFERRER;
	public final String NORMAL_FIRST_KEYWORD;
	public final String NORMAL_SECOND_KEYWORD;
	public final String NORMAL_HREF;
	public final int 	NORMAL_OFFSET;
	public final String BAD_REFERRER;
	public final String BAD_FIRST_KEYWORD;
	public final String BAD_SECOND_KEYWORD;
	public final String BAD_HREF;
	public final int 	BAD_OFFSET;
	public final String ADD_REFERRER;
	public final String ADD_FIRST_KEYWORD;
	public final String ADD_SECOND_KEYWORD;
	public final String ADD_HREF;
	public final int 	ADD_OFFSET;
	public final String RATE_REFERRER;
	public final String RATE_FIRST_KEYWORD;
	public final String RATE_SECOND_KEYWORD;
	public final String RATE_HREF;
	public final int 	RATE_OFFSET;
	public final String NAME_TYPE;
	public final String SHOP_TYPE;
	public final String PRICE_TYPE;
	public final String COUNT_TYPE;
	public final String GOOD_TYPE;
	public final String NORMAL_TYPE;
	public final String BAD_TYPE;
	public final String ADD_TYPE;
	public final String RATE_TYPE;
	public final String PRICE_ID_NAME;
	public final String COUNT_ID_NAME;
	public final String GOOD_ID_NAME;
	public final String NORMAL_ID_NAME;
	public final String BAD_ID_NAME;
	public final String ADD_ID_NAME;
	public final String RATE_ID_NAME;
	
	private String name_first_keyword;
	private String name_second_keyword;
	private String shop_first_keyword;
	private String shop_second_keyword;
	private String price_referrer;
	private String price_first_keyword;
	private String price_second_keyword;
	private String price_href;
	private int	   price_offset;
	private String count_referrer;
	private String count_first_keyword;
	private String count_second_keyword;
	private String count_href;
	private int	   count_offset;
	private String good_referrer;
	private String good_first_keyword;
	private String good_second_keyword;
	private String good_href;
	private int	   good_offset;
	private String normal_referrer;
	private String normal_first_keyword;
	private String normal_second_keyword;
	private String normal_href;
	private int	   normal_offset;
	private String bad_referrer;
	private String bad_first_keyword;
	private String bad_second_keyword;
	private String bad_href;
	private int	   bad_offset;
	private String add_referrer;
	private String add_first_keyword;
	private String add_second_keyword;
	private String add_href;
	private int	   add_offset;
	private String rate_referrer;
	private String rate_first_keyword;
	private String rate_second_keyword;
	private String rate_href;
	private int	   rate_offset;
	private String name_type;
	private String shop_type;
	private String price_type;
	private String count_type;
	private String good_type;
	private String normal_type;
	private String bad_type;
	private String add_type;
	private String rate_type;
	private String price_id_name;
	private String count_id_name;
	private String good_id_name;
	private String normal_id_name;
	private String bad_id_name;
	private String add_id_name;
	private String rate_id_name;
	
	private String xmlPath;
	private SAXReader xmlReader;
	private Document xml;
	private Element rootElement;

	public GetterRule(String xmlPath) {
		// TODO Auto-generated constructor stub
		this.xmlPath = xmlPath;
		this.xmlReader = new SAXReader();
		try {
			this.xml = xmlReader.read(new File(xmlPath));
			rootElement = xml.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		this.init(rootElement);
		
		this.NAME_FIRST_KEYWORD 	= 		this.name_first_keyword;
		this.SHOP_FIRST_KEYWORD 	= 		this.shop_first_keyword;
		this.PRICE_FIRST_KEYWORD 	= 		this.price_first_keyword;
		this.COUNT_FIRST_KEYWORD 	= 		this.count_first_keyword;
		this.GOOD_FIRST_KEYWORD 	= 		this.good_first_keyword;
		this.NORMAL_FIRST_KEYWORD 	= 		this.normal_first_keyword;
		this.BAD_FIRST_KEYWORD 		= 		this.bad_first_keyword;
		this.ADD_FIRST_KEYWORD 		= 		this.add_first_keyword;
		this.RATE_FIRST_KEYWORD 	= 		this.rate_first_keyword;
		this.NAME_SECOND_KEYWORD 	= 		this.name_second_keyword;
		this.SHOP_SECOND_KEYWORD 	= 		this.shop_second_keyword;
		this.PRICE_SECOND_KEYWORD 	= 		this.price_second_keyword;
		this.COUNT_SECOND_KEYWORD 	= 		this.count_second_keyword;
		this.GOOD_SECOND_KEYWORD 	= 		this.good_second_keyword;
		this.NORMAL_SECOND_KEYWORD 	= 		this.normal_second_keyword;
		this.BAD_SECOND_KEYWORD 	= 		this.bad_second_keyword;
		this.ADD_SECOND_KEYWORD 	= 		this.add_second_keyword;
		this.RATE_SECOND_KEYWORD 	= 		this.rate_second_keyword;
		this.PRICE_REFERRER 		= 		this.price_referrer;
		this.COUNT_REFERRER 		= 		this.count_referrer;
		this.GOOD_REFERRER 			= 		this.good_referrer;
		this.NORMAL_REFERRER 		= 		this.normal_referrer;
		this.BAD_REFERRER 			= 		this.bad_referrer;
		this.ADD_REFERRER 			= 		this.add_referrer;
		this.RATE_REFERRER 			= 		this.rate_referrer;
		this.PRICE_HREF 			= 		this.price_href;
		this.COUNT_HREF 			= 		this.count_href;
		this.GOOD_HREF 				= 		this.good_href;
		this.NORMAL_HREF 			= 		this.normal_href;
		this.BAD_HREF 				= 		this.bad_href;
		this.ADD_HREF 				= 		this.add_href;
		this.RATE_HREF 				= 		this.rate_href;
		this.PRICE_OFFSET 			= 		this.price_offset;
		this.COUNT_OFFSET 			= 		this.count_offset;
		this.GOOD_OFFSET 			= 		this.good_offset;
		this.NORMAL_OFFSET 			= 		this.normal_offset;
		this.BAD_OFFSET 			= 		this.bad_offset;
		this.ADD_OFFSET 			= 		this.add_offset;
		this.RATE_OFFSET 			= 		this.rate_offset;
		this.NAME_TYPE 				= 		this.name_type;
		this.SHOP_TYPE 				= 		this.shop_type;
		this.PRICE_TYPE 			= 		this.price_type;
		this.COUNT_TYPE 			= 		this.count_type;
		this.GOOD_TYPE 				= 		this.good_type;
		this.NORMAL_TYPE 			= 		this.normal_type;
		this.BAD_TYPE 				= 		this.bad_type;
		this.ADD_TYPE 				= 		this.add_type;
		this.RATE_TYPE 				= 		this.rate_type;
		this.PRICE_ID_NAME 			= 		this.price_id_name;
		this.COUNT_ID_NAME 			= 		this.count_id_name;
		this.GOOD_ID_NAME 			= 		this.good_id_name;
		this.NORMAL_ID_NAME 		= 		this.normal_id_name;
		this.BAD_ID_NAME 			= 		this.bad_id_name;
		this.ADD_ID_NAME 			= 		this.add_id_name;
		this.RATE_ID_NAME 			= 		this.rate_id_name;
		
	}
	
	public String getXmlPath() {
		return this.xmlPath;
	}
	
    private void init(Element element){
    	if(element.getName() == "name") {
    		this.name_type = element.attribute(0).getText();
    	}
    	if(element.getName() == "shop") {
    		this.shop_type = element.attribute(0).getText();
    	}
    	if(element.getName() == "price") {
    		this.price_type = element.attribute(0).getText();
    	}
    	if(element.getName() == "count") {
    		this.count_type = element.attribute(0).getText();
    	}
    	if(element.getName() == "good") {
    		this.good_type = element.attribute(0).getText();
    	}
    	if(element.getName() == "normal") {
    		this.normal_type = element.attribute(0).getText();
    	}
    	if(element.getName() == "bad") {
    		this.bad_type = element.attribute(0).getText();
    	}
    	if(element.getName() == "add") {
    		this.add_type = element.attribute(0).getText();
    	}
    	if(element.getName() == "rate") {
    		this.rate_type = element.attribute(0).getText();
    	}
    	if(element.getName() == "first-keyword") {
    		if(element.getParent().getName() == "name") {
    			this.name_first_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "shop") {
    			this.shop_first_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "price") {
    			this.price_first_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "count") {
    			this.count_first_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "good") {
    			this.good_first_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "normal") {
    			this.normal_first_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "bad") {
    			this.bad_first_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "add") {
    			this.add_first_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "rate") {
    			this.rate_first_keyword = element.getText();
    		}
    	}
    	if(element.getName() == "second-keyword") {
    		if(element.getParent().getName() == "name") {
    			this.name_second_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "shop") {
    			this.shop_second_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "price") {
    			this.price_second_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "count") {
    			this.count_second_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "good") {
    			this.good_second_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "normal") {
    			this.normal_second_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "bad") {
    			this.bad_second_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "add") {
    			this.add_second_keyword = element.getText();
    		}
    		if(element.getParent().getName() == "rate") {
    			this.rate_second_keyword = element.getText();
    		}
    	}
    	if(element.getName() == "referrer") {
    		if(element.getParent().getName() == "price") {
    			this.price_referrer = element.getText();
    		}
    		if(element.getParent().getName() == "count") {
    			this.count_referrer = element.getText();
    		}
    		if(element.getParent().getName() == "good") {
    			this.good_referrer = element.getText();
    		}
    		if(element.getParent().getName() == "normal") {
    			this.normal_referrer = element.getText();
    		}
    		if(element.getParent().getName() == "bad") {
    			this.bad_referrer = element.getText();
    		}
    		if(element.getParent().getName() == "add") {
    			this.add_referrer = element.getText();
    		}
    		if(element.getParent().getName() == "rate") {
    			this.rate_referrer = element.getText();
    		}
    	}
    	if(element.getName() == "href") {
    		if(element.getParent().getName() == "price") {
    			this.price_href = element.getText();
    			this.price_id_name = element.attribute(0).getText();
    		}
    		if(element.getParent().getName() == "count") {
    			this.count_href = element.getText();
    			this.count_id_name = element.attribute(0).getText();
    		}
    		if(element.getParent().getName() == "good") {
    			this.good_href = element.getText();
    			this.good_id_name = element.attribute(0).getText();
    		}
    		if(element.getParent().getName() == "normal") {
    			this.normal_href = element.getText();
    			this.normal_id_name = element.attribute(0).getText();
    		}
    		if(element.getParent().getName() == "bad") {
    			this.bad_href = element.getText();
    			this.bad_id_name = element.attribute(0).getText();
    		}
    		if(element.getParent().getName() == "add") {
    			this.add_href = element.getText();
    			this.add_id_name = element.attribute(0).getText();
    		}
    		if(element.getParent().getName() == "rate") {
    			this.rate_href = element.getText();
    			this.rate_id_name = element.attribute(0).getText();
    		}
    	}
    	if(element.getName() == "offset" && !element.getText().isEmpty()) {
    		if(element.getParent().getName() == "price") {
    			this.price_offset = Integer.parseInt(element.getText());
    		}
    		if(element.getParent().getName() == "count") {
    			this.count_offset = Integer.parseInt(element.getText());
    		}
    		if(element.getParent().getName() == "good") {
    			this.good_offset = Integer.parseInt(element.getText());
    		}
    		if(element.getParent().getName() == "normal") {
    			this.normal_offset = Integer.parseInt(element.getText());
    		}
    		if(element.getParent().getName() == "bad") {
    			this.bad_offset = Integer.parseInt(element.getText());
    		}
    		if(element.getParent().getName() == "add") {
    			this.add_offset = Integer.parseInt(element.getText());
    		}
    		if(element.getParent().getName() == "rate") {
        		this.rate_offset = Integer.parseInt(element.getText());
    		}
    	}
        Iterator<Element> iterator = element.elementIterator();  
        while(iterator.hasNext()){  
            Element e = iterator.next();  
            init(e);
        }
    }  

}
