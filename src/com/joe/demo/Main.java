package com.joe.demo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws IOException {
//    	WebClient wc=new WebClient();
//    	wc.getOptions().setCssEnabled(true);
//    	wc.getOptions().setJavaScriptEnabled(true);
//    	wc.waitForBackgroundJavaScript(10000);
//    	HtmlPage page=wc.getPage("https://tuchong.com/tags/%E7%A7%81%E6%88%BF");
//    	System.out.println(page.asXml());
//    	DomNodeList<DomElement> elementsByTagNameSubmit = page.getElementsByTagName("li");
//    	System.out.println(elementsByTagNameSubmit.size());
//    	for(int i=0;i<elementsByTagNameSubmit.size();i++) {
//    		
//    	}
    	while(true) {
    		Scanner scan=new Scanner(System.in);
    		System.out.println("输入网址：");
        	String url=scan.nextLine();
        	Document document = (Jsoup.connect(url)
                    .get());
        	// 得到Document对象
            //System.out.println(document);
            //System.out.println(document); [href*=rqt_id]
            Elements imgs=document.select("img[src*=https://photo.tuchong.com]");
            //System.out.println(imgs);
            for (Element element: imgs) {
                //获取每个img标签URL "abs:"表示绝对路径
                String imgSrc = element.attr("abs:src");
                new DownPic().downImages("d:/tuchong", imgSrc);
                System.out.print("-");
            }
            System.out.println("下载完成");
    	}
    }
}