package com.apps.twitter.client.activities;

import com.apps.twitter.client.R;
import com.apps.twitter.client.fragments.FriendsFragment;
import com.apps.twitter.client.model.AppUser;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;


public class FriendsActivity extends FragmentActivity {
	private Long user_id;
	FriendsFragment friendsFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		user_id = getIntent().getLongExtra("user_id", AppUser.getAppUser().getUid());
		Toast.makeText(this, user_id.toString(), Toast.LENGTH_LONG).show();
		
		friendsFragment = FriendsFragment.newInstance(user_id);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_friends_list, friendsFragment);
		ft.commit();
	}
}
