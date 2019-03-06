package com.appStore.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.appStore.entity.BWListReadExcel;
import com.appStore.entity.Filtering;
import com.appStore.service.FilteringService;

@Controller  
public class BWListImportHandler {
	@Autowired
	HttpSession session;
	@Autowired
	private FilteringService filteringService;
    
	@Transactional
    @RequestMapping(value="importBWList",method = RequestMethod.POST) 
	@ResponseBody
    public JSONObject  upload(@RequestParam(value="file",required = false)MultipartFile file,HttpServletRequest request, HttpServletResponse response){  
    	JSONObject json = new JSONObject();
    	String model = request.getParameter("importModel");	//页面传递过来的机型
        int status = Integer.valueOf(request.getParameter("importFilterState"));//页面传递过来的黑白名单状态码
	    int filterStatus = 0;	//数据库存在黑白名单的状态码
	    int total = 0;			//Excel表单的总条数
    	int sucTotal = 0;  		//添加成功总条数
    	int faiTotal = 0;		//添加失败总条数
    	
    	//查询现存的黑白名单集合
        List<Filtering> list = filteringService.getBWList(model);
        //判断数据是否重复添加信息
        if(list.size()!=0) {
        	try {
				for (Filtering filtering : list) {
					filterStatus = filtering.getFilterState();
					if(filtering.getSn() == null && filtering.getMac() == null || filtering.getSn().equals("") && filtering.getMac().equals("")) {
						if(filterStatus==1) {
							json.put("msg", "The model has been whitelisted.");
						}else {
							json.put("msg", "The model has been blacklisted.");
						}
						json.put("code",-1);
						json.put("data", "sn-mac-isnone");
						return json;
					}else {
						if(status != filterStatus) {
							if(filterStatus==1) {
								json.put("msg", "Whitelist exists for this model.");
							}else {
								json.put("msg", "Blacklist exists for this model.");
							}
							json.put("code",-1);
							json.put("data", "sn-mac-isnone");
							return json;
						}
					}
				}
			} catch (Exception e) {
				json.put("code", -1);
    			json.put("msg", "Add failed,Model, state judgment is abnormal.");
    			json.put("data", null);
				e.printStackTrace();
				return json;
			}
    	}
        //创建处理EXCEL的类  
    	BWListReadExcel readExcel= new BWListReadExcel();  
        //解析excel，获取上传的事件单  
        List<Filtering> filteringList = readExcel.getExcelInfo(file);  
        //至此已经将excel中的数据转换到list里面了,接下来就可以操作list,可以进行保存到数据库,或者其他操作,
        
        //Excel文件中存有数据，操作数据
        if(filteringList != null && !filteringList.isEmpty()){
        	//循环拿出Excel表单里面的数据
        	 for (Filtering filter : filteringList) {
        		String sn = filter.getSn();
        		String mac = filter.getMac();
        		//sn和Mac存在空数据，不执行操作，跳过循环
             	if(mac==null || mac.equals("") || sn == null || sn.equals("")) continue;
             	total ++;		//总数累计
        		try {
        			//如果之前机型不存在过滤信息，直接执行添加操作
        			if(list.size()==0) {
        				filter.setModel(model);
        				filter.setFilterState(status);
        				int ret = filteringService.insertSelective(filter);
        				if(ret>0) sucTotal++;		//添加成功累计
        				else faiTotal++;			//失败条数累计
						continue;
        			}else {
        				int retb = 0;//用于判断数据库是否存在相同的sn和Mac
        				for (Filtering filtering : list) {
    						//sn和Mac相等,存在设备信息
    						if(sn.equals(filtering.getSn()) && mac.equals(filtering.getMac())) {
    							retb = 1;
    							continue;
    						}
        				}
        				//等于1数据库存在相同的sn和Mac，跳过添加操作
        				if(retb==1) {
        					faiTotal++;
        					continue;
        				}
        				else {
        					filter.setModel(model);
            				filter.setFilterState(status);
            				int ret = filteringService.insertSelective(filter);
            				if(ret>0) sucTotal++;		//添加成功累计
            				else faiTotal++;			//失败条数累计
    						continue;
        				}
        			}
    			
        		} catch (Exception e) {
        			//异常状态值
        			json.put("code", -1);
        			json.put("msg", "Add failed,Adding data is Exception.");
        			json.put("data", null);
        			e.printStackTrace();
        		}
     				
     		}
            //添加页面显示情况
        	json.put("code", 1);
			json.put("msg", "Total of "+total+" records, "+sucTotal+" successful additions, "+faiTotal+" failures");
			json.put("data", null); 
        	 
        }else{  
        	json.put("code", -1);
			json.put("msg", "Add failed,Excel form has no data information for use.");
			json.put("data", null);
        }  
        return json; 
    }  
}
