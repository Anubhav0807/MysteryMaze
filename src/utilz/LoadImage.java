package utilz;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class LoadImage {
	
	public static BufferedImage GetSprite(String filename) {
		BufferedImage img = null;
		InputStream inputStream = LoadImage.class.getResourceAsStream("/images/V01_" + filename + ".png");
		try {
			img = ImageIO.read(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}
	
	public static BufferedImage GetRotatedImage(BufferedImage originalImage, double angle) {
        // Calculate the center of the image
        double centerX = originalImage.getWidth() / 2.0;
        double centerY = originalImage.getHeight() / 2.0;

        // Create an AffineTransform instance
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angle), centerX, centerY);

        // Create a new buffered image with the same width and height as the original
        BufferedImage rotatedImage = new BufferedImage(
            originalImage.getWidth(),
            originalImage.getHeight(),
            originalImage.getType()
        );

        // Draw the original image onto the rotated image using the transform
        Graphics2D g2d = rotatedImage.createGraphics();
        g2d.setTransform(transform);
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
    }

}
