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
	
	//1、构造函数，就是得到数据库连接
	public DBOperation(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection(dbname,user,password);
		}catch(SQLException e){
			System.out.println("数据库连接失败");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/* author:Bill
	 * 将【商品详情】映射并写入数据库
	 * */
	public void DBmapping(String link,Map<String, String> map)
	{
		//定义变量
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
			//先插入主键，后面方便更新
			if(s.equals("型号"))
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
				System.out.println("找不到主键，出错了！！");
				return ;		//找不到主键，出错
			}
			
			try {
				
				if(stmt == null)
					stmt = (Statement)conn.createStatement();
				
				//检查主键是否已存在
				sql = "SELECT * FROM shoes where goods_id = \"" + goodsId + "\";";
				//System.out.println(sql);
				rs = stmt.executeQuery(sql);
				if(!rs.next()){
					//不存在，插入主键
					sql = "insert into shoes(goods_id) values(\"" + goodsId + "\");";
					stmt.executeUpdate(sql);
	    		}
				
			   System.out.println("goods_id == "+goodsId);
			} catch (Exception e) {
				
				System.out.println("goods_id插入失败。。");
				e.printStackTrace();
			   //将数据回滚  
			   try{
			      conn.rollback();
			   }catch(Exception e1){  
			   }
				// TODO: handle exception
			}finally{
				//this.close();		//这里不应当就close，否则后面不能用连接了
			}
		}//第一个for结束
		
		
		
		
		//因为已有主键，正式写入数据库
//		System.out.println(link);
		//link和map都传送过来，准备进行数据库写操作
		for(String s:map.keySet()){
			
			if(s.equals("内里材质")){
				//多对多
				innerMaterial = map.get(s);
				this.writeDBnTon(goodsId,innerMaterial,"inner_material","shoes_inner_material",
						"inner_material_id","inner_material_name","shoes_inner_material_id");
			}
			
			else if(s.equals("price")){
				//一对多
				//这个要存入online_store表，顺便存入link，这个表有点特殊，必须单独存
				price = map.get(s);
				//因为URL做主键，直接插入即可
				sql = "insert into online_store(online_url,goods_id,price,img_url) values(\""+link+"\",\""+goodsId+"\","+price+",\""+imgUrl+"\");";
				System.out.println(sql);
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			else if(s.equals("品牌")){
				//这个是典型的多对一关系
				brand = map.get(s);
				this.writeDBnToone(goodsId,brand,"brand", "brand_id", "brand_name");
			}
			
			else if(s.equals("类型")){
				//多对多
				hotPoint = map.get(s);
				this.writeDBnTon(goodsId,hotPoint,"hot_point","shoes_hot",
						"hot_point_id","hot_point_name","shoes_hot_id");
			}
			
			else if(s.equals("鞋面材质") || s.equals("材质")){
				//多对一
				upperMaterial = map.get(s);
				this.writeDBnToone(goodsId,upperMaterial,"upper_material", "upper_material_id", "upper_material_name");
			}
			
			else if(s.equals("风格")){
				//多对多
				style = map.get(s);
				this.writeDBnTon(goodsId,style,"style","shoes_style",
						"style_id","style_name","shoes_style_id");
			}
			
			else if(s.equals("跟型")){
				//作一对一
				heelStyle = map.get(s);
				this.writeDBoneToone(goodsId, heelStyle, "heel_style");
			}
			
			else if(s.equals("流行元素")){
				//多对多
				fashion = map.get(s);
				this.writeDBnTon(goodsId,fashion,"fashion","shoes_fashion",
						"fashion_id","fashion_name","shoes_fashion_id");
			}
			
			else if(s.equals("单鞋开口")){
				//作一对一
				upperHeight = map.get(s);
				this.writeDBoneToone(goodsId, upperHeight, "upper_height");
			}
			
			else if(s.equals("跟高")){
				//作一对一
				heelHeight = map.get(s);
				this.writeDBoneToone(goodsId, heelHeight, "heel_height");
			}
			
			else if(s.equals("女鞋头款")){
				//作一对一
				toe = map.get(s);
				this.writeDBoneToone(goodsId, toe, "toe");
			}
			
			else if(s.equals("场合")){
				//多对一
				occasion = map.get(s);
				this.writeDBnToone(goodsId,occasion,"occasion", "occasion_id", "occasion_name");
			}
			
			else if(s.equals("季节")){
				//作一对一
				season = map.get(s);
				this.writeDBoneToone(goodsId, season, "season");
			}
			
			else if(s.equals("图案")){
				//多对一
				pattern = map.get(s);
				this.writeDBnToone(goodsId,pattern,"pattern", "pattern_id", "pattern_name");
			}
			
		}
		
	}
	
	
	
	
	
	//处理n对n关系的表
	private void writeDBnTon(String goodsId,String content,
			String entityTableName,String relationTableName,
			String entityIdName,String entityName,String relationIdName) {
		//专门用来处理多对多关系的函数，参数分别为   1、goodsId 2、要插入到实体表中的内容【重要！！一个map】
		//3、实体表和关系表名	4、实体id名和实体名       5、关系表Id名
		
		//如果为空，创建一个Statement
		try {
			if(stmt == null)
				stmt = (Statement)conn.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(content);
		//将content的多个内容拆开
		String s[] = content.split(" 　");
		
		int entityId=-1;
//		int relationId=-1;
		for (int i = 0; i < s.length; i++) {
			
			try {
				//查询是否已经存在
				sql = "SELECT "+entityIdName+" FROM "+entityTableName+" where "+entityName+" = \"" + s[i] + "\";";
				System.out.println("多对多sql == "+sql);
				
				rs = stmt.executeQuery(sql);
				
				//如果已存在，直接找出实体Id
				if(rs.next()){
	    			//请注意，一定要rs.next()，不然就差不到getInt    			
					entityId=rs.getInt(1);
					//System.out.println("已存在实体id == "+entityId);
	    		}else{
	    			//如果不存在，插入实体表
					sql = "insert into "+entityTableName+"("+entityName+") values(\"" + s[i] + "\");";
					//System.out.println("实体表没有记录: "+sql);
					stmt.executeUpdate(sql);
					
					//插入后找实体Id
					rs = stmt.executeQuery("SELECT MAX("+entityIdName+") from "+entityTableName+";");
					if(rs.next())
						entityId=rs.getInt(1);
					//System.out.println("插入后实体id == " + entityId);
	    		}
				
				
				
				//插入关系表
				//查询是否已经存在
				sql = "SELECT "+relationIdName+" FROM "+relationTableName+" where "+entityIdName+" = " + entityId + " and goods_id = \""+goodsId+"\";";
				//System.out.println(sql);
				
				rs = stmt.executeQuery(sql);
				//如果已存在，直接找出关系表Id
				if(rs.next()){
//					relationId=rs.getInt(1);
					//System.out.println("已存在关系表id == "+relationId);
	    		}else{
	    			//如果不存在，插入关系表
	    			sql = "insert into "+relationTableName+"(goods_id,"+entityIdName+") values(\""+ goodsId + "\","+ + entityId + ");";
					System.out.println("关系表没有记录: "+sql);
					stmt.executeUpdate(sql);
					
					//插入后找实体Id
					rs = stmt.executeQuery("SELECT MAX("+entityIdName+") from "+entityTableName+";");
//					if(rs.next())
//						relationId=rs.getInt(1);
					//System.out.println("插入后 关系表id == " + relationId);
	    		}
				
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//try-catch
			
		}//for
		
		
	}
	
	
	private void writeDBnToone(String goodsId,String content,String entityTableName,String entityIdName,String entityName ) {
		//参数依次是：1、主键     2、待插入的实体content   3、实体表   4、实体Id名    5、实体名
		
		int entityId=-1;
		try {
			
			//如果为空，创建一个Statement
			if(stmt == null)
				stmt = (Statement)conn.createStatement();
			
			//查询是否已经存在
			sql = "SELECT "+entityIdName+" FROM "+entityTableName+" where "+entityName+" = \"" + content + "\";";
			System.out.println("多对一sql == "+sql);
			
			rs = stmt.executeQuery(sql);
			
			//如果已存在，直接找出实体Id
			if(rs.next()){
    			//请注意，一定要rs.next()，不然就差不到getInt    			
				entityId=rs.getInt(1);
				//System.out.println("已存在实体id == "+entityId);
    		}else{
    			//如果不存在，插入实体表
				sql = "insert into "+entityTableName+"("+entityName+") values(\"" + content + "\");";
				//System.out.println("实体表没有记录: "+sql);
				stmt.executeUpdate(sql);
				
				//插入后找实体Id
				rs = stmt.executeQuery("SELECT MAX("+entityIdName+") from "+entityTableName+";");
				if(rs.next())
					entityId=rs.getInt(1);
				//System.out.println("插入后实体id == " + entityId);
    		}
			
			//更新主表
			sql = "update shoes set "+entityIdName+"= "+entityId+" where goods_id=\""+goodsId+"\";";
			stmt.executeUpdate(sql);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void writeDBoneToone(String goodsId,String content,String filedName) {
		//参数：1、主键   2、插入内容   3、字段名
		
		sql = "update shoes set "+filedName+" = \""+content+"\" where goods_id=\""+goodsId+"\";";
		System.out.println("一对一sql == "+sql);
		
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//关闭资源函数
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
