package com.bionx.res.activity;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bionx.res.R;
import com.bionx.res.about.Changelog;
import com.bionx.res.about.InformationCenter;
import com.bionx.res.about.User;
import com.bionx.res.performance.PerformanceActivity;
import com.bionx.res.ui.Tools;
import com.sbstrm.appirater.Appirater;

public class DashMainActivity extends Activity {
	
	// URL Buttons
	private Button mBtnURL1;
	private Button mBtnURL2;
	private Button mBtnURL3;
	private Button mBtnURL4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Appirater.appLaunched(this);
		
		TextView textView = (TextView) findViewById(R.id.system);
		StringBuilder string = new StringBuilder("");

		string.append("Model : " + Build.MODEL + "\n");
		string.append("Android Version : " + Build.VERSION.RELEASE + "\n");
		string.append("Rom : " + Build.DISPLAY + "\n");
		string.append("Brand : " + Build.BRAND + "\n");
		string.append("ID : " + Build.ID + "\n");
		string.append("Host : " + Build.HOST + "\n");
		string.append("Radio : " + Build.getRadioVersion() + "\n");
		string.append("CPU0 : " + Build.CPU_ABI + "\n");
		string.append("CPU1 : " + Build.CPU_ABI2 + "\n");
		string.append("Bootloader : " + Build.BOOTLOADER + "\n");

		textView.setText(string);
		
		// Web URL Buttons
		mBtnURL1 = (Button) findViewById(R.id.url1);
		mBtnURL2 = (Button) findViewById(R.id.url2);
		mBtnURL3 = (Button) findViewById(R.id.url3);
		mBtnURL4 = (Button) findViewById(R.id.url4);
		// Launch URLs via Intents
		mBtnURL1.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) {
				Intent url1 = new Intent("android.intent.action.VIEW", Uri
						.parse("http://www.xda-developers.com/"));
				startActivity(url1);
			}
		});
		mBtnURL2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent url2 = new Intent("android.intent.action.VIEW", Uri
						.parse("http://phandroid.com/"));
				startActivity(url2);
			}
		});
		mBtnURL3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent webview = new Intent(getBaseContext(),
						BionxWebs.class);
				startActivity(webview);
			}
		});
		mBtnURL4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent url4 = new Intent("android.intent.action.VIEW", Uri
						.parse("https://play.google.com/store/apps/developer?id=Biotic"));
				startActivity(url4);
			}
		});

		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		@SuppressWarnings("rawtypes")
		List rs = am.getRunningAppProcesses();
		for (int i = 0; i > rs.size(); i++) {
			@SuppressWarnings("unused")
			ActivityManager.RunningServiceInfo rsi = (RunningServiceInfo) rs
					.get(i);
			@SuppressWarnings("unused")
			ActivityManager.MemoryInfo mi = (MemoryInfo) rs.get(i);
		}
	}
	//Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.dash_menu, menu);
      return true;
    } 
    @SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case R.id.action_about:
    	  showDialog(11);
    	  break;
      case R.id.settings:
    	  @SuppressWarnings("unused")
		final Context context = this;
    	  Intent settings_menu = new Intent(Settings.ACTION_SETTINGS);
			startActivity(settings_menu);
			return true;
      case R.id.tools:
    	  final Context context0 = this;
    	  Intent options_menu = new Intent(context0, Tools.class);
			startActivity(options_menu);
			return true;
      case R.id.perform:
    	  final Context context1 = this;
    	  Intent perform = new Intent(context1, PerformanceActivity.class);
			startActivity(perform);
			return true;
      case R.id.cat:
    	  final Context context2 = this;
    	  Intent cat = new Intent(context2, Catalyst.class);
			startActivity(cat);
			return true;
      case R.id.power:
    	  final Context context3 = this;
    	  Intent power = new Intent(context3, PowerMenu.class);
			startActivity(power);
			return true;
      case R.id.user:
    	  final Context context4 = this;
    	  Intent user = new Intent(context4, User.class);
			startActivity(user);
			return true;
      case R.id.changelog:
    	  final Context context5 = this;
    	  Intent changelog = new Intent(context5, Changelog.class);
			startActivity(changelog);
			return true;
      case R.id.exit:
          Toast.makeText(getApplicationContext(),
          "Bionx FTW!",
          Toast.LENGTH_SHORT).show();
          DashMainActivity.this.finish();
      }
      return false;
    }
@SuppressWarnings("deprecation")
@Override
protected Dialog onCreateDialog(int id) {
    switch (id) {    
        case 11:
        // Create our About Dialog
        TextView aboutMsg  = new TextView(this);
        aboutMsg.setMovementMethod(LinkMovementMethod.getInstance());
        aboutMsg.setPadding(30, 30, 30, 30);
        aboutMsg.setText(Html.fromHtml("Welcome to the Bionx Dashboard! Created by Biotic to sponsor Nx Industries which host the Bionx Rom Development! For more information, continue to the Information Center; If your cool, proceed to continue your experience."));

        Builder builder = new AlertDialog.Builder(this);
            builder.setView(aboutMsg)
            .setTitle(Html.fromHtml("Dashboard <b><font color='" + getResources().getColor(R.color.holo_blue) + "'>Info</font></b>"))
            .setIcon(R.drawable.ic_launcher)
            .setCancelable(true)
            .setPositiveButton("Information Center",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                            int which) {
	                            Intent intent = new Intent(getBaseContext(), InformationCenter.class);
	                            startActivity(intent);
                        }
                    })
            .setNegativeButton("Proceed",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                int which) {
                                Toast.makeText(getApplicationContext(),
                                "Bionx Dashboard",
                                Toast.LENGTH_LONG).show();
                        }
                    });

        return builder.create();
    }

    return super.onCreateDialog(id);
}
}