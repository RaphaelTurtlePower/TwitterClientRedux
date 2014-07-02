package com.apps.twitter.client.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.twitter.client.R;
import com.apps.twitter.client.adapters.ListViewItemArrayAdapter;
import com.apps.twitter.client.helpers.EndlessScrollListener;
import com.apps.twitter.client.interfaces.ListViewItemModel;
import com.apps.twitter.client.interfaces.NetworkControls;
import com.apps.twitter.client.interfaces.RefreshListener;
import com.apps.twitter.client.interfaces.ScrollListener;
import com.apps.twitter.client.model.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class ListFragment extends Fragment implements RefreshListener, ScrollListener{
	private ArrayList<ListViewItemModel> itemList;
	private ListViewItemArrayAdapter itemsAdapter;
	private PullToRefreshListView lView;
	private ScrollListener scrollListener;
	private RefreshListener refreshListener;
	protected Long lastId;
	protected Long firstId;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		refreshListener = (RefreshListener) (this);
		scrollListener = (ScrollListener) (this);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//Non-view inflation
		itemList = new ArrayList<ListViewItemModel>();
		itemsAdapter = new ListViewItemArrayAdapter(getActivity(), itemList);
		lastId=null;
		firstId=null;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//view related inflation
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		//Assign view references
		lView = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
		lView.setAdapter(itemsAdapter);
		lView.setOnRefreshListener(new OnRefreshListener(){
			@Override
			public void onRefresh() {
				refreshListener.onRefresh(firstId);
			}
		});
		lView.setOnScrollListener(new EndlessScrollListener(){
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				scrollListener.loadMore(lastId);
			}
			
		});
		return v;
	}
	
	public void addAll(ArrayList lvims){
		itemsAdapter.addAll(lvims);
		setFirstAndLastIds();
	}
	
	public void setFirstAndLastIds(){
		int totalCount = itemsAdapter.getCount();
		if(totalCount > 0){
			lastId = itemsAdapter.getItem(totalCount-1).getUserId() - 1;
			firstId = itemsAdapter.getItem(0).getUserId();
		}
	}
	
	//insert a tweet to array adapter
	public void insert(ListViewItemModel lvim, int position){
		itemsAdapter.insert(lvim, position);
		setFirstAndLastIds();
	}
	
	//return the first Tweet Id from adapter
	public Long getFirstId(){
		return firstId;
	}
	//return the last tweet from adapter
	public Long getLastId(){
		return lastId;
	}

	//refresh list view
	public void onRefreshComplete(){
		lView.onRefreshComplete();
	}

	@Override
	public void loadMore(Long max_id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh(Long since_id) {
		// TODO Auto-generated method stub
		
	}
	 
	  	
}
