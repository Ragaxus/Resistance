package com.aufbau.resistance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

public class RoleCardViewer extends ActionBarActivity {
	
	CollectionPagerAdapter mColl;
	String[] playerList;
	DiodePager vp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_role_card_viewer);	
		vp = (DiodePager) findViewById(R.id.pager);
		Intent intent = getIntent();
		playerList = intent.getStringArrayExtra("PLAYERLIST");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		mColl = new CollectionPagerAdapter(getSupportFragmentManager(), playerList, prefs);
		vp.setAdapter(mColl);
	}
	
	public void setPagerItem(int position) {
		vp.setCurrentItem(position,false);
		if (position < 2*playerList.length+1) {
		((RoleCard) mColl.getRegisteredFragment(position)).makeWayBack();
		}
	}

}
