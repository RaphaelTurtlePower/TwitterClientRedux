package com.apps.twitter.client.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import com.apps.twitter.client.helpers.TwitterApplication;
import com.apps.twitter.client.helpers.TwitterClient;
import com.apps.twitter.client.interfaces.RefreshListener;
import com.apps.twitter.client.interfaces.ScrollListener;
import com.apps.twitter.client.model.AppUser;
import com.apps.twitter.client.model.Tweet;
import com.apps.twitter.client.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class FollowersFragment extends ListFragment implements RefreshListener,
		ScrollListener {
	private TwitterClient client;
	private Long user_id;
	
	public static FollowersFragment newInstance(Long user_id){
		System.out.println("FollowersFragment:newInstance");
		
		FollowersFragment followersFragment = new FollowersFragment();
		Bundle args = new Bundle();
		args.putLong("user_id", user_id);
		followersFragment.setArguments(args);
		return followersFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		System.out.println("FollowersFragment:onCreate");
		
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();
		user_id = getArguments().getLong("user_id", AppUser.getAppUser().getUid());
		//populateTimeline(getFirstId(), true);

	}
	
	public void populateTimeline(Long cursor, final Boolean refresh){
		System.out.println("FollowersFragment:populateTimeline");
		
		if(client == null){
			client = TwitterApplication.getRestClient();
		}
    		client.getFollowersList(user_id, cursor, new JsonHttpResponseHandler(){
    			@Override
    			public void handleFailureMessage(Throwable e, String responseBody){
    				System.out.println("Handle failure Message");
    				System.out.println("error:"+ e.toString());
    				System.out.println("error"+ responseBody);
    			}
    			
				@Override
				public void onFailure(Throwable e, String s){
					System.out.println("Handle failure Message");
					Log.d("debug", e.toString());
					Log.d("debug", s);
				}
				
				@Override
				public void onSuccess(JSONObject jsonObject){
					System.out.println("Success JSON OBJECT!!");
					JSONArray json;
					try {
						json = jsonObject.getJSONArray("users");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if(refresh){
						ArrayList<User> users = User.fromJSONArray(json);
		            	for(int i=users.size()-1; i>=0; i--){
		            		insert(users.get(i), 0);
		            	}
		                onRefreshComplete();
					}else{
						addAll(User.fromJSONArray(json));
					}
				}
				
			});
    		System.out.println("End of getFriendsList Call.");
	}


	@Override
	public void loadMore(Long max_id) {
		populateTimeline(max_id, false);
	}
	
	@Override
	public void onRefresh(Long since_id) {
		populateTimeline(since_id, true);
	}
}
