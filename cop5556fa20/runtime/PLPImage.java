/**
 * This code was for the class project in COP5556 Programming Language Principles 
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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Arrays;



/**
 * This class represents variables of type image in our language.  An instance of this class should be created when a variable of type image is declared.
 * 
 * Field image is a java.awt.image.BufferedImage object of type TYPE_INT_ARGB.  The field may be null if the variable has not yet been initialized.
 * Field declaredSize is a java.awt.Dimension object that stored the declared size of an image, or null if no size was declared.
 * 
 * If a variable of type image is declared with a size, then that image maintains that size throughout its lifetime.  
 * Any image that is loaded to that variable will be resized as necessary to maintain the declared size.
 * 

 * @author Beverly Sanders
 *
 */
public class PLPImage {
	
	@SuppressWarnings("serial")
	public static class PLPImageException extends Exception {
		
		int line;
		int posInLine;

		public PLPImageException(int line, int posInLine, String message) {
			super(line + ":" + posInLine + " " + message);
			this.line = line;
			this.posInLine = posInLine;
		}
	}

	public static final  String className = "cop5556fa20/runtime/PLPImage";
	public static final String desc = "Lcop5556fa20/runtime/PLPImage;";
	

	//declared is the declared size of image, or null if not specified.
	//image is the image, it may be null.  
	//Invariant:  if image is not null, and dimension is not null, then the image has size dimension.
	public BufferedImage image;
	final Dimension declaredSize; //declared size of image, or null if none
	
	
	public PLPImage(BufferedImage image, Dimension declaredSize) {
		super();
		this.image = image;
		this.declaredSize = declaredSize;
		
	}

	public static final String getWidthSig = "()I";
	public int getWidth() {
		if(declaredSize != null) {
			return declaredSize.width;
		}
		return image.getWidth();
	}
	
	public static final String getWidthThrowsSig = "(II)I";
	public int getWidthThrows(int line, int posInLine) throws PLPImageException {
		System.out.println("Entering the method getWidthThrows()");
		if(declaredSize != null) {
			return declaredSize.width;
		}
		if (image != null) {
			return image.getWidth();
		}
		System.out.println("Throwing the exception");
		throw new PLPImageException(line, posInLine, "attempting to get width of uninitialized image");
	}
	
	public static final String getHeightSig = "()I";
	public int getHeight() {
		if(declaredSize != null) {
			return declaredSize.height;
		}
		return image.getHeight();
	}
	
	public static final String getHeightThrowsSig = "(II)I";
	public int getHeightThrows(int line, int posInLine) throws PLPImageException {
		if(declaredSize != null) {
			return declaredSize.height;
		}
		if (image != null) {
		return image.getHeight();
		}
		throw new PLPImageException(line, posInLine, "attempting to get height of uninitialized image");
	}

	public static final String selectPixelSig = "(II)I";
	public int selectPixel(int x, int y) {
		return image.getRGB(x, y);
	}
	
	public static final String updatePixelSig = "(III)V";
	public void updatePixel(int x, int y, int value) {
		image.setRGB(x, y, value);
	}
	
	public static final String compareWidthSig = "("+desc+"III)"+desc;
	public PLPImage compareWidth(PLPImage im, int width, int line , int posInLine) throws PLPImageException
	{
		if(im.getWidth() != width)
		{
			throw new PLPImageException(line, posInLine, "Attemptinh to assign an image to an image of unidentical width");
		}
		else
		{
			return im;
		}
	}
	
	public static final String compareHeigthSig = "("+desc+"III)"+desc;;
	public PLPImage compareHeigth(PLPImage im, int heigth, int line , int posInLine) throws PLPImageException
	{
		if(im.getHeight()!= heigth)
		{
			throw new PLPImageException(line, posInLine, "Attemptinh to assign an image to an image of unidentical heigth");
		}
		else
		{
			return im;
		}
	}
	
	public static final String ensureImageAllocatedSig= "(II)V";
	public void ensureImageAllocated(int line, int posInLine) throws PLPImageException {
		if (image == null) {
			if (declaredSize == null) {
				throw new PLPImageException(line, posInLine,"attempting to create image without specified size");
			}
			image = BufferedImageUtils.createBufferedImage(declaredSize.width,declaredSize.height);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		//compare the BufferedImages
		PLPImage other = (PLPImage) obj;
		if (image == other.image) { return true;}
        if (image == null || other.image == null) { 
        	//if both are null, this is still false as uninitialized images are 
        	//not equal to anything.
        	return false;
        	}
        //neither BufferedImages are null;
		int w = image.getWidth();
		int h = image.getHeight();
		int w1 = other.image.getWidth();
		int h1 = other.image.getHeight();
		if (w != w1 || h != h1) return false;
		int[] pixels = image.getRGB(0, 0, w, h, null, 0, w);
		int[] otherPixels = other.image.getRGB(0, 0, w, h, null, 0, w);
		return Arrays.equals(pixels,otherPixels);
	}        
        	

}
