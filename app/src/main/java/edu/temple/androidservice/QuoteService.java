package edu.temple.androidservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class QuoteService extends Service {

    IBinder quoteBinder = new QuoteBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return quoteBinder;
    }

    public class QuoteBinder extends Binder {
        QuoteService getService (){
            return QuoteService.this;
        }
    }

    public void getQuote(final String symbol, final Handler handler) {
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
                    Message msg = Message.obtain();
                    msg.obj = stockObject;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
}
