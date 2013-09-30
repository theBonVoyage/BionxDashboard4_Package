package com.bionx.res.about;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.bionx.res.R;

public class InformationCenter extends FragmentActivity implements ActionBar.TabListener {

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_center);

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new Bionx();
                case 1:
                	return new About2();
                case 2:
                	return new About3();
                case 3:
                	return new Partner();
            }
			return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Slide " + (position + 1);
        }
    }

    /**
     * About Bionx Fragment
     */
    public static class Bionx extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.about_info, container, false);
            
    Button myButtonOne = (Button) rootView.findViewById(R.id.button);
    myButtonOne.setOnClickListener(
        new OnClickListener() {
            public void onClick(View view) {
                 handleButtonClick(view.getId());
            }
        });
    Button myButtonTwo = (Button) rootView.findViewById(R.id.button1);
    myButtonTwo.setOnClickListener(
        new OnClickListener() {
            public void onClick(View view) {
                 handleButtonClick(view.getId());
            }
        });
    Button myButtonThree = (Button) rootView.findViewById(R.id.button2);
    myButtonThree.setOnClickListener(
        new OnClickListener() {
            public void onClick(View view) {
                 handleButtonClick(view.getId());
            }
        });
    Button myButtonFour = (Button) rootView.findViewById(R.id.button3);
    myButtonFour.setOnClickListener(
        new OnClickListener() {
            public void onClick(View view) {
                 handleButtonClick(view.getId());
            }
        });
	return rootView;
    }

    private void handleButtonClick(final int resourceId) {
        switch( resourceId ) {
            case R.id.button:
            	Intent button = new Intent("android.intent.action.VIEW", Uri
    					.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=K8A7YGG74XRCA"));
    			startActivity(button);
                //Paypal
                break;

            case R.id.button1:
            	Intent button1 = new Intent("android.intent.action.VIEW", Uri
    					.parse("https://www.facebook.com/pages/Bionx/133692896729322"));
    			startActivity(button1);
                //Facebook
                break;

            case R.id.button2:
        			Intent button2 = new Intent("android.intent.action.VIEW", Uri
        					.parse("https://plus.google.com/u/0/100201209156317802250"));
        			startActivity(button2);
                //Google
                break;

            case R.id.button3:
            	Intent button3 = new Intent("android.intent.action.VIEW", Uri
    					.parse("https://twitter.com/Rgocal"));
    			startActivity(button3);
                //Twitter
                break;

            default:
                break;

        }
        
    }
    }
    /**
     * About JBX Fragment
     */
    public static class About2 extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.about_jbx, container, false);
            return rootView;
        }
    }
    
    /**
     * About License Fragment
     */
    public static class About3 extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.about_source, container, false);
            return rootView;
        }
    }
    /**
    *Partners Fragment
    */
    public static class Partner extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.about_omap4, container, false);
            return rootView;
        }
    }
}