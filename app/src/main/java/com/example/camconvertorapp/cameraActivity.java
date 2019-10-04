package com.example.camconvertorapp;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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

        imageView  = findViewById(R.id.imag);

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

    public void displayImg(String url)
    {
        Picasso.with(this).load(url).into(cameraActivity.imageView); //Fixme what the hell

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
