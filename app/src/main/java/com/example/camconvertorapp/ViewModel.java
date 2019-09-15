package com.example.camconvertorapp;
import android.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kotlin.Triple;


public class ViewModel extends androidx.lifecycle.ViewModel
{
    // maybe we dont need it because now we have the "ConversionTypeToSigns" FIXME
    String typesForConversionList[] = {"Currency", "Weight", "Temperature", "Length", "Volume"};
    
    public static HashMap<String, ArrayList<String>> ConversionTypeToSigns = new HashMap<String,ArrayList<String>>();// create hash map - key: unit type , value: set of all signs of that type

    // key - frequency , value - (source,target)
    public static HashMap<String, Pair<String, String>> frequenciesMap = new HashMap<String, Pair<String, String>>();


    public void initHashMap()
    {
        ConversionTypeToSigns.put("Currency", new ArrayList<String>());
        ConversionTypeToSigns.get("Currency").add("$");
        ConversionTypeToSigns.get("Currency").add("€");
        ConversionTypeToSigns.get("Currency").add("£");
        ConversionTypeToSigns.get("Currency").add("¥");
        ConversionTypeToSigns.get("Currency").add("₪");
        ConversionTypeToSigns.get("Currency").add("\u20BD");


        ConversionTypeToSigns.put("Weight", new ArrayList<String>());
        ConversionTypeToSigns.get("Weight").add("kg");
        ConversionTypeToSigns.get("Weight").add("lb"); // pound


        ConversionTypeToSigns.put("Temperature", new ArrayList<String>());
        ConversionTypeToSigns.get("Temperature").add("°C"); // part of the metric system
        ConversionTypeToSigns.get("Temperature").add("°F"); // used in usa (but not actually in the imperial metric system)


        ConversionTypeToSigns.put("Length", new ArrayList<String>());
        ConversionTypeToSigns.get("Length").add("m");
        ConversionTypeToSigns.get("Length").add("yd"); // yard


        ConversionTypeToSigns.put("Volume", new ArrayList<String>());
        ConversionTypeToSigns.get("Volume").add("L");
        ConversionTypeToSigns.get("Volume").add("gal"); // pound
    }


    // return target by conversion type
    public static String getTargetByFrequencyType(String FrequencyType)
    {
        if (frequenciesMap.containsKey(FrequencyType))
        {
            return frequenciesMap.get(FrequencyType).second;
        }
        return null;
    }

    // return target by conversion type
    public static String getSourceByFrequencyType(String FrequencyType)
    {
        if (frequenciesMap.containsKey(FrequencyType))
        {
            return frequenciesMap.get(FrequencyType).first;
        }
        return null;
    }


    // FIXME
    public HashMap<String, Pair<String, String>> getAllTypesStored(){

        HashMap<String, Pair<String, String>> allSortedTypes = new HashMap<String, Pair<String, String>>();

            for(String frequency:typesForConversionList)
            {
                if(frequenciesMap.containsKey(frequency))
                {
                    allSortedTypes.put(frequency,frequenciesMap.get(frequency));
                }
            }
            return allSortedTypes;
    }


    // read frequencies (entities) from room db into view model's hash map - "frequenciesMap"
    public void setFrequenciesMap(List<Frequency> frequencyList )
    {
        for(Frequency frequency:frequencyList)
        {
            frequenciesMap.put(frequency.type, new Pair<String, String>(frequency.source,frequency.target));
        }
    }



    // check if useer hasn't selected all types and moves to another activity
    public boolean checkIfNotAllTypesSelected() {
        boolean isTrue = false;
        for (String type : typesForConversionList) {
            if (this.frequenciesMap == null) {
                return true;
            }
            if (!this.frequenciesMap.containsKey(type)) {
                isTrue = false;
            }
        }
        return isTrue;
    }



    // maybe for debug purposes
    public StringBuilder getAllTypesOrdered(HashMap<String, Pair<String,String>> typesUpdated){
        Triple<String,String, String> str ;
        ArrayList<Triple<String,String,String>> list = new ArrayList<Triple<String, String, String>>();
        StringBuilder str2  = new StringBuilder();

        String[] strs = (String[]) typesUpdated.keySet().toArray(new String[0]);
        for(String type :  strs)
        {
            str2.append("\n").append("type: " + type + "\n")
                    .append("     -> source sign: " + typesUpdated.get(type).first )
                    .append("\n")
                    .append("     -> target sign: " +typesUpdated.get(type).second)
                    .append("\n");


            str =  new Triple<String, String, String>(type , typesUpdated.get(type).first , typesUpdated.get(type).second);
            list.add(str);


        }
        //return list;
        return str2;
    }

    // set default conversion types source unit based on location - metric or imperial base unit types
    // and currency of country / region only on conversion types that was not set yet

    public void setDefaultFreq(String timezone, String country){
        String [] parts = timezone.split("/",0);

        boolean isMetric = true;

        String value = country;

        // only in Europe get continent
        if (parts[0].equals("Europe"))
        {
            value = parts[0];
        }

        if (country.equals("GB"))
        {
            value = country;
        }

        if (value.equals("US"))
        {
            isMetric = false;
        }

        for(String type: typesForConversionList)
        {
            if(frequenciesMap == null)
            {
                return;
            }
            if (!frequenciesMap.containsKey(type))
            {
                if (isMetric)
                {
                    if(type.equals("Currency"))
                        frequenciesMap.put(type,new Pair<String, String>(getCurrency(value), "update"));

                    if(type.equals("Weight"))
                        frequenciesMap.put(type,new Pair<String, String>("kilogram (kg)", "update"));

                    if(type.equals("Temperature"))
                        frequenciesMap.put(type,new Pair<String, String>("degree Celsius (°C)", "update"));

                    if(type.equals("Length"))
                        frequenciesMap.put(type,new Pair<String, String>("meter (m)", "update"));

                    if(type.equals("Volume"))
                        frequenciesMap.put(type,new Pair<String, String>("Liter (L)", "update"));
                }
                else
                {
                    if(type.equals("Currency"))
                        frequenciesMap.put(type,new Pair<String, String>(getCurrency(value), "update"));

                    if(type.equals("Weight"))
                        frequenciesMap.put(type,new Pair<String, String>("pound (lb)", "update"));

                    if(type.equals("Temperature"))
                        frequenciesMap.put(type,new Pair<String, String>("degree Fahrenheit (°F)", "update"));

                    if(type.equals("Length"))
                        frequenciesMap.put(type,new Pair<String, String>("Yard (yd)", "update"));

                    if(type.equals("Volume"))
                        frequenciesMap.put(type,new Pair<String, String>("gallon (gal)", "update"));
                }
            }
        }
    }

    // get currency by given country name
    public String getCurrency(String country)
    {
        switch(country)
        {
            case "IL":
            {
                return "NIS (₪)";
            }
            case "US":
            {
                return "Dollar ($)";
            }
            case "EU":
            {
                return "Euro (€)";
            }
            case "GB":
            {
                return "Pound (£)";
            }
            case "JP":
            {
                return "Yen (¥)";
            }
            case "RU":
            {
                return "RUB (\u20BD)";
            }
            default:
                return "";
        }
    }
}
