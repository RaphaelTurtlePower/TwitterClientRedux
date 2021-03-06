package com.apps.twitter.client.helpers;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "V98sazxFWwtWSvS6mQ87pNlWx";       // Change this
    public static final String REST_CONSUMER_SECRET = "ReaXPhJ6QzTbaGDf6yLXBeywTZdfmQh2xT1AZmTWtrDvBszAxo"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://twitterclient"; // Change this (here and in manifest)
    
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    public void getTwitterTimeline(Long since_id, Long max_id, AsyncHttpResponseHandler handler){
    	System.out.println("Get HomeTimeline called.");
    	String apiUrl = getApiUrl("statuses/home_timeline.json");
    	RequestParams params = new RequestParams();
    	if(since_id != null){
    		System.out.println("Since_Id:" + since_id.toString());
    		params.put("since_id", since_id.toString());
    	}
    	if(max_id != null){
    		System.out.println("Max_id:" + max_id.toString());
        	params.put("max_id", max_id.toString() );
    	}
    	client.get(apiUrl,params, handler);
    	
    }
    
    
    public void getUserInfo(Long user_id,  AsyncHttpResponseHandler handler){
    	if(user_id == null){
    		return;
    	}
    	String apiUrl = getApiUrl("users/show.json");
    	RequestParams params = new RequestParams();
    	params.put("user_id", user_id.toString());
    	client.get(apiUrl,params, handler);
    }
    
    public void getUserTimeline(Long since_id, Long max_id, Long user_id, AsyncHttpResponseHandler handler){
    	System.out.println("Get USERTimeline called.");
    	String apiUrl = getApiUrl("statuses/user_timeline.json");
    	RequestParams params = new RequestParams();
    	if(since_id != null){
    		System.out.println("Since_Id:" + since_id.toString());
        	params.put("since_id", since_id.toString());
    	}
    	if(max_id != null){
    		System.out.println("Max_id:" + max_id.toString());
        	params.put("max_id", max_id.toString() );
    	}
    	if(user_id != null){
    		params.put("user_id", user_id.toString());
    	}
    	client.get(apiUrl,params, handler);
    	
    }
    
    public void getMentionsTimeline(Long since_id, Long max_id, AsyncHttpResponseHandler handler){
    	System.out.println("Get MENTIONSTimeline called.");
    	String apiUrl = getApiUrl("statuses/mentions_timeline.json");
    	RequestParams params = new RequestParams();
    	if(since_id != null){
    		System.out.println("Since_Id:" + since_id.toString());
        	params.put("since_id", since_id.toString());
    	}
    	if(max_id != null){
    		System.out.println("Max_id:" + max_id.toString());
        	params.put("max_id", max_id.toString() );
    	}
    	client.get(apiUrl,params, handler);
    	
    }
    
    public void getFriendsList(Long user_id, Long cursor, AsyncHttpResponseHandler handler){
    	System.out.println("Client:getFriendsListCalled");
    	if(user_id == null){
    		System.out.println("User id is null for friends list");
        	return;
    	}
    	String apiUrl = getApiUrl("friends/list.json");
    	RequestParams params = new RequestParams();
    	params.put("user_id", user_id.toString());
    	if(cursor != null){
    		System.out.println("Cursor:" + cursor.toString());
    		params.put("cursor", cursor.toString());
    	}
    	client.get(apiUrl,params, handler);
    }
    
    public void getFollowersList(Long user_id, Long cursor, AsyncHttpResponseHandler handler){
    	System.out.println("Get FOLLOWERSLIST called.");
    	
    	if(user_id == null){
    		System.out.println("User id is null for followers list");
    		return;
    	}
    	String apiUrl = getApiUrl("followers/list.json");
    	RequestParams params = new RequestParams();
    	params.put("user_id", user_id.toString());
    	if(cursor != null){
    		System.out.println("Cursor:" + cursor.toString());
        	params.put("cursor", cursor.toString());
    	}
    	client.get(apiUrl,params, handler);
    } 
    public void getVerifiedCredentials(AsyncHttpResponseHandler handler){
    	String apiUrl = getApiUrl("account/verify_credentials.json");
    	client.get(apiUrl,  handler);
    }
    
    public void postTweet(AsyncHttpResponseHandler handler, String tweet){
    	String apiUrl = getApiUrl("statuses/update.json");
    	RequestParams params = new RequestParams();
    	params.put("status", tweet);
    	client.post(apiUrl,  params, handler);
    }
    
    // CHANGE THIS
    // DEFINE METHODS for different API endpoints here
  /*  public void getInterestingnessList(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        client.get(apiUrl, params, handler);
    }
    */
    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
}