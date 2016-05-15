package com.aufbau.resistance;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetme.android.horizontallistview.HorizontalListView;

public class MissionRoster extends Fragment {
	
	Mission[] gameRoster;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_mission_roster, container, false);
		Button next = (Button) v.findViewById(R.id.nextmission);
		gameRoster = ((GameLoop) getActivity()).gameRoster;
		next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			//Toast.makeText(getActivity(), "Missions considered harmful", Toast.LENGTH_SHORT).show();
			startLeaderSelect();
			}
		});
		HorizontalListView list = (HorizontalListView) v.findViewById(R.id.list);
		list.setAdapter(new MissionAdapter(getActivity(), R.layout.missionroster_element, gameRoster));
		//getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		return v;

	}
	
	public void startLeaderSelect() {
		Fragment leadsel = new LeaderSelect();
		Bundle bundle = new Bundle();
		bundle.putStringArray(LeaderSelect.param1, ((GameLoop) getActivity()).playerlist);
		bundle.putInt(LeaderSelect.param2, ((GameLoop) getActivity()).currentLeader);
		bundle.putInt(LeaderSelect.param3, gameRoster[((GameLoop) getActivity()).currentMission].numPlayers);
		leadsel.setArguments(bundle);
		Fragment contents = getFragmentManager().findFragmentByTag("MissionList");
		//if (contents == null) Log.w("Res","null contents MissionList");
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		getFragmentManager().beginTransaction().detach(contents).commit();
		getFragmentManager().beginTransaction().add(R.id.lscontainer,leadsel,"LeaderSelect").commit();
		
		
	}
	public class MissionAdapter extends ArrayAdapter<Mission> {
		Context mContext;
		int layoutResourceId;
		Mission data[] = null;

		public MissionAdapter(Context mContext, int layoutResourceId, Mission[] data) {
			super(mContext, layoutResourceId, data);
			
			this.layoutResourceId = layoutResourceId;
			this.mContext = mContext;
			this.data = data;

		}

		 @Override
		    public View getView(int position, View convertView, ViewGroup parent) {

		        /*
		         * The convertView argument is essentially a "ScrapView" as described is Lucas post 
		         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
		         * It will have a non-null value when ListView is asking you recycle the row layout. 
		         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
		         */
		        if(convertView==null){
		            // inflate the layout
		            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		            convertView = inflater.inflate(layoutResourceId, parent, false);
		        }

		        // object item based on the position
		        Mission objectItem = data[position];
		        TextView textViewItem = (TextView) convertView.findViewById(R.id.numPlayers);
		        if (objectItem.done == 0) {
		        // get the TextView and then set the text (item name) and tag (item ID) values
		        textViewItem.setText(Integer.toString(objectItem.numPlayers));
		        }
		        else {
		        	FrameLayout layout = (FrameLayout) convertView.findViewById(R.id.FrameLayout1);
		        	layout.removeAllViews();
		        	ImageView img = new ImageView(getActivity());
		        	int resource = (objectItem.done > 0) ? R.drawable.ic_launcher : R.drawable.fail_icon;
		        	img.setImageResource(resource);
		        	layout.addView(img);
		        }
		        convertView.getLayoutParams().width = parent.getWidth() / data.length;
		        convertView.getLayoutParams().height = parent.getWidth() / data.length;
		        convertView.requestLayout();
		        return convertView;

		    }

	}
}

