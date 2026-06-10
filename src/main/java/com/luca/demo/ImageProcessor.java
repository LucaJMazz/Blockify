package com.luca.demo;

import java.awt.image.BufferedImage;

public class ImageProcessor {

	public BufferedImage editImage(BufferedImage img, int scale) {
		BufferedImage pixelated = pixelateImage(img, scale);
		return pixelated;
	}
	
	/**
	 * Pixelates an image 
	 * @param img
	 * @param width
	 * @param height
	 * @return
	 */
	public BufferedImage pixelateImage(BufferedImage img, int scale) {
		int height = img.getHeight();
		int width = img.getWidth();
		int pixelSize = Math.min(height, width) / (scale);
		int pixelArea = pixelSize * pixelSize;
		BufferedImage childImage = new BufferedImage(width/pixelSize, height/pixelSize, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < img.getHeight(); y += pixelSize) {
            for (int x = 0; x < img.getWidth(); x += pixelSize) {
            	
            	int averages[] = new int[3];
            	
            	for (int i = 0; i < pixelSize && x + i < width; i++) {
            	    for (int j = 0; j < pixelSize && y + j < height; j++) {
            			int p = img.getRGB(x+i, y+j);
            			
            			averages[0] += (p >> 16) & 0xff; // R
                        averages[1] += (p >> 8)  & 0xff; // G
                        averages[2] +=  p        & 0xff; // B
                	}
            	}
            	
            	int p = (averages[0]/pixelArea << 16) 
            			| (averages[1]/pixelArea << 8) 
            			| averages[2]/pixelArea;
            	
            	if (width/pixelSize > x/pixelSize && height/pixelSize > y/pixelSize)
            		childImage.setRGB(x / pixelSize, y / pixelSize, p);
            	
            }
        }
		return childImage;
	}
}
