package com.appStore.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.appStore.entity.ApkInfo;

/**
 * 通过ApkInfo 里的applicationIcon从APK里解压出icon图片并存放到磁盘上
 */
public class ApkIconUtil {
	private static ZipFile zFile = null;
	private static ZipEntry entry = null;

	/**
	 * 从指定的apk文件里获取指定file的流
	 *
	 * @param apkPath
	 * @param fileName
	 * @return
	 */
	public static InputStream extractFileFromApk(String apkPath, String fileName) {
		try {
			zFile = new ZipFile(apkPath);
			entry = zFile.getEntry(fileName);
			entry.getComment();
			entry.getCompressedSize();
			entry.getCrc();
			entry.isDirectory();
			entry.getSize();
			entry.getMethod();
			InputStream stream = zFile.getInputStream(entry);
			return stream;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从指定的apk文件里解压出icon图片并存放到指定的磁盘上
	 *
	 * @param apkPath    apk文件路径
	 * @param fileName   apk的icon
	 * @param outputPath 指定的磁盘路径
	 * @throws Exception
	 */
	public static void extractFileFromApk(String apkPath, String fileName, String outputPath) throws Exception {
		InputStream is = extractFileFromApk(apkPath, fileName);
		File file = new File(outputPath);
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
		byte[] b = new byte[1024];

		BufferedInputStream bis = new BufferedInputStream(is, 1024);
		while (bis.read(b) != -1) {
			bos.write(b);
		}
		bos.flush();
		bis.close();
		bos.close();
		fos.close();
		is.close();
		if (entry != null) {
			entry.clone();
		}
		if (zFile != null) {
			try {
				zFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public byte[] InputStream2ByteArray(String filePath, String fileName) throws IOException {
		InputStream is = null;
		byte[] data = new byte[1024 * 4];
		try {
			is = extractFileFromApk(filePath, fileName);
			data = toByteArray(is);
			return data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(data != null) {
				data.clone();
			}
			if(is != null) {
				is.close();
			}
			if (entry != null) {
				entry.clone();
			}
			if (zFile != null) {
				try {
					zFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private static byte[] toByteArray(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 4];
		int n = 0;
		try {
			while ((n = in.read(buffer)) != -1) {
				out.write(buffer, 0, n);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (buffer != null) {
					buffer.clone();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return out.toByteArray();
	}

	/**
	 * demo 获取apk文件的icon并写入磁盘指定位置
	 *
	 * @param args
	 */
	/*
	 * public static void main(String[] args) { try { String apkPath =
	 * "D:\\Java\\idea_app\\ReadApkAndIpa1\\src\\main\\java\\shenmiaotaowang_966.apk";
	 * if (args.length > 0) { apkPath = args[0]; } ApkInfo apkInfo = new
	 * ApkUtil().getApkInfo(apkPath); System.out.println(apkInfo); long now =
	 * System.currentTimeMillis(); extractFileFromApk(apkPath,
	 * apkInfo.getApplicationIcon(), "D:\\image\\apkIcon" + now + ".png"); } catch
	 * (Exception e) { e.printStackTrace(); } }
	 */

}
