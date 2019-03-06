package com.appStore.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.appStore.entity.User;
import com.appStore.service.LoginService;


@Controller
public class LoginHandler {
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private HttpSession session;
	
	/**
	 * 检测访问路径是否超时
	 * @param strurl
	 * @return
	 */
	private static String getWebData(String strurl) {   
        try {   
            URL url = new URL(strurl);   
            // 打开连接，此处只是创建一个实例，并没有真正的连接   
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();   
            httpCon.setConnectTimeout(10000);   
            httpCon.setReadTimeout(10000);   
                 
                
            httpCon.connect();//建立连接   
            InputStream inputStream = httpCon.getInputStream();   
            InputStreamReader inputReader = new InputStreamReader(inputStream,"utf-8");   
            BufferedReader bufferReader = new BufferedReader(inputReader);   
            StringBuffer sb = new StringBuffer();   
            String inputLine = null;     
            while ((inputLine = bufferReader.readLine()) != null) {      
                sb.append(inputLine+"\n");   
            }      
            bufferReader.close();     
            inputReader.close();   
            inputStream.close();   
            httpCon.disconnect();   
            //System.out.println(sb.toString().trim());   
            return sb.toString();   
        } catch (Exception e) { 
            System.out.println("Connection timed out");
        }   
        return null;   
    } 	

	@RequestMapping(value = "/checkin",method = RequestMethod.POST)
	@ResponseBody
	public ModelMap login(@RequestBody User user) {
		ModelMap mv = new ModelMap();
		String msgcode = null;
		/*String str = getWebData("http://47.90.127.105:8080/ota/login");
		//访问超时
		if(str == null){
			msgcode = "-2";
			mv.put("msgcode", msgcode);
			return mv;
		}*/
		User userIndatabase = loginService.userLogin(user);
		if(userIndatabase == null) {
			msgcode = "-1";
		} else {
			msgcode = "1";
			session.setAttribute("userid", userIndatabase.getId());
			session.setAttribute("username", userIndatabase.getUsername());
			session.setAttribute("user", userIndatabase);
			mv.put("href", "index.html");
		}
		mv.put("msgcode", msgcode);
		return mv;
	}
	
	@RequestMapping("index")
	public ModelAndView index() {
		System.out.println("index");
		return new ModelAndView("index");
	}
	
	@RequestMapping("login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}
	
	@RequestMapping("logout")
	@ResponseBody
	public JSONObject logout() {
		JSONObject json = new JSONObject();
		session.invalidate();
		json.put("code", 1);
		return json;
	}
	
	
}
