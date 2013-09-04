package com.damjadler;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;

public class MainActivity extends ListActivity implements Comparator, OnQueryTextListener, OnCloseListener {
	
	protected static final String FILE_NAME = "fileName";
	//String[]fileList;
	ArrayList<Case> caseList;
	HashMap<String, Case> nameMap;
	private static MediaPlayer media;
	private StickyListHeadersListView stickyList;
	InputStream in;
	BufferedReader reader;
	MyAdapter adapter;
	
	String infoStringA="Foreign names in legal matters present a " +
			"particular challenge for legal professionals, and " +
			"the Pronouncing Dictionary of United States Supreme" +
			" Court—compiled by a collaboration between the Yale " +
			"Law School and the Yale University Linguistics " +
			"Department—strives to help conscientious lawyers, " +
			"judges, teachers, students, and journalists correctly " +
			"pronounce often-perplexing case names.";
    	 
	String infoStringB="Drawing on textbooks, recordings, accounts by" +
			" litigants or counsel, pronunciation guides, journalism, " +
			"and surveys, we have identified those Supreme Court cases " +
			"that are most susceptible of mispronunciation and " +
			"determined the proper pronunciation. To be sure, " +
			"this is an inexact process, but we hope it will " +
			"be a useful tool for those seeking accuracy and " +
			"authenticity.\n\n For more information, go to " +
			"http://documents.law.yale.edu/pronouncing-dictionary";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AssetManager manager=getAssets();
		media= new MediaPlayer();
		
		/*try {
			fileList=manager.list("audio");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		nameMap=new HashMap<String, Case>();
		
		caseList=new ArrayList<Case> ();
		
		readFile();
		fillMap();
		
		
		/*
		for(int x=0;x<fileList.length;x++)
		{
			String start=fileList[x];
			start=start.substring(0, start.length()-4);
			nameList.add(start);
			nameMap.put(start, fileList[x]);
		}
		*/
		
		stickyList = (StickyListHeadersListView) getListView();
		Collections.sort(caseList, this);
		//MyAdapter adapter = new MyAdapter(this, caseList, "");
		adapter = new MyAdapter(this, caseList);
		setListAdapter(adapter);
		getListView().setFastScrollEnabled(true);
		//getListView().setFastScrollAlwaysVisible(true);
		
				
		//final ArrayAdapter<String> adapter= new ArrayAdapter (this, android.R.layout.simple_list_item_1, nameList);
		
		//setListAdapter(adapter);
		
		
	}
	
	private void readFile() {
		try {
			in = getAssets().open("names/casenames.txt");
		
	    reader = new BufferedReader(new InputStreamReader(in));
	    
	    String line;
	    line=reader.readLine();
	    while(line!=null)
	    {
	    	int commaSpot=line.indexOf(",");
	    	if(line.contains("\""))
	    	{
	    		commaSpot=line.lastIndexOf("\"")+1;
	    		line=line.replace("\"", "");
	    		commaSpot=commaSpot-2;
	    	}
	    	
	    	String caseName=line.substring(0,commaSpot);
	    	String difficultName=line.substring(commaSpot+1, line.length()-1);
	    	    	
	    	//add character checks if necessary
	    	Case toAdd=new Case(caseName, difficultName);
	    	caseList.add(toAdd);
	    	line=reader.readLine();
	    }
	    
	    	reader.close();
	    	in.close();
	    	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	   
		
	}

	private void fillMap() {
		
		for(int x=0;x<caseList.size();x++)
		{
			Case currentCase=caseList.get(x);
			nameMap.put(currentCase.getCaseName(), currentCase);
		}
		
		
		/*
		for(int x=0;x<caseList.size();x++)
		{
			//get difficult name
			String nameCheck=caseList.get(x).getDifficultParty();
			
			//associate difficult name with file name
			for(int y=0;y<fileList.length;y++)
			{
				String fileName=fileList[x];
				if(fileName.substring(0, fileName.length()-4).equals(nameCheck))
					{
					nameMap.put(nameCheck, caseList.get(x));
					y=fileList.length;
					}
					
			}
			
			
		}*/
		
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		  Case item = (Case) getListAdapter().getItem(position);
		  String difficultName=item.getDifficultParty();
		  difficultName=difficultName.toLowerCase();
		  difficultName=difficultName.replace("\'", "");
		  difficultName=difficultName.replace(".", "");
		  
		  difficultName=difficultName+".mp3";		  
		  //String name=nameMap.get(item).difficultParty+".mp3";
		  
		  startAudio(difficultName);	  
		 		  		  
		}

	private void startAudio(String name) {
		media.reset();
		String path="audio/"+name;
		
			   
		try {
			AssetFileDescriptor afd = getAssets().openFd(path);
			media.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
		    media.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		media.start();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);
	    
	    final CloseableSearchView searchView=(CloseableSearchView)menu.findItem(R.id.search).getActionView();
	    	    
	    searchView.setAdapter(adapter);
	    searchView.setOnQueryTextListener(this);
	    searchView.setOnCloseListener(this);
	    String hint=this.getString(R.string.search_hint);
	    searchView.setQueryHint(hint);
	    
	    searchView.setOnKeyListener(new OnKeyListener()
	    {
	    	 /**
	         * This listens for the user to press the enter button on 
	         * the keyboard and then hides the virtual keyboard
	         */
	   	public boolean onKey(View arg0, int arg1, KeyEvent event) {
	           // If the event is a key-down event on the "enter" button
	           if ( (event.getAction() == KeyEvent.ACTION_DOWN  ) &&
	                (arg1           == KeyEvent.KEYCODE_ENTER)   )
	           {               
	           	InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
	                   imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);   
	                   return true;
	           }
	           return false;
	        }
	   } );
	   
	    
	    
	    /*// Associate searchable configuration with the SearchView
	    SearchManager searchManager =
	           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView =
	            (SearchView) menu.findItem(R.id.case_name).getActionView();
	    searchView.setSearchableInfo(
	            searchManager.getSearchableInfo(getComponentName()));*/


	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId()==R.id.info)
		{
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			// 2. Chain together various setter methods to set the dialog characteristics
			
			
			builder.setMessage(infoStringA+"\n\n"+infoStringB);
			builder.setTitle(R.string.dialog_title);
			
			//builder.setMessage(R.string.dialog_message)
			  //     .setTitle(R.string.dialog_title);
			
			// Add the buttons
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               // User clicked OK button
			           }
			       });

			// 3. Get the AlertDialog from create()
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		return false;
	}
	
	/*public void onBackPressed()
	{
		adapter.getFilter().filter("");
		super.onBackPressed();
	}*/
	
	
	public void onDestroy()
	{
		media.reset();
		super.onDestroy();
	}

	@Override
	public int compare(Object lhs, Object rhs) {
		
		Case first=(Case)lhs;
		Case second=(Case)rhs;
		
		return first.difficultParty.compareTo(second.difficultParty);
		
		
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		
		if (newText.equals(""))
	    adapter.getFilter().filter("");
		else 
		{
			adapter.getFilter().filter(newText.toString());
		}
			
		
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onClose() {
		adapter.getFilter().filter("");
		return true;
	}
	
	/*protected void onNewIntent(Intent intent)
	{
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		 if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	            String query = intent.getStringExtra(SearchManager.QUERY);

	            MyAdapter adapter = new MyAdapter(this, caseList, query);
	    		setListAdapter(adapter);
	        }
		
	}*/


}
