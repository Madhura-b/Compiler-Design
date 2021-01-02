package cop5556fa20.runtime;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import cop5556fa20.runtime.PLPImage.PLPImageException;

public class DecImageUtils {

	public static final String className = "cop5556fa20/runtime/DecImageUtils";
	public static final String dimensionDesc = "Ljava/awt/Dimension;";
	public static final String BufferedImageDesc = "Ljava/awt/image/BufferedImage;";
	
	public static final String createPLPImageSig = "("+BufferedImageDesc+dimensionDesc+")"+PLPImage.desc;
	public static PLPImage createPLPImage(BufferedImage image, Dimension dimension) {
		PLPImage plpimage = new PLPImage(image,dimension);
		return plpimage;
	}
	
	public static final String createNewDimensionSig = "(II)"+dimensionDesc;
	public static Dimension createNewDimension(int w, int h) {
		
		Dimension size = new Dimension(w,h);
		return size;
	}
	
	public static final String getBufferedImageFromPLPImageSig = "("+PLPImage.desc+")"+BufferedImageUtils.BufferedImageDesc;
	public static BufferedImage getBufferedImageFromPLPImage(PLPImage plpimage) {
		System.out.println("In the function *************************************************************************************************************************************************************");
		return plpimage.image;
	}
}
