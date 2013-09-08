package com.asianux.http;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Application;

public class MyApplication extends Application{

	 private HttpClient httpClient =null;  
	 
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		httpClient = this.createHttpClient();  
		
		System.out.println("Myapplication =========================");
	}
	
	private HttpClient createHttpClient() {
		// TODO Auto-generated method stub
        HttpParams params = new BasicHttpParams();  
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);  
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);  
        HttpProtocolParams.setUseExpectContinue(params, true);  
          
        SchemeRegistry schReg = new SchemeRegistry();  
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));  
          
        ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params, schReg);  
          
        return new DefaultHttpClient(connMgr, params); 
		//return null;
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		shutdownHttpClient();
	}
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		shutdownHttpClient();
	}
    private void shutdownHttpClient() {  
        if (httpClient != null && httpClient.getConnectionManager() != null) {  
            httpClient.getConnectionManager().shutdown();  
        }  
    } 
	
	public HttpClient getHttpClient()
	{
		if(httpClient !=null)
		{
			System.out.println("Myapplication ====getHttpClient");
			return httpClient;
		}
		
		return null;
	}
}
