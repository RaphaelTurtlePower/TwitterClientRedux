package com.apps.twitter.client.listeners;

import com.apps.twitter.client.interfaces.ListViewItemModel;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;


public class ItemClickListener implements View.OnClickListener {
	Class<?> cls;
	ListViewItemModel lookup;
	
	public ItemClickListener(ListViewItemModel lookup, Class<?> cls){
		this.cls = cls;
		this.lookup = lookup;
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(v.getContext(), lookup.getUserName(), Toast.LENGTH_LONG).show();
		Intent i = new Intent(v.getContext(), cls);
		i.putExtra("user_id", lookup.getUserId());
		i.putExtra("name", lookup.getUserName());
		v.getContext().startActivity(i);
		
	}
	
	
	
}
