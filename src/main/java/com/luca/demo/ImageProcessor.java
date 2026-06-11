package com.luca.demo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.luca.demo.BlockPalette.MinecraftBlock;


public class ImageProcessor {

	/**
	 * 
	 * @param img input image
	 * @param scale scale of output image
	 * @return
	 */
	public static BufferedImage editImage(BufferedImage img, int scale) {
		BufferedImage newImage = pixelateImage(img, scale);
		newImage = blockifyImage(newImage);
		return newImage;
	}

	/**
	 * Pixelates an image by a scale
	 * 
	 * @param img
	 * @param width
	 * @param height
	 * @return child image scaled down to pixels
	 */
	public static BufferedImage pixelateImage(BufferedImage img, int scale) {
		int height = img.getHeight();
		int width = img.getWidth();
		int pixelSize = Math.min(height, width) / (scale);
		int pixelArea = pixelSize * pixelSize;
		BufferedImage childImage = new BufferedImage(width / pixelSize, height / pixelSize, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < img.getHeight(); y += pixelSize) {
			for (int x = 0; x < img.getWidth(); x += pixelSize) {

				int averages[] = new int[3];

				for (int i = 0; i < pixelSize && x + i < width; i++) {
					for (int j = 0; j < pixelSize && y + j < height; j++) {
						int p = img.getRGB(x + i, y + j);

						averages[0] += (p >> 16) & 0xff; // R
						averages[1] += (p >> 8) & 0xff; // G
						averages[2] += p & 0xff; // B
					}
				}

				int p = (averages[0] / pixelArea << 16)
						| (averages[1] / pixelArea << 8)
						| averages[2] / pixelArea;

				if (width / pixelSize > x / pixelSize && height / pixelSize > y / pixelSize)
					childImage.setRGB(x / pixelSize, y / pixelSize, p);

			}
		}
		return childImage;
	}
	
	public static BufferedImage blockifyImage(BufferedImage img) {
		BufferedImage blockedImg = new BufferedImage(img.getWidth()*16, img.getHeight()*16, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < img.getHeight(); y ++) {
			for (int x = 0; x < img.getWidth(); x ++) {
				int p = img.getRGB(x, y);
				
				int r = (p >> 16) & 0xff; // R
				int g = (p >> 8) & 0xff; // G
				int b = p & 0xff; // B
				
				MinecraftBlock block =	BlockPalette.findClosest(r, g, b);
				BufferedImage blockImg = null;
				try {
		            //File file = new File("../../../../resources/static/block_textures/"+block.name()+".png");
					InputStream is  = ImageProcessor.class.getResourceAsStream("/static/block_textures/"+block.name()+".png");
		            blockImg = ImageIO.read(is);
		            
		        } catch (IOException e) {
		            System.err.println("Error: Could not find or read the file.");
		            e.printStackTrace();
		        }
				
				
				for (int i = 0; i < 16; i++) {
					for (int j = 0; j < 16; j++) {
						int pixel = blockImg.getRGB(i, j);
						blockedImg.setRGB(x*16+i, y*16+j, pixel);
					}
				}
				
				
			}
		}
		
		return blockedImg;
	}
}
