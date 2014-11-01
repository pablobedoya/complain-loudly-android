package main.java.com.zetauma.complainloudly.model;

import com.google.android.gms.maps.model.LatLng;

public class Complaint {
	private String user;
	private String image;
	private String commentary;
	public double latitude;
	public double longitude;
	
	public Complaint() {
	}
	
	public Complaint(String user, String image, String commentary, double latitude, double longitude) {
		this.user = user;
		this.image = image;
		this.commentary = commentary;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public String getCommentary() {
		return commentary;
	}
	
	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public LatLng getLocation() {
		return new LatLng(latitude, longitude);
	}
		
}
