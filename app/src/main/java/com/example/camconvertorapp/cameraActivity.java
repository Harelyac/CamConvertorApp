package com.example.camconvertorapp;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.camconvertorapp.barcodeScanningModule.BarcodeScanningProcessor;
import com.example.camconvertorapp.cameraModule.CameraSource;
import com.example.camconvertorapp.cameraModule.CameraSourcePreview;
import com.example.camconvertorapp.cameraModule.GraphicOverlay;
import com.example.camconvertorapp.currencyModule.FixerApi;
import com.example.camconvertorapp.currencyModule.Response;
import com.example.camconvertorapp.textRecognitionModule.TextRecognitionProcessor;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class cameraActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback{


    public static String model;
    private static final String TAG = "LivePreviewActivity";
    private static final int PERMISSION_REQUESTS = 1;

    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;

    public static EditText detectedValue;
    public static ImageView imageView;
    public static WebView webView;
    Response fixerResponse;

    private String baseCurrency = "ILS";
    private String targetCurrency = "ILS";
    private float conversionRate = 1.0f;

    private TextRecognitionProcessor textRecognitionProcessor;
    private ViewModel viewModel;

    // MainActivity main OnCreate method.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Set camera source and overlay.
        preview = (CameraSourcePreview) findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = (GraphicOverlay) findViewById(R.id.fireFaceOverlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }


        // load views
        detectedValue = findViewById(R.id.detectedValue);
        detectedValue.setInputType(0);
        //imageView  = findViewById(R.id.imag);
        //webView = findViewById(R.id.web);

        // generate the object
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        // init all hashmaps needed
        viewModel.initHashMap();


        // if we are on a convert mode, try to fetch up to date rates with restful
        if (model.equals("Text Detection"))
        {
            textRecognitionProcessor = new TextRecognitionProcessor();

            // set the conversion rates for the chosen unit types
            float weightconversionRate  = viewModel.ratioToBase.get(viewModel.getSourceByFrequencyType("Weight")) / viewModel.ratioToBase.get(viewModel.getTargetByFrequencyType("Weight"));
            textRecognitionProcessor.setConversionRate("Weight", weightconversionRate);

            float lengthconversionRate  = viewModel.ratioToBase.get(viewModel.getSourceByFrequencyType("Length")) / viewModel.ratioToBase.get(viewModel.getTargetByFrequencyType("Length"));
            textRecognitionProcessor.setConversionRate("Length",lengthconversionRate);

            float volumeconversionRate  = viewModel.ratioToBase.get(viewModel.getSourceByFrequencyType("Volume")) / viewModel.ratioToBase.get(viewModel.getTargetByFrequencyType("Volume"));
            textRecognitionProcessor.setConversionRate("Volume",volumeconversionRate);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://data.fixer.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FixerApi fixerApi = retrofit.create(FixerApi.class);
            Call<Response> call = fixerApi.getResponse();
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    if (!response.isSuccessful()) {
                        return;
                    }
                    // if successful!, update currencies rates
                    fixerResponse = response.body();

                    // the base assumption here is that, that we are on camera activity and all types has been set already
                    baseCurrency = ViewModel.getSourceByFrequencyType("Currency");
                    targetCurrency = ViewModel.getTargetByFrequencyType("Currency");
                    conversionRate = fixerResponse.rates.getConversionRate(baseCurrency, targetCurrency);

                    // set the conversion rate for currency
                    textRecognitionProcessor.setConversionRate("Currency",conversionRate);
                    Toast.makeText(getApplicationContext(),"Succeeded to get response from server", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Failed to get response from server", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Exception: "+Log.getStackTraceString(t));
                }
            });
        }


        // Check permissions and start camera.
        if (allPermissionsGranted()) {
            createCameraSource(model);
        } else {
            getRuntimePermissions();
        }

    }


    // Camera
    private void createCameraSource(String model) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null)
        {
            cameraSource = new CameraSource(this, graphicOverlay);
        }
        if(model.equals("Text Detection"))
        {
            Log.i(TAG, "Using Text Detector Processor");
            textRecognitionProcessor = new TextRecognitionProcessor();
            cameraSource.setMachineLearningFrameProcessor(textRecognitionProcessor);
        }
        else
        {
            Log.i(TAG, "Using Barcode Detector Processor");
            cameraSource.setMachineLearningFrameProcessor(new BarcodeScanningProcessor());
        }
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    public void drawImage() throws IOException {
        if(!Scrape_Asynctasks.imgurl.equals(""))
        {

            // from string to uri and then to bitmap
            String image = "data:image/jpeg;base64,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";

            Uri imageUri = Uri.parse(image);

            //byte[] imageAsBytes = Base64.decode(image.getBytes() , Base64.DEFAULT);

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        startCameraSource();
    }

    /** Stops the camera. */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }


    // Permissions
    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            createCameraSource(model);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }

}
