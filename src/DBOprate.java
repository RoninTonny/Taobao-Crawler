
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.management.monitor.StringMonitor;


import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class DBOprate {

	
	static boolean k = false;

	static boolean check(String url, String user, String pswd, String name)
			throws ClassNotFoundException, SQLException {
		Connection con = getConnection(url, user, pswd);
		;
		java.sql.Statement st1 = con.createStatement();

		ResultSet slt;

		String sql = " select * from information_schema.schemata where schema_name='" + name + "';";
		slt = st1.executeQuery(sql);
		try {
			System.out.println(slt.getString(0));
			k = true;

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("创建数据库失败");
		}
		if (!k) {
			sql = "create database  " + name+" CHARACTER SET utf8 COLLATE utf8_general_ci";
			st1.execute(sql);
			System.out.println("成功创建数据库");
			con.close();
			Connection con1 = getConnection(
					"jdbc:mysql://localhost:3306/" + name + "?useUnicode=true&characterEncoding=utf-8", "root", "toor");
			Statement st = (Statement) con1.createStatement();
			System.out.println("成功连接新创建的数据库!");// ������������
			sql = "create table category (" + "ID varchar(40)  primary key," + "Num varchar(10) " + ")";

			// String sql="create database "+name +" if not exists "+name
			st.executeUpdate(sql);
			// System.out.println("�ɹ�������");
			// ��ױ
			sql = "create table Cosmetic(" + "ID varchar(40)  primary key," + "Num varchar(10)" + ")";

			st.executeUpdate(sql);
			// ĸӤ
			sql = "create table M_and_B(" + "ID varchar(40) primary key," + "Num varchar(10)" + ")";

			st.executeUpdate(sql);
			// ��װ
			sql = "create table M_clothes(" + "ID varchar(40)  primary key," + "Num varchar(10)" + ")";

			st.executeUpdate(sql);
			// Ůװ
			sql = "create table G_clothes(" + "ID varchar(40)  primary key," + "Num varchar(10)" + ")";

			st.executeUpdate(sql);
			// ����
			sql = "create table Other(" + "ID varchar(40)  primary key," + "Num varchar(10)" + ")";

			st.executeUpdate(sql);
			// ʳƷ
			sql = "create table Food(" + "ID varchar(40) primary key," + "Num varchar(10)" + ")";

			st.executeUpdate(sql);
			// ��Ʒ
			sql = "create table Decoration(" + "ID varchar(40) primary key," + "Num varchar(10)" + ")";

			st.executeUpdate(sql);
			// ����ҵ�
			sql = "create table DigitalAppliance(" + "ID varchar(40)  primary key," + "Num varchar(10)" + ")";

			st.executeUpdate(sql);
			// ͼ��
			sql = "create table Book(" + "ID varchar(40)  primary key," + "Num varchar(10)" + ")";

			st.executeUpdate(sql);
			// Ь��
			sql = "create table Shoes_Bag(" + "ID varchar(40)  primary key," + "Num varchar(10)" + ")";

			st.executeUpdate(sql);
			// �鱦
			sql = "create table ewelry(" + "ID varchar(10)  primary key," + "Num varchar(10)" + ")";

			st.executeUpdate(sql);
			// ��Ʒ����
			sql = "create table Link(" + "ID varchar(20)  primary key," + "link varchar(100) " + ")";

			st.executeUpdate(sql);
			// ��Ʒ����
			sql = "create table Attributes(" + "ID varchar(100) primary key,"// ��Ʒ���
					+ "Name varchar(100) not null,"// ��Ʒ����
					+ "Price double,"// �۸�
					+ "ShopName varchar(100),"// ������
					+ "SaleVolume float,"// ����
					+ "PraiseNum float,"// ������
					+ "MediumNum float,"// ������
					+ "BadNum float,"// ������
					+ "Origin varchar(100),"// ����
					+ "Category varchar(10),"// �����
					+ "Smallcate varchar(10),"// С���
					+ "Sore float,"// ����
					+ "Source varchar(10)"// ��Դ
					+ ")";

			st.executeUpdate(sql);
			// ������ͼ��
			sql = "create view TaoBao as "
					+ "select ID,Name,Price,ShopName,SaleVolume,PraiseNum,MediumNum,BadNum,Origin,Category "
					+ " from Attributes " + " where Source like  " + "'taobao'";
			;

			st.executeUpdate(sql);
			sql = "create view TianMao as " + " select ID,Name,Price,ShopName,SaleVolume,Origin,Category"
					+ " from Attributes " + " where Source like " + " 'tianmao'";

			st.executeUpdate(sql);
			sql = "create view JingDong  as " + " select ID,Name,Price,ShopName,SaleVolume,Origin,Category,Sore"
					+ " from Attributes " + " where Source like " + " 'jingdong'";

			st.executeUpdate(sql);
			sql = "create view DangDang as "
					+ " select ID,Name,Price,ShopName,SaleVolume,PraiseNum,MediumNum,BadNum,Origin,Category,Sore"
					+ " from Attributes " + " where Source like " + " 'dangdang'";

			st.executeUpdate(sql);

			System.out.println("成功创建视图");
			sql = "create trigger tgr_1" + " after insert" + " on attributes" + " for each row"

					+ " begin" + " if (new.Category='C1')" + " then "
					+ " insert into  g_clothes  values(new.ID,new.Smallcate);" + " end if;" + "  if(new.Category='C2')"
					+ " then" + " insert into  M_clothes  values(new.ID,new.Smallcate);" + " end if;"
					+ "  if(new.Category='C3')" + " then" + " insert into  Shoes_Bag  values(new.ID,new.Smallcate);"
					+ " end if;" + "  if(new.Category='C4')" + " then"
					+ " insert into  Cosmetic  values(new.ID,new.Smallcate);" + " end if;" + "  if(new.Category='C5')"
					+ " then" + " insert into  DigitalAppliance  values(new.ID,new.Smallcate);" + " end if;"
					+ "  if(new.Category='C6')" + " then" + " insert into  M_and_B  values(new.ID,new.Smallcate);"
					+ " end if;" + "  if(new.Category='C7')" + " then"
					+ " insert into  Decoration  values(new.ID,new.Smallcate);" + " end if;" + "  if(new.Category='C8')"
					+ " then" + " insert into  Food  values(new.ID,new.Smallcate);" + " end if;"
					+ "  if(new.Category='C9')" + " then" + " insert into  Book  values(new.ID,new.Smallcate);"
					+ " end if;" + "  if(new.Category='C10')" + " then"
					+ " insert into  Jewelry   values(new.ID,new.Smallcate);" + " end if;" + "  if(new.Category='C11')"
					+ " then" + " insert into   Other  values(new.ID,new.Smallcate);" + " end if;" + " end;";
			sql = "create trigger tgr_2" + " after update" + " on attributes" + " for each row"

					+ " begin" + " if (updated.Category='C1')" + " then "
					+ " update  g_clothes set smallcate = updated.smallcate where updated.ID = g_clothes.ID  ;"
					+ " end if;" + "  if(updated.Category='C2')" + " then"
					+ " update  M_clothes set smallcate = updated.smallcate where updated.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(updated.Category='C3')" + " then"
					+ " update  Shoes_Bag set smallcate = updated.smallcate where updated.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(updated.Category='C4')" + " then"
					+ " update  Cosmetic set smallcate = updated.smallcate where updated.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(updated.Category='C5')" + " then"
					+ " update   DigitalAppliance set smallcate = updated.smallcate where updated.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(updated.Category='C6')" + " then"
					+ " update   M_and_B set smallcate = updated.smallcate where updated.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(updated.Category='C7')" + " then"
					+ " update  Decoration set smallcate = updated.smallcate where updated.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(updated.Category='C8')" + " then"
					+ " update   Food set smallcate = updated.smallcate where updated.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(updated.Category='C9')" + " then"
					+ " update   Book set smallcate = updated.smallcate where updated.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(updated.Category='C10')" + " then"
					+ " update   Jewelry set smallcate = updated.smallcate where updated.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(updated.Category='C11')" + " then"
					+ " update    Other set smallcate = updated.smallcate where updated.ID = g_clothes.ID  ;"

					+ " end if;" + " end;";

			st.executeUpdate(sql);
			sql = "create trigger tgr_3" + " after delete " + " on attributes  " + " for each row "

					+ " begin " + " if (old.Category='C1')" + " then "
					+ " delete from   g_clothes  where old.ID = g_clothes.ID  ;" + " end if;"
					+ "  if(old.Category='C2')" + " then" + " delete from   M_clothes  where old.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(old.Category='C3')" + " then"
					+ " delete from   Shoes_Bag   where old.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(old.Category='C4')" + " then"
					+ " delete from   Cosmetic  where old.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(old.Category='C5')" + " then"
					+ " delete from   DigitalAppliance  where old.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(old.Category='C6')" + " then"
					+ " delete from    M_and_B  where old.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(old.Category='C7')" + " then"
					+ " delete from    Decoration  where old.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(old.Category='C8')" + " then"
					+ " delete from    Food   where old.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(old.Category='C9')" + " then"
					+ " delete from      Book  where old.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(old.Category='C10')" + " then"
					+ " delete from     Jewelry  where old.ID = g_clothes.ID  ;"

					+ " end if;" + "  if(old.Category='C11')" + " then"
					+ " delete from     Other  where old.ID = g_clothes.ID  ;"

					+ " end if;" + " end;";

			st.executeUpdate(sql);
			System.out.println("成功创建触发器");

			return true;
		}
		return k;

	}

	static Connection getConnection(String url, String user, String pswd) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("成功加载数据库");
		Connection con = (Connection) DriverManager.getConnection(url, user, pswd);
		System.out.println("成功连接数据库");

		return con;

	}

	static boolean Insert(Map<String, Object> map,Connection con){
		if(con!=null){
			String id,name,shop;
			double price,score;
			int count,good,bad,normal,add;
			id=(String)map.get("id");
			name = (String)map.get("name");
			shop=(String)map.get("shop");
			price=(double)map.get("price");
			count=(int)map.get("count");
			good=(int)map.get("good");
			bad=(int)map.get("bad");
			normal=(int)map.get("normal");
			score=(double)map.get("rate");
			String sql="insert into attributes(Id,Name,Price,ShopName,SaleVolume,PraiseNum,MediumNum,BadNum,Sore) values(?,?,?,?,?,?,?,?,?)";
			try {
				PreparedStatement s = (PreparedStatement) con.prepareStatement(sql);
				s.setString(1, id);
				s.setString(2, name);
				s.setDouble(3, price);
				s.setString(4, shop);
				s.setInt(5, count);
				s.setInt(6, good);
				s.setInt(7, normal);
				s.setInt(8, bad);
				s.setDouble(9, score);
				s.executeUpdate();
				System.out.println("成功插入数据");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		return false;
	}

	public static void main(String[] args) {
		try {
			try {
				String name = "zr";
				check("jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf-8", "root", "toor", name);
				Map map = new HashMap();
				map.put("id", "3423434");
				map.put("name", "长袖");
				map.put("price", 433.4);
				map.put("count", 4343);
				map.put("good", 434);
				map.put("normal", 434);
				map.put("bad", 434);
				map.put("rate", 4.5);
				Connection con = getConnection("jdbc:mysql://localhost:3306/"+name+"?useUnicode=true&characterEncoding=utf-8", "root", "toor");
				//Insert(map, con);
				// credit�������е����ݿ�����
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println("" + e.toString());
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("" + e.toString());
			// e.printStackTrace();
		}
	}
}
