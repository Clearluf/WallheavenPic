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
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Wallheaven {
	/**
	 * 下载wallheaven图片到指定目录
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
			while (-1 != (size = in.read(buf))) {
				out.write(buf, 0, size);
			}
			out.close();
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从网页的url中获取图片url
	 *
	 * @param url 网页url
	 */
	static void DownloadImgLinkFromUrl(String url) {
		ArrayList<String> picsUrl = new ArrayList<String>();
		// 利用Jsoup获得连接
		Connection connect = Jsoup.connect(url);
		try {
			// 得到Document对象
			Document document = connect.get();
			// 查找所有img标签
			Elements imgs = document.getElementsByTag("img");

			// 遍历img标签并获得src的属性
			for (Element element : imgs) {
				// 获取每个img标签URL "abs:"表示绝对路径
				String imgSrc = element.attr("abs:src");
				if (imgSrc.contains("logo") || imgSrc.contains("user/avatar")) {
					continue;
				}
				Wallheaven.downImages("d:/wallheaven", imgSrc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		// 点击下载大图而非预览图
		int pages = 0;
		String netpage = null;
		Scanner s = new Scanner(System.in);
		System.out.println("网页地址：");
		netpage = s.nextLine();
		System.out.println("图片页数：");
		pages = s.nextInt();

		ArrayList<String> picsUrl = new ArrayList<String>();
		int i = 1;// 下载的页数
		int picNum = 0;// 图片总数
		while (i <= pages) {
			String url = netpage.substring(0, netpage.length() - 1) + i;
			System.out.println("从第" + i + "页中开始获取图片URL");
			Connection connect = Jsoup.connect(url);
			// 得到Document对象
			Document document = connect.get();
			Elements imgs = document.select("a[href*=//wallhaven.cc/w/]");
			for (Element element : imgs) {
				// 获取每个img标签URL "abs:"表示绝对路径
				String imgSrc = element.attr("abs:href");
				picsUrl.add(imgSrc);
			}
			i++;
		}

		picNum = picsUrl.size();
		System.out.println("获取完毕，总计" + picNum + "张图片");
		System.out.println("开始下载：");
		Date d1 = new Date();
		for (int k = 0; k < picNum; k++) {
			DownloadImgLinkFromUrl(picsUrl.get(k));
			System.out.println("第" + (k + 1) + "张图片下载完成");
		}
		Date d2 = new Date();
		long time = (d2.getTime() - d1.getTime()) / 1000;
		System.out.println("所有图片下载完毕,用时" + time + "秒");
	}
}
