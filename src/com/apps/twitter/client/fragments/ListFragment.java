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
		System.out.println("ListFragment's onCreate triggered. LastId=null, FirstId=null");
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
				System.out.println("Pull to Refresh's OnRefresh is firing.");
				refreshListener.onRefresh(firstId);
			}
		});
		lView.setOnScrollListener(new EndlessScrollListener(){
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				System.out.println("ScrollListener's On Load More is firing.");
				scrollListener.loadMore(lastId);
			}
			
		});
		return v;
	}
	
	public void addAll(ArrayList lvims){
		System.out.println("Add all is called. This should be onScroll. LastId must be updated.");
		System.out.println("Items Adapter:" + itemsAdapter.getCount());
		System.out.println("*******New values to add:" + lvims.size());
		itemsAdapter.addAll(lvims);
		System.out.println("*******New Array Size:" + itemsAdapter.getCount());
		setFirstAndLastIds();
	}
	
	public void setFirstAndLastIds(){
		System.out.println("First and Last Ids are being set.");
		System.out.println("Initial Values -> First: " + firstId + " and Last:" + lastId);
		int totalCount = itemsAdapter.getCount();
		if(totalCount > 0){
			System.out.println("Last Item:" + itemsAdapter.getItem(totalCount-1).getDescription());
			System.out.println("First Item: " + itemsAdapter.getItem(0).getDescription());
			lastId = itemsAdapter.getItem(totalCount-1).getUid();
			firstId = itemsAdapter.getItem(0).getUid();
		}
		System.out.println("New Values -> First: " + firstId + " and Last:" + lastId);
	}
	
	//insert a tweet to array adapter
	public void insert(ListViewItemModel lvim, int position){
		System.out.println("Insert is called. This should be onRefresh. FirstId must be updated.");
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
		System.out.println("On Refresh Complete for ListView is called");
		lView.onRefreshComplete();
	}

	@Override
	public void loadMore(Long max_id) {
		System.out.println("Load more is called.");
	}

	@Override
	public void onRefresh(Long since_id) {
		System.out.println("OnRefresh is called.");
	}
	 
	  	
}
