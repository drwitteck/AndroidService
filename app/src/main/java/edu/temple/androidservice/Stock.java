package edu.temple.androidservice;

import org.json.JSONException;
import org.json.JSONObject;

public class Stock {
    private String name, symbol;
    private double price;

    public Stock(String name, String symbol, double price) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
    }

    public Stock (JSONObject stockObject) throws JSONException{
        this(stockObject.getString("name"), stockObject.getString("symbol"), stockObject.getDouble("price"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object object){
        return (object instanceof Stock) &&
            this.symbol.equalsIgnoreCase(((Stock)object).symbol);
    }

    public JSONObject getStockAsJSON(){
        JSONObject stockObject = new JSONObject();
        try {
            stockObject.put("name", name);
            stockObject.put("symbol", symbol);
            stockObject.put("price", price);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stockObject;
    }
}
