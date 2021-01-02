/**
 * Code developed for the class project in COP5556 Programming Language Principles 
 * at the University of Florida, Fall 2020.
 * 
 * This software is solely for the educational benefit of students 
 * enrolled in the course during the Fall 2020 semester.  
 * 
 * This software, and any software derived from it,  may not be shared with others or posted to public web sites,
 * either during the course or afterwards.
 * 
 *  @Beverly A. Sanders, 2020
 *
 */

package cop5556fa20.runtime;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class BufferedImageUtils {
	
	public static final String BufferedImageDesc = "Ljava/awt/image/BufferedImage;";
	public static final String BufferedImageClassName = "java/awt/image/BufferedImage";	
	public static final String className = "cop5556fa20/runtime/BufferedImageUtils";
	

	
	/**
	 * Displays the image on the primary screen at the indicated location.
	 * Precondition:  image != null
	 * 
	 * [0,0] is the upper left corner of the screen.
	 * 
	 * @param image
	 * @param xloc
	 * @param yloc
	 * @return
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	public static String makeFrameSig = "("+BufferedImageDesc+"II)Ljavax/swing/JFrame;";
	public static final JFrame makeFrame(BufferedImage image, int xloc, int yloc)
			throws InvocationTargetException, InterruptedException {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setSize(image.getWidth(), image.getHeight());
		JLabel label = new JLabel(new ImageIcon(image));
		frame.add(label);
		frame.pack();
		frame.setLocation(xloc, yloc);
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				frame.setVisible(true);
			}
		});
		return frame;
	}
	
	/**
	 * Copies the pixels from the given image into a new BufferedImage.
	 * Precondition:  image != null
	 * 
	 * @param image
	 * @return
	 */
	public static final String copyImagesig = "("+BufferedImageDesc+")"+BufferedImageDesc;
	public  static BufferedImage copyBufferedImage(BufferedImage image) {
		boolean isAlphaPremultiplied = false;
		WritableRaster raster = image.copyData(image.getRaster().createCompatibleWritableRaster());
		return new BufferedImage(ColorModel.getRGBdefault(), raster, isAlphaPremultiplied, null);
	}
	
	/**
	 * Resize a BufferedImage
	 * Precondition:  image != null.  
	 * 
	 * @param image
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static final String resizeImagesig = "("+BufferedImageDesc+"II)"+BufferedImageDesc;
	public static BufferedImage resizeBufferedImage(BufferedImage image, int newWidth, int newHeight) {
		int w = image.getWidth();
		int h = image.getHeight();
		AffineTransform at = new AffineTransform();
		at.scale(((float) newWidth) / w, ((float) newHeight) / h);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage resized = null;
		resized = scaleOp.filter(image, resized);
		return resized;
	}
	
	/**
	 * Retrieves an image from the given url or file name.  The returned image is
	 * in the TYPE_INT_ARGB format.  A conversion is done if necessary.
	 * An IOException is thrown if the source is neither the URL nor filename of
	 * an image. 
	 * 
	 * @param source
	 * @return
	 * @throws IOException
	 */
	public static final String fetchImagesig = "(Ljava/lang/String;)"+BufferedImageDesc;
	public static BufferedImage fetchBufferedImage(String source) throws IOException {
		BufferedImage image;
		try {
			URL url = new URL(source);
			image = ImageIO.read(url);
		} catch (MalformedURLException e) {
			// wasn't a URL, maybe it is a file
			File file = new File(source);
			image = ImageIO.read(file);
		}
		int type = image.getType();
		if (type == BufferedImage.TYPE_INT_ARGB) {
			//image is in desired type, return it.
			return image;
		}
		// convert to INT_ARGB type
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		g.dispose();
		return newImage;
	}
	
	public static final String createBufferedImagesig = "(II)"+BufferedImageDesc;
	public static BufferedImage createBufferedImage(int w, int h) {
		System.out.println(new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB));
		return new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * Writes the given image to a file on the local system indicated by the
	 * given filename.
	 * 
	 * @param image
	 * @param filename
	 * @throws IOException 
	 */
	public static final String writeSig = "("+BufferedImageDesc+"Ljava/lang/String;" + ")V";
	public static void write(BufferedImage image, String name) throws IOException {
		String filename = name + ".png";
		File f = new File(filename);
			System.out.println("writing image to file: " + filename);
			ImageIO.write(image, "png", f);
	}
	


}
