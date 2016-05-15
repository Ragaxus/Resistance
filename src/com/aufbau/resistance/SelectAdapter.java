package com.aufbau.resistance;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SelectAdapter extends ArrayAdapter<String> {
	Context context;
	int resid;
	List<String> data;
	

	public SelectAdapter(Context context,
			int textViewResourceId, List<String> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.resid = textViewResourceId;
		this.data = objects;
	}
	
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(resid, parent, false);
			TextView txt = (TextView) row.findViewById(R.id.leadelmtext);
			txt.setText(data.get(position));
			txt.setEnabled(false);
		}
		return row;
	}
}