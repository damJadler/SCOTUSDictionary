package com.damjadler;

import java.util.ArrayList;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

	private ArrayList<Case> caseList;
	private LayoutInflater inflater;
	String sectionStart="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	String sections="";
	
	public MyAdapter(Context context, ArrayList<Case> cases)
	{
		super();
		inflater = LayoutInflater.from(context);
		caseList=cases;
		for(int x=0;x<sectionStart.length();x++)
		{
			String firstChar=""+sectionStart.charAt(x);
			for(int y=0;y<caseList.size();y++)
			{
				if((""+caseList.get(y).difficultParty.charAt(0)).equals(firstChar))
					{
						sections=sections+firstChar;
						x++;
						if(x>=sectionStart.length())
							return;
						firstChar=""+sectionStart.charAt(x);
						
					}
			}
		}
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return caseList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return caseList.get(position);
		
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private static class ViewHolder {
		TextView caseName;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			holder.caseName = (TextView) convertView.findViewById(android.R.id.text1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Case currentCase=caseList.get(position);
		String fullName=currentCase.getCaseName();
		String difficultName=currentCase.getDifficultParty();
		
		if(difficultName.contains("~"))
		difficultName=difficultName.substring(0, difficultName.indexOf("~"));
		
		int index=-1;
		
		fullName=fullName.replace("é", "e");
		fullName=fullName.replace("ã", "a");
		//if(fullName.contains("St. Cyr"))
			//index=-1;
		
		if(fullName.contains(difficultName))
			index=fullName.indexOf(difficultName);
		
		if(index!=-1)
		{
		String firstPart=fullName.substring(0,Math.max(0,index-1));
		String difficultPart=fullName.substring(index, index+difficultName.length());
		String lastPart=fullName.substring(index+difficultName.length());
		
		
		Spannable toSpan=new SpannableString(fullName);
		toSpan.setSpan(new ForegroundColorSpan(Color.BLUE), index, index+difficultName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.caseName.setText(toSpan);
		}
		else
		holder.caseName.setText(fullName);

		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		  HeaderViewHolder holder;
	        if (convertView == null) {
	            holder = new HeaderViewHolder();
	            convertView = inflater.inflate(R.layout.header, parent, false);
	            holder.text = (TextView) convertView.findViewById(R.id.text);
	            convertView.setTag(holder);
	        } else {
	            holder = (HeaderViewHolder) convertView.getTag();
	        }
	        //set header text as first char in name
	        String headerText = "" + caseList.get(position).getDifficultParty().subSequence(0, 1).charAt(0);
	        headerText=headerText.toUpperCase();
	        holder.text.setText(headerText);
	        return convertView;
	    }
	
	 class HeaderViewHolder {
	        TextView text;
	    }

	@Override
	public long getHeaderId(final int position) {
		// return the first character of the country as ID because this is what
		// headers are based upon
		return caseList.get(position).getDifficultParty().subSequence(0,1).charAt(0);
	}

	//String sections="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	@Override
	public int getPositionForSection(int section) {
		
		for(int x=0;x<caseList.size();x++)
		{
			
			String sectionAt=""+sections.charAt(section);
			
			String firstLetter=""+caseList.get(x).difficultParty.charAt(0);
			if(firstLetter.equals(sectionAt))
				return x;
		}
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		String section=""+caseList.get(position).difficultParty.charAt(0);
		return sections.indexOf(section);
		
	}

	@Override
	public Object[] getSections() {
		
		String [] sectionsArr=new String[sections.length()];
		for(int x=0;x<sections.length();x++)
		sectionsArr[x]=""+sections.charAt(x);
		
		// TODO Auto-generated method stub
		return sectionsArr;
	}
	
	
	

}
