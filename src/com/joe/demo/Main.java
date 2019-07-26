package com.joe.demo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
public class Main {
    public static void main(String[] args) throws IOException {
    	String url="https://wallhaven.cc/";
    	Connection connect = Jsoup.connect(url);
    	// �õ�Document����
        Document document = connect.get();
        Elements imgs=document.select("a[href*=//wallhaven.cc/w/]");
        for (Element element: imgs) {
            //��ȡÿ��img��ǩURL "abs:"��ʾ����·��
            String imgSrc = element.attr("abs:href");
            System.out.println(imgSrc);
        }
    }
}