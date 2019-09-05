package com.example.camconvertorapp;
import android.util.Pair;
import java.util.HashMap;
import java.util.List;


public class TheViewModel extends androidx.lifecycle.ViewModel {
    String typesForConversionList[] = {"Currency", "Mass", "Temperature", "Length", "Volume" , "Pressure", "Time", "Angle" };

    // stands for :{Frequency_Type: {source_type, target_type}}
    public HashMap<String, Pair<String, String>> frequenciesMap = new HashMap<String, Pair<String, String>>();



    // return target by conversion type
    public String getTargetByFrequencyType(String FrequencyType)
    {
        return frequenciesMap.get(FrequencyType).second;
    }

    public HashMap<String, Pair<String, String>> getAllTypesStored(){

        HashMap<String, Pair<String, String>> allSortedTypes = new HashMap<String, Pair<String, String>>();

            for(String frequency:typesForConversionList)
            {
                if(frequenciesMap.containsKey(frequency))
                {
                    allSortedTypes.put(frequency,this.frequenciesMap.get(frequency));
                }
            }
            return allSortedTypes;
    }

    public void setFrequenciesMap(List<Frequency> frequencyList )
    {
        for(Frequency frequency:frequencyList)
        {
            frequenciesMap.put(frequency.type,new Pair<String, String>(frequency.source,frequency.target));
        }
    }

    public boolean checkIfNotAllTypesSelectd() {
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



        /**in case the user forgot or started processing the camera without setting the desired
         * source-target frequency's types - then set the default frequencies and notice him
         * by pop-up alert message
         */
    public boolean setDefaultFreq(){
        boolean isTrue = false;
        for(String type: typesForConversionList)
        {
            if(this.frequenciesMap == null)
            {
                return true;
            }
            if (!this.frequenciesMap.containsKey(type))
            {
                if(type.equals("Currency"))
                    this.frequenciesMap.put(type,new Pair<String, String>("Dollar $", "NIS"));

                if(type.equals("Weight"))
                    this.frequenciesMap.put(type,new Pair<String, String>("update", "update"));


                if(type.equals("Temperature"))
                    this.frequenciesMap.put(type,new Pair<String, String>("degree kelvin", "degree Celsius &#8451;"));


                //TODO didnt choose relevant types yet at these frequency's types-->
                if(type.equals("Length"))
                    this.frequenciesMap.put(type,new Pair<String, String>("update", "update"));

                if(type.equals("volume"))
                    this.frequenciesMap.put(type,new Pair<String, String>("update", "update"));

                if(type.equals("pressure"))
                    this.frequenciesMap.put(type,new Pair<String, String>("update", "update"));

                if(type.equals("time"))
                    this.frequenciesMap.put(type,new Pair<String, String>("update", "update"));

                if(type.equals("speed"))
                    this.frequenciesMap.put(type,new Pair<String, String>("update", "update"));

                if(type.equals("angle"))
                    this.frequenciesMap.put(type,new Pair<String, String>("update", "update"));

                isTrue = true;

            }
        }
        return isTrue;

    }



}
