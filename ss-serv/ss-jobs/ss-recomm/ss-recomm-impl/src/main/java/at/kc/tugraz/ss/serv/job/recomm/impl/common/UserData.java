package at.kc.tugraz.ss.serv.job.recomm.impl.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserData implements Comparable<UserData> {

	private int userID;
	private int wikiID;
	private String timestamp;
	private double rating;
	
	private List<Integer> categories;
	private List<Integer> tags;
	
	public UserData(int userID, int wikiID, String timestamp) {
		this.userID = userID;
		this.wikiID = wikiID;
		this.timestamp = timestamp;
		this.rating = -2.0;
		
		this.categories = new ArrayList<Integer>();
		this.tags = new ArrayList<Integer>();
	}
	
	@Override
	public int compareTo(UserData data) {
		//return (Long.parseLong(getTimestamp()) <= Long.parseLong(data.getTimestamp()) ? - 1 : 1);
		if (this.userID < data.getUserID()) {
			return -1;
		} else if (this.userID > data.userID) {
			return 1;
		} else {
			if (!this.timestamp.isEmpty() && !data.timestamp.isEmpty()) {
				if (Long.parseLong(this.timestamp) < Long.parseLong(data.timestamp)) {
					return -1;
				} else if (Long.parseLong(this.timestamp) > Long.parseLong(data.timestamp)) {
					return 1;
				}
			}
		}
		return 0;
	}
	
	// Getter -------------------------------------------------------------------------
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public int getUserID() {
		return this.userID;
	}
	
	public void setWikiID(int wikiID) {
		this.wikiID = wikiID;
	}
	
	public int getWikiID() {
		return this.wikiID;
	}
	
	public String getTimestamp() {
		return this.timestamp;
	}
	
	public double getRating() {
		return this.rating;
	}
	
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public List<Integer> getCategories() {	
		return this.categories;
	}
	
	public void setTags(List<Integer> tags) {
		this.tags = tags;
	}
	
	public List<Integer> getTags() {
		return this.tags;
	}
	
	// Statics ----------------------------------------------------------------------------------
	
	public static UserData getUserData(List<UserData> lines, int userID, int resID) {
		UserData returnData = null;
		for (UserData data : lines) {
			if (data.userID == userID) {
				returnData = data;
				if (data.wikiID == resID) {
					return returnData;
				}
			}
		}
		return returnData;
	}
	
	public static UserData getResData(List<UserData> lines, int userID, int resID) {
		UserData returnData = null;
		for (UserData data : lines) {
			if (data.wikiID == resID) {
				returnData = data;
				if (data.userID == userID) {
					return returnData;
				}
			}
		}
		return returnData;
	}
	
	public static UserData getLastData(List<UserData> lines, Set<Integer> ids) {
		long maxTimestamp = Long.MAX_VALUE;
		UserData returnData = null;
		for (UserData data : lines) {
			if (ids.contains(data.userID)) {
				long timestamp = Long.parseLong(data.timestamp);
				if (timestamp < maxTimestamp) {
					maxTimestamp = timestamp;
					returnData = data;
				}
			}
		}
		return returnData;
	}
	
	public static List<Integer> getResourcesFromUser(List<UserData> lines, int userID) {
		Set<Integer> resourceList = new HashSet<Integer>();		
		for (UserData data : lines) {		
			if (data.userID == userID) {
				resourceList.add(data.wikiID);
			}
		}		
		return new ArrayList<Integer>(resourceList);
	}
}