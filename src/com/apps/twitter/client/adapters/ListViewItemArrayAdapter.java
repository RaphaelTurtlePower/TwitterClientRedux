package com.apps.twitter.client.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.twitter.client.R;
import com.apps.twitter.client.activities.ProfileActivity;
import com.apps.twitter.client.interfaces.ListViewItemModel;
import com.apps.twitter.client.listeners.ItemClickListener;
import com.apps.twitter.client.model.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ListViewItemArrayAdapter extends ArrayAdapter<ListViewItemModel> {
	
	public ListViewItemArrayAdapter(Context context, List<ListViewItemModel> items){
		super(context, 0, items);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent ){
		ListViewItemModel m = getItem(position);
		View v;
	//	if(convertView == null){
			LayoutInflater inflator = LayoutInflater.from(getContext());
			v = inflator.inflate(R.layout.tweet_item, parent, false);
	//	}else{
	//		v = convertView;
	//	}
		
		ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
		TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
		TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
		TextView tvCreatedAt = (TextView) v.findViewById(R.id.tvCreatedAt);
		TextView tvName = (TextView) v.findViewById(R.id.tvName);
		
		ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(m.getProfileImageUrl(), ivProfileImage);
		tvUserName.setText("@" + m.getUserScreenName());
		tvBody.setText(m.getDescription());
		tvCreatedAt.setText(m.getRelativeTimeAgo());
		tvName.setText(m.getUserName());
		ItemClickListener clickListener = new ItemClickListener(m, ProfileActivity.class);
		ivProfileImage.setOnClickListener(clickListener);
		return v;
	}
	
}
