package com.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class parsePage_ProLink {
	
	private String pageContent = null;
	private ArrayList<String> urlList;
	private String nextPage = null;
	
	//构造方法，保存 URL 实例
	public parsePage_ProLink(String url) throws ClientProtocolException, IOException {
		readPage rP = new readPage(url);
		//getPageContent 方法，根据url得到商品页面，返回页面主体
		this.pageContent = rP.getPageContent();
		
		//test ok
		//System.out.println("pageContent == "+pageContent);
		
		urlList = new ArrayList<String>();
	}
	
	
	
	public String getNextPage() throws ParserException{
		setNextPage();  //找下一页
		return nextPage;
	}
	
	
	
	public ArrayList<String> getUrlList() throws ParserException{
		setUrlList();  //get的时候，立即去set了那个list
		return urlList;
	}
	
	
	
	private void setNextPage() throws ParserException{
		Parser p = new Parser(pageContent);
		
		/*各种过滤器*/
		AndFilter nextNode = new AndFilter(new TagNameFilter("li"),new HasAttributeFilter("class","next"));
		TagNameFilter aTag = new TagNameFilter("a");
		
		NodeList nl = p.parse(nextNode);
		/*如果没有这个节点就证明已经是最后一页了*/
		if(nl.size() == 0)
		{
			this.nextPage = "end";
			return;
		}
		NodeList nodeList = nl.elementAt(0).getChildren().extractAllNodesThatMatch(aTag);
		
		TagNode t = (TagNode) nodeList.elementAt(0);
		//下一页的链接
		this.nextPage = "http://category.dangdang.com" + t.getAttribute("href");
	}
	
	
	
	
	private void setUrlList() throws ParserException{
		Parser p = new Parser(pageContent);
		String u = null;
		
		/*各种过滤器*/
		AndFilter proListTag = new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("name","fashion_products"));
		AndFilter liTag = new AndFilter(new TagNameFilter("li"),new HasAttributeFilter("name","lb"));
		AndFilter aTag = new AndFilter(new TagNameFilter("a"),new HasAttributeFilter("class","pic"));
		
		NodeList nl = p.parse(proListTag);
		NodeList proListNodes = nl.elementAt(0).getChildren().elementAt(1).getChildren().extractAllNodesThatMatch(liTag);
		Node aNode ;
		TagNode t;
		
		/*将链接装载进list*/
		for(int i = 0;  i < proListNodes.size(); i++){
			aNode = proListNodes.elementAt(i).getChildren().elementAt(1).getChildren().extractAllNodesThatMatch(aTag).elementAt(0);
			t = (TagNode) aNode;
			u = t.getAttribute("href");
			this.urlList.add(u);  //加入后面的超链接
		}
	}
	
	
	
}
