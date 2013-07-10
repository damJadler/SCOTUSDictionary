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
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity implements Comparator {
	
	protected static final String FILE_NAME = "fileName";
	//String[]fileList;
	ArrayList<Case> caseList;
	HashMap<String, Case> nameMap;
	private static MediaPlayer media;
	private StickyListHeadersListView stickyList;
	InputStream in;
	BufferedReader reader;
	
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
		MyAdapter adapter = new MyAdapter(this, caseList);
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onDestroy()
	{
		media.reset();
	}

	@Override
	public int compare(Object lhs, Object rhs) {
		
		Case first=(Case)lhs;
		Case second=(Case)rhs;
		
		return first.difficultParty.compareTo(second.difficultParty);
		
		
	}


}
