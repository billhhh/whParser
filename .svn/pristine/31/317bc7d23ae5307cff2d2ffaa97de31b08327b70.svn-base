package com.main;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class PureTest {

	public static void main(String[] args){
		
		/*1、测试string的split函数
		String s = "金属装饰 　亮片 　松糕跟 　镂空 　色拼接 　";
		String strArr[] = s.split(" 　");
		
		for (int i = 0; i < strArr.length; i++) {
			System.out.println(i+strArr[i]+"$");
		}*/
		
		
		
		String str = "人造革";
		//gbk编码使用2个字节表示一个汉字,所以buffer的长度应该为6
		byte[] buffer;
		try {
			
			buffer = str.getBytes("gbk");
			//[-56, -53, -44, -20, -72, -17]
			System.out.println("gbk编码的byte信息:" + Arrays.toString(buffer));
			
			//将gbk编码的buffer数据编码成iso8859-1格式.
			// buffer里有6个字节的数据,iso8859-1编码中每一个字节表示字符.
			//[È,Ë,Ô,ì,¸,ï]
			//所以打印出的iso8859-1编码的字符串会有6个字符,就是肉眼看见的乱码.
			String isoStr = new String(buffer, "iso8859-1");
			System.out.println("编码为iso8859-1后的字符:" + isoStr);
			System.out.println("还原后的字符:" + new String(isoStr.getBytes("iso8859-1"), "gbk"));
			//看一看iso8859-1编码的byte数组,用utf-8查看的效果吧,为什么会有12个字节那么多呢?
			/*
			utf-8是变长编码,UTF-8编码是变长编码字符可能由1~3个字节组成，
			第一个字节大于224的，它与它之后的2个字节一起组成一个UTF-8字符
			第一个字节大于192小于224的，它与它之后的1个字节组成一个UTF-8字符
			否则第一个字节本身就是一个英文字符（包括数字和一小部分标点符号）。
			*/
			System.out.println("用utf-8编码查看一个本来是iso8859-1编码的字符串的byte信息:" + Arrays.toString(isoStr.getBytes("utf-8")));
			//虽然变成了12字节,可依然还是救不活这个已经乱码的字符串
			System.out.println("utf-8查看iso8859-1直观的形式,依然救不活:" + new String(isoStr.getBytes("utf-8"), "utf-8"));
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
