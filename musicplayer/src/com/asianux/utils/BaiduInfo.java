package com.asianux.utils;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月8日 下午5:07:39 类说明
 */

public class BaiduInfo {

	private String encode = null;

	private String decode = null;

	private String type = null;

	private String lrcid = null;

	private String flag = null;

	public String toString() {
		return  encode+"\t"+decode+"\t"+type+"\t"+lrcid+"\t"+flag;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public String getDecode() {
		return decode;
	}

	public void setDecode(String decode) {
		this.decode = decode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLrcid() {
		return lrcid;
	}

	public void setLrcid(String lrcid) {
		this.lrcid = lrcid;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}