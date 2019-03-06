package com.appStore.tools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Component
@SuppressWarnings("deprecation")
public class AmazonS3Utils {
	// 申明变量
	@Value("${aws.accessKey}")
	private String accessKeyID; // 用户Access key ID，存配置文件中
	@Value("${aws.secretKey}")
	private String secretKey; // 用户Secret access key，存配置文件中
	@Value("${aws.eweBucket}")
	private String bucketName; // S3存储桶名称，存配置文件中
	private AWSCredentials credentials;
	private AmazonS3 s3Client;

	/**
	 * 获取s3Url前缀固定路径
	 * @param key
	 * @return
	 */
	public String getAmazonS3Url(String key) {
		getInit();
		URL url = s3Client.getUrl(bucketName, key);
		System.out.println("s3Url前缀固定路径================="+s3Client.getUrl(bucketName, key));
		if(url != null) {
			return url.toString();
		}
		return null;
	}

	private void getInit() {
		// TODO Auto-generated constructor stub
		// 初始化
		if (s3Client == null) {
			// 创建Amazon S3对象
			credentials = new BasicAWSCredentials(accessKeyID, secretKey);
			
			s3Client = new AmazonS3Client(credentials);
			// 设置区域
			s3Client.setRegion(Region.getRegion(Regions.EU_WEST_1));// EU_WEST_1表示爱尔兰区域
			
		}
	}

	/**
	 * 查看所有可用的bucket
	 * 
	 * @param s3Client
	 */
	public void getAllBucket(AmazonS3 s3Client) {
		List<Bucket> buckets = s3Client.listBuckets();
		for (Bucket bucket : buckets) {
			System.out.println("Bucket: " + bucket.getName());
		}
	}

	/**
	 * 查看bucket下所有的object
	 * 
	 * @param bucketName 存储桶
	 */
	public void getAllBucketObject(AmazonS3 s3Client) {
		getInit();
		ObjectListing objects = s3Client.listObjects(bucketName);
		do {
			for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
				System.out.println("Object: " + objectSummary.getKey());
			}
			objects = s3Client.listNextBatchOfObjects(objects);
		} while (objects.isTruncated());
	}

	/**
	 * amazonS3文件上传
	 * 
	 * @param s3Client
	 * @param bucketName 保存到某个存储桶
	 * @param key        保存文件的key （以key-value形式保存）其实就是一个url路劲
	 * @param file       上传文件
	 */
	public String amazonS3Upload(String key, byte[] data) {
		getInit();
		System.setProperty(SDKGlobalConfiguration.AWS_SESSION_TOKEN_ENV_VAR, "true");
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(data.length);
			// 上传文件
			PutObjectResult result = s3Client.putObject(bucketName, key, new ByteArrayInputStream(data), metadata);
			if (StringUtils.isNotBlank(result.getETag())) {
				System.out.println("Send Data to Amazon S3 - Success, ETag is " + result.getETag());
			}
			System.out.println("Send Data to Amazon S3 - End");

//			S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));
			// 获取一个request
			GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, key);
	
			/**
			 * 设置过期时间
			 */
//			Date expirationDate = null;
//			try {
//				expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse("2025-12-31");  
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			urlRequest.setExpiration(expirationDate);
//			
			// 生成公用的url
			URL url = s3Client.generatePresignedUrl(urlRequest);
			
			System.out.println("=========URL=================" + url + "============URL=============");
			if (url != null) {
				//路径处理
				String str = url.toString(); 
				int endIndex =str.indexOf("?");
				//截图URL中"?"之前的字段
				String toStr = str.substring(0,endIndex);
				//https替换http处理
				toStr = toStr.replace("https","http");
				System.err.println("路径截取替换处理后==============URL："+toStr);
				return toStr;
			}
		} catch (AmazonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AmazonClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	
	/**
	 * 返回amazonS3文件下的下载路径
	 * 
	 * @param s3Client
	 * @param bucketName     下载某个存储桶的数据
	 * @param key            下载文件的key
	 * @param targetFilePath 下载文件保存地址
	 */
	public void amazonS3ApkDownloadPath(String filePath, String fileName, HttpServletResponse response) {
		getInit();
		S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, filePath));
		if (object != null) {
			System.out.println("Content-Type: " + object.getObjectMetadata().getContentType());
			InputStream input = null;
			// FileOutputStream fileOutputStream = null;
			OutputStream out = null;
			byte[] data = null;
			try {
				// 获取文件流
				// 信息头，相当于新建的名字
				response.setHeader("content-disposition",
						"attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
				input = object.getObjectContent();
				data = new byte[input.available()];
				int len = 0;
				out = response.getOutputStream();
				// fileOutputStream = new FileOutputStream(targetFilePath);
				while ((len = input.read(data)) != -1) {
					out.write(data, 0, len);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 关闭输入输出流
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	

	/**
	 * amazonS3文件下载
	 * 
	 * @param s3Client
	 * @param bucketName     下载某个存储桶的数据
	 * @param key            下载文件的key
	 * @param targetFilePath 下载文件保存地址
	 */
	public void amazonS3Downloading(String filePath, String fileName, HttpServletResponse response) {
		getInit();
		S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, filePath));
		if (object != null) {
			System.out.println("Content-Type: " + object.getObjectMetadata().getContentType());
			InputStream input = null;
			// FileOutputStream fileOutputStream = null;
			OutputStream out = null;
			byte[] data = null;
			try {
				// 获取文件流
				// 信息头，相当于新建的名字
				response.setHeader("content-disposition",
						"attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
				input = object.getObjectContent();
				data = new byte[input.available()];
				int len = 0;
				out = response.getOutputStream();
				// fileOutputStream = new FileOutputStream(targetFilePath);
				while ((len = input.read(data)) != -1) {
					out.write(data, 0, len);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 关闭输入输出流
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 文件删除
	 * 
	 * @param s3Client
	 * @param bucketName 删除文件所在存储桶
	 * @param key        删除文件key
	 */
	public void amazonS3DeleteObject(String key) {
		getInit();
		s3Client.deleteObject(bucketName, key);
	}

	/**
	 * 删除存储桶
	 * 
	 * @param s3Client
	 * @param bucketName 需要删除的存储桶
	 */
	public void amazonS3DeleteBucket(AmazonS3 s3Client, String bucketName) {
		s3Client.deleteBucket(bucketName);
	}

	// 在文件比较大的时候，有必要用多线程上传。（未测试）
	/*
	 * TransferManager tm = new TransferManager(s3Client);
	 * TransferManagerConfiguration conf = tm.getConfiguration(); int threshold = 4
	 * * 1024 * 1024; conf.setMultipartUploadThreshold(threshold);
	 * tm.setConfiguration(conf); Upload upload = tm.upload(bucketName, s3Path, new
	 * File(localPath)); TransferProgress p = upload.getProgress(); while
	 * (upload.isDone() == false) { int percent = (int)(p.getPercentTransfered());
	 * Thread.sleep(500); } // 默认添加public权限 s3Client.setObjectAcl(bucketName,
	 * s3Path, CannedAccessControlList.PublicRead);
	 */

}
