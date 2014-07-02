package com.apps.twitter.client.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.apps.twitter.client.helpers.TwitterApplication;
import com.apps.twitter.client.helpers.TwitterClient;
import com.apps.twitter.client.interfaces.RefreshListener;
import com.apps.twitter.client.interfaces.ScrollListener;
import com.apps.twitter.client.model.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsTimelineFragment extends ListFragment implements RefreshListener, ScrollListener  {
	private TwitterClient client;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();
		firstId=1L;
		lastId=1L;
		populateTimeline(getFirstId(), null, true);
	}
	
	public void populateTimeline(Long start_id, Long max_id, final Boolean refresh){
		if(client == null){
			client = TwitterApplication.getRestClient();
		}
    		client.getMentionsTimeline(start_id,max_id, new JsonHttpResponseHandler(){
				@Override
				public void onFailure(Throwable e, String s){
					Log.d("debug", e.toString());
					Log.d("debug", s);
				}
				
				@Override
				public void onSuccess(JSONArray json){
					if(refresh){
						//update at the top
						ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);
		            	for(int i=newTweets.size()-1; i>=0; i--){
		            		insert(newTweets.get(i), 0);
		            	}
		                onRefreshComplete();
					}else{
						//append at the bottom
						addAll(Tweet.fromJSONArray(json));
					}
				}
			});
	}

	@Override
	public void loadMore(Long max_id) {
		populateTimeline(null, max_id, false);
		
	}

	@Override
	public void onRefresh(Long since_id) {
		populateTimeline(since_id, null, true);
	}
  
  
	
}
