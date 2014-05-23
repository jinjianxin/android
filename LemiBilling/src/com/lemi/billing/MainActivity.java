package com.lemi.billing;

import java.util.HashSet;
import java.util.Set;

import com.lemi.dungeons.BillingService;
import com.lemi.dungeons.Consts;
import com.lemi.dungeons.PurchaseDatabase;
import com.lemi.dungeons.PurchaseObserver;
import com.lemi.dungeons.ResponseHandler;
import com.lemi.dungeons.BillingService.RequestPurchase;
import com.lemi.dungeons.BillingService.RestoreTransactions;
import com.lemi.dungeons.Consts.PurchaseState;
import com.lemi.dungeons.Consts.ResponseCode;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private String TAG = "lemi";
	private Handler mHandler = null;
	private GooglePurchaseObserver mGooglePurchaseObserver;
	private BillingService mBillingService;
	private PurchaseDatabase mPurchaseDatabase;
	
	private Button buyButton = null;
	
	private class GooglePurchaseObserver extends PurchaseObserver
	{

		public GooglePurchaseObserver( Handler handler) {
			super(MainActivity.this, handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onBillingSupported(boolean supported, String type) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onBillingSupported");
			Log.d(TAG, "type = "+type);
			
			if (type == null || type.equals(Consts.ITEM_TYPE_INAPP)) {
                if (supported) {
                	buyButton.setEnabled(true);
                } else {
                   /* showDialog(DIALOG_BILLING_NOT_SUPPORTED_ID);*/
                }
            } else if (type.equals(Consts.ITEM_TYPE_SUBSCRIPTION)) {
               /* mCatalogAdapter.setSubscriptionsSupported(supported);*/
            } else {
                /*showDialog(DIALOG_SUBSCRIPTIONS_NOT_SUPPORTED_ID);*/
            }
		}

		@Override
		public void onPurchaseStateChange(PurchaseState purchaseState,
				String itemId, int quantity, long purchaseTime,
				String developerPayload) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onBillingSupported");
			
			if (Consts.DEBUG) {
                Log.i(TAG, "onPurchaseStateChange() itemId: " + itemId + " " + purchaseState);
            }

/*            if (developerPayload == null) {
                logProductActivity(itemId, purchaseState.toString());
            } else {
                logProductActivity(itemId, purchaseState + "\n\t" + developerPayload);
            }
            
            if (purchaseState == PurchaseState.PURCHASED) {
                mOwnedItems.add(itemId);
                
                // If this is a subscription, then enable the "Edit
                // Subscriptions" button.
                for (CatalogEntry e : CATALOG) {
                    if (e.sku.equals(itemId) &&
                            e.managed.equals(Managed.SUBSCRIPTION)) {
                        mEditSubscriptionsButton.setVisibility(View.VISIBLE);
                    }
                }
            }*/
			
		}

		@Override
		public void onRequestPurchaseResponse(RequestPurchase request,
				ResponseCode responseCode) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onBillingSupported");
			
			if (Consts.DEBUG) {
                Log.d(TAG, request.mProductId + ": " + responseCode);
            }
            if (responseCode == ResponseCode.RESULT_OK) {
                if (Consts.DEBUG) {
                    Log.i(TAG, "purchase was successfully sent to server");
                }
                //logProductActivity(request.mProductId, "sending purchase request");
            } else if (responseCode == ResponseCode.RESULT_USER_CANCELED) {
                if (Consts.DEBUG) {
                    Log.i(TAG, "user canceled purchase");
                }
                //logProductActivity(request.mProductId, "dismissed purchase dialog");
            } else {
                if (Consts.DEBUG) {
                    Log.i(TAG, "purchase failed");
                }
                //logProductActivity(request.mProductId, "request purchase returned " + responseCode);
            }
		}

		@Override
		public void onRestoreTransactionsResponse(RestoreTransactions request,
				ResponseCode responseCode) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onBillingSupported");
			
			if (responseCode == ResponseCode.RESULT_OK) {
                if (Consts.DEBUG) {
                    Log.d(TAG, "completed RestoreTransactions request");
                }
            } else {
                if (Consts.DEBUG) {
                    Log.d(TAG, "RestoreTransactions error: " + responseCode);
                }
            }
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        mHandler = new Handler();
        mGooglePurchaseObserver = new GooglePurchaseObserver(mHandler);
        mBillingService = new BillingService();
        mBillingService.setContext(this);

        mPurchaseDatabase = new PurchaseDatabase(this);
  

        // Check if billing is supported.
        ResponseHandler.register(mGooglePurchaseObserver);
        
        if (!mBillingService.checkBillingSupported()) {
           // showDialog(DIALOG_CANNOT_CONNECT_ID);
        	Log.d(TAG, "error1");
        }
        
        if (!mBillingService.checkBillingSupported(Consts.ITEM_TYPE_SUBSCRIPTION)) {
            //showDialog(DIALOG_SUBSCRIPTIONS_NOT_SUPPORTED_ID);
        	Log.d(TAG, "error2");
        }
        
        initUi();
	}
	
	protected void onStart() {
        super.onStart();
        ResponseHandler.register(mGooglePurchaseObserver);
        initializeOwnedItems();
        Log.d(TAG, "onStart");
    }

    /**
     * Called when this activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        ResponseHandler.unregister(mGooglePurchaseObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPurchaseDatabase.close();
        mBillingService.unbind();
    }
    
    public void initUi()
    {
    	buyButton = (Button) findViewById(R.id.buyButton);
    	
    	buyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(!mBillingService.requestPurchase("sword_001", Consts.ITEM_TYPE_INAPP, null))
				{
					Log.d(TAG, "error1");
				}
				else
				{
					Log.d(TAG, "error2");
				}
			}
		});
    }
    
    
    private void initializeOwnedItems() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                doInitializeOwnedItems();
            }
        }).start();
    }
    
    private void doInitializeOwnedItems() {
        Cursor cursor = mPurchaseDatabase.queryAllPurchasedItems();
        if (cursor == null) {
            return;
        }

        final Set<String> ownedItems = new HashSet<String>();
        try {
            int productIdCol = cursor.getColumnIndexOrThrow(
                    PurchaseDatabase.PURCHASED_PRODUCT_ID_COL);
            while (cursor.moveToNext()) {
                String productId = cursor.getString(productIdCol);
                ownedItems.add(productId);
                Log.d(TAG, "productId = "+productId);
            }
        } finally {
            cursor.close();
        }

        // We will add the set of owned items in a new Runnable that runs on
        // the UI thread so that we don't need to synchronize access to
        // mOwnedItems.
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                /*mOwnedItems.addAll(ownedItems);
                mCatalogAdapter.setOwnedItems(mOwnedItems);*/
            }
        });
    }
    
}
