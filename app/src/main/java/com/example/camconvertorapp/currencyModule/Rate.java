package com.example.camconvertorapp.currencyModule;

import java.util.HashMap;
import java.util.Map;

public class Rate {
    public static float ILS;
    public static float EUR;
    public static float USD;
    public static float AUD;
    public static float CAD;
    public static float GBP;
    public static float JPY;
    public static float RUB;

    public static float getConversionRate(String baseRateName, String targetRateName) {
        Map<String,Float> rates = new HashMap<String,Float>();
        rates.put("ILS",ILS);
        rates.put("€",EUR);
        rates.put("$",USD);
        rates.put("AUD",AUD); // sign same as dollar, we wont use it
        rates.put("CAD",CAD); // sign same as dollar, we wont use it
        rates.put("£",GBP);
        rates.put("¥",JPY); // yen, japan
        rates.put("RUB",RUB); // rubel, russia

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
