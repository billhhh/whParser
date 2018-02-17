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

	private static int cnt=0; //����Ь��detail����
	
	//main����
	public static void main(String[] args) throws ClientProtocolException, IOException, ParserException, SQLException {
		String url = "http://category.dangdang.com/cid4001867.html";
//		String link = "http://category.dangdang.com/cid4001867-a1%3A2037.html";
		insertProLinkToDB(url);
//		parseProDetail("http://product.dangdang.com/1325150302.html#ddclick?act=click&pos=1325150302_0_1_m&cat=4001867&key=&qinfo=&pinfo=&minfo=92_1_58&ninfo=&custid=&permid=&ref=&rcount=&type=&t=1422366672000");
	}
	
	
	
	//��ؼ���һ������
	public static void insertProLinkToDB(String url) throws ClientProtocolException, IOException, ParserException, SQLException{
		Map<String, String> map = new HashMap<String, String>(); //��ʱ���Ʒ�� Url �� hash������
		ArrayList<String> list = new ArrayList<String>();   
		ArrayList<String> brandlist = new ArrayList<String>();  //Ʒ��list
		String nextpage = null;
		int pagenum = 0;
		int brandpagenum = 0;
		
		parsePage_Brand pB = new parsePage_Brand(url);
		parsePage_ProLink pP;
		map = pB.getBrandUrlMap();
		
		
		for(String brandUrl:map.keySet()){
			//�� map �е�brandUrl���� brandlist��
			brandlist.add(brandUrl);
//			System.out.println(brandUrl);
		}
		
		
		//���� brandlist
		for(String s:brandlist){
			//������ֵ����һҳ
			nextpage = s;
			//System.out.println("��;���nextpage���ԣ�"+nextpage);
			//nextpage�д����brand
			while(!nextpage.equals("end")){
				pP = new parsePage_ProLink(nextpage);  //����һҳ������
				list = pP.getUrlList();  //ÿҳbrand��Ь������
				
				//test��Ʒ����
				for(String urlStr : list){
					parseProDetail(urlStr);
				}
				
				list.clear();
				nextpage = pP.getNextPage();  //getNextPage���ҵ�brand���оͼ���while
				pagenum++;
				brandpagenum++;
			}
			
//			System.out.println("this brand have "+ brandpagenum +" page");
			brandpagenum = 0;//ÿ��brand��num
		}
		
		System.out.println("end crawler.");
		System.out.println(pagenum);
	}
	
	
	
	
	/*
	 * ��ȡ��Ʒ���麯��*/
	//�ô�����Ʒ�����link
	public static void parseProDetail(String link) throws ClientProtocolException, IOException, ParserException, SQLException{
		cnt++; //����Ь��detail����
		System.out.println("cnt == "+cnt);
		System.out.println(link);  //��ӡ����
		//�½�hash�������з����ӦЬ�����Ժ�����ֵ
		Map<String, String> map = new HashMap<String, String>();
		//link���ڹ��췽���д��룬��������Ҫ����һ�� getDetailListMap ����
		
		parseProDetail pPD = new parseProDetail();
		pPD.setUrl(link);
		map = pPD.getDetailListMap();  //���ж���������+�۸񣩶��������map
		
		
		//���
		for(String s:map.keySet()){
			//������Ժ�����ֵ������ÿһ����������Ʒ���ڡ�s��ӳ�������
			System.out.println(s + " == " + map.get(s));
		}
		System.out.println("end\n");
		
		
//		DBOperation dbo = new DBOperation();
//		dbo.DBmapping(link, map);
		
	}
	
	
	
	/* �Ѳ��� ok
	 * ���Ի�ȡ����Ʒ����ַ����*/
	public static void testParsePage_Brand(String url) throws ClientProtocolException, IOException, ParserException{
		parsePage_Brand p = new parsePage_Brand(url);
		Map<String, String> map = new HashMap<String, String>();
		map = p.getBrandUrlMap();
		for(String key:map.keySet()){
			System.out.println(key + "  " + map.get(key));
		}
	}
	
	
	/*���Ի�ȡҳ������Ь�����Ӻ���*/
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
		System.out.println("���У�"+i+"����¼��");
	}
	
	
}