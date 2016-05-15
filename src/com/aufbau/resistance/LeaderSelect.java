package com.aufbau.resistance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LeaderSelect extends Fragment {
	final static public String param1 = "PlayerList";
	final static public String param2 = "LeaderPos";
	final static public String param3 = "PlayersToGo";
	String leadername;
	int playersToGo;
	
	public interface NoticeDialogListener {
        public void onDialogPositiveClick(VoteResultPrompt dialog);
        public void onDialogNegativeClick(VoteResultPrompt dialog);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    String[] PlayerList = getArguments().getStringArray(param1);
	    leadername = PlayerList[getArguments().getInt(param2)];
	    playersToGo = getArguments().getInt(param3);
		View v = inflater.inflate(R.layout.fragment_leader_select, container, false);
		TextView header = (TextView) v.findViewById(R.id.leaderHeader);
		header.setText("Leader "+leadername+" is choosing a team of "+Integer.toString(playersToGo)+" players");
		final ListView SelectList = (ListView) v.findViewById(R.id.PlayerList);
		SelectAdapter sadpt = new SelectAdapter(getActivity(), R.layout.leaderselect_element, new ArrayList<String>(Arrays.asList(PlayerList)));
		SelectList.setAdapter(sadpt);
		SelectList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> sadpt, View v, int position,
					long arg3) {
				TextView txt = (TextView) v.findViewById(R.id.leadelmtext);
				boolean enb = !txt.isEnabled();
				txt.setEnabled(enb);
				//Toast.makeText(getActivity(), "Player "+txt.getText()+((enb)?" enabled":" disabled"), Toast.LENGTH_SHORT).show();
				
			}
		});
		Button runmission = (Button) v.findViewById(R.id.RunMission);
		runmission.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				List<String> selectedPlayers = new ArrayList<String>();
				for(int childIndex = 0; childIndex < SelectList.getAdapter().getCount(); childIndex++) {
				     TextView childtxt = (TextView) SelectList.getChildAt(childIndex).findViewById(R.id.leadelmtext);
				     if(childtxt.isEnabled()) {
				    	 selectedPlayers.add(childtxt.getText().toString());
				     }
				}
				if (selectedPlayers.size() == playersToGo) {
				VoteResultPrompt vote = new VoteResultPrompt();
				Bundle args = new Bundle();
				args.putStringArray(VoteResultPrompt.PARAM1, selectedPlayers.toArray(new String[selectedPlayers.size()]));
				args.putString(VoteResultPrompt.PARAM2, leadername);
				vote.setArguments(args);
				vote.show(getFragmentManager(), "Dialog");
				}
				else {
					Toast.makeText(getActivity(), 
					"This mission must consist of "+Integer.toString(playersToGo)+" players. "
							+ "You have selected "+Integer.toString(selectedPlayers.size()),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		return v;

	}
	
	public static class VoteResultPrompt extends DialogFragment {
		public static final String PARAM1 = "TeamRoster";
		public static final String PARAM2 = "Leader";
		public String[] teamroster;
		public String leader;
		// Use this instance of the interface to deliver action events
	    NoticeDialogListener mListener;
	    
	    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
	    @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        // Verify that the host activity implements the callback interface
	        try {
	            // Instantiate the NoticeDialogListener so we can send events to the host
	            mListener = (NoticeDialogListener) activity;
	        } catch (ClassCastException e) {
	            // The activity doesn't implement the interface, throw exception
	            throw new ClassCastException(activity.toString()
	                    + " must implement NoticeDialogListener");
	        }
	    }
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    	teamroster = getArguments().getStringArray(PARAM1);
	    	leader = getArguments().getString(PARAM2);
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(constructMessage())
	               .setPositiveButton("Accepted", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   mListener.onDialogPositiveClick(VoteResultPrompt.this);
	                   }
	               })
	               .setNegativeButton("Rejected", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                      mListener.onDialogNegativeClick(VoteResultPrompt.this);
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	    private String constructMessage() {
	    	String msg = "Vote on a mission with the following members: \n";
	    	for (String player: teamroster) {
	    		msg += player + "\n";
	    	}
	    	return msg;
	    }
	}
}
