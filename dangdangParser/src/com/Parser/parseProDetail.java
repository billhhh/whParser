package com.Parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class parseProDetail {
	
	private String pageContent = null;
	private Map<String,String> detailListMap = null;
	private String price;
	private String url;
	private String imgUrl;
	
	//爬取detail页面内容
	public void setUrl(String link) {
		//根据url得到商品页面，返回页面主体
		try {
			readPage rp = new readPage(link);  //传入link
			url=link;
			
			//根据url得到商品页面，返回页面主体
			this.pageContent = rp.getPageContent();//pageContent就是html内容
//			System.out.println(pageContent);
			
			detailListMap = new HashMap<String, String>();
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//pageContent就是html内容

	}
	
	
	
	
	public String getPrice() throws ParserException{
		setPrice();
		return this.price;
	}
	
	
	//返回detail的hashmap
	public Map<String,String> getDetailListMap() throws ParserException{
		//既然已经得到特定URL的pagecontent，那就可以正式爬取真正有用的内容了
		setDetailList();
		return this.detailListMap;
	}
	
	
	//检查price，去掉里面不是ascii码数字的或.号
	public String checkPrice(String price) {
		
		StringBuffer newPrice = new StringBuffer();
		for (int i = 0; i < price.length(); i++) {
			char ch = price.charAt(i);
			if((ch >= '0' && ch <= '9') || ch == '.' )
				newPrice.append(ch);
		}
		
		return newPrice.toString();
		
	}
	
	
	
	/*取得价格*/
	private boolean setPrice(){
		
		try {
			Parser p = new Parser(pageContent);
			
			//price过滤器，加一个promo_price
			HasAttributeFilter priceTag = new HasAttributeFilter("id","promo_price");
			//System.out.println("priceTag == "+priceTag);
			NodeList nl = p.parse(priceTag);
//			System.out.println("nl == "+nl.elementAt(0));
			
			if(nl.size()>0){
				//如果有【promo_price】标签，代表促销中。。。直接给促销价
				//这里不能直接爬外面的值，要爬prpr里面的值，因为有中文特殊字符
				price = nl.elementAt(0).toPlainTextString();
				//这里检测一下每个字符，不是ascii码数字的或.号的去掉
				price = checkPrice(price);
			}else{
				
				p = new Parser(pageContent);
				priceTag = new HasAttributeFilter("id","salePriceTag");
				nl = p.parse(priceTag);
				
				if(nl.size()>0)
					price = nl.elementAt(0).toPlainTextString();
				else
					return false;
				
			}
			
			
			
			//这里顺便将图片标志搞定
			p = new Parser(pageContent);
			nl = p.parse(new HasAttributeFilter("id","largePic"));
			
			if(nl.size()==0)	//爬不到图片，错误
				return false;
			
			TagNode tmp = (TagNode) nl.elementAt(0);
//			System.out.println("tmp == "+tmp);
			imgUrl = tmp.getAttribute("wsrc");
//			System.out.println("imgUrl == "+imgUrl);
			
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	
	
	
	
	//get之前先set详细list
	private void setDetailList() throws ParserException{
		String detailCache[] = null;
		Parser p = new Parser(pageContent); //pageContent就是html内容
		
		if(setPrice()==false)  //把每个鞋子详情的价格搞进去了
			return ;
			
//		System.out.println(url);
//		System.out.println("price == "+price);
		
		/*各种过滤器*/
		//AndFilter是一个【与】过滤器，返回符合两种情况的tag，【显然不好】，如果换了格式不具有普遍性
		//AndFilter detailTag = new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class","mall_goods_foursort_style"));
		HasAttributeFilter detailTag = new HasAttributeFilter("class","mall_goods_foursort_style");
		
		//找到detail
		NodeList nl = p.parse(detailTag);
//		System.out.println("nl.size == "+nl.size());
//		System.out.println("nl == "+nl);
		if(nl.size()==0)
			return ;
		
		NodeList detailNodeList = nl.elementAt(0).getChildren().extractAllNodesThatMatch(new TagNameFilter("div"));
//		System.out.println("elementAt(0) "+nl.elementAt(0).toPlainTextString());
//		System.out.println("getChildren "+nl.elementAt(0).getChildren().toString());
//		System.out.println("extractAllNodesThatMatch "+nl.elementAt(0).getChildren().extractAllNodesThatMatch(t).toString());
		
		/*处理字符，以":"为分割点，存入map*/
		for(int i = 1; i < detailNodeList.size()-1; i++){
			detailCache = detailNodeList.elementAt(i).toPlainTextString().split("：");
			detailListMap.put(detailCache[0], detailCache[1]);
		}
		detailListMap.put("price", price);
		detailListMap.put("imgUrl", imgUrl);
		
	}
	
	
}
