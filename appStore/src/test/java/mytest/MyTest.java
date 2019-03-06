package mytest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.appStore.tools.AmazonS3Utils;


public class MyTest {
	@Autowired
	private AmazonS3Utils S3Util;
	
	@Test
	public  void test() {
//		S3Util.amazonS3DeleteObject("appStore/2019-02-26/app_vsmarket_v21551166852575.0.apk");;
	}

}
