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
package com.example.camconvertorapp.textxRecognitionModule;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.camconvertorapp.ViewModel;
import com.example.camconvertorapp.currencyModule.Rate;
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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

/**
 * Processor for the text recognition demo.
 */
public class TextRecognitionProcessor extends VisionProcessorBase<FirebaseVisionText> {

    private static final String TAG = "TextRecProc";

    // initialize those values from the view model
    private float conversionRate = 1.0f;
    private double source_price = 0.0f ;
    private double target_price = 0.0f ;
    private String source_sign = "";
    private String target_sign = "";
    private String temp_sign = "";
    private String conversion_type = "";
    private final FirebaseVisionTextRecognizer detector;


    public TextRecognitionProcessor()
    {
        detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
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
                        source_price = Float.parseFloat(elements.get(k).getText());
                        if(k < elements.size() - 1){
                            source_sign = elements.get(k+1).getText();
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


                        // check
                        target_sign = ViewModel.getTargetByFrequencyType(conversion_type);


                        if (target_sign != null)
                        {
                            target_sign = target_sign.substring(target_sign.indexOf("(") + 1, target_sign.indexOf(")"));

                            temp_sign = ViewModel.getSourceByFrequencyType(conversion_type);
                            // check if the source identified is not what actually being configured on room
                            if (!source_sign.equals(temp_sign.substring(temp_sign.indexOf("(") + 1, temp_sign.indexOf(")"))))
                            {
                                continue;
                            }

                            target_price = source_price * conversionRate;

                            // output the target price concatenated with the target sign
                            GraphicOverlay.Graphic priceGraphic = new TextGraphic(graphicOverlay,
                                    String.valueOf(target_price) + target_sign, elements.get(k).getBoundingBox());
                            graphicOverlay.add(priceGraphic);
                        }
                    }
                    catch (NumberFormatException ex) {
                        // Not a float
                    }
                    // clear target price
                    target_price = 0.0f;
                    target_sign = "";
                }
            }
        }
        graphicOverlay.postInvalidate();
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.w(TAG, "Text detection failed." + e);
    }

    public void setConversionRate(float newConversionRate) {
        conversionRate = newConversionRate;
    }
}
