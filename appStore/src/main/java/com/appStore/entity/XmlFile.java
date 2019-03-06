package com.appStore.entity;

import java.util.Date;

public class XmlFile {
    private Integer id;

    private String model;
    
    private String device;

    private String android;

    private String firmware;

    private String xmlurl;
    
    private String s3xmlkey;

    private String description;

    private Date uploadtime;

    public String getS3xmlkey() {
        return s3xmlkey;
    }

    public void setS3xmlkey(String s3xmlkey) {
        this.s3xmlkey = s3xmlkey == null ? null : s3xmlkey.trim();
    }
    
    public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getAndroid() {
        return android;
    }

    public void setAndroid(String android) {
        this.android = android == null ? null : android.trim();
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware == null ? null : firmware.trim();
    }

    public String getXmlurl() {
        return xmlurl;
    }

    public void setXmlurl(String xmlurl) {
        this.xmlurl = xmlurl == null ? null : xmlurl.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }
}