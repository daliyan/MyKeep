
package com.akiyama.base.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class SdHelper {
    public final static String SD_DIR = android.os.Environment.getExternalStorageDirectory() + "";
    public static String MYMONEY_DIR_NAME = ".mykeep";
    
    /**
	 * 以前的照片的根目录
	 */
    public static final String PHOTO_BASE_PATH = Environment.getExternalStorageDirectory().getPath() +
	        File.separator + MYMONEY_DIR_NAME + File.separator;
	
	/**
     * 保存照片的临时文件夹
     */
    public static final String MYMONEY_TEMP_PHOTO_DIR = PHOTO_BASE_PATH + "tmp" + File.separator;
    
    public static boolean isSdReady() {
        if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }
    
    public static long getAvailableStorage() {

        String storageDirectory = null;
        storageDirectory = Environment.getExternalStorageDirectory().toString();

        try {
            StatFs stat = new StatFs(storageDirectory);
            long availableSize = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
            return availableSize;
        } catch (RuntimeException ex) {
            return 0;
        }
    }

}
