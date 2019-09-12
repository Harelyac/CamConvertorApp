package com.example.camconvertorapp;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;
import android.widget.Toast;
import com.example.camconvertorapp.cameraModule.CameraSource;
import com.example.camconvertorapp.cameraModule.CameraSourcePreview;
import com.example.camconvertorapp.cameraModule.GraphicOverlay;
import com.example.camconvertorapp.currencyModule.FixerApi;
import com.example.camconvertorapp.currencyModule.Response;
import com.example.camconvertorapp.textxRecognitionModule.TextRecognitionProcessor;
import java.io.IOException;
import java.net.SocketOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import kotlin.Triple;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class cameraActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback{

    // Fields.
    private static final String TEXT_DETECTION = "Text Detection";
    private static final String TAG = "LivePreviewActivity";
    private static final int PERMISSION_REQUESTS = 1;

    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;

    TextView fixerRate;
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
        setContentView(R.layout.camera_activity);

        // Set camera source and overlay.
        preview = (CameraSourcePreview) findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = (GraphicOverlay) findViewById(R.id.fireFaceOverlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        // generate the object
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.initHashMap();

        // fixerRate = (TextView) findViewById(R.id.conversionRate);

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
                    fixerRate.setText("Code: " + response.code());
                    return;
                }
                    // if successful!
                    fixerResponse = response.body();
                    HashMap<String, Pair<String, String>> allTypes = viewModel.getAllTypesStored();
                    baseCurrency = ViewModel.getSourceByFrequencyType("Currency");
                    targetCurrency = ViewModel.getTargetByFrequencyType("Currency");
                    conversionRate = fixerResponse.rates.getConversionRate(baseCurrency, targetCurrency);
                    textRecognitionProcessor.setConversionRate(conversionRate);
                    Toast.makeText(getApplicationContext(),"Succeeded to get response from server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed to get response from server", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Exception: "+Log.getStackTraceString(t));
            }
        });




        // Check permissions and start camera.
        if (allPermissionsGranted()) {
            createCameraSource();
        } else {
            getRuntimePermissions();
        }

    }


    // use it just to notify the user about the chosen unit types
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
//        return list;
        return str2;
    }

    // Camera
    private void createCameraSource() {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        Log.i(TAG, "Using Text Detector Processor");
        textRecognitionProcessor = new TextRecognitionProcessor();
        cameraSource.setMachineLearningFrameProcessor(textRecognitionProcessor);
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
            createCameraSource();
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
