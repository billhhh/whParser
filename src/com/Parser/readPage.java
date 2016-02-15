/* ���������url�����ش����������page(String)
 * */
package com.Parser;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class readPage {
	private String page = null;
	private String url;
	
	public readPage(String url){
		this.url = url;
	}
	
	
	/* @param: url(ȫ�ֵ����Ա)
	 * ����url�õ���Ʒҳ�棬����ҳ������*/
	public String getPageContent() throws ClientProtocolException, IOException{
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try{
			//���ó�ʱ
			/*CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet=new HttpGet("http://www.baidu.com");//HTTP Get����(POST��ͬ)
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();//��������ʹ��䳬ʱʱ��
			httpGet.setConfig(requestConfig);
			httpClient.execute(httpGet);//ִ������*/
			
			
			
			/*��get��ʽ������ҳ*/
			HttpGet httpget = new HttpGet(url);
			//��������ʹ��䳬ʱʱ�� 10s
			/*RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
			httpget.setConfig(requestConfig);*/
			
			/*������Ӧ������������������Ϣ*/
			ResponseHandler<String> responsehandler = new ResponseHandler<String>(){
				
				//�����ڲ���
				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					
					int status = response.getStatusLine().getStatusCode();
					if( status >=200 && status <= 300){
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					}else{
						throw new ClientProtocolException("Unexcepted response status: " + status);
					}
				}//handleResponse����
				
			};
			
			String responseBody = httpclient.execute(httpget, responsehandler);
			this.page = responseBody;
		}finally{
			httpclient.close();
		}
		
		//System.out.println(page);
		return page;
	}
	
	
	
}