package com.aufbau.resistance;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RunMission extends Fragment {
	RunMissionAdapter mColl;
	public final static String PARAM1 = "Playerlist";
	public final static String PARAM2 = "Leader";
	String[] playerList;
	String leader;
	DiodePager vp;

	public static RunMission newInstance(String[] playerlist, String leader) {
		RunMission fragment = new RunMission();
		Bundle args = new Bundle();
		args.putStringArray(PARAM1, playerlist);
		args.putString(PARAM2, leader);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.activity_run_mission, container, false);	
		vp = (DiodePager) v.findViewById(R.id.runmispager);
		vp.setOffscreenPageLimit(0);
		playerList = getArguments().getStringArray(RunMission.PARAM1);
		leader = getArguments().getString(RunMission.PARAM2);
		mColl = new RunMissionAdapter(getChildFragmentManager(), playerList, leader);
		vp.setAdapter(mColl);
		vp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected (int position) {
				if (position % 2 == 1) {
					vp.setHalt(true);
				}
			}
		});
		return v;
	}

	public static class PassFail extends Fragment {
		public static final String ARG_PARAM1 = "Player";
		String player;
		int color = 0;
		public static PassFail newInstance(String player) {
			PassFail fragment = new PassFail();
			Bundle args = new Bundle();
			args.putString(ARG_PARAM1, player);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if (getArguments() != null) {
				player = getArguments().getString(ARG_PARAM1);
			}


		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Inflate the layout for this fragment
			final View v = inflater.inflate(R.layout.fragment_passfail, container, false);
			final ImageView bluered = (ImageView) v.findViewById(R.id.bluered);
			if (Math.random() < .5) {
				bluered.setImageResource(R.drawable.redblue);
				((TextView) v.findViewById(R.id.textTopLeft)).setText("Fail");
				((TextView) v.findViewById(R.id.textBottomRight)).setText("Pass");
			}
			final RunMission parent = (RunMission) getParentFragment();
			final Button confirm = (Button) v.findViewById(R.id.confirm);
			confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (color != 0) {
						parent.vp.setHalt(false);
						parent.vp.setCurrentItem(parent.vp.getCurrentItem() + 1);
					}
					else {
						Toast.makeText(getActivity(), "You must choose Pass or Fail before continuing!", Toast.LENGTH_SHORT).show();
					}
				}
			});
			bluered.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int x = (int) event.getX();
					int y = (int) event.getY();

					Bitmap bitmap = ((BitmapDrawable)bluered.getDrawable()).getBitmap();
					color = bitmap.getPixel(x,y);
					if (Color.red(color) > Color.blue(color)) {}
					confirm.setTextColor(color);
					return false;
				}
			});
			return v;
		}
	}

	public class RunMissionAdapter extends FragmentStatePagerAdapter {

		String[] PlayerList;
		String Leader;
		SparseArray<PassFail> map = new SparseArray<PassFail>();

		public RunMissionAdapter(FragmentManager fm, String [] playerList, String leader) {
			super(fm);
			PlayerList = playerList;
			Leader = leader;

		}

		@Override
		public Fragment getItem(int i) {
			if (i == this.getCount()-1) {
				return ResultsCard.newInstance(((GameLoop)getActivity()).currentMission); //TODO: Make ResultsCard

			}
			if (i % 2 == 1) {
				int pos  =(int)(.5*(i-1));
				Fragment card = PassFail.newInstance(PlayerList[pos]);
				map.put(i, (PassFail) card);
				return card;
			}
			else {
				int pos = (int) (.5*i);
				if (pos < PlayerList.length){return DividerCard.newInstance(PlayerList[pos]);}
				else {return DividerCard.newInstance(Leader);}
			}
		}

		@Override
		public int getCount() {
			return 2*PlayerList.length+2;
		}



		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment fragment = (Fragment) super.instantiateItem(container,
					position);
			return fragment;
		}



		public Fragment getRegisteredFragment(int position) {
			return map.get(position);
		}
	}
}
