package com.apps.twitter.client.activities;
import com.apps.twitter.client.R;
import com.apps.twitter.client.fragments.FollowersFragment;
import com.apps.twitter.client.model.AppUser;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class FollowersActivity extends FragmentActivity {
	private Long user_id;
	FollowersFragment followersFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("FollowersActivity:onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_followers);
		user_id = getIntent().getLongExtra("user_id", AppUser.getAppUser().getUid());
		followersFragment = FollowersFragment.newInstance(user_id);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_followers_list, followersFragment);
		ft.commit();
		getSupportFragmentManager().executePendingTransactions();
	}
}
