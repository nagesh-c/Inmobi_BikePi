package com.bitgriff.androidcalls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class RequestTask extends AsyncTask<String, String, String>{

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
        	if(!uri[0].endsWith("?")){
                uri[0] += "?";
        	}
        	//add parameters to link
        	List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        	params.add(new BasicNameValuePair("contact",uri[1]));
        	URI main_uri = null;
			try {
				main_uri = new URI(uri[0] + URLEncodedUtils.format( params, "utf-8" ));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				Log.d("error",e.toString());
			}
        	response = httpclient.execute(new HttpGet(main_uri));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                Log.d("response", responseString);
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        	Log.d("error",e.toString());
        } catch (IOException e) {
            //TODO Handle problems..
        	Log.d("error",e.toString());
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..
    }
}
