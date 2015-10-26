package edu.temple.androidservice;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class QuoteService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public QuoteService() {
        super("QuoteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getQuote(intent.getStringExtra("stock_symbol"));
    }

    public void getQuote(final String symbol) {
        Thread t = new Thread() {
            @Override
            public void run() {

                URL stockQuoteUrl;

                try {

                    stockQuoteUrl = new URL("http://finance.yahoo.com/webservice/v1/symbols/" + symbol + "/quote?format=json");

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    stockQuoteUrl.openStream()));

                    String response = "", tmpResponse;

                    tmpResponse = reader.readLine();
                    while (tmpResponse != null) {
                        response = response + tmpResponse;
                        tmpResponse = reader.readLine();
                    }

                    JSONObject stockObject = new JSONObject(response);
                    Log.d("Saved stock data", stockObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

}
