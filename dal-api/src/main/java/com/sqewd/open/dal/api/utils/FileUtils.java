/**
 * 
 */
package com.sqewd.open.dal.api.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author subhagho
 * 
 */
public class FileUtils {
	/**
	 * Recursively delete the directory and all it's contents.
	 * 
	 * @param dir
	 *            - root directory (will also be deleted).
	 * 
	 * @throws Exception
	 */
	public static void delete(File dir) throws Exception {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				delete(file);
			}
		} else
			dir.delete();
	}

	public static void savefile(InputStream source, String destination)
			throws Exception {
		FileOutputStream fos = new FileOutputStream(destination);
		int size = 0;
		byte[] data = new byte[4096];
		while (true) {
			size = source.read(data);
			if (size < 0)
				break;
			fos.write(data, 0, size);
		}
		fos.flush();
		fos.close();
	}
}
