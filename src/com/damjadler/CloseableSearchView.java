package com.damjadler;

import android.content.Context;
import android.widget.SearchView;

public class CloseableSearchView extends SearchView {
	
	MyAdapter adapter;

	public CloseableSearchView(Context context) {
		super(context);
		adapter=null;
		// TODO Auto-generated constructor stub
	}
	
	public void onActionViewCollapsed()
	{
		super.onActionViewCollapsed();
		if(adapter!=null)
		adapter.getFilter().filter("");
		return;
		
		
	}

	public void setAdapter(MyAdapter submittedAdapter) {
		adapter=submittedAdapter;
		
	}

}
