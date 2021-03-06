/* 处理请求的url，返回处理后的整个page(String)
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
	
	
	/* @param: url(全局的类成员)
	 * 根据url得到商品页面，返回页面主体*/
	public String getPageContent() throws ClientProtocolException, IOException{
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try{
			//设置超时
			/*CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet=new HttpGet("http://www.baidu.com");//HTTP Get请求(POST雷同)
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();//设置请求和传输超时时间
			httpGet.setConfig(requestConfig);
			httpClient.execute(httpGet);//执行请求*/
			
			
			
			/*以get方式请求网页*/
			HttpGet httpget = new HttpGet(url);
			//设置请求和传输超时时间 10s
			/*RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
			httpget.setConfig(requestConfig);*/
			
			/*创建响应处理器处理服务器信息*/
			ResponseHandler<String> responsehandler = new ResponseHandler<String>(){
				
				//匿名内部类
				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					
					int status = response.getStatusLine().getStatusCode();
					if( status >=200 && status <= 300){
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					}else{
						throw new ClientProtocolException("Unexcepted response status: " + status);
					}
				}//handleResponse方法
				
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
