package com.aufbau.resistance;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aufbau.resistance.RunMission.PassFail;

public class ResultsCard extends Fragment {
	static {
		System.loadLibrary("ndk1");
	}
	private native boolean runMission(Mission[] missionlist, int index, boolean[] results);
	public interface ContinueListener {
		public void OnContinue();
	}
	
	// TODO: Rename parameter arguments, choose names that match
		// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
		public static final String ARG_PARAM1 = "Mission ID";

		// TODO: Rename and change types of parameters
		private int MissionID;
		boolean result;


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
		public static ResultsCard newInstance(int missionid) {
			ResultsCard fragment = new ResultsCard();
			Bundle args = new Bundle();
			args.putInt(ARG_PARAM1, missionid);
			fragment.setArguments(args);
			return fragment;
		}

		public ResultsCard() {
			// Required empty public constructor
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if (getArguments() != null) {
				MissionID = getArguments().getInt(ARG_PARAM1);
			}
			SparseArray<PassFail> map = ((RunMission) getParentFragment()).mColl.map;
			int key = 0;
			int siz = map.size();
			boolean[] ress = new boolean[siz];
			for(int i = 0; i < siz; i++) {
			   key = map.keyAt(i);
			   // get the object by the key.
			   int color = map.get(key).color;
			   ress[i] = (Color.blue(color) > Color.red(color));
			}
			
			result = runMission(((GameLoop)getActivity()).gameRoster,MissionID,ress);

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Inflate the layout for this fragment
			View v = inflater.inflate(R.layout.fragment_result_card, container, false);
			TextView resulttext = (TextView) v.findViewById(R.id.resultcard_result);
			resulttext.setText((result)? "Pass" : "Fail");
			Button cont = (Button) v.findViewById(R.id.result_continue);
			cont.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					((ContinueListener) getActivity()).OnContinue();
					
				}
			});
			return v;			
		}

}
