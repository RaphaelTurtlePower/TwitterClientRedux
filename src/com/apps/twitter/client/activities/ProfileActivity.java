package com.apps.twitter.client.activities;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.twitter.client.R;
import com.apps.twitter.client.fragments.UserTimelineFragment;
import com.apps.twitter.client.helpers.TwitterApplication;
import com.apps.twitter.client.helpers.TwitterClient;
import com.apps.twitter.client.listeners.ItemClickListener;
import com.apps.twitter.client.model.AppUser;
import com.apps.twitter.client.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {
	private TwitterClient client; 
	private User user;
	private Long user_id;
	UserTimelineFragment userTimelineFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		client = TwitterApplication.getRestClient();
		user_id = getIntent().getLongExtra("user_id", AppUser.getAppUser().getUid());
		String name = getIntent().getStringExtra("name");	
		
		userTimelineFragment = UserTimelineFragment.newInstance(user_id);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_user_timeline, userTimelineFragment);
		ft.commit();
	
		loadProfileInfo();
	}
	
	public void loadProfileInfo(){
		client.getUserInfo(user_id, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json){
				user = User.fromJSON(json);
				getActionBar().setTitle("@" + user.getScreenName());
				populateProfileHeader(user);
			}
		});
	}
	
	private void populateProfileHeader(User user) {
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		tvName.setText(user.getName());
		tvTagline.setText(user.getTagline());
		tvFollowers.setText(user.getFollowersCount() + " Followers");
		tvFollowing.setText(user.getFollowingCount() + " Following");
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(),ivProfileImage);
		ItemClickListener followersListener = new ItemClickListener(user, FollowersActivity.class);
		tvFollowers.setOnClickListener(followersListener);
		ItemClickListener friendsListener = new ItemClickListener(user, FriendsActivity.class);
		tvFollowing.setOnClickListener(friendsListener);
	}
	
}
