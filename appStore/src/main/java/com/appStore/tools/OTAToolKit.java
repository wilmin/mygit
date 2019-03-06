package com.appStore.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.PrivilegedAction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;


import java.lang.reflect.Method;




public class OTAToolKit {

	public static String getV4IP(){
		String ip = "";
		String chinaz = "http://ip.chinaz.com";
		
		StringBuilder inputLine = new StringBuilder();
		String read = "";
		URL url = null;
		HttpURLConnection urlConnection = null;
		BufferedReader in = null;
		try {
			url = new URL(chinaz);
			urlConnection = (HttpURLConnection) url.openConnection();
		    in = new BufferedReader( new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
			while((read=in.readLine())!=null){
				inputLine.append(read+"\r\n");
			}
			//System.out.println(inputLine.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
		Matcher m = p.matcher(inputLine.toString());
		if(m.find()){
			String ipstr = m.group(1);
			ip = ipstr;
			//System.out.println(ipstr);
		}
		return ip;
	}
	
	public static String string2StandardFormOfTimeString(String string) {
		String year = string.substring(0, 4);
		String month = string.substring(4, 6);
		String day = string.substring(6, 8);
		String hour = string.substring(8, 10);
		String minute = string.substring(10, 12);
		String second = string.substring(12, 14);
		return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
	}

	public static String getSystemTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		return df.format(new Date());
	}

	public static String getSystemDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		return df.format(new Date());
	}

	public static Object pagelistToJSONMapNew(PageList list) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (list != null) {
			Paginator paginator = list.getPaginator();
			map.put("total", paginator.getTotalCount());
			map.put("rows", new ArrayList(list));
		}
		return map;
	}

	public static PageBounds getPagerBoundsByParameter(int pageSize, int offset) {
		if (pageSize == 0) {
			return null;
		}

		PageBounds pageBounds = new PageBounds(offset / pageSize + 1, pageSize);
		return pageBounds;
	}

	public static String getWebsiteDatetime(String webUrl) {
		try {
			URL url = new URL(webUrl);// 取得资源对象
			URLConnection uc = url.openConnection();// 生成连接对象
			uc.connect();// 发出连接
			long ld = uc.getDate();// 读取网站日期时间
			Date date = new Date(ld);// 转换为标准时间对象
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			return sdf.format(date);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getMd5ByFile(File file) throws FileNotFoundException {
		String value = null;
		FileInputStream in = new FileInputStream(file);
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
			clean(byteBuffer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	/**
	 * MappedByteBuffer文件句柄释放问题
	 * @param buffer
	 * @throws Exception
	 */
	public static void clean(final Object buffer) throws Exception {
		AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				try {
					Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
					getCleanerMethod.setAccessible(true);
					sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
					cleaner.clean();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});

	}
	
	public static String tranferToMac(String macString) {
		StringBuilder sb = new StringBuilder();
		if (macString.length() < 12) {
			for (int j = 1; j <= 12 - macString.length(); j++) {
				sb.append("0");
			}
		}

		sb.append(macString);

		for (int i = 2; i < 16; i += 3) {
			sb.insert(i, ':');
		}
		String re = new String(sb);
		//转成大写字母
		//re = re.toUpperCase();
		return re;
	}

	public static String addTimeStamp(String filename) {
		if (filename.length() <= 0)
			return null;

		StringBuilder temp = new StringBuilder(filename);
		int lastdot = temp.indexOf(".");
		temp.insert(lastdot, System.currentTimeMillis());
		return new String(temp);
	}

	public static String getAddr(String serverAddress, int serverPort, String downloadUrlInDataBase) {
		StringBuffer downloadUrl = new StringBuffer("http://");
		downloadUrlInDataBase = downloadUrlInDataBase.replace("\\", "/");
		downloadUrl.append(serverAddress);
		downloadUrl.append(":");
		downloadUrl.append(serverPort);
		downloadUrl.append("/");
		downloadUrl.append(downloadUrlInDataBase);

		return new String(downloadUrl);
	}

	public static boolean matchFromLeft(String matcher, String matcherPattern) {
		boolean ismatched = true;

		if ((matcherPattern != null) && (matcherPattern.length() > 0)) {
			if (matcher.length() <= 0)
				return false;
			char[] matchPatternArray = matcherPattern.toCharArray();
			char[] matcherArray = matcher.toCharArray();
			for (int i = 0; i < matchPatternArray.length; i++) {
				if (matcherArray[i] != matchPatternArray[i]) {
					ismatched = false;
					break;
				}
			}
		}
		return ismatched;
	}

	public static boolean ismatchIPBlock(String requestIP, String IPBlockBegin, String IPBlockEnd) {
		String requestIPLeft = requestIP.substring(0, requestIP.lastIndexOf("."));
		String IPBlockBeginLeft = IPBlockBegin.substring(0, IPBlockBegin.lastIndexOf("."));
		String IPBlockEndLeft = IPBlockEnd.substring(0, IPBlockEnd.lastIndexOf("."));

		int requstRight = Integer.parseInt(requestIP.substring(requestIP.lastIndexOf(".") + 1));
		int IPBlockBeginRight = Integer.parseInt(IPBlockBegin.substring(IPBlockBegin.lastIndexOf(".") + 1));
		int IPBlockEndRight = Integer.parseInt(IPBlockEnd.substring(IPBlockEnd.lastIndexOf(".") + 1));

		if (requestIPLeft.equals(IPBlockBeginLeft) && requestIPLeft.equals(IPBlockEndLeft)
				&& requstRight <= IPBlockEndRight && requstRight >= IPBlockBeginRight) {
			return true;
		}

		return false;

	}


	public static String deleteFilterIdInFile(String mappingIDInFile, String filterID) {

		String[] mappingStringArray = mappingIDInFile.split(",");
		StringBuilder temp = new StringBuilder();
		for (String singleMappingID : mappingStringArray) {
			if (singleMappingID.equals(filterID))
				continue;
			temp.append(singleMappingID);
			temp.append(",");
		}
		int indexOfLastComma = temp.lastIndexOf(",");
		if (indexOfLastComma != -1)
			temp.deleteCharAt(indexOfLastComma);

		return new String(temp);
	}

	public static String insertFilterIDIntoFileMappingID(String mappingIDInFile, String filterID) {
		StringBuilder temp = new StringBuilder();

		return new String(temp);

	}

	public static String getOSUrl(String path, String businessName) {
		Properties prop = System.getProperties();
		String osname = prop.getProperty("os.name");
		if (osname.startsWith("win") || osname.startsWith("Win")) {
			path = path + "/" + businessName;
		} else {
			path = path + "/" + businessName;
		}

		return path;
	}

	public static String getOSUrl(String path, String businessName, String filename) {
		Properties prop = System.getProperties();
		String osname = prop.getProperty("os.name");
		if (osname.startsWith("win") || osname.startsWith("Win")) {
			path = path + "/" + businessName + "/" + filename;
		} else {
			path = path + "/" + businessName + "/" + filename;
		}

		return path;
	}

	public static String getAddr(int serverPort, String downloadUrlInDataBase) throws UnknownHostException {
		StringBuffer downloadUrl = new StringBuffer("http://");
//		InetAddress address = InetAddress.getLocalHost();
		String serverAddress = getV4IP();
		downloadUrlInDataBase = downloadUrlInDataBase.replace("\\", "/");
		downloadUrl.append(serverAddress);
		downloadUrl.append(":");
		downloadUrl.append(serverPort);
		downloadUrl.append("/");
		downloadUrl.append(downloadUrlInDataBase);

		return new String(downloadUrl);
	}
}
