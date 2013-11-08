package com.bionx.res.performance;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import com.bionx.res.R;
import com.bionx.res.activity.ShellInterface;
import com.bionx.res.fragments.Advanced;
import com.bionx.res.fragments.BatteryInfo;
import com.bionx.res.fragments.CPUInfo;
import com.bionx.res.fragments.CPUSettings;
import com.bionx.res.fragments.DiskInfo;
import com.bionx.res.fragments.OOMSettings;
import com.bionx.res.fragments.TimeInState;
import com.bionx.res.fragments.VoltageControlSettings;
import com.bionx.res.util.Constants;
import com.bionx.res.util.Helpers;

public class PerformanceActivity extends Activity implements Constants {

    SharedPreferences mPreferences;
    PagerTabStrip mPagerTabStrip;
    ViewPager mViewPager;

    private static boolean mVoltageExists;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!ShellInterface.isRooted()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Root Error");
			builder.setMessage("SU Permissions were not found in your system and are required to continue...Are you rooted?");
			builder.setCancelable(false);
			builder.setPositiveButton("Exit",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			builder.create().show();
		} else if (!ShellInterface.hasSysfs()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Fatal Error");
			builder.setMessage("CPU Overclock directory was not found...Cannot continue");
			builder.setCancelable(false);
			builder.setNegativeButton("Exit",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			builder.create().show();
		} else {
        
        setContentView(R.layout.nx_performance);
        
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mVoltageExists = Helpers.voltageFileExists();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        TitleAdapter titleAdapter = new TitleAdapter(getFragmentManager());
        mViewPager.setAdapter(titleAdapter);
        mViewPager.setCurrentItem(0);

        mPagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
        mPagerTabStrip.setBackgroundColor(getResources().getColor(R.color.transparent));
        mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.holo_blue));
        mPagerTabStrip.setDrawFullUnderline(true);

	    Helpers.shCreate();
    }
    }

    class TitleAdapter extends FragmentPagerAdapter {
        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];

        public TitleAdapter(FragmentManager fm) {
            super(fm);
            if (mVoltageExists) {
            	if(Helpers.showBattery()){
	                frags[0] = new CPUSettings();
		            frags[1] = new BatteryInfo();
		            frags[2] = new OOMSettings();
	                frags[3] = new VoltageControlSettings();
	                frags[4] = new Advanced();
	                frags[5] = new TimeInState();
	                frags[6] = new CPUInfo();
                    frags[7] = new DiskInfo();
            	}
            	else{
			        frags[0] = new CPUSettings();
	        	    frags[1] = new OOMSettings();
                	frags[2] = new VoltageControlSettings();
                	frags[3] = new Advanced();
                	frags[4] = new TimeInState();
                	frags[5] = new CPUInfo();
                    frags[6] = new DiskInfo();
            	}
            } 
            else {
                if(Helpers.showBattery()){
                    frags[0] = new CPUSettings();
                    frags[1] = new BatteryInfo();
                    frags[2] = new OOMSettings();
                    frags[3] = new Advanced();
                    frags[4] = new TimeInState();
                    frags[5] = new CPUInfo();
                    frags[6] = new DiskInfo();
                }
                else{
                    frags[0] = new CPUSettings();
                    frags[1] = new OOMSettings();
                    frags[2] = new Advanced();
                    frags[3] = new TimeInState();
                    frags[4] = new CPUInfo();
                    frags[5] = new DiskInfo();
                }
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }

    /**
     * Get a list of titles for the tabstrip to display depending on if the
     * voltage control fragment and battery fragment will be displayed. (Depends on the result of
     * Helpers.voltageTableExists() & Helpers.showBattery()
     *
     * @return String[] containing titles
     */
    private String[] getTitles() {
        String titleString[];
        if (mVoltageExists) {
        	if(Helpers.showBattery()){
                titleString = new String[]{
                        getString(R.string.t_cpu_settings),
                        getString(R.string.t_battery_info),
                        getString(R.string.t_oom_settings),
                        getString(R.string.t_volt_settings),
                        getString(R.string.t_adv_settings),
                        getString(R.string.t_time_in_state),
                        getString(R.string.t_cpu_info),
                        getString(R.string.t_disk_info)};
                }
                else{
                    titleString = new String[]{
                            getString(R.string.t_cpu_settings),
                            getString(R.string.t_oom_settings),
                            getString(R.string.t_volt_settings),
                            getString(R.string.t_adv_settings),
                            getString(R.string.t_time_in_state),
                            getString(R.string.t_cpu_info),
                            getString(R.string.t_disk_info)};
                }
        } 
        else {
        	if(Helpers.showBattery()){
                titleString = new String[]{
                        getString(R.string.t_cpu_settings),
                        getString(R.string.t_battery_info),
                        getString(R.string.t_oom_settings),
                        getString(R.string.t_adv_settings),
                        getString(R.string.t_time_in_state),
                        getString(R.string.t_cpu_info),
                        getString(R.string.t_disk_info)};
            }
        	else{
                titleString = new String[]{
                        getString(R.string.t_cpu_settings),
                        getString(R.string.t_oom_settings),
                        getString(R.string.t_adv_settings),
                        getString(R.string.t_time_in_state),
                        getString(R.string.t_cpu_info),
                        getString(R.string.t_disk_info)};
            }
        }
        return titleString;
    }
    }
}
    