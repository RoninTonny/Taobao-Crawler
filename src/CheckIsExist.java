

import java.util.HashMap;
import java.util.Map;

public class CheckIsExist {
	static BloomFilter bloomFilter = new BloomFilter();
	
public static boolean checkIsExist(Map map){
	// ID,Price,Name,SaleVolume,PraiseNum,MediumNum,BadNum,Sore,Add_ 
	//��Ʒid���۸�,��Ʒ��������������������������������������
	String id,name,shop;
	double price,rate;
	int count,good,normal,bad,add;
id=	(String) map.get("id");
name=(String)map.get("name");
price=(double)map.get("price");
//rate=(double)map.get("rate");
count=(int)map.get("count");
good=(int)map.get("good");
normal=(int)map.get("normal");
bad=(int)map.get("bad");
add=(int)map.get("add");
String date="";
date+=id+price+name+count+good+normal+bad+add;
if(	bloomFilter.contains(date))
	return true;
	return false;
}
public static void main(String[] args) {
	LoadMessage.Load(bloomFilter);
	Map map=new HashMap();
	
	map.put("id","8483076163");
	map.put("name", "顺丰包邮G4400升G4560家用办公台式diy组装机电脑主机兼容机整机");
	map.put("price",1768.0);
	map.put("count",160);
	map.put("good", 474);
	map.put("normal", 0);
	map.put("bad", 1);
	map.put("add", 0);
	//map.put("rate", 0.0);
	
	
	if(checkIsExist(map))
		System.out.println("存在");
	else 
		System.out.println("不存在");
}
}
