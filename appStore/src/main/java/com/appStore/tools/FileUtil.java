package com.appStore.tools;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtil {
	
	/**
	 * //文件上传工具类服务方法
	 * @param file
	 * @param filePath
	 * @param fileName
	 * @throws Exception
	 */
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception{
    	File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
      
    }
    
    
    /**
	 * //删除上传文件方法
	 * @param file
	 * @param filePath
	 * @param fileName
	 * @throws Exception
	 */
    public static void deleteFile(String filePath) throws Exception{
    	//读取文件路径
        File targetFile = new File(filePath);
        //文件存在执行删除方法
        if(targetFile.exists()){
            targetFile.delete();
        }
    }
    
}
