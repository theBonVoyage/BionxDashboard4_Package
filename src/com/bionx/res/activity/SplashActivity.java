package com.bionx.res.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bionx.res.R;

public class SplashActivity extends Activity {	
	
	private Thread thread;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

	    // Splash screen view
    	setContentView(R.layout.splash);
    	
        // Start animating the image
	    final ImageView splashImageView = (ImageView) findViewById(R.id.SplashImageView);
	    splashImageView.setBackgroundResource(R.drawable.flag);
	    final AnimationDrawable frameAnimation = (AnimationDrawable)splashImageView.getBackground();
	    splashImageView.post(new Runnable(){
			@Override
			public void run() {
				frameAnimation.start();				
			}	    	
	    });
	        	
    	
    	final SplashActivity sPlashScreen = this;   
    	
    	// The thread to wait for splash screen events
    	thread =  new Thread(){
    		@Override
    		public void run(){
    			try {
    				synchronized(this){
    					// Wait given period of time or exit on touch
    					wait(5000);
    				}
    			} 
    			catch(InterruptedException ex){    				
    			}

    			finish();
    			
    			// Run next activity
    			Intent intent = new Intent();
    			intent.setClass(sPlashScreen, DashMainActivity.class);
    			startActivity(intent);    				
    		}
    	};
    	
    	thread.start();
    	
	}
	  
    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
    	if(evt.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		synchronized(thread){
    			thread.notifyAll();
    		}
    	}
    	return true;
    }
	

}
