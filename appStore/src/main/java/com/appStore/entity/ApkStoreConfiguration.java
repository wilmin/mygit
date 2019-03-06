package com.appStore.entity;

public class ApkStoreConfiguration {
    private Integer id;

    private Integer modelVersionId;

    private Integer appUpgradeId;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModelVersionId() {
        return modelVersionId;
    }

    public void setModelVersionId(Integer modelVersionId) {
        this.modelVersionId = modelVersionId;
    }

    public Integer getAppUpgradeId() {
        return appUpgradeId;
    }

    public void setAppUpgradeId(Integer appUpgradeId) {
        this.appUpgradeId = appUpgradeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}