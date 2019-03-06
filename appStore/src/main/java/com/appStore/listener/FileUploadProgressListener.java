package com.appStore.listener;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;

import com.appStore.entity.ProgressBean;


@Component
public class FileUploadProgressListener implements ProgressListener {

	private HttpSession session;

    @Override
    public void update(long bytesRead, long contentLength, int items) {
        //设置上传进度
        ProgressBean progress = new ProgressBean(bytesRead, contentLength, items); 
        //将上传进度保存到session中
        session.setAttribute("progress", progress); 
    }

    public void setSession(HttpSession session){
        this.session = session;
    }

}
