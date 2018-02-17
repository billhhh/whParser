package com.DBOperation;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.mysql.jdbc.Statement;

public class DBOperation {
	
	Connection conn=null;
	String dbname = "jdbc:mysql://localhost:3306/whrsdbtesttest?useUnicode=true&characterEncoding=utf8";
	String user = "root";
	String password = "root";
	String sql=null;
	Statement stmt=null;
	ResultSet rs=null;
	
	//1�����캯�������ǵõ����ݿ�����
	public DBOperation(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection(dbname,user,password);
		}catch(SQLException e){
			System.out.println("���ݿ�����ʧ��");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/* author:Bill
	 * ������Ʒ���顿ӳ�䲢д�����ݿ�
	 * */
	public void DBmapping(String link,Map<String, String> map)
	{
		//�������
		String goodsId = null;
		String innerMaterial=null;
		String price=null;
		String imgUrl=null;
		String brand=null;
		String hotPoint=null;
		String upperMaterial=null;
		String style=null;
		String heelStyle = null;
		String fashion=null;
		String upperHeight=null;
		String heelHeight=null;
		String toe=null;
		String occasion=null;
		String season=null;
		String pattern=null;
		
		for(String s:map.keySet()){
			//�Ȳ������������淽�����
			if(s.equals("�ͺ�"))
				goodsId = map.get(s);
			else if(s.equals("imgUrl"))
			{
				imgUrl = map.get(s);
				continue;
			}
			else
				continue;
			
			if(goodsId == null)
			{
				System.out.println("�Ҳ��������������ˣ���");
				return ;		//�Ҳ�������������
			}
			
			try {
				
				if(stmt == null)
					stmt = (Statement)conn.createStatement();
				
				//��������Ƿ��Ѵ���
				sql = "SELECT * FROM shoes where goods_id = \"" + goodsId + "\";";
				//System.out.println(sql);
				rs = stmt.executeQuery(sql);
				if(!rs.next()){
					//�����ڣ���������
					sql = "insert into shoes(goods_id) values(\"" + goodsId + "\");";
					stmt.executeUpdate(sql);
	    		}
				
			   System.out.println("goods_id == "+goodsId);
			} catch (Exception e) {
				
				System.out.println("goods_id����ʧ�ܡ���");
				e.printStackTrace();
			   //�����ݻع�  
			   try{
			      conn.rollback();
			   }catch(Exception e1){  
			   }
				// TODO: handle exception
			}finally{
				//this.close();		//���ﲻӦ����close��������治����������
			}
		}//��һ��for����
		
		
		
		
		//��Ϊ������������ʽд�����ݿ�
//		System.out.println(link);
		//link��map�����͹�����׼���������ݿ�д����
		for(String s:map.keySet()){
			
			if(s.equals("�������")){
				//��Զ�
				innerMaterial = map.get(s);
				this.writeDBnTon(goodsId,innerMaterial,"inner_material","shoes_inner_material",
						"inner_material_id","inner_material_name","shoes_inner_material_id");
			}
			
			else if(s.equals("price")){
				//һ�Զ�
				//���Ҫ����online_store��˳�����link��������е����⣬���뵥����
				price = map.get(s);
				//��ΪURL��������ֱ�Ӳ��뼴��
				sql = "insert into online_store(online_url,goods_id,price,img_url) values(\""+link+"\",\""+goodsId+"\","+price+",\""+imgUrl+"\");";
				System.out.println(sql);
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			else if(s.equals("Ʒ��")){
				//����ǵ��͵Ķ��һ��ϵ
				brand = map.get(s);
				this.writeDBnToone(goodsId,brand,"brand", "brand_id", "brand_name");
			}
			
			else if(s.equals("����")){
				//��Զ�
				hotPoint = map.get(s);
				this.writeDBnTon(goodsId,hotPoint,"hot_point","shoes_hot",
						"hot_point_id","hot_point_name","shoes_hot_id");
			}
			
			else if(s.equals("Ь�����") || s.equals("����")){
				//���һ
				upperMaterial = map.get(s);
				this.writeDBnToone(goodsId,upperMaterial,"upper_material", "upper_material_id", "upper_material_name");
			}
			
			else if(s.equals("���")){
				//��Զ�
				style = map.get(s);
				this.writeDBnTon(goodsId,style,"style","shoes_style",
						"style_id","style_name","shoes_style_id");
			}
			
			else if(s.equals("����")){
				//��һ��һ
				heelStyle = map.get(s);
				this.writeDBoneToone(goodsId, heelStyle, "heel_style");
			}
			
			else if(s.equals("����Ԫ��")){
				//��Զ�
				fashion = map.get(s);
				this.writeDBnTon(goodsId,fashion,"fashion","shoes_fashion",
						"fashion_id","fashion_name","shoes_fashion_id");
			}
			
			else if(s.equals("��Ь����")){
				//��һ��һ
				upperHeight = map.get(s);
				this.writeDBoneToone(goodsId, upperHeight, "upper_height");
			}
			
			else if(s.equals("����")){
				//��һ��һ
				heelHeight = map.get(s);
				this.writeDBoneToone(goodsId, heelHeight, "heel_height");
			}
			
			else if(s.equals("ŮЬͷ��")){
				//��һ��һ
				toe = map.get(s);
				this.writeDBoneToone(goodsId, toe, "toe");
			}
			
			else if(s.equals("����")){
				//���һ
				occasion = map.get(s);
				this.writeDBnToone(goodsId,occasion,"occasion", "occasion_id", "occasion_name");
			}
			
			else if(s.equals("����")){
				//��һ��һ
				season = map.get(s);
				this.writeDBoneToone(goodsId, season, "season");
			}
			
			else if(s.equals("ͼ��")){
				//���һ
				pattern = map.get(s);
				this.writeDBnToone(goodsId,pattern,"pattern", "pattern_id", "pattern_name");
			}
			
		}
		
	}
	
	
	
	
	
	//����n��n��ϵ�ı�
	private void writeDBnTon(String goodsId,String content,
			String entityTableName,String relationTableName,
			String entityIdName,String entityName,String relationIdName) {
		//ר�����������Զ��ϵ�ĺ����������ֱ�Ϊ   1��goodsId 2��Ҫ���뵽ʵ����е����ݡ���Ҫ����һ��map��
		//3��ʵ���͹�ϵ����	4��ʵ��id����ʵ����       5����ϵ��Id��
		
		//���Ϊ�գ�����һ��Statement
		try {
			if(stmt == null)
				stmt = (Statement)conn.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(content);
		//��content�Ķ�����ݲ�
		String s[] = content.split(" ��");
		
		int entityId=-1;
//		int relationId=-1;
		for (int i = 0; i < s.length; i++) {
			
			try {
				//��ѯ�Ƿ��Ѿ�����
				sql = "SELECT "+entityIdName+" FROM "+entityTableName+" where "+entityName+" = \"" + s[i] + "\";";
				System.out.println("��Զ�sql == "+sql);
				
				rs = stmt.executeQuery(sql);
				
				//����Ѵ��ڣ�ֱ���ҳ�ʵ��Id
				if(rs.next()){
	    			//��ע�⣬һ��Ҫrs.next()����Ȼ�Ͳ��getInt    			
					entityId=rs.getInt(1);
					//System.out.println("�Ѵ���ʵ��id == "+entityId);
	    		}else{
	    			//��������ڣ�����ʵ���
					sql = "insert into "+entityTableName+"("+entityName+") values(\"" + s[i] + "\");";
					//System.out.println("ʵ���û�м�¼: "+sql);
					stmt.executeUpdate(sql);
					
					//�������ʵ��Id
					rs = stmt.executeQuery("SELECT MAX("+entityIdName+") from "+entityTableName+";");
					if(rs.next())
						entityId=rs.getInt(1);
					//System.out.println("�����ʵ��id == " + entityId);
	    		}
				
				
				
				//�����ϵ��
				//��ѯ�Ƿ��Ѿ�����
				sql = "SELECT "+relationIdName+" FROM "+relationTableName+" where "+entityIdName+" = " + entityId + " and goods_id = \""+goodsId+"\";";
				//System.out.println(sql);
				
				rs = stmt.executeQuery(sql);
				//����Ѵ��ڣ�ֱ���ҳ���ϵ��Id
				if(rs.next()){
//					relationId=rs.getInt(1);
					//System.out.println("�Ѵ��ڹ�ϵ��id == "+relationId);
	    		}else{
	    			//��������ڣ������ϵ��
	    			sql = "insert into "+relationTableName+"(goods_id,"+entityIdName+") values(\""+ goodsId + "\","+ + entityId + ");";
					System.out.println("��ϵ��û�м�¼: "+sql);
					stmt.executeUpdate(sql);
					
					//�������ʵ��Id
					rs = stmt.executeQuery("SELECT MAX("+entityIdName+") from "+entityTableName+";");
//					if(rs.next())
//						relationId=rs.getInt(1);
					//System.out.println("����� ��ϵ��id == " + relationId);
	    		}
				
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//try-catch
			
		}//for
		
		
	}
	
	
	private void writeDBnToone(String goodsId,String content,String entityTableName,String entityIdName,String entityName ) {
		//���������ǣ�1������     2���������ʵ��content   3��ʵ���   4��ʵ��Id��    5��ʵ����
		
		int entityId=-1;
		try {
			
			//���Ϊ�գ�����һ��Statement
			if(stmt == null)
				stmt = (Statement)conn.createStatement();
			
			//��ѯ�Ƿ��Ѿ�����
			sql = "SELECT "+entityIdName+" FROM "+entityTableName+" where "+entityName+" = \"" + content + "\";";
			System.out.println("���һsql == "+sql);
			
			rs = stmt.executeQuery(sql);
			
			//����Ѵ��ڣ�ֱ���ҳ�ʵ��Id
			if(rs.next()){
    			//��ע�⣬һ��Ҫrs.next()����Ȼ�Ͳ��getInt    			
				entityId=rs.getInt(1);
				//System.out.println("�Ѵ���ʵ��id == "+entityId);
    		}else{
    			//��������ڣ�����ʵ���
				sql = "insert into "+entityTableName+"("+entityName+") values(\"" + content + "\");";
				//System.out.println("ʵ���û�м�¼: "+sql);
				stmt.executeUpdate(sql);
				
				//�������ʵ��Id
				rs = stmt.executeQuery("SELECT MAX("+entityIdName+") from "+entityTableName+";");
				if(rs.next())
					entityId=rs.getInt(1);
				//System.out.println("�����ʵ��id == " + entityId);
    		}
			
			//��������
			sql = "update shoes set "+entityIdName+"= "+entityId+" where goods_id=\""+goodsId+"\";";
			stmt.executeUpdate(sql);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void writeDBoneToone(String goodsId,String content,String filedName) {
		//������1������   2����������   3���ֶ���
		
		sql = "update shoes set "+filedName+" = \""+content+"\" where goods_id=\""+goodsId+"\";";
		System.out.println("һ��һsql == "+sql);
		
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//�ر���Դ����
	private void close(){
		try {
			if(rs!=null){
				
				rs.close();
				rs=null;
			}
			
			if(stmt!=null){
				
				stmt.close();
				stmt=null;
			}
			
			if(!conn.isClosed()){
				conn.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
}
