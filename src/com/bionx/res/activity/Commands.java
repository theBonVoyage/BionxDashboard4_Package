package com.bionx.res.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StatFs;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.text.format.Formatter;

import com.bionx.res.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class Commands extends PreferenceActivity {
	// Apps2SD & Dalvik-Cache options
	private static final String APPS2SD_PREF = "apps2sd_opt";
	@SuppressWarnings("unused")
	private CheckBoxPreference mApps2SDPref;
	private static final String DC_CACHE_PREF = "dccache_opt";
	@SuppressWarnings("unused")
	private CheckBoxPreference mDCCachePref;
	private static final String DC_SDCARD_PREF = "dcsdcard_opt";
	@SuppressWarnings("unused")
	private CheckBoxPreference mDCSDCardPref;

	// Memory/SWAP Options
	private static final String COMPCACHE_PREF = "compcache_opt";
	@SuppressWarnings("unused")
	private CheckBoxPreference mCompcachePref;

	private static final String LINUXSWAP_PREF = "linuxswap_opt";
	private CheckBoxPreference mLinuxSWAPPref;

	@SuppressWarnings("unused")
	private static final String SWAPPINESS_PREF = "swappiness_opt";
	@SuppressWarnings("unused")
	private ListPreference mSwappinessPref;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

		// Listeners
		addPreferencesFromResource(R.xml.commands);
		
		SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.slide_menu);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
		
		PreferenceScreen prefSet = getPreferenceScreen();

		// Setup DataApp/Dalvik-Cache Options
		mApps2SDPref = (CheckBoxPreference) prefSet
				.findPreference(APPS2SD_PREF);
		mDCCachePref = (CheckBoxPreference) prefSet
				.findPreference(DC_CACHE_PREF);
		mDCSDCardPref = (CheckBoxPreference) prefSet
				.findPreference(DC_SDCARD_PREF);

		// Memory/SWAP Options
		mCompcachePref = (CheckBoxPreference) prefSet
				.findPreference(COMPCACHE_PREF);
		mLinuxSWAPPref = (CheckBoxPreference) prefSet
				.findPreference(LINUXSWAP_PREF);
		{
			mLinuxSWAPPref.setEnabled(false);
			mLinuxSWAPPref.setSummaryOff("no linuxswap partition");
		}
	}

	// Obtain FileSystem Partition Sizes
	@SuppressWarnings("unused")
	private String ObtainFSPartSize(String PartitionPath) {
		String retstr;
		File extraPath = new File(PartitionPath);
		StatFs extraStat = new StatFs(extraPath.getPath());
		@SuppressWarnings("deprecation")
		long eBlockSize = extraStat.getBlockSize();
		@SuppressWarnings("deprecation")
		long eTotalBlocks = extraStat.getBlockCount();
		@SuppressWarnings("deprecation")
		long eAvailableBlocks = extraStat.getAvailableBlocks();

		retstr = formatSize(eAvailableBlocks * eBlockSize);
		retstr += " / ";
		retstr += formatSize(eTotalBlocks * eBlockSize);

		return retstr;
	}

	private String formatSize(long size) {
		return Formatter.formatFileSize(this, size);
	}

	@SuppressWarnings("unused")
	private class SuServer extends AsyncTask<String, String, Void> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(Commands.this, "Working",
					"Running command ...", true, false);
		}

		@Override
		protected void onProgressUpdate(String... values) {
		}

		@Override
		protected void onPostExecute(Void result) {
			pd.dismiss();
		}

		@Override
		protected Void doInBackground(String... args) {
			final Process p;

			try {
				p = Runtime.getRuntime().exec("su -c sh");
				BufferedReader stdInput = new BufferedReader(
						new InputStreamReader(p.getInputStream()));
				BufferedReader stdError = new BufferedReader(
						new InputStreamReader(p.getErrorStream()));
				BufferedWriter stdOutput = new BufferedWriter(
						new OutputStreamWriter(p.getOutputStream()));

				stdOutput.write(args[0] + " && exit\n");
				stdOutput.flush();

				Thread t = new Thread() {
					@Override
					public void run() {
						try {
							p.waitFor();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				t.start();

				while (t.isAlive()) {
					String status = stdInput.readLine();
					if (status != null)
						publishProgress(status);
					Thread.sleep(20);
				}

				stdInput.close();
				stdError.close();
				stdOutput.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}