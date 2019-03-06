package com.appStore.entity;

import java.util.Date;

public class Appstore implements Comparable {
    private Integer id;

    private String apkpackagename;

    private String apkversionname;

    private Date updateTime;

    private Date onlineTime;

    private Long apksize;

    private Integer downloadtotal;

    private String apkdownload;

    private String s3iconkey;

    private String s3appkey;

    private String s3screenshotkey;

    private String apkname;

    private Long apkiconsize;

    private String apkicon;

    private String apkprofile;

    private String apkscreenshot;

    private String apkbigicon;

    private Integer apktype;

    private String apkversion;

    private String apklanguage;

    private String website;

    private String email;

    private String privacypolicy;

    private String description;

    private String md5;

    private String apkinfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApkpackagename() {
        return apkpackagename;
    }

    public void setApkpackagename(String apkpackagename) {
        this.apkpackagename = apkpackagename == null ? null : apkpackagename.trim();
    }

    public String getApkversionname() {
        return apkversionname;
    }

    public void setApkversionname(String apkversionname) {
        this.apkversionname = apkversionname == null ? null : apkversionname.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Long getApksize() {
        return apksize;
    }

    public void setApksize(Long apksize) {
        this.apksize = apksize;
    }

    public Integer getDownloadtotal() {
		return downloadtotal;
	}

	public void setDownloadtotal(Integer downloadtotal) {
		this.downloadtotal = downloadtotal;
	}

	public String getApkdownload() {
        return apkdownload;
    }

    public void setApkdownload(String apkdownload) {
        this.apkdownload = apkdownload == null ? null : apkdownload.trim();
    }

    public String getS3iconkey() {
        return s3iconkey;
    }

    public void setS3iconkey(String s3iconkey) {
        this.s3iconkey = s3iconkey == null ? null : s3iconkey.trim();
    }

    public String getS3appkey() {
        return s3appkey;
    }

    public void setS3appkey(String s3appkey) {
        this.s3appkey = s3appkey == null ? null : s3appkey.trim();
    }

    public String getS3screenshotkey() {
        return s3screenshotkey;
    }

    public void setS3screenshotkey(String s3screenshotkey) {
        this.s3screenshotkey = s3screenshotkey == null ? null : s3screenshotkey.trim();
    }

    public String getApkname() {
        return apkname;
    }

    public void setApkname(String apkname) {
        this.apkname = apkname == null ? null : apkname.trim();
    }

    public Long getApkiconsize() {
        return apkiconsize;
    }

    public void setApkiconsize(Long apkiconsize) {
        this.apkiconsize = apkiconsize;
    }

    public String getApkicon() {
        return apkicon;
    }

    public void setApkicon(String apkicon) {
        this.apkicon = apkicon == null ? null : apkicon.trim();
    }

    public String getApkprofile() {
        return apkprofile;
    }

    public void setApkprofile(String apkprofile) {
        this.apkprofile = apkprofile == null ? null : apkprofile.trim();
    }

    public String getApkscreenshot() {
        return apkscreenshot;
    }

    public void setApkscreenshot(String apkscreenshot) {
        this.apkscreenshot = apkscreenshot == null ? null : apkscreenshot.trim();
    }

    public String getApkbigicon() {
        return apkbigicon;
    }

    public void setApkbigicon(String apkbigicon) {
        this.apkbigicon = apkbigicon == null ? null : apkbigicon.trim();
    }

    public Integer getApktype() {
        return apktype;
    }

    public void setApktype(Integer apktype) {
        this.apktype = apktype;
    }

    public String getApkversion() {
        return apkversion;
    }

    public void setApkversion(String apkversion) {
        this.apkversion = apkversion == null ? null : apkversion.trim();
    }

    public String getApklanguage() {
        return apklanguage;
    }

    public void setApklanguage(String apklanguage) {
        this.apklanguage = apklanguage == null ? null : apklanguage.trim();
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website == null ? null : website.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPrivacypolicy() {
        return privacypolicy;
    }

    public void setPrivacypolicy(String privacypolicy) {
        this.privacypolicy = privacypolicy == null ? null : privacypolicy.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5 == null ? null : md5.trim();
    }

    public String getApkinfo() {
        return apkinfo;
    }

    public void setApkinfo(String apkinfo) {
        this.apkinfo = apkinfo == null ? null : apkinfo.trim();
    }
	//集合sort排序使用
   	@Override
   	public int compareTo(Object o) {
   		// TODO Auto-generated method stub
   		Appstore app = (Appstore) o; 
   		return Integer.valueOf(this.apkversion)-Integer.valueOf(app.getApkversion());
   	}
}