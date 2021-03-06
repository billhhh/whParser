package com.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.htmlparser.util.ParserException;


import com.DBOperation.DBOperation;
import com.Parser.*;

public class test {

	private static int cnt=0; //计算鞋子detail总数
	
	//main函数
	public static void main(String[] args) throws ClientProtocolException, IOException, ParserException, SQLException {
		String url = "http://category.dangdang.com/cid4001867.html";
//		String link = "http://category.dangdang.com/cid4001867-a1%3A2037.html";
		insertProLinkToDB(url);
//		parseProDetail("http://product.dangdang.com/1325150302.html#ddclick?act=click&pos=1325150302_0_1_m&cat=4001867&key=&qinfo=&pinfo=&minfo=92_1_58&ninfo=&custid=&permid=&ref=&rcount=&type=&t=1422366672000");
	}
	
	
	
	//最关键的一个函数
	public static void insertProLinkToDB(String url) throws ClientProtocolException, IOException, ParserException, SQLException{
		Map<String, String> map = new HashMap<String, String>(); //临时存放品牌 Url 的 hash表。。
		ArrayList<String> list = new ArrayList<String>();   
		ArrayList<String> brandlist = new ArrayList<String>();  //品牌list
		String nextpage = null;
		int pagenum = 0;
		int brandpagenum = 0;
		
		parsePage_Brand pB = new parsePage_Brand(url);
		parsePage_ProLink pP;
		map = pB.getBrandUrlMap();
		
		
		for(String brandUrl:map.keySet()){
			//将 map 中的brandUrl放入 brandlist中
			brandlist.add(brandUrl);
//			System.out.println(brandUrl);
		}
		
		
		//遍历 brandlist
		for(String s:brandlist){
			//遍历赋值给下一页
			nextpage = s;
			//System.out.println("中途输出nextpage测试："+nextpage);
			//nextpage中存的是brand
			while(!nextpage.equals("end")){
				pP = new parsePage_ProLink(nextpage);  //爬下一页的内容
				list = pP.getUrlList();  //每页brand的鞋子链接
				
				//test商品详情
				for(String urlStr : list){
					parseProDetail(urlStr);
				}
				
				list.clear();
				nextpage = pP.getNextPage();  //getNextPage是找到brand还有就继续while
				pagenum++;
				brandpagenum++;
			}
			
//			System.out.println("this brand have "+ brandpagenum +" page");
			brandpagenum = 0;//每个brand的num
		}
		
		System.out.println("end crawler.");
		System.out.println(pagenum);
	}
	
	
	
	
	/*
	 * 获取商品详情函数*/
	//得传入商品详情的link
	public static void parseProDetail(String link) throws ClientProtocolException, IOException, ParserException, SQLException{
		cnt++; //计算鞋子detail总数
		System.out.println("cnt == "+cnt);
		System.out.println(link);  //打印链接
		//新建hash表，表中放入对应鞋子属性和属性值
		Map<String, String> map = new HashMap<String, String>();
		//link放在构造方法中传入，返回另外要调用一个 getDetailListMap 函数
		
		parseProDetail pPD = new parseProDetail();
		pPD.setUrl(link);
		map = pPD.getDetailListMap();  //所有东东（详情+价格）都放入这个map
		
		
		//输出
		for(String s:map.keySet()){
			//输出属性和属性值，所有每一轮爬到的商品都在【s】映射表里面
			System.out.println(s + " == " + map.get(s));
		}
		System.out.println("end\n");
		
		
//		DBOperation dbo = new DBOperation();
//		dbo.DBmapping(link, map);
		
	}
	
	
	
	/* 已测试 ok
	 * 测试获取所有品牌网址函数*/
	public static void testParsePage_Brand(String url) throws ClientProtocolException, IOException, ParserException{
		parsePage_Brand p = new parsePage_Brand(url);
		Map<String, String> map = new HashMap<String, String>();
		map = p.getBrandUrlMap();
		for(String key:map.keySet()){
			System.out.println(key + "  " + map.get(key));
		}
	}
	
	
	/*测试获取页面所有鞋子链接函数*/
	public static void testParsePage_ProLink() throws ClientProtocolException, IOException, ParserException{
		String url = "http://category.dangdang.com/cid4001867-a1%3A2037-pg2.html";
		List<String> l = new ArrayList<String>();
		parsePage_ProLink p = new parsePage_ProLink(url);
		l = p.getUrlList();
		int i = 0;
		for(String u:l){
			System.out.println(u);
			i++;
		}
		System.out.println("共有："+i+"条记录。");
	}
	
	
}
