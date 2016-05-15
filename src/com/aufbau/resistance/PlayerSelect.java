package com.aufbau.resistance;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class PlayerSelect extends ActionBarActivity {
	ArrayList<String> players = new ArrayList<String>();
	
	EditText newplayer;
	PlayerNameAdapter playersAdapter;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_select);

		Button buttonStart = (Button)findViewById(R.id.buttonStart);        
	    buttonStart.setOnClickListener(startListener);
	    Button buttonAdd = (Button)findViewById(R.id.buttonAdd);        
	    buttonAdd.setOnClickListener(addListener);
	    ListView playerlist = (ListView)findViewById(R.id.PlayerSelectList);
	    playersAdapter = new PlayerNameAdapter(this, R.layout.playerselect_row, players);
	    playerlist.setAdapter(playersAdapter);
	    newplayer = (EditText)findViewById(R.id.editPlayerName);
	    players.add("A");
	    players.add("B");
	    players.add("C");
	    players.add("D");
	    players.add("E");
	}
	//Create anonymous implementations of OnClickListener
    private OnClickListener startListener = new OnClickListener() {
        public void onClick(View v) {
        	if (players.size() >= 5) {
        	String[] playerarray = players.toArray(new String[players.size()]);
        	intent = new Intent(PlayerSelect.this, RoleCardViewer.class);
        	intent.putExtra("PLAYERLIST", playerarray);
        	PlayerSelect.this.startActivity(intent);
        }
        	else {
				Toast.makeText(PlayerSelect.this, "Resistance requires at least 5 players!",
						Toast.LENGTH_LONG).show();
        	}
        }
    };
    private OnClickListener addListener = new OnClickListener() {
        public void onClick(View v) {
        	players.add(newplayer.getText().toString());
        	playersAdapter.notifyDataSetChanged();
        	newplayer.setText("");
        	
        }
    };
		
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player_select, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			//Start Settings activity.
			Intent iset = new Intent(PlayerSelect.this,SettingsActivity.class);
			startActivity(iset);
		}
		return super.onOptionsItemSelected(item);
	}

}
