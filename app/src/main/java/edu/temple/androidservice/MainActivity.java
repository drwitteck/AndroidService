package edu.temple.androidservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class MainActivity extends Activity {

    QuoteService quoteService;
    boolean connected;

    ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            QuoteService.QuoteBinder binder = (QuoteService.QuoteBinder) service;
            quoteService = binder.getService();
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }
    };

    @Override
    public void onStart(){
        super.onStart();
        Intent serviceIntent = new Intent(this, QuoteService.class);
        bindService(serviceIntent, myConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop(){
        super.onStop();
        unbindService(myConnection);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.quoteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connected){
                    quoteService.getQuote((
                            ((EditText) findViewById(R.id.symbolEditText)).getText().toString()
                    ), serviceHandler);
                }
            }
        });
    }

    Handler serviceHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            JSONObject responseObject = (JSONObject) msg.obj;
            Stock currentStock = null;
            try {
                currentStock = new Stock(responseObject.getJSONObject("list")
                        .getJSONArray("resources")
                        .getJSONObject(0)
                        .getJSONObject("resource")
                        .getJSONObject("fields"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            updateViews(currentStock);

            return false;
        }
    });

    private void updateViews(Stock currentStock) {
        ((TextView)findViewById(R.id.companyName)).setText(currentStock.getName());
        ((TextView)findViewById(R.id.stockPrice)).setText(String.valueOf("$" + currentStock.getPrice()));
    }

}
