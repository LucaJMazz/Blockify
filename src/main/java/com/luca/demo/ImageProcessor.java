package com.luca.demo;

import java.awt.image.BufferedImage;

public class ImageProcessor {

	public BufferedImage editImage(BufferedImage img, int width, int height) {
		BufferedImage childImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int pixelSize = (img.getWidth() > img.getHeight()) 
				? img.getWidth()/width 
				: img.getHeight()/height;
		
		for (int y = 0; y < img.getHeight(); y += pixelSize) {
            for (int x = 0; x < img.getWidth(); x += pixelSize) {
            	
            	int averages[] = new int[3];
            	
            	for (int i = 0; i < pixelSize && x + i < img.getWidth(); i++) {
            	    for (int j = 0; j < pixelSize && y + j < img.getHeight(); j++) {
            			int p = img.getRGB(x+i, y+j);
            			
            			averages[0] += (p >> 16) & 0xff; // R
                        averages[1] += (p >> 8)  & 0xff; // G
                        averages[2] +=  p        & 0xff; // B
                	}
            	}
            	
            	int total = pixelSize * pixelSize;
            	int p = (averages[0]/total << 16) 
            			| (averages[1]/total << 8) 
            			| averages[2]/total;
            	
            	if (width > x/pixelSize && height > y/pixelSize)
            		childImage.setRGB(x / pixelSize, y / pixelSize, p);
            	
            }
        }
		return childImage;
	}
}
