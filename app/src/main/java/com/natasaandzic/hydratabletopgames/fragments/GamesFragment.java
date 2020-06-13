package com.natasaandzic.hydratabletopgames.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.natasaandzic.hydratabletopgames.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GamesFragment extends ListFragment {

	String games[] = { "prva", "druga", "treca"};


	public GamesFragment(){

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_games, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.layout_row_view_games, R.id.gameNameTv, games);
		setListAdapter(adapter);
	}

}
