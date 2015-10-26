package edu.temple.androidservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
