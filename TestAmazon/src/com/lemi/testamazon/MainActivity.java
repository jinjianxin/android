/**
 * Copyright 2013-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at http://aws.amazon.com/apache2.0/
 * or in the "license" file accompanying this file.
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lemi.testamazon;

import com.amazon.device.ads.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


public class MainActivity extends Activity {
    private AdLayout adView; 
    private static final String APP_KEY = "sample-app-v1_pub-2";
    private static final String LOG_TAG = "lemi"; 

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdRegistration.enableLogging(true);      
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main, null);
        
        this.adView = (AdLayout) view.findViewById(R.id.ad_view);
        this.adView.setListener(new SampleAdListener());
        
        addContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        
        try {
            AdRegistration.setAppKey(APP_KEY);
        } catch (final Exception e) {
            Log.e(LOG_TAG, "Exception thrown: " + e.toString());
            return;
        }

        loadAd();
        
    }
    

    public void loadAd() {
        this.adView.loadAd();
    }
    
    class SampleAdListener extends DefaultAdListener
    {

        @Override
        public void onAdLoaded(final Ad ad, final AdProperties adProperties) {
            Log.i(LOG_TAG, adProperties.getAdType().toString() + " ad loaded successfully.");
        }

        @Override
        public void onAdFailedToLoad(final Ad ad, final AdError error) {
            Log.w(LOG_TAG, "Ad failed to load. Code: " + error.getCode() + ", Message: " + error.getMessage());
        }
    
        @Override
        public void onAdExpanded(final Ad ad) {
            Log.i(LOG_TAG, "Ad expanded.");
           
        }
        
        @Override
        public void onAdCollapsed(final Ad ad) {
            Log.i(LOG_TAG, "Ad collapsed.");
           
        }
    }
}
