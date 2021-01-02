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


public class PixelOps
{
	
public static final String className = "cop5556fa20/runtime/PixelOps";

//values for selecting individual color intensities in packed pixels
public static final int SELECT_RED = 0x00ff0000,
                        SELECT_GRN = 0x0000ff00,
                        SELECT_BLU = 0x000000ff,
                        SELECT_ALPHA = 0xff000000;

//bits to shift to convert to and from int
public static final int SHIFT_ALPHA  = 24,
						SHIFT_RED = 16, 
                        SHIFT_GRN = 8,
                        SHIFT_BLU = 0;

//values for zeroing individual colors in packed pixels
public static final int ZERO_RED = 0xff00ffff,
                        ZERO_GRN = 0xffff00ff,
                        ZERO_BLU = 0xffffff00,
						ZERO_ALPHA = 0x00ffffff;




//above values in arrays indexed by color for convenience
public static final int[] BITMASKS = {SELECT_ALPHA, SELECT_RED, SELECT_GRN, SELECT_BLU, },
                          ZERO = {ZERO_ALPHA, ZERO_RED, ZERO_GRN, ZERO_BLU,  },
                          BITOFFSETS = {SHIFT_ALPHA, SHIFT_RED, SHIFT_GRN, SHIFT_BLU, };


	public static final int ALPHA = 0;
	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;
	

	/*extract red sample*/
	public static final String getRedSig = "(I)I";
	public static int getRed(int pixel) {
		return (pixel & SELECT_RED) >>> SHIFT_RED;
	}

	/*	extract green sample*/
	public static final String getGreenSig = "(I)I";
	public static int getGreen(int pixel) {
		return (pixel & SELECT_GRN) >>> SHIFT_GRN;
	}

	// extract blue sample
	public static final String getBlueSig = "(I)I";
	public static int getBlue(int pixel) {
		return (pixel & SELECT_BLU) >>> SHIFT_BLU;
	}
	
	//Extract alpha sample. For completenes, not used in project
	public static final String getAlphaSig = "(I)I";
	public static int getAlpha(int pixel) {
		return (pixel & SELECT_ALPHA) >>> SHIFT_ALPHA;
	}

	/*create a pixel with the given color values.
    Values less than 0 or greater than 255 are truncated.
	 */
	public static final String makePixelSig = "(III)I";
	public static int makePixel(int redVal, int grnVal, int bluVal) {
			int pixel =  ((0xFF << SHIFT_ALPHA) | (truncate(redVal) << SHIFT_RED)
					| (truncate(grnVal) << SHIFT_GRN) | (truncate(bluVal) << SHIFT_BLU));
			return pixel;
	}


/*	truncates an int to value in range of [0,Z)*/
	static private int truncate(int z) {
		if (z < 0)
			return 0;
		else if (z > 255)
			return 255;
		else
			return z;
	}
	
	/* String showing pixel in Hex format, alpha, red, grn, and blu
	 * components are each two digits.  Not needed in project, but
	 * may be useful for debugging.
	 */
	public static String toString(int val)
	{  return Integer.toHexString(val); }
}
