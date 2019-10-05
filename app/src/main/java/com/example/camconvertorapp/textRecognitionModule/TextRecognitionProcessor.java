// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.example.camconvertorapp.textRecognitionModule;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.camconvertorapp.ViewModel;
import com.example.camconvertorapp.cameraActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.example.camconvertorapp.cameraModule.CameraImageGraphic;
import com.example.camconvertorapp.cameraModule.FrameMetadata;
import com.example.camconvertorapp.cameraModule.GraphicOverlay;
import com.example.camconvertorapp.VisionProcessorBase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Processor for the text recognition demo.
 */
public class TextRecognitionProcessor extends VisionProcessorBase<FirebaseVisionText> {

    private static final String TAG = "TextRecProc";

    // initialize those values from the view model
    private float conversionRate = 1.0f;
    private String source;
    private double source_price = 0.0f ;
    private double target_price = 0.0f ;
    private String source_sign = "";
    private String temp_source_sign = "";
    private String target_sign = "";
    private String temp_sign = "";
    private String conversion_type = "";
    private final FirebaseVisionTextRecognizer detector;
    private ArrayList<String> currency_signs = new ArrayList<>();
    private ArrayList<String> other_1_char_signs = new ArrayList<>();
    private ArrayList<String> other_2_char_signs = new ArrayList<>();

    // key - unit type , value - rate compare to 'base rate' (on each type certain base rate was chosen)
    public static HashMap<String, Float> coversionRateMap = new HashMap<String, Float>();

    public TextRecognitionProcessor()
    {
        detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        currency_signs.add("$");
        currency_signs.add("£");
        currency_signs.add("¥");
        currency_signs.add("€");
        currency_signs.add("\u20BD");

        other_1_char_signs.add("L");
        other_1_char_signs.add("m");

        other_2_char_signs.add("km");
        other_2_char_signs.add("cm");
        other_2_char_signs.add("mm");
        other_2_char_signs.add("mi");
        other_2_char_signs.add("yd");
        other_2_char_signs.add("ft");
        other_2_char_signs.add("in");
        other_2_char_signs.add("kg");
        other_2_char_signs.add("lb");
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Text Detector: " + e);
        }
    }

    @Override
    protected Task<FirebaseVisionText> detectInImage(FirebaseVisionImage image) {
        return detector.processImage(image);
    }

    @Override
    protected void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull FirebaseVisionText results,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {
        graphicOverlay.clear();
        if (originalCameraImage != null) {
            CameraImageGraphic imageGraphic = new CameraImageGraphic(graphicOverlay,
                    originalCameraImage);
            graphicOverlay.add(imageGraphic);
        }
        List<FirebaseVisionText.TextBlock> blocks = results.getTextBlocks();
        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {

                    try {

                        // if there is no space between sign and number ex -> [$4] or [4m] or [4kg]
                        source = elements.get(k).getText();
                        if (source.length() > 1)
                        {
                            // if currency sign before number
                            temp_source_sign = source.substring(0,1);
                            if (currency_signs.contains(temp_source_sign))
                            {
                                Log.d("SIGN", source_sign);
                                source_price = Float.parseFloat((source.substring(1)));
                                source_sign = temp_source_sign;
                            }

                            // if 1 char sign after number 2 char sign after number
                            temp_source_sign = source.substring(source.length() - 1);
                            if (other_1_char_signs.contains(temp_source_sign))
                            {
                                Log.d("SIGN", source_sign);
                                source_price = Float.parseFloat(source.substring(0, source.length() - 1));
                                source_sign = temp_source_sign;
                            }

                            // or 2 char sign after number
                            if (source.length() > 2)
                            {

                                temp_source_sign = source.substring(source.length() - 2);
                                if (other_2_char_signs.contains(temp_source_sign))
                                {
                                    Log.d("SIGN", source_sign);
                                    source_price = Float.parseFloat(source.substring(0, source.length() - 2));
                                    source_sign = temp_source_sign;
                                }
                            }

                            else if(source.length() == 2 && other_2_char_signs.contains(source))
                            {
                                temp_source_sign = source;
                                if(k > 0)
                                {
                                    source_price = Float.parseFloat(elements.get(k-1).getText());
                                    source_sign = temp_source_sign;
                                }
                            }


                        }

                        // if there is space ex -> [$] [4] or [4] [kg]
                        else if(source.length() == 1 && currency_signs.contains(source))
                        {
                            temp_source_sign = source;
                            if(k < elements.size())
                            {
                                source_price = Float.parseFloat(elements.get(k+1).getText());
                                source_sign = temp_source_sign;
                            }
                        }


                        else if(source.length() == 1 && other_1_char_signs.contains(source))
                        {
                            temp_source_sign = source;
                            if(k > 0)
                            {
                                source_price = Float.parseFloat(elements.get(k-1).getText());
                                source_sign = temp_source_sign;
                            }
                        }

                        if ((source_sign != null))
                        {
                            outer:
                            // check the conversion type based on source sign
                            for (String key : ViewModel.ConversionTypeToSigns.keySet())
                            {
                                for (String sign : ViewModel.ConversionTypeToSigns.get(key))
                                {
                                    if (sign.equals(source_sign))
                                    {
                                        Log.d(TAG, "match!");
                                        conversion_type = key;
                                        break outer;
                                    }
                                }
                            }
                        }


                        // check the target sign based on conversion type
                        target_sign = ViewModel.getTargetByFrequencyType(conversion_type);


                        if (target_sign != null)
                        {
                            target_sign = target_sign.substring(target_sign.indexOf("(") + 1, target_sign.indexOf(")"));

                            temp_sign = ViewModel.getSourceByFrequencyType(conversion_type);
                            // check if the source sign that was detected is not what actually being configured on room
                            if (!source_sign.equals(temp_sign.substring(temp_sign.indexOf("(") + 1, temp_sign.indexOf(")"))))
                            {
                                continue;
                            }

                            target_price = source_price * coversionRateMap.get(conversion_type);

                            // output the target price concatenated with the target sign
                            GraphicOverlay.Graphic priceGraphic = new TextGraphic(graphicOverlay,
                                    String.valueOf(target_price) + target_sign, elements.get(k).getBoundingBox());

                            cameraActivity.detectedValue.setText(Float.toString(Float.valueOf(String.format(Locale.getDefault(), "%.4f", target_price))) + " " + target_sign);
                            graphicOverlay.add(priceGraphic);
                        }
                    }
                    catch (NumberFormatException ex) {
                        // Not a float
                    }
                    // clear target price and sign
                    target_price = 0.0f;
                    target_sign = "";

                    // clear source price and sign
                    source_price = 0.0f;
                    source_sign = "";
                    temp_source_sign = "";
                }
            }
        }
        graphicOverlay.postInvalidate();
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.w(TAG, "Text detection failed." + e);
    }


    // being called from outside class before converting
    public void setConversionRate(String type, float newConversionRate)
    {
        coversionRateMap.put(type,newConversionRate);
    }
}
