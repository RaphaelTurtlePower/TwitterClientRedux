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
import com.apps.twitter.client.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class FriendsFragment extends ListFragment implements RefreshListener,
		ScrollListener {
	private TwitterClient client;
	private Long user_id;
	
	public static FriendsFragment newInstance(Long user_id){
		System.out.println("newInstance:FriendsFragment called");
    	
		FriendsFragment friendsFragment = new FriendsFragment();
		Bundle args = new Bundle();
		args.putLong("user_id", user_id);
		friendsFragment.setArguments(args);
		return friendsFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		System.out.println("onCreate:FriendsFragment called");
    	
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();
		user_id = getArguments().getLong("user_id", AppUser.getAppUser().getUid());
		//populateTimeline(getFirstId(), true);

	}
	
	public void populateTimeline(Long cursor, final Boolean refresh){
		System.out.println("populateTimeline:FriendsFragment called: " + cursor + " id: " + user_id);
    	
		if(client == null){
			client = TwitterApplication.getRestClient();
		}
		client.getFriendsList(user_id, cursor, new JsonHttpResponseHandler(){
    			@Override
    			public void handleFailureMessage(Throwable e, String responseBody){
    				System.out.println("Handle failure Message");
    				Log.d("error", e.toString());
    				Log.d("error", responseBody);
    			}
    			
				@Override
				public void onFailure(Throwable e, String s){
					System.out.println("Handle failure Message");
			    	
					Log.d("debug", e.toString());
					Log.d("debug", s);
				}
				
				@Override
				public void onSuccess(JSONObject jsonObject){
					JSONArray json;
					Long firstT;
					Long lastT;
					try {
						json = jsonObject.getJSONArray("users");
						firstT = jsonObject.getLong("previous_cursor");
						lastT = jsonObject.getLong("next_cursor");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if(refresh){
						//update at the top		
						ArrayList<User> users = User.fromJSONArray(json);
		            	for(int i=users.size()-1; i>=0; i--){
		            		insert(users.get(i), 0);
		            	}
		                onRefreshComplete();
					}else{
						//append at the bottom
						addAll(User.fromJSONArray(json));
					}
					firstId=firstT;
					lastId=lastT;
				}
			});
	}

	@Override
	public void loadMore(Long max_id) {
		System.out.println("FriendsFragment:LoadMore triggered");
		populateTimeline(max_id, false);
	}
	
	@Override
	public void onRefresh(Long since_id) {
		System.out.println("FriendsFragment:On Refresh Triggered.");
		populateTimeline(since_id, true);
	}
}
