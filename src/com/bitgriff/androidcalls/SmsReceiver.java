package com.bitgriff.androidcalls;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent) 
       {	
		Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String str = "";            
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            String phno = null;
            String msgBody = null;
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
                str += "SMS from " + msgs[i].getOriginatingAddress();
                phno = msgs[i].getOriginatingAddress();
                str += " :";
                str += msgs[i].getMessageBody().toString();
                msgBody = msgs[i].getMessageBody().toString();
                str += "\n";        
            }
            //---display the new SMS message---
            //Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            String name = null;
            if(phno != null){
            	name = getContactName(phno,context);
            }
            if(name == null)
            {
            	name = "Unknown";
            }
            String url = "http://192.168.2.3:3000/msg";
            new RequestTask().execute(url,name,msgBody,"msg",phno);
        }
    }
	public String getContactName(String phoneNumber,Context ctx){
		String contactName = null;
		ContentResolver localContentResolver = ctx.getContentResolver();
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