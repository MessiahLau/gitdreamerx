package com.droidstouch.iweibo.bean;

import android.graphics.Bitmap;

public class listInfo {
	private String content;
	private String username;
	private String userphotoUrl;
	private String photoUrl;
	private String id;
	private Bitmap userphoto;
	private Bitmap photo;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserphotoUrl() {
		return userphotoUrl;
	}
	public void setUserphotoUrl(String userphotoUrl) {
		this.userphotoUrl = userphotoUrl;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Bitmap getUserphoto() {
		return userphoto;
	}
	public void setUserphoto(Bitmap userphoto) {
		this.userphoto = userphoto;
	}
	public Bitmap getPhoto() {
		return photo;
	}
	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

}
