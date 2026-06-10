package com.luca.demo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ApiController {

	/**
	 * Get status of spring boot server
	 * @return json saying status is online
	 */
	@GetMapping("/status")
		public Map<String, String> getGreeting() {
			Map<String, String> json = new HashMap<String, String>();
			json.put("Status","Online");
			return json;
	}
	
	/**
	 * Upload image with POST to endpoint, runs minecraft block algorithm and send image back
	 * @param file
	 * @return byte array of image data to be returned to sender
	 */
	@PostMapping(value = "/upload", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> uploadImage(
			@RequestParam("file") MultipartFile file,
	        @RequestParam("scale") int scale
	        ) {
		
		// init vars
		BufferedImage image = null;
		byte[] imageBytes = null;
		// Convert file to image data type
		try {
			image = ImageIO.read(file.getInputStream());
		} catch (IOException e) {
			e.getMessage();
		}
		
		/**
		 * Runs process to convert image into minecraft blocks
		 */
		ImageProcessor IP = new ImageProcessor(); 
		image = IP.editImage(image, scale);
		
		// Convert BufferedImage back to raw bytes
		try {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(image, "png", baos);
	        imageBytes = baos.toByteArray();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.internalServerError().body(null);
		}

        // Return the bytes directly as an image response
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageBytes);
	}
}
