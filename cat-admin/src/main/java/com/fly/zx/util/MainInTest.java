package com.fly.zx.util;

import java.io.File;
import java.io.FileInputStream;

public class MainInTest {
	public static void main(String[] args) {
		String src = "C:\\Users\\soyea\\Desktop\\地铁图\\find.png";
		String dest = "/Users/mac_py/Desktop/cocl-n-y.png";
		String target = "C:\\Users\\soyea\\Desktop\\地铁图\\data1.png";
		long start = System.currentTimeMillis();
		try {
			// ScalImage.zoomImage(src, dest,320,180);
			// SnippingImage.saveImageWithSize(568,850,240,106,src,"/Users/mac_py/Desktop/cocl-n.png");
			// SnippingImage.saveImageWithSize(71,106,30,13,src,"/Users/mac_py/Desktop/cocl-n-s-y.png");
			// ScalImage.zoomImage(src, dest,30,13);
			// SearchPixelPosition.getAllRGB(src);
			SearchPixelPosition searchPixelPosition = new SearchPixelPosition();
			ResultBean result = searchPixelPosition.getAllRGB(src, target);
			if (result != null) {
				SnippingImage.saveImageWithSize(result.x, result.y, result.width, result.height, src,
						"/Users/mac_py/Desktop/cocl-ai.png");
				ImagePHash p = new ImagePHash();
				System.out.println("进行相似度计算");
				String image1 = p.getHash(new FileInputStream(new File(target)));
				String image2 = p.getHash(new FileInputStream(new File("/Users/mac_py/Desktop/cocl-ai.png")));
				System.out.println("相似度为" + (p.distance(image1, image2)==0?"相似度100%":"不相似"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("总共耗时：" + (end - start));
	}
}
