package com.aufbau.resistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the {@link FinalCard#newInstance} factory method to
 * create an instance of this fragment.
 * 
 */
public class FinalCard extends ListFragment {
	
	public static final String ARG_PARAM1 = "PlayerList";
	public static final String ARG_PARAM2 = "RoleList";

	private String[] PlayerList;
	private int[] rolelist;
	


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
	public static FinalCard newInstance(String[] PlayerList,int[] rolelist) {
		FinalCard fragment = new FinalCard();
		Bundle args = new Bundle();
		args.putStringArray(ARG_PARAM1, PlayerList);
		args.putIntArray(ARG_PARAM2, rolelist);
		fragment.setArguments(args);
		return fragment;
	}

	public FinalCard() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			PlayerList = getArguments().getStringArray(ARG_PARAM1);
			rolelist = getArguments().getIntArray(ARG_PARAM2);
			
		}

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_final_card, container, false);
		ArrayAdapter<String> x = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		x.addAll(PlayerList);
		setListAdapter(x);
		((RoleCardViewer) getActivity()).vp.setDiode(false);
		Button start = (Button) v.findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),GameLoop.class);
				i.putExtra(GameLoop.PARAM2, PlayerList);
				i.putExtra(GameLoop.PARAM3, rolelist);
				getActivity().startActivity(i);
			}
		});

		return v;
	}
	
	@Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        // When list item is clicked, display corresponding card
		//Toast.makeText(getActivity(), PlayerList[position], Toast.LENGTH_SHORT).show();
		((RoleCardViewer) getActivity()).setPagerItem(2*position+1);
    }

}
