package com.appStore.entity;

import java.util.Date;

public class AppstoreMapping {
    private Integer id;

    private String romversion;

    private String boxtype;

    private Boolean isCompulsive;

    private String sn;

    private Integer appstoreid;

    private Integer groupid;

    private String customerid;

    private Date updatetime;

    private String appversion;

    private String hardwareversion;

    private String manufacturer;

    private Boolean dvbsupport;
    
    private String groupName;

    private String homeui;

    private String ipbegin;

    private String ipend;
    
    public AppstoreMapping(String romVersion, String type, String sn, String appVersion,
			String hardwareversion, String manufacturer, Boolean dvbSupport, String homeUI, String ipbegin,
			String ipend) {
		super();
		this.romversion = romVersion;
		this.boxtype = type;
		this.sn = sn;
		this.appversion = appVersion;
		this.hardwareversion = hardwareversion;
		this.manufacturer = manufacturer;
		this.dvbsupport = dvbSupport;
		this.homeui = homeUI;
		this.ipbegin = ipbegin;
		this.ipend = ipend;
    	
	}
    
    public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public AppstoreMapping() {
    	super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRomversion() {
        return romversion;
    }

    public void setRomversion(String romversion) {
        this.romversion = romversion == null ? null : romversion.trim();
    }

    public String getBoxtype() {
        return boxtype;
    }

    public void setBoxtype(String boxtype) {
        this.boxtype = boxtype == null ? null : boxtype.trim();
    }

    public Boolean getIsCompulsive() {
        return isCompulsive;
    }

    public void setIsCompulsive(Boolean isCompulsive) {
        this.isCompulsive = isCompulsive;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    public Integer getAppstoreid() {
        return appstoreid;
    }

    public void setAppstoreid(Integer appstoreid) {
        this.appstoreid = appstoreid;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid == null ? null : customerid.trim();
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion == null ? null : appversion.trim();
    }

    public String getHardwareversion() {
        return hardwareversion;
    }

    public void setHardwareversion(String hardwareversion) {
        this.hardwareversion = hardwareversion == null ? null : hardwareversion.trim();
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer == null ? null : manufacturer.trim();
    }

    public Boolean getDvbsupport() {
        return dvbsupport;
    }

    public void setDvbsupport(Boolean dvbsupport) {
        this.dvbsupport = dvbsupport;
    }

    public String getHomeui() {
        return homeui;
    }

    public void setHomeui(String homeui) {
        this.homeui = homeui == null ? null : homeui.trim();
    }

    public String getIpbegin() {
        return ipbegin;
    }

    public void setIpbegin(String ipbegin) {
        this.ipbegin = ipbegin == null ? null : ipbegin.trim();
    }

    public String getIpend() {
        return ipend;
    }

    public void setIpend(String ipend) {
        this.ipend = ipend == null ? null : ipend.trim();
    }
}