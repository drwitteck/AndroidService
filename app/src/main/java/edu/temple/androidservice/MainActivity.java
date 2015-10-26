package edu.temple.androidservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    // Respond to various notification messages
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("ACTION_BROADCAST_QUOTE")){
                try {
                    Stock displayStock = new Stock(
                            new JSONObject(intent.getStringExtra("stock_data")).getJSONObject("list")
                                    .getJSONArray("resources")
                                    .getJSONObject(0)
                                    .getJSONObject("resource")
                                    .getJSONObject("fields"));
                    updateViews(displayStock);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register to receive intents
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_BROADCAST_QUOTE");
        registerReceiver(receiver, filter);

        findViewById(R.id.quoteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(MainActivity.this, QuoteService.class);
                serviceIntent.putExtra("stock_symbol"
                        , ((EditText) findViewById(R.id.symbolEditText)).getText().toString());
                startService(serviceIntent);
            }
        });

        findViewById(R.id.stopServiceButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, QuoteService.class));
            }
        });
    }

    private void updateViews(Stock currentStock) {
        ((TextView)findViewById(R.id.companyName)).setText(currentStock.getName());
        ((TextView)findViewById(R.id.stockPrice)).setText(String.valueOf("$" + currentStock.getPrice()));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
