package edu.temple.androidservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ((Button) findViewById(R.id.quoteButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stockQuoteIntent = new Intent(MainActivity.this, QuoteService.class);
                stockQuoteIntent.putExtra("stock_symbol",
                        ((EditText) findViewById(R.id.symbolEditText)).getText().toString());
                startService(stockQuoteIntent);
            }
        });
    }

}
