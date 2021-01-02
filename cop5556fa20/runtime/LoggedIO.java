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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import cop5556fa20.resources.ImageResources;

public class LoggedIO {
	
	public static final ArrayList<Object> globalLog = new ArrayList<Object>();
	
	public final static String className = "cop5556fa20/runtime/LoggedIO";	
	
	public final static String stringToScreenSig = "(Ljava/lang/String;)V";
	
	public static class PLPImageFile {
		final PLPImage plpImage;
		final String name;
		public PLPImageFile(PLPImage plpImage, String name) {
			super();
			this.plpImage = plpImage;
			this.name = name;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((plpImage == null) ? 0 : plpImage.hashCode());
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
			PLPImageFile other = (PLPImageFile) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (plpImage == null) {
				if (other.plpImage != null)
					return false;
			} else if (!plpImage.equals(other.plpImage))
				return false;
			return true;
		}
		
		
	}


	public static void stringToScreen(String s) {
		globalLog.add(s);
		System.out.println(s);
	}
	
	public final static String intToScreenSig = "(I)V";
	public static void intToScreen(int n) {
		globalLog.add(Integer.valueOf(n));
		System.out.println(n);
	}
	
	public static void clearGlobalLog() {
		globalLog.clear();
	}
	
	public final static String imageToScreenSig = "("+PLPImage.desc + "II)V";
	public static void imageToScreen(PLPImage m, int xloc, int yloc) throws InvocationTargetException, InterruptedException {
		globalLog.add(m);
		BufferedImageUtils.makeFrame(m.image, xloc, yloc);
	}

	public final static String imageToFileSig = "(" + PLPImage.desc + "Ljava/lang/String;)V";
	public static void imageToFile(PLPImage m, String name) throws IOException {
		globalLog.add(new PLPImageFile(m,  name));
		BufferedImageUtils.write(m.image, ImageResources.binDir + File.separator + name);
	}
}
