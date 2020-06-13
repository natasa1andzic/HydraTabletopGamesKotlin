package com.natasaandzic.hydratabletopgames.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.natasaandzic.hydratabletopgames.fragments.CalendarFragment;
import com.natasaandzic.hydratabletopgames.fragments.GamesFragment;

/**
 * FragmentPagerAdapter koristimo za fragmente, da prikazemo listu naziva fragmenata,
 * Moramo da override-ujemo metode getPageTitle, getItem i getCount.
 * U konstruktoru prosledjujemo FragmentManager i numOfTabs.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	private String[] tabTitles = new String[]{"Calendar", "Games"};
	int mNumOfTabs;

	public MyFragmentPagerAdapter(FragmentManager fm, int NumOfTabs) {
		super(fm);
		this.mNumOfTabs = NumOfTabs;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return tabTitles[position];
	}

	@Override
	public Fragment getItem(int position) {

		switch (position) {
			case 0:
				CalendarFragment calendarFragment = new CalendarFragment();
				return calendarFragment;
			case 1:
				GamesFragment gamesFragment = new GamesFragment();
				return gamesFragment;
			default:
				return null;
		}
	}

	@Override
	public int getCount() {
		return mNumOfTabs;
	}
}

