package com.example.custombroadsidemenu.fragment;

import com.example.custombroadsidemenu.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 *  ≤‚ ‘”√µƒfragment
 * @author wsd_leiguoqiang
 */
public class FragmentFirst extends Fragment{
	private View view;
	private TextView tv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_layout, null);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tv = (TextView) view.findViewById(R.id.textview);
		tv.setText("fragment_01");
	}
}
