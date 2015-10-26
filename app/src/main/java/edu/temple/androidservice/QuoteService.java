package edu.temple.androidservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class QuoteService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId){
        getQuote(intent.getStringExtra("stock_symbol"));
        return START_STICKY;
    }


    @Override
    public void onCreate(){
        startForeground();
    }

    public void getQuote(final String symbol) {
        Thread t = new Thread() {
            @Override
            public void run() {

                URL stockQuoteUrl;

                try {
                    for (int i = 0; i < 10; i++) {

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
                        Thread.sleep(2000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private void startForeground(){
        Notification.Builder n;

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setAction("SOME_ACTION");
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
        n  = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Your service is running")
                .setSmallIcon(R.drawable.ic_small)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pIntent)
                .setAutoCancel(false);

        startForeground(1234, n.build());
    }
}
