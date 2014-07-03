package com.apps.twitter.client.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.apps.twitter.client.helpers.TwitterApplication;
import com.apps.twitter.client.helpers.TwitterClient;
import com.apps.twitter.client.interfaces.ListViewItemModel;
import com.apps.twitter.client.interfaces.RefreshListener;
import com.apps.twitter.client.interfaces.ScrollListener;
import com.apps.twitter.client.model.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment extends ListFragment implements RefreshListener, ScrollListener  {
	private TwitterClient client;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();

		//grab initial tweet list
		ArrayList<Tweet> tweetList = Tweet.getInitialTweetList();
		if(tweetList != null && tweetList.size() > 0){
			addAll(tweetList);
		}
		populateTimeline(1L, null, true);

	}
	
	public void populateTimeline(Long start_id, Long max_id, final Boolean refresh){
		System.out.println("Calling populateTimeline with startId:" + start_id + " and max_id:" + max_id);
		if(client == null){
			client = TwitterApplication.getRestClient();
		}
    		client.getTwitterTimeline(start_id,max_id, new JsonHttpResponseHandler(){
    			@Override
    			public void handleFailureMessage(Throwable e, String responseBody){
    				Log.d("error", e.toString());
    				Log.d("error", responseBody);
    			}
    			
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
						System.out.println(json.toString());
						addAll(Tweet.fromJSONArray(json));
					}
				}
			});
	}

	@Override
	public void loadMore(Long max_id) {
		System.out.println("HomeTimelineFragment: LoadMore called Max_id:" + max_id);
		populateTimeline(null, max_id, false);
	}

	@Override
	public void onRefresh(Long since_id) {
		System.out.println("HomeTimelineFragment: onRefresh called. Since_id: " + since_id);
		populateTimeline(since_id, null, true);
	}
  
	
}
