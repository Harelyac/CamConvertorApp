package com.example.camconvertorapp.currencyModule;


public class Response {
    public boolean success; // changed to boolean, was string before
    public int timestamp;
    public String base;
    public String date;
    public Rate rates;
}
