package com.aufbau.resistance;

import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.aufbau.resistance.LeaderSelect.VoteResultPrompt;

public class GameLoop extends FragmentActivity
	implements LeaderSelect.NoticeDialogListener, ResultsCard.ContinueListener {

	
	String[] playerlist = {"Olivia","Quinn","Abby","Harrison","David Rosen","Huck"};
	int[] roles = {0,-1,0,0,-1,2};
	int nplayers = 6;
	int currentMission = 0;
	int currentLeader = 0;
	int failedRosters = 0;
	
	static {
		System.loadLibrary("ndk1");
	}
	
	private native void setMissions(int numPlayers, Mission[] roster);
	Mission[] gameRoster = new Mission[5];
	static String PARAM1 = "numPlayers";
	static String PARAM2 = "playerlist";
	static String PARAM3 = "roles";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent rcvi = getIntent();
		playerlist = rcvi.getStringArrayExtra(PARAM2);
		roles = rcvi.getIntArrayExtra(PARAM3);
		nplayers = playerlist.length;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_mission_roster);
		// TODO: set playerlist equal to what was passed from prev activity
		for (int i=0; i < 5; i++) {
			gameRoster[i] = new Mission();
		}
	    setMissions(nplayers, gameRoster);
		MissionRoster newFragment = new MissionRoster();	
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.lscontainer,newFragment,"MissionList").commit();
        Random rn = new Random();
        currentLeader = rn.nextInt(nplayers);
	}
	
	protected void runMission(String[] teamroster,String leader) {
		Fragment newFragment = RunMission.newInstance(teamroster,leader);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.lscontainer,newFragment,"RunMission").commit();
	}
	
	private int countVictories() {
		int svicts = 0;
		int rvicts = 0;
		for (Mission m: gameRoster) {
			if (m.done == 1) {rvicts++; if (rvicts==3) return 1;}
			if (m.done == -1) {svicts++; if (svicts==3) return -1;}
		}
		return 0;		
	}
	
	public void OnContinue() {
		int done = countVictories();
		if (done == 0) {
		currentLeader = (currentLeader+1) % nplayers;
		currentMission += 1;
		MissionRoster newFragment = new MissionRoster();	
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.lscontainer,newFragment,"MissionList").commit();
		}
		else {
			int cond = (done > 0) ? R.integer.RES_WIN : R.integer.SPIES_WIN;
			endGame(cond);
		}
	}

	@Override
	public void onDialogPositiveClick(VoteResultPrompt dialog) {
		runMission(dialog.teamroster, dialog.leader);
		
	}

	@Override
	public void onDialogNegativeClick(VoteResultPrompt dialog) {
		currentLeader = (currentLeader+1) % nplayers;
		failedRosters += 1;
		if (failedRosters == 5) {endGame(R.integer.FAILED_ROSTERS);}
		
	}
	
	public void endGame(int condition) {
		Intent intent = new Intent(GameLoop.this, GameEnd.class);
		//GameEnd needs end condition, playerlist, rolelist
		Log.i("GameLoop","end condition code is "+Integer.toString(condition));
		intent.putExtra(GameEnd.PARAM1, condition);
		intent.putExtra(GameEnd.PARAM2, playerlist);
		intent.putExtra(GameEnd.PARAM3, roles);
    	startActivity(intent);
	}

}
