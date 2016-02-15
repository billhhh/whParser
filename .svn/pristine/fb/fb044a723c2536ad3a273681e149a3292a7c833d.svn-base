package com.Parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class parsePage_Brand {
	
	private String pageContent;
	private Map<String,String> brandUrlMap;  /*品牌和网址的键值对*/
	private String link;
	
	public parsePage_Brand(String url) throws ClientProtocolException, IOException{
		readPage page = new readPage(url);
		link = url;
		this.pageContent = page.getPageContent();	//得到url，立马去得到pageContent
	}
	
	public Map<String,String> getBrandUrlMap() throws ParserException{
		setBrandUrlList();  //要get必先set
		return this.brandUrlMap;
	}
	
	
	//set 品牌的url list表
	private void setBrandUrlList() throws ParserException{
		String brandUrlCache = null;
		String brandCache = null;
		this.brandUrlMap = new HashMap<String, String>();
		Parser p = new Parser(pageContent);
		
		//System.out.println("爬brand的页面URL == "+link);
		//System.out.println("pageContent == "+pageContent);
		
		/*各种过滤器*/
		//过滤出页面里面的 class=list 的
		//HasAttributeFilter brandListTag = new HasAttributeFilter("class","list");
		//其实直接过滤出有用信息就可以了
		HasAttributeFilter brandListTag = new HasAttributeFilter("class","brand_opt brand_height allbrand");
		
		NodeList nl = p.parse(brandListTag);
		//System.out.println("nlWithoutBrandListTag == "+nl);
		
		/*过滤掉空白标签*/
		NodeFilter spanTag = new TagNameFilter("span");
		//去掉包含 span 的所有元素
		
		//System.out.println("nl == "+nl.elementAt(0));  //这里elementAt(3)总会变，之前是elementAt(7)
		NodeList brands = nl.elementAt(0).getChildren().extractAllNodesThatMatch(spanTag);
		
//		System.out.println("请按任意键继续");
//		Scanner input = new Scanner(System.in);
//		input.next();
		
		TagNode tmp;
		/*提取各个品牌的链接*/
		for(int i = 0; i < brands.size()-1; i++){
			tmp = (TagNode) brands.elementAt(i).getChildren().elementAt(1);
//			System.out.println("每个brand： "+tmp.toPlainTextString());
			
			brandUrlCache = "http://category.dangdang.com" + tmp.getAttribute("href");
			brandCache = tmp.getAttribute("title");
			brandUrlMap.put(brandUrlCache, brandCache);
//			System.out.println(brandUrlCache + "   " + brandCache);
		}
		
		
		/*查看map里面的测试代码*/
		/*for(String brand:brandUrlMap.keySet()){
			System.out.println(brand + " " + brandUrlMap.get(brand));
		}*/
	}
	
	
	
}
