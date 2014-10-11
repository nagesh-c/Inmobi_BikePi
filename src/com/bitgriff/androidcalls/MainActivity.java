package com.bitgriff.androidcalls;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Main activity, with button to toggle phone calls detection on and off.
 *
 */
public class MainActivity extends Activity {

	private boolean detectEnabled;
	
	private TextView textViewDetectState;
	private Button buttonToggleDetect;
	private Button buttonExit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textViewDetectState = (TextView) findViewById(R.id.textViewDetectState);
        
        buttonToggleDetect = (Button) findViewById(R.id.buttonDetectToggle);
        buttonToggleDetect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setDetectEnabledCall(!detectEnabled);
			}
		});
        
        buttonExit = (Button) findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setDetectEnabledCall(false);
				MainActivity.this.finish();
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void setDetectEnabledCall(boolean enable) {
    	detectEnabled = enable;
    	
        Intent intent = new Intent(this, CallDetectService.class);
        Intent intent_msg = new Intent(this, SmsReceiver.class);
        Intent intent_noti = new Intent(this,NotificationService.class);
    	if (enable) {
    		 // start detect service 
            startService(intent);
            //startService(intent_msg);
            try{
            startService(intent_noti);
            }
            catch(Exception e){
            	Log.d("error_inmobi",e.toString());
            	Toast.makeText(getApplicationContext(), e.toString(), 3000).show();
            }
            buttonToggleDetect.setText("Turn off");
    		textViewDetectState.setText("Detecting");
    		//finish();
    	}
    	else {
    		// stop detect service
    		stopService(intent);
    		
    		buttonToggleDetect.setText("Turn on");
    		textViewDetectState.setText("Not detecting");
    	}
    }
}
