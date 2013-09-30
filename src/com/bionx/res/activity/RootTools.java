package com.bionx.res.activity;

import java.util.List;

import android.preference.PreferenceActivity;

import com.bionx.res.R;

public class RootTools extends PreferenceActivity {
	
    @Override
public void onBuildHeaders(List<Header> headers) {
    loadHeadersFromResource(R.xml.root_tools, headers);
    }
}