package com.appStore.entity;

public class XmlFilter {
    private Integer id;

    private String beginmac;

    private String endmac;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBeginmac() {
        return beginmac;
    }

    public void setBeginmac(String beginmac) {
        this.beginmac = beginmac == null ? null : beginmac.trim();
    }

    public String getEndmac() {
        return endmac;
    }

    public void setEndmac(String endmac) {
        this.endmac = endmac == null ? null : endmac.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}