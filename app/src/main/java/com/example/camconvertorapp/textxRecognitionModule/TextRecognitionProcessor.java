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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Processor for the text recognition demo.
 */
public class TextRecognitionProcessor extends VisionProcessorBase<FirebaseVisionText> {

    private static final String TAG = "TextRecProc";
    private static HashMap<String, HashSet> signs = new HashMap<String,HashSet>();// create hash map - key: unit type , value: set of all signs of that type

    // initialize those values from the view model
    private float conversionRate = 1.0f;
    private float source_price = 0.0f ;
    private float target_price = 0.0f ;
    private String sign = "";
    private final FirebaseVisionTextRecognizer detector;

    public TextRecognitionProcessor() {

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
                            sign = elements.get(k+1).getText();
                        }

                        // now check the conversion type based on sign using the hashtable (key = conversion type, value = all the sign representing unit types)
                        // now identifiy target unit type using the getTatgetBySourceType(sign,conversion type)
                        // now calculate the conversion rate
                        target_price = source_price * conversionRate;
                        GraphicOverlay.Graphic textGraphic = new TextGraphic(graphicOverlay,
                                String.valueOf(target_price), elements.get(k).getBoundingBox());
                        graphicOverlay.add(textGraphic);
                    } catch (NumberFormatException ex) {
                        // Not a float
                    }
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
