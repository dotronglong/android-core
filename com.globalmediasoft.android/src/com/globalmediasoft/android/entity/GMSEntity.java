package com.globalmediasoft.android.entity;

public class GMSEntity {

	private String mOsVersion = null;
	private String mBuildVersion = null;
	private String mApiVersion = null;
	private String mBuildDevice = null;
	private String mBuildModel = null;
	private String mMacAddress = null;
	private String mBuildProduct = null;
	private String mIMSI = null;
	private String mIMEI = null;

	public GMSEntity() {
		/*
		 * Init variable here
		 */
	}

	public GMSEntity(String imei) {
		this.mIMEI = imei;
	}

	public GMSEntity(String osVersion, String buildVersion, String apiVersion,
			String buildDevice, String buildModel, String macAddress,
			String buildProduct, String imsi, String imei) {
		this.mOsVersion = osVersion;
		this.mBuildVersion = buildVersion;
		this.mApiVersion = apiVersion;
		this.mBuildDevice = buildDevice;
		this.mBuildModel = buildModel;
		this.mMacAddress = macAddress;
		this.mBuildProduct = buildProduct;
		this.mIMSI = imsi;
		this.mIMEI = imei;
	}

	public String getOsVersion() {
		return mOsVersion;
	}

	public void setOsVersion(String mOsVersion) {
		this.mOsVersion = mOsVersion;
	}

	public String getBuildVersion() {
		return mBuildVersion;
	}

	public void setBuildVersion(String mBuildVersion) {
		this.mBuildVersion = mBuildVersion;
	}

	public String getApiVersion() {
		return mApiVersion;
	}

	public void setApiVersion(String mApiVersion) {
		this.mApiVersion = mApiVersion;
	}

	public String getBuildDevice() {
		return mBuildDevice;
	}

	public void setBuildDevice(String mBuildDevice) {
		this.mBuildDevice = mBuildDevice;
	}

	public String getBuildModel() {
		return mBuildModel;
	}

	public void setBuildModel(String mBuildModel) {
		this.mBuildModel = mBuildModel;
	}

	public String getBuildProduct() {
		return mBuildProduct;
	}

	public void setBuildProduct(String mBuildProduct) {
		this.mBuildProduct = mBuildProduct;
	}

	public String getIMSI() {
		return mIMSI;
	}

	public void setIMSI(String mIMSI) {
		this.mIMSI = mIMSI;
	}

	public String getIMEI() {
		return mIMEI;
	}

	public void setIMEI(String mIMEI) {
		this.mIMEI = mIMEI;
	}

	public String getMacAddress() {
		return mMacAddress;
	}

	public void setMacAddress(String mMacAddress) {
		this.mMacAddress = mMacAddress;
	}
}
