package com.example.camconvertorapp.currencyModule;

import java.util.HashMap;
import java.util.Map;

public class Rate {
    public  float ILS;
    public  float EUR;
    public  float USD;
    public  float AUD; // not in use, just for fetching response
    public  float CAD; // not in use, just for fetching response
    public  float GBP;
    public  float JPY;
    public  float RUB;

    public float getConversionRate(String baseRateName, String targetRateName) {
        Map<String, Float> rates = new HashMap<String, Float>();
        rates.put("NIS (₪)",ILS); // we wont use it, firebase can't identify it
        rates.put("Euro (€)",EUR);
        rates.put("Dollar ($)",USD);
        rates.put("AUD",AUD); // sign same as dollar, we wont use it
        rates.put("CAD",CAD); // sign same as dollar, we wont use it
        rates.put("Pound (£)",GBP);
        rates.put("Yen (¥)",JPY);
        rates.put("RUB (\u20BD)",RUB);

        Float baseRate = rates.get(baseRateName);
        Float targetRate = rates.get(targetRateName);

        if (baseRate == null) {
            baseRate = 1.0f;
        }
        if (targetRate == null) {
            targetRate = 1.0f;
        }

        // return the conversion rate
        return targetRate / baseRate;
    }
}
