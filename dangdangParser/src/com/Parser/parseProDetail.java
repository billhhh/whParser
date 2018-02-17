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
	
	//��ȡdetailҳ������
	public void setUrl(String link) {
		//����url�õ���Ʒҳ�棬����ҳ������
		try {
			readPage rp = new readPage(link);  //����link
			url=link;
			
			//����url�õ���Ʒҳ�棬����ҳ������
			this.pageContent = rp.getPageContent();//pageContent����html����
//			System.out.println(pageContent);
			
			detailListMap = new HashMap<String, String>();
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//pageContent����html����

	}
	
	
	
	
	public String getPrice() throws ParserException{
		setPrice();
		return this.price;
	}
	
	
	//����detail��hashmap
	public Map<String,String> getDetailListMap() throws ParserException{
		//��Ȼ�Ѿ��õ��ض�URL��pagecontent���ǾͿ�����ʽ��ȡ�������õ�������
		setDetailList();
		return this.detailListMap;
	}
	
	
	//���price��ȥ�����治��ascii�����ֵĻ�.��
	public String checkPrice(String price) {
		
		StringBuffer newPrice = new StringBuffer();
		for (int i = 0; i < price.length(); i++) {
			char ch = price.charAt(i);
			if((ch >= '0' && ch <= '9') || ch == '.' )
				newPrice.append(ch);
		}
		
		return newPrice.toString();
		
	}
	
	
	
	/*ȡ�ü۸�*/
	private boolean setPrice(){
		
		try {
			Parser p = new Parser(pageContent);
			
			//price����������һ��promo_price
			HasAttributeFilter priceTag = new HasAttributeFilter("id","promo_price");
			//System.out.println("priceTag == "+priceTag);
			NodeList nl = p.parse(priceTag);
//			System.out.println("nl == "+nl.elementAt(0));
			
			if(nl.size()>0){
				//����С�promo_price����ǩ����������С�����ֱ�Ӹ�������
				//���ﲻ��ֱ���������ֵ��Ҫ��prpr�����ֵ����Ϊ�����������ַ�
				price = nl.elementAt(0).toPlainTextString();
				//������һ��ÿ���ַ�������ascii�����ֵĻ�.�ŵ�ȥ��
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
			
			
			
			//����˳�㽫ͼƬ��־�㶨
			p = new Parser(pageContent);
			nl = p.parse(new HasAttributeFilter("id","largePic"));
			
			if(nl.size()==0)	//������ͼƬ������
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
	
	
	
	
	
	//get֮ǰ��set��ϸlist
	private void setDetailList() throws ParserException{
		String detailCache[] = null;
		Parser p = new Parser(pageContent); //pageContent����html����
		
		if(setPrice()==false)  //��ÿ��Ь������ļ۸���ȥ��
			return ;
			
//		System.out.println(url);
//		System.out.println("price == "+price);
		
		/*���ֹ�����*/
		//AndFilter��һ�����롿�����������ط������������tag������Ȼ���á���������˸�ʽ�������ձ���
		//AndFilter detailTag = new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class","mall_goods_foursort_style"));
		HasAttributeFilter detailTag = new HasAttributeFilter("class","mall_goods_foursort_style");
		
		//�ҵ�detail
		NodeList nl = p.parse(detailTag);
//		System.out.println("nl.size == "+nl.size());
//		System.out.println("nl == "+nl);
		if(nl.size()==0)
			return ;
		
		NodeList detailNodeList = nl.elementAt(0).getChildren().extractAllNodesThatMatch(new TagNameFilter("div"));
//		System.out.println("elementAt(0) "+nl.elementAt(0).toPlainTextString());
//		System.out.println("getChildren "+nl.elementAt(0).getChildren().toString());
//		System.out.println("extractAllNodesThatMatch "+nl.elementAt(0).getChildren().extractAllNodesThatMatch(t).toString());
		
		/*�����ַ�����":"Ϊ�ָ�㣬����map*/
		for(int i = 1; i < detailNodeList.size()-1; i++){
			detailCache = detailNodeList.elementAt(i).toPlainTextString().split("��");
			detailListMap.put(detailCache[0], detailCache[1]);
		}
		detailListMap.put("price", price);
		detailListMap.put("imgUrl", imgUrl);
		
	}
	
	
}
