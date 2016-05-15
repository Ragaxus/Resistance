package com.aufbau.resistance;



import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
//When requested, this adapter returns a RoleCard,
// representing an object in the collection.
//Since this is an object collection, use a FragmentStatePagerAdapter,
//and NOT a FragmentPagerAdapter.
public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
	static {
		System.loadLibrary("ndk1");
	}
	private native int[] genAllgs(int size, boolean[] avalon);
	
	int[] RoleList;
	String[] PlayerList;
	SparseArray<Fragment> map = new SparseArray<Fragment>();
	
 public CollectionPagerAdapter(FragmentManager fm, String [] playerList, SharedPreferences sharedPref) {
     super(fm);
     boolean merlin = sharedPref.getBoolean("merlin", false);
     boolean percmorg = sharedPref.getBoolean("percmorg", false);
     boolean[] avalon = new boolean[2];
     avalon[0] = merlin;
     avalon[1] = percmorg;
     RoleList = genAllgs(playerList.length,avalon);
     PlayerList = playerList;
     
 }

 @Override
 public Fragment getItem(int i) {
	 if (i == 2*RoleList.length) {
		 return FinalCard.newInstance(PlayerList,RoleList);
	 }
	 if (i % 2 == 1) {
		 
		 Fragment card = RoleCard.newInstance((int)(.5*(i-1)), RoleList, PlayerList);
		 map.put(i, card);
		 return card;
	 }
	 else {
		 return DividerCard.newInstance(PlayerList[(int) (.5*i)]);
	 }
 }

 @Override
 public int getCount() {
     return 2*RoleList.length+1;
 }
 


 @Override
 public Object instantiateItem(ViewGroup container, int position) {
     Fragment fragment = (Fragment) super.instantiateItem(container,
             position);
     map.put(position, fragment);
     return fragment;
 }
 @Override
 public void destroyItem(ViewGroup container, int position, Object object) {
     map.remove(position);
     super.destroyItem(container, position, object);
 }

 public Fragment getRegisteredFragment(int position) {
     return map.get(position);
 }
}