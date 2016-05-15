package com.aufbau.resistance;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GameEnd extends ActionBarActivity {
	public static String PARAM1 = "condition";
	public static String PARAM2 = "playerlist";
	public static String PARAM3 = "rolelist";
	String merlin;
	
	public interface OnPlayAgain {
		public void playAgain();
	}
	private String findmerlin(int[] rolelist, String[] playerlist) {
		for (int j=0; j<playerlist.length; j++) {
			if (rolelist[j] == 2) return playerlist[j];
		}
		return null;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_end);
		Intent intt = getIntent();
		int condition = intt.getIntExtra(PARAM1,0);
		int[] rolelist = intt.getIntArrayExtra(PARAM3);
		String[] playerlist = intt.getStringArrayExtra(PARAM2);
		merlin = findmerlin(rolelist, playerlist);
		Log.i("GameEnd","merlin is "+merlin+" and the end condition code is "+Integer.toString(condition));
		Log.i("Sanity","4+1="+Integer.toString(5));
		Fragment firstfrag; 
		if (merlin != null && condition == R.integer.RES_WIN) {
			ArrayList<String> reslist = new ArrayList<String>();
			for (int j=0; j<playerlist.length; j++) {
				if (rolelist[j] >= 0) reslist.add(playerlist[j]);
			}
			String[] resarr = new String[reslist.size()];
			resarr = reslist.toArray(resarr);
			firstfrag = KillMerlin.newInstance(resarr,merlin);
			}
		else {
			firstfrag = WhoWon.newInstance(condition,(String) null);
		}

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, firstfrag).commit();
		}

	}
	
	public static class KillMerlin extends Fragment {
		String[] ResistanceList;
		String merlin;
		public static KillMerlin newInstance(String[] reslist, String merlin) {
			KillMerlin frag = new KillMerlin();
			Bundle bundle = new Bundle();
			bundle.putString(GameEnd.PARAM2, merlin);
			bundle.putStringArray("reslist", reslist);
			frag.setArguments(bundle);
			return frag;
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ResistanceList = getArguments().getStringArray("reslist");
			merlin = getArguments().getString(GameEnd.PARAM2);
			View rootView = inflater.inflate(R.layout.fragment_kill_merlin,
					container, false);
			final ListView SelectList = (ListView) rootView.findViewById(R.id.killmerlinlist);
			final SelectAdapter sadpt = new SelectAdapter(getActivity(), R.layout.leaderselect_element, new ArrayList<String>(Arrays.asList(ResistanceList)));
			if (SelectList == null) {
				Log.w("GameEnd","null ListView");
			}
			SelectList.setAdapter(sadpt);
			SelectList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> sadpt, View v, int position,
						long arg3) {
					TextView txt = (TextView) v.findViewById(R.id.leadelmtext);
					boolean enb = !txt.isEnabled();
					txt.setEnabled(enb);
					// disable all other views
					for (int i = 0; i<sadpt.getChildCount(); i++) {
						View vv = sadpt.getChildAt(i);
						TextView txtv = (TextView) vv.findViewById(R.id.leadelmtext);
						if (txtv.isEnabled() && i != position) txtv.setEnabled(false);
					}
					//Toast.makeText(getActivity(), "Player "+txt.getText()+((enb)?" enabled":" disabled"), Toast.LENGTH_SHORT).show();				
				}
			});
			Button kill = (Button) rootView.findViewById(R.id.killmerlin);
			kill.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String merlinchoice = (String) null;
					for (int i = 0; i<SelectList.getCount(); i++) {
						View vv = ((ViewGroup) SelectList).getChildAt(i);
						TextView txtv = (TextView) vv.findViewById(R.id.leadelmtext);
						if (txtv.isEnabled()) merlinchoice = txtv.getText().toString();
					}
					if (merlinchoice != null) {
						int condtouse = (merlinchoice == merlin) ? 
								R.integer.MERLIN_ASSASSINATED : R.integer.RES_WIN;
						Fragment wwfrag = WhoWon.newInstance(condtouse, merlin);
						getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.container, wwfrag).commit();
					}
				}
			});
			return rootView;
		}
	}


	public static class WhoWon extends Fragment {
		
		public WhoWon() {
		}
		
		public static WhoWon newInstance(int condition,String merlin) {
			Bundle args = new Bundle();
			WhoWon frag = new WhoWon();
			args.putInt(GameEnd.PARAM1, condition);
			args.putString(GameEnd.PARAM2, merlin);
			frag.setArguments(args);
			return frag;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_game_end,
					container, false);
			int condition = getArguments().getInt(GameEnd.PARAM1);
			TextView header = (TextView) rootView.findViewById(R.id.whowonheader);
			TextView desc = (TextView) rootView.findViewById(R.id.whowondesc);
			ImageView icon = (ImageView) rootView.findViewById(R.id.whowonicon);
			Button playagain = (Button) rootView.findViewById(R.id.playagain);
			header.setText(((condition > 0) ? "The Resistance wins!" : "The Spies win!"));
			switch (condition) {
			case R.integer.RES_WIN:
				icon.setImageResource(R.drawable.game_end_success);
				break;
			case R.integer.SPIES_WIN:
				icon.setImageResource(R.drawable.game_end_fail_missions);
				desc.setText("The dastardly spies have foiled the efforts of the uprising! Oh, the humanity!");
				break;
			case R.integer.FAILED_ROSTERS:
				icon.setImageResource(R.drawable.game_end_fail_rosters);
				desc.setText("Idle hands are the devil's workshop.");
				break;
			case R.integer.MERLIN_ASSASSINATED:
				icon.setImageResource(R.drawable.game_end_fail_assassination);
				desc.setText("The odious spies have killed a feeble old man! One can only hope that they are proud of themselves.");
			}
			playagain.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent playAgain = new Intent((GameEnd)getActivity(),PlayerSelect.class);
					getActivity().startActivity(playAgain);
					
				}
			});
			return rootView;
		}
	}

}
