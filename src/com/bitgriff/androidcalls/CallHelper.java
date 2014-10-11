package com.bitgriff.androidcalls;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Helper class to detect incoming and outgoing calls.
 *
 */
public class CallHelper {

	/**
	 * Listener to detect incoming calls. 
	 */
	private class CallStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			String url = "http://192.168.2.3:3000/test";
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// called when someone is ringing to this phone
				
				Toast.makeText(ctx, 
						"Incoming: "+incomingNumber, 
						Toast.LENGTH_LONG).show();
				String contact_name = null;
				try{
				contact_name = getContactName(incomingNumber);
				}
				catch(Exception e){
					Log.d("error",e.toString());
				}
				if(contact_name == null){
					Toast.makeText(ctx, 
							"couldn't get contact name", 
							Toast.LENGTH_LONG).show();
					new RequestTask().execute(url,incomingNumber);
				}
				else{
					Toast.makeText(ctx, 
							"contact name:" + contact_name, 
							Toast.LENGTH_LONG).show();
					new RequestTask().execute(url,contact_name);
				}
				
				break;
			}
		}
	}
	
	/**
	 * Broadcast receiver to detect the outgoing calls.
	 */
	/*public class OutgoingReceiver extends BroadcastReceiver {
	    public OutgoingReceiver() {
	    }

	    @Override
	    public void onReceive(Context context, Intent intent) {
	        String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
	        
	        Toast.makeText(ctx, 
	        		"Outgoing: "+number, 
	        		Toast.LENGTH_LONG).show();
	    }
  
	}
*/
	private Context ctx;
	private TelephonyManager tm;
	private CallStateListener callStateListener;
	
	//private OutgoingReceiver outgoingReceiver;

	public CallHelper(Context ctx) {
		this.ctx = ctx;
		
		callStateListener = new CallStateListener();
		//outgoingReceiver = new OutgoingReceiver();
	}
	
	/**
	 * Start calls detection.
	 */
	public void start() {
		tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		
		//IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		//ctx.registerReceiver(outgoingReceiver, intentFilter);
	}
	
	/**
	 * Stop calls detection.
	 */
	public void stop() {
		tm.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
		//ctx.unregisterReceiver(outgoingReceiver);
	}
	
	public String getContactName(String phoneNumber){
		String contactName = null;
		ContentResolver localContentResolver = this.ctx.getContentResolver();
		Cursor contactLookupCursor =  
		   localContentResolver.query(
		            Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, 
		            Uri.encode(phoneNumber)), 
		            new String[] {PhoneLookup.DISPLAY_NAME, PhoneLookup._ID}, 
		            null, 
		            null, 
		            null);
		try {
		while(contactLookupCursor.moveToNext()){
		    contactName = contactLookupCursor.getString(contactLookupCursor.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME));
		    String contactId = contactLookupCursor.getString(contactLookupCursor.getColumnIndexOrThrow(PhoneLookup._ID));
		    Log.d("name", "contactMatch name: " + contactName);
		    Log.d("id", "contactMatch id: " + contactId);
		    return contactName;
		    }
		} finally {
		contactLookupCursor.close();
		}
		return contactName;
	}

}
