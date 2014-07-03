package com.apps.twitter.client.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.Log;
import com.apps.twitter.client.interfaces.ListViewItemModel;


@Table(name="User")
public class User extends Model implements ListViewItemModel{
	@Column(name="name")
	private String name;
	
	@Column(name="u_id", unique=true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	
	@Column(name="screen_name")
	private String screenName;
	
	@Column(name="profile_image_url")
	private String profileImageUrl;
	
	@Column(name="followers_count")
	private Integer followersCount;
	
	@Column(name="following_count")
	private Integer followingCount;
	
	@Column(name="description")
	private String tagline;
	
	public static ArrayList<User> fromJSONArray(JSONArray jsonArray){
		ArrayList<User> users = new ArrayList<User>(jsonArray.length());
		for(int i=0; i<jsonArray.length(); i++){
			JSONObject userJson = null;
			try{
				userJson = jsonArray.getJSONObject(i);
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
		
			User user = User.fromJSON(userJson);
			if(user != null){
				users.add(user);
			}
		}
		return users;
	}
	
	public static User fromJSON(JSONObject jsonObject){
		try{
			User user = findUser(jsonObject.getLong("id"));
			if(user == null){
				user = new User();
			}
			user.name = jsonObject.getString("name");
			user.uid = jsonObject.getLong("id");
			user.screenName = jsonObject.getString("screen_name");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
			user.tagline = jsonObject.getString("description");
			user.followersCount = jsonObject.getInt("followers_count");
			user.followingCount = jsonObject.getInt("friends_count");
		//	user.save();
			return user;
		}catch(JSONException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getFollowersCount() {
		return followersCount;
	}

	public Integer getFollowingCount() {
		return followingCount;
	}

	public String getTagline() {
		return tagline;
	}

	public static User findUser(Long id){
		return new Select().from(User.class).where("u_id = ?", id).executeSingle();
	}
	
	public String getName() {
		return name;
	}
	public Long getUid() {
		return uid;
	}
	public String getScreenName() {
		return screenName;
	}
	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	@Override
	public Long getUserId() {
		return uid;
	}

	@Override
	public String getUserName() {
		return name;
	}

	@Override
	public String getUserScreenName() {
		return screenName;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return getTagline();
	}

	@Override
	public String getRelativeTimeAgo() {
		// TODO Auto-generated method stub
		return "";
	}
	
	
}
