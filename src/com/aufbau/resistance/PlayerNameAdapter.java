package com.aufbau.resistance;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class PlayerNameAdapter extends ArrayAdapter<String>{

	Context context;
	int layoutResourceId;
	ArrayList<String> data = new ArrayList<String>();

	public PlayerNameAdapter(Context context, int layoutResourceId,
			ArrayList<String> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	public void delete(int position) {
		data.remove(position);
		this.notifyDataSetChanged();
	}
	
	protected void confirmedit(int position, String obj) {
		data.set(position, obj);
		this.notifyDataSetChanged();
		
	}


	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		View row = convertView;
		PlayerHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new PlayerHolder(row);
			row.setTag(holder);
		} else {
			holder = (PlayerHolder) row.getTag();
		}
		String player = data.get(position);
		holder.textName.setText(player);
		holder.btnEdit.setOnClickListener(holder.oclEdit);
		holder.btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = ((ListView)parent).getPositionForView((View) v.getParent());		    
				PlayerNameAdapter.this.delete(position);
			}
		});
		final PlayerHolder OKholder = holder;
		holder.btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = ((ListView)parent).getPositionForView((View) v.getParent());
				PlayerNameAdapter.this.confirmedit(position,OKholder.edittext.getText().toString());
				OKholder.show();
			}
		});
		return row;


	}



	static class PlayerHolder {
		TextView textName;
		Button btnEdit;
		Button btnDelete;
		OnClickListener oclEdit;
		EditText edittext;
		Button btnOK;
		public PlayerHolder(View row) {
			this.textName = (TextView) row.findViewById(R.id.list_item);
			this.btnEdit = (Button) row.findViewById(R.id.playerselect_row_edit);
			this.btnDelete = (Button) row.findViewById(R.id.playerselect_row_delete);
			this.edittext = (EditText) row.findViewById(R.id.edittext);
			this.btnOK = (Button) row.findViewById(R.id.buttonOK);
			oclEdit = new OnClickListener() {

				@Override
				public void onClick(View v) {
					edittext.setText(textName.getText());
					PlayerHolder.this.hide();
				}
			};


		}
		public void hide() {
			textName.setVisibility(View.GONE);
			btnEdit.setVisibility(View.GONE);
			btnDelete.setVisibility(View.GONE);
			btnOK.setVisibility(View.VISIBLE);
			edittext.setVisibility(View.VISIBLE);
		}
		public void show() {
			textName.setVisibility(View.VISIBLE);
			btnEdit.setVisibility(View.VISIBLE);
			btnDelete.setVisibility(View.VISIBLE);
			btnOK.setVisibility(View.GONE);
			edittext.setVisibility(View.GONE);
		}
	}
}
