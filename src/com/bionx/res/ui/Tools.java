package com.bionx.res.ui;

import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.bionx.res.R;
import com.bionx.res.activity.RomTools;
import com.bionx.res.activity.RootTools;

// Bionx Tools Tabs
@SuppressWarnings("deprecation")
public class Tools extends TabActivity {

	public static final String tab1 = "Tools";
	public static final String tab2 = "Root Tools";

	// Generate Tabs
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab);
		TabHost th = (TabHost) findViewById(android.R.id.tabhost);
		// Lets ID the tabs
		TabSpec ts1 = th.newTabSpec("tab_id_1");
		TabSpec ts2 = th.newTabSpec("tab_id_2");
		// Launch Activities on each tab selected
		ts1.setIndicator(tab1).setContent(new Intent(this, RomTools.class));
		ts2.setIndicator(tab2).setContent(new Intent(this, RootTools.class));
		// Identifiers
		th.addTab(ts1);
		th.addTab(ts2);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}
}