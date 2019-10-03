package com.example.camconvertorapp;
import android.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kotlin.Triple;
import com.example.camconvertorapp.settingsActivity;

public class ViewModel extends androidx.lifecycle.ViewModel
{
    String typesForConversionList[] = {"Currency", "Weight", "Length", "Volume"};

    // create hash map - key: unit type , value: set of all signs of that type
    public static HashMap<String, ArrayList<String>> ConversionTypeToSigns = new HashMap<String,ArrayList<String>>();

    // key - frequency , value - (source,target)
    public static HashMap<String, Pair<String, String>> frequenciesMap = new HashMap<String, Pair<String, String>>();


    // key - unit type , value - rate compare to 'base rate' (on each type certain base rate was chosen)
    public static HashMap<String, Float> ratioToBase = new HashMap<String, Float>();



    // can be saved on sp also
    public void initHashMap()

    {

        // init ConversionTypeToSigns hashmap
        ConversionTypeToSigns.put("Currency", new ArrayList<String>());
        ConversionTypeToSigns.get("Currency").add("$");
        ConversionTypeToSigns.get("Currency").add("€");
        ConversionTypeToSigns.get("Currency").add("£");
        ConversionTypeToSigns.get("Currency").add("¥");
        ConversionTypeToSigns.get("Currency").add("\u20BD");


        ConversionTypeToSigns.put("Weight", new ArrayList<String>());
        ConversionTypeToSigns.get("Weight").add("kg");
        ConversionTypeToSigns.get("Weight").add("lb"); // pound



        ConversionTypeToSigns.put("Length", new ArrayList<String>());
        ConversionTypeToSigns.get("Length").add("km");
        ConversionTypeToSigns.get("Length").add("m");
        ConversionTypeToSigns.get("Length").add("cm");
        ConversionTypeToSigns.get("Length").add("mm");
        ConversionTypeToSigns.get("Length").add("mi");
        ConversionTypeToSigns.get("Length").add("yd");
        ConversionTypeToSigns.get("Length").add("ft");
        ConversionTypeToSigns.get("Length").add("in");

        ConversionTypeToSigns.put("Volume", new ArrayList<String>());
        ConversionTypeToSigns.get("Volume").add("L");
        ConversionTypeToSigns.get("Volume").add("gal");


        // init ratioToBase hashmap (all but currency)
        ratioToBase.put("Kilogram (kg)",1.f);
        ratioToBase.put("Pound (lb)",0.453592f);

        ratioToBase.put("Liter (L)",1.f);
        ratioToBase.put("Gallon (gal)",3.78541f);

        ratioToBase.put("Kilometer (km)",1.f);
        ratioToBase.put("Meter (m)",0.001f);
        ratioToBase.put("Centimeter (cm)",0.00001f);
        ratioToBase.put("Milimeter (mm)",0.0000001f);
        ratioToBase.put("Mile (mi)",1.60934f);
        ratioToBase.put("Yard (yd)",0.0009144f);
        ratioToBase.put("Foot (ft)",0.0003048f);
        ratioToBase.put("Inch (in)",0.0000254f);

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



    // check if user hasn't selected all types and moves to another activity
    // if the target is empty ~
    public boolean checkIfNotAllTypesSelected() {
        // we start positive
        boolean isTrue = false;

        if (frequenciesMap == null) {
            return true;
        }

        for (String type : typesForConversionList)
        {
            if (!frequenciesMap.containsKey(type))
            {
                isTrue = true;
            }
            else
            {
                if (frequenciesMap.get(type).second.equals("Select an item..."))
                {
                    isTrue = true;
                }
            }
        }
        return isTrue;
    }



    // for alert dialog
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


}
