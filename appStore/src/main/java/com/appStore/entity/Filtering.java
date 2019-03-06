package com.appStore.entity;

public class Filtering {
    private Integer id;

    private String model = null;

    private String sn = null;

    private String mac = null;

    private Integer filterState;

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

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac == null ? null : mac.trim();
    }

	public Integer getFilterState() {
		return filterState;
	}

	public void setFilterState(Integer filterState) {
		this.filterState = filterState;
	}

 
}