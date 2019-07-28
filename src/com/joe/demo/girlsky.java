package com.joe.demo;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class girlsky {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url="http://www.girlsky.cn/mntp/list_1_2.html";
		ArrayList<String> urls=getAllImgurl(url);
		for(int i=0;i<urls.size();i++) {
			downall(urls.get(i), "d:/Girlsky");
		}
		
	}
	private static ArrayList<String> getAllImgurl(String url) {
		// TODO Auto-generated method stub
		ArrayList<String> urls=new ArrayList<>();
		try {
			Document doc=Jsoup.connect(url).get();
			Elements eles=doc.select("a[class=TypeBigPics]");
			for(Element e:eles) {
				String imgurl=e.attr("abs:href");
				urls.add(imgurl);
				System.out.println("任务已添加："+imgurl);
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return urls;
	}
	private static void downall(String url,String filepath) {
		// TODO Auto-generated method stub
		int num=0;
		try {
			Document doc=Jsoup.connect(url).get();
			//System.out.println(doc);
			Elements eles=doc.select("a[class=page-en]");
			num=Integer.valueOf(eles.get(eles.size()-1).ownText());						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("开始下载："+url);
		downfromPage(url, filepath);
		String sourceurl=url.substring(0, url.length()-5);
		for(int i=2;i<=num;i++) {
			System.out.println("开始下载："+(sourceurl+"_"+i+".html"));
			downfromPage((sourceurl+"_"+i+".html"), filepath);;
		}
	}
	private static void downfromPage(String url,String filepath) {
		// TODO Auto-generated method stub		
		try {
			Document doc=Jsoup.connect(url).get();			
			Elements eles=doc.getElementsByTag("img");
			for(Element e:eles) {
				String imgsrc=e.attr("abs:src");
				if(imgsrc.length()>5) {
					System.out.println("正在下载："+imgsrc);
					new DownPic();
					DownPic.downImages(filepath,imgsrc);
				}

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
