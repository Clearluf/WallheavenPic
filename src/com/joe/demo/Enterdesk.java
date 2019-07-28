package com.joe.demo;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Enterdesk {

	public static void main(String[] args) {
		//还剩2560*1440 1920*1200 1920*1080
		// TODO Auto-generated method stub
		Scanner s=new Scanner(System.in);
		System.out.println("分辨率：");
		String fbl=s.nextLine();//分辨率
		System.out.println("开始页：");
		int downFrom=s.nextInt();
		System.out.println("总页数：");
		int pageNum=s.nextInt();//页数
		String filePath="d:/enterdesk/"+fbl;
		for(int i=downFrom;i<pageNum+downFrom;i++) {
			String url="https://www.enterdesk.com/search/"+i+"-26-6-0-"+fbl+"-0/";
			System.out.println("开始下载："+url);
	    	try {
				Document document = (Jsoup.connect(url).get());
				Elements es=document.select("a[href*=/bizhi]");
				int k=1;
				for(Element e:es) {
					if((k%2)==1) {
						System.out.println("开始下载："+e.attr("abs:href"));
						downfromPicurl(filePath, e.attr("abs:href"));	
					}
					k++;
				}		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	private static void downfromPicurl(String filePath,String url) {
		// TODO Auto-generated method stub
		Document doc = null;
		try {
			doc = (Jsoup.connect(url).get());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Elements eles=doc.select("a[class=pics_pics]");
		for(Element e:eles) {
			String picsrc=(e.attr("abs:src").replace("edpic", "edpic_source"));
			System.out.println("正在下载："+picsrc);
			new DownPic();
			DownPic.downImages(filePath,picsrc);;
		}
	}
	

}
