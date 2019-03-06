package com.appStore.entity;

public class AppstoreWithAllInfo extends Appstore {
	// apk类型编号
	private Integer typeId;
	// apk类型名称
	private String typeName;

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
