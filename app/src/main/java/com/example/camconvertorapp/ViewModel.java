package com.example.camconvertorapp;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ViewModel extends androidx.lifecycle.ViewModel
{
    // maybe we dont need it because now we have the "ConversionTypeToSigns" FIXME
    String typesForConversionList[] = {"Currency", "Mass", "Temperature", "Length", "Volume" , "Pressure", "Time", "Angle" };
    
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
            return frequenciesMap.get(FrequencyType).second;
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


    //FIXME - delete unnecessary conversion types, and use default values based on location (ip - using retrofit)
    /**in case the user forgot or started processing the camera without setting the desired
     * source-target frequency's types - then set the default frequencies and notice him
     * by pop-up alert message
     */
    public boolean setDefaultFreq(){
        boolean isTrue = false;
        for(String type: typesForConversionList)
        {
            if(frequenciesMap == null)
            {
                return true;
            }
            if (!frequenciesMap.containsKey(type))
            {
                if(type.equals("Currency"))
                    frequenciesMap.put(type,new Pair<String, String>("Dollar $", "NIS"));

                if(type.equals("Weight"))
                    frequenciesMap.put(type,new Pair<String, String>("update", "update"));

                if(type.equals("Temperature"))
                    frequenciesMap.put(type,new Pair<String, String>("degree kelvin", "degree Celsius &#8451;"));


                //TODO didnt choose relevant types yet at these frequency's types-->
                if(type.equals("Length"))
                    frequenciesMap.put(type,new Pair<String, String>("update", "update"));

                if(type.equals("volume"))
                    frequenciesMap.put(type,new Pair<String, String>("update", "update"));

                if(type.equals("pressure"))
                    frequenciesMap.put(type,new Pair<String, String>("update", "update"));

                if(type.equals("time"))
                    frequenciesMap.put(type,new Pair<String, String>("update", "update"));

                if(type.equals("speed"))
                    frequenciesMap.put(type,new Pair<String, String>("update", "update"));

                if(type.equals("angle"))
                    frequenciesMap.put(type,new Pair<String, String>("update", "update"));

                isTrue = true;

            }
        }
        return isTrue;
    }
}
