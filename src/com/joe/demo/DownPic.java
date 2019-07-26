package com.joe.demo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownPic {
    /**
     * 下载图片到指定目录
     *
     * @param filePath 文件路径
     * @param imgUrl   图片URL
     */
    public static void downImages(String filePath, String imgUrl) {
        // 若指定文件夹没有，则先创建
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 截取图片文件名
        String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1, imgUrl.length());
        try {
            // 文件名里面可能有中文或者空格，所以这里要进行处理。但空格又会被URLEncoder转义为加号
            String urlTail = URLEncoder.encode(fileName, "UTF-8");
            // 因此要将加号转化为UTF-8格式的%20
            imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf('/') + 1) + urlTail.replaceAll("\\+", "\\%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 写出的路径
        File file = new File(filePath + File.separator + fileName);
        try {
            // 获取图片URL
        	URL url = new URL(imgUrl);
            // 获得连接
            URLConnection connection = url.openConnection();
            // 设置10秒的相应时间
            connection.setConnectTimeout(10 * 1000);
            // 获得输入流
            InputStream in = connection.getInputStream();
            // 获得输出流
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            // 构建缓冲区
            byte[] buf = new byte[1024];
            int size;
            // 写入到文件
            while (-1 != (size = in .read(buf))) {
                out.write(buf, 0, size);
            }
            out.close(); in .close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        // 利用Jsoup获得连接
    	Scanner sc=new Scanner(System.in);
    	System.out.println("链接：");
    	String url=sc.nextLine();    	
        Connection connect = Jsoup.connect(url);
        try {
            // 得到Document对象
            Document document = connect.get();
            // 查找所有img标签
            Elements imgs = document.getElementsByTag("img");
            System.out.println(imgs.toString());
            System.out.println(imgs.size());
            System.out.println("共检测到下列图片URL：");
            System.out.println("开始下载");
            // 遍历img标签并获得src的属性
            int i=1;
            for (Element element: imgs) {
                //获取每个img标签URL "abs:"表示绝对路径
                String imgSrc = element.attr("abs:src");
                if(imgSrc.length()==0) {
                	imgSrc=element.attr("abs:data-src");
                
                }
                if(!(imgSrc.contains(".jpg")||imgSrc.contains(".png"))) {
                	continue;
                }
                // 打印URL
                System.out.println("第"+i+"张图片:"+imgSrc);
                //下载图片到本地
                DownPic.downImages("d:/img", imgSrc);
                i++;
            }
            System.out.println("下载完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}