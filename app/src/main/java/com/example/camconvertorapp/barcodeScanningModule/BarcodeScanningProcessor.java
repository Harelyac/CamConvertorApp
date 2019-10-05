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
package com.example.camconvertorapp.barcodeScanningModule;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import android.util.Base64;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.camconvertorapp.MainActivity;
import com.example.camconvertorapp.Scrape_Asynctasks;
import com.example.camconvertorapp.VisionProcessorBase;
import com.example.camconvertorapp.cameraActivity;
import com.example.camconvertorapp.cameraModule.CameraImageGraphic;
import com.example.camconvertorapp.cameraModule.FrameMetadata;
import com.example.camconvertorapp.cameraModule.GraphicOverlay;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;




import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;


/**
 * Barcode Detector Demo.
 */
public class BarcodeScanningProcessor extends VisionProcessorBase<List<FirebaseVisionBarcode>> {

    private static final String TAG = "BarcodeScanProc";
    private cameraActivity camera = new cameraActivity();
    private final FirebaseVisionBarcodeDetector detector;



    public BarcodeScanningProcessor() {
        // Note that if you know which format of barcode your app is dealing with, detection will be
        // faster to specify the supported barcode formats one by one, e.g.
        // new FirebaseVisionBarcodeDetectorOptions.Builder()
        //     .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector();
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Barcode Detector: " + e);
        }
    }

    @Override
    protected Task<List<FirebaseVisionBarcode>> detectInImage(FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }

    @Override
    protected void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull List<FirebaseVisionBarcode> barcodes,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) throws IOException {
        graphicOverlay.clear();
        if (originalCameraImage != null) {
            CameraImageGraphic imageGraphic = new CameraImageGraphic(graphicOverlay, originalCameraImage);
            graphicOverlay.add(imageGraphic);
        }
        for (int i = 0; i < barcodes.size(); ++i) {
            FirebaseVisionBarcode barcode = barcodes.get(i);
            BarcodeGraphic barcodeGraphic = new BarcodeGraphic(graphicOverlay, barcode);

            // set barcode number of textview
            String value = barcode.getDisplayValue();
            cameraActivity.detectedValue.setText(value);


            String url = "https://www.google.com/search?q=";

            if (!value.isEmpty())
            {
                // scrape google search using Jsoup on bg thread using Async task
                url = url + "885178979613" + " price" + "&num10";

                Log.i("BEFORE", "ASYNC");

                // start scraping using bg thread
                Scrape_Asynctasks task = new Scrape_Asynctasks();
                task.execute(url);

                if (!Scrape_Asynctasks.imgurl.equals(""))
                {
                    cameraActivity.webView.loadUrl(Scrape_Asynctasks.imgurl);
                }
                // call draw method
                //camera.drawImage();

                // from string to uri and then to bitmap
                //String image = "data:image/jpeg;base64,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";

                /* byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                cameraActivity.imageView.setImageBitmap(decodedByte);*/

                graphicOverlay.add(barcodeGraphic);
            }
        }
        graphicOverlay.postInvalidate();
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Barcode detection failed " + e);
    }
}
