package com.aufbau.resistance;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link RoleCard.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link RoleCard#newInstance} factory method to
 * create an instance of this fragment.
 * 
 */
public class RoleCard extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	public static final String ARG_PARAM1 = "PlayerID";
	public static final String ARG_PARAM2 = "RoleList";
	public static final String ARG_PARAM3 = "PlayerList";

	// TODO: Rename and change types of parameters
	private int PlayerID;
	private int[] RoleList;
	private String[] PlayerList;


	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param PlayerID
	 *            ID of the player to which this RoleCard corresponds.
	 * @param RoleList
	 *            All the information about people's roles.
	 * @return A new instance of fragment RoleCard.
	 */
	public static RoleCard newInstance(int PlayerID, int[] RoleList, String[] PlayerList) {
		RoleCard fragment = new RoleCard();
		Bundle args = new Bundle();
		args.putInt(ARG_PARAM1, PlayerID);
		args.putIntArray(ARG_PARAM2, RoleList);
		args.putStringArray(ARG_PARAM3, PlayerList);
		fragment.setArguments(args);
		return fragment;
	}

	public RoleCard() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			PlayerID = getArguments().getInt(ARG_PARAM1);
			RoleList = getArguments().getIntArray(ARG_PARAM2);
			PlayerList = getArguments().getStringArray(ARG_PARAM3);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_role_card, container, false);
		RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.RelativeLayout1);
		TextView teamname = (TextView) v.findViewById(R.id.TeamName);
		TextView rch = (TextView) v.findViewById(R.id.rolecardheader);
		int RoleID = RoleList[PlayerID];
		if (RoleID < 0) {		
			layout.setBackgroundColor(Color.RED);
			if (RoleID == -2) {
				rch.setText(R.string.youare);
				teamname.setText(R.string.avalon2b);
				
			}
			else {
				teamname.setText(R.string.team2);
			}
		}
		if (RoleID >= 0) {		
			layout.setBackgroundColor(Color.BLUE);
			teamname.setText(R.string.team1);
			if (RoleID == 1) {
				rch.setText(R.string.youare);
				teamname.setText(R.string.avalon2a);
			}
			if (RoleID == 2) {
				rch.setText(R.string.youare);
				teamname.setText(R.string.avalon1);
			}
		}
		if (RoleID != 0) publishInfo(v,RoleID);
		return v;

	}

	private void publishInfo(View v,int RoleID) {
		// Provides info necessary for each role.	
			String infolist = "";
			String header = "";
			if (RoleID == 2 || RoleID < 0) { //Spies and Merlin
				header = "The spies are: \n";
				for (int j = 0; j< RoleList.length; j++) {
					if (j != PlayerID && RoleList[j] < 0) {
						infolist += PlayerList[j] + "\n";
					}
				}
			}
			else { //Percival
				header = "Merlin and Morgana are: \n";
				for (int j = 0; j< RoleList.length; j++) {
					if (Math.abs(RoleList[j]) == 2) {
						infolist += PlayerList[j] + "\n";
					}
				}
			}
			TextView tv1 = new TextView(getActivity());
			LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.BELOW, R.id.TeamName);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			tv1.setTextColor(Color.WHITE);
			tv1.setText(header+infolist);
			tv1.setGravity(Gravity.CENTER_HORIZONTAL);
			((RelativeLayout) v.findViewById(R.id.RelativeLayout1)).addView(tv1, lp);			
	}
	
	public void makeWayBack() {
		RelativeLayout layout = (RelativeLayout) getView().findViewById(R.id.RelativeLayout1);
		layout.removeView(getView().findViewById(R.id.textView2));
		Button wayBack = new Button(getActivity());
		wayBack.setText("Back");
		wayBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	((RoleCardViewer) getActivity()).setPagerItem(2*RoleList.length+1);
            }
        });
		LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layout.addView(wayBack,lp);
	}





}
