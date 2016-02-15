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
	private Map<String,String> brandUrlMap;  /*Ʒ�ƺ���ַ�ļ�ֵ��*/
	private String link;
	
	public parsePage_Brand(String url) throws ClientProtocolException, IOException{
		readPage page = new readPage(url);
		link = url;
		this.pageContent = page.getPageContent();	//�õ�url������ȥ�õ�pageContent
	}
	
	public Map<String,String> getBrandUrlMap() throws ParserException{
		setBrandUrlList();  //Ҫget����set
		return this.brandUrlMap;
	}
	
	
	//set Ʒ�Ƶ�url list��
	private void setBrandUrlList() throws ParserException{
		String brandUrlCache = null;
		String brandCache = null;
		this.brandUrlMap = new HashMap<String, String>();
		Parser p = new Parser(pageContent);
		
		//System.out.println("��brand��ҳ��URL == "+link);
		//System.out.println("pageContent == "+pageContent);
		
		/*���ֹ�����*/
		//���˳�ҳ������� class=list ��
		//HasAttributeFilter brandListTag = new HasAttributeFilter("class","list");
		//��ʵֱ�ӹ��˳�������Ϣ�Ϳ�����
		HasAttributeFilter brandListTag = new HasAttributeFilter("class","brand_opt brand_height allbrand");
		
		NodeList nl = p.parse(brandListTag);
		//System.out.println("nlWithoutBrandListTag == "+nl);
		
		/*���˵��հױ�ǩ*/
		NodeFilter spanTag = new TagNameFilter("span");
		//ȥ������ span ������Ԫ��
		
		//System.out.println("nl == "+nl.elementAt(0));  //����elementAt(3)�ܻ�䣬֮ǰ��elementAt(7)
		NodeList brands = nl.elementAt(0).getChildren().extractAllNodesThatMatch(spanTag);
		
//		System.out.println("�밴���������");
//		Scanner input = new Scanner(System.in);
//		input.next();
		
		TagNode tmp;
		/*��ȡ����Ʒ�Ƶ�����*/
		for(int i = 0; i < brands.size()-1; i++){
			tmp = (TagNode) brands.elementAt(i).getChildren().elementAt(1);
//			System.out.println("ÿ��brand�� "+tmp.toPlainTextString());
			
			brandUrlCache = "http://category.dangdang.com" + tmp.getAttribute("href");
			brandCache = tmp.getAttribute("title");
			brandUrlMap.put(brandUrlCache, brandCache);
//			System.out.println(brandUrlCache + "   " + brandCache);
		}
		
		
		/*�鿴map����Ĳ��Դ���*/
		/*for(String brand:brandUrlMap.keySet()){
			System.out.println(brand + " " + brandUrlMap.get(brand));
		}*/
	}
	
	
	
}
