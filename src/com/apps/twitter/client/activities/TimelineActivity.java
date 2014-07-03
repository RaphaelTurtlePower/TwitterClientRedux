package com.apps.twitter.client.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.apps.twitter.client.R;
import com.apps.twitter.client.dialogs.CreateTweetDialog;
import com.apps.twitter.client.fragments.HomeTimelineFragment;
import com.apps.twitter.client.fragments.ListFragment;
import com.apps.twitter.client.fragments.MentionsTimelineFragment;
import com.apps.twitter.client.interfaces.DialogResult;
import com.apps.twitter.client.interfaces.NetworkControls;
import com.apps.twitter.client.listeners.FragmentTabListener;
import com.apps.twitter.client.model.Tweet;

public class TimelineActivity extends FragmentActivity implements NetworkControls{
	private Boolean isConnected;
	FragmentTabListener home;
	FragmentTabListener mentions;
	
	private void initializeNetworkState(){
		isConnected = getIntent().getBooleanExtra("isConnected", false);	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		initializeNetworkState();
		setupTabs();
	}
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		home = new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "Home",
				HomeTimelineFragment.class);
		Tab tab1 = actionBar
			.newTab()
			.setText("Home")
			.setIcon(R.drawable.ic_action_home)
			.setTag("HomeTimelineFragment")
			.setTabListener(home);

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		mentions = new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "Mentions",
				MentionsTimelineFragment.class);
		Tab tab2 = actionBar
			.newTab()
			.setText("Mentions")
			.setIcon(R.drawable.ic_action_mentions)
			.setTag("MentionsTimelineFragment")
			.setTabListener(mentions);

		actionBar.addTab(tab2);
	}
	
	// Inflate the menu; this adds items to the action bar if it is present.
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.timeline, menu);
			return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    // Handle item selection
		    if(item.getItemId() == R.id.new_tweet) {
		    	checkNetworkConnection();
		    	if(!isConnected){
		    		return true;
		    	}
		    	CreateTweetDialog newDialog = new CreateTweetDialog();
		    	newDialog.setDialogResult(new DialogResult(){
		    		@Override
					public void finish(Tweet result) {
		    			Log.d("debug", result.getBody() + result.getUid());
		    			Toast.makeText(TimelineActivity.this, result.getBody(), Toast.LENGTH_LONG).show();
		    			ListFragment h = (ListFragment)home.mFragment;
		    			h.insert(result,0);
		    			if(result.getBody().contains("@" + result.getUserScreenName())){
		    				ListFragment m = (ListFragment)mentions.mFragment;
		    				m.insert(result, 0);
		    			}
		    		}
		    		
		    	});
		    	newDialog.show(getFragmentManager(), "dialog");
		    }
		    return true;
		}
	  
		 public Boolean isNetworkAvailable() {
			    ConnectivityManager connectivityManager 
			          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
			}
		 
	    public void checkNetworkConnection(){
	    	if(isNetworkAvailable()){
	     		Toast.makeText(this, "Online Mode", Toast.LENGTH_LONG).show();
	 		   isConnected = true;
	    	}else{
	    		Toast.makeText(this, "Offline Mode", Toast.LENGTH_LONG).show();
	 		   	isConnected = false;
	    	}
	  	}
	    
	    public Boolean isConnected(){
	    	return isConnected;
	    }
	    
	    public void onProfileView(MenuItem mi){
	    	Intent i = new Intent(this, ProfileActivity.class);
	    	startActivity(i);
	    }
}
