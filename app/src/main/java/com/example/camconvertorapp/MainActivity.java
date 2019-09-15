package com.example.camconvertorapp;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;

import com.example.camconvertorapp.locationModule.IpApi;
import com.example.camconvertorapp.locationModule.Response;
import com.daimajia.androidanimations.library.Techniques;
import com.eftimoff.androipathview.PathView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import nl.dionsegijn.konfetti.KonfettiView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.transition.Slide;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.daimajia.androidanimations.library.YoYo;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public ViewModel viewModel;
    public AppDatabase db;
    public Response IpResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // start with initializing local App DB
        db = AppDatabase.getDatabase(this);

        // init view model
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        // observe into live data and update view model with it
        db.freqDao().getAll().observe(this, new Observer<List<Frequency>>() {
            @Override
            public void onChanged(@Nullable final List<Frequency> frequencies) {

                viewModel.setFrequenciesMap(frequencies);
            }
        });


        // FIXME - need to find a an api to get location
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ipinfo.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IpApi ipApi = retrofit.create(IpApi.class);
        Call<Response> call = ipApi.getResponse();
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (!response.isSuccessful()) {
                    Log.d("", "onResponse: " + response.toString());
                    return;
                }
                // if successful!
                IpResponse = response.body();
                viewModel.setDefaultFreq(IpResponse.timezone, IpResponse.country);
                Toast.makeText(getApplicationContext(),"Succeeded to get response from server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed to get response from server", Toast.LENGTH_SHORT).show();
                Log.e("", "Exception: "+Log.getStackTraceString(t));
            }
        });





        Toolbar toolbar = findViewById(R.id.toolbar);

        PathView pathView= findViewById(R.id.path);
        pathView.getSequentialPathAnimator()
                .delay(100)
                .interpolator(new AccelerateDecelerateInterpolator())
                .start();


        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setBackgroundResource(R.color.theme_yellow_accent);

        TextView text = findViewById(R.id.title1);


        YoYo.with(Techniques.Tada)
                .duration(1200)
                .repeat(YoYo.INFINITE)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), cameraActivity.class));
            }
        });

        KonfettiView konfettiView = findViewById(R.id.viewKonfetti);
        BubblePicker bubblePicker = findViewById(R.id.picker);


        final String[] titles = getResources().getStringArray(R.array.title);
        final TypedArray colors = getResources().obtainTypedArray(R.array.colors);
        final TypedArray images = getResources().obtainTypedArray(R.array.images);


        bubblePicker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return titles.length;
            }

            @NotNull
            @Override
            public PickerItem getItem(int position) {
                PickerItem item = new PickerItem();
                item.setTitle(titles[position]);
                item.setGradient(new BubbleGradient(colors.getColor((position * 2) % 8, 0),
                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));

                item.setGradient(new BubbleGradient(colors.getColor(position , 0),
                        colors.getColor(position , 0), BubbleGradient.VERTICAL));
                item.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.black));
                item.setTextSize(80);
                item.setBackgroundImage(ContextCompat.getDrawable(MainActivity.this, images.getResourceId(position, 0)));
                return item;
            }
        });
        bubblePicker.setCenterImmediately(true);
        bubblePicker.setBubbleSize(58);

        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {

                if ( item.component1().equals("Settings")){
                    startActivity(new Intent(getApplicationContext(), settingsActivity.class));
                }

                if ( item.component1().equals("Default Types")){

                    final HashMap<String, Pair<String, String>> typesUpdated =  viewModel.getAllTypesStored();


                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
                    boolean hasNotInit = viewModel.checkIfNotAllTypesSelected();
                    if(ViewModel.frequenciesMap.isEmpty()){
                        //the user is first time on App. send him to settings
                        alertDialog.setTitle("hi, new user!");
                        alertDialog.setMessage("tell us what types/signs you would like to convert");
                        alertDialog.setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

//                                viewModel.setDefaultFreq();
                                startActivity(new Intent(getApplicationContext(), settingsActivity.class));

                                dialog.cancel();
                            }
                        });

                        alertDialog.setNegativeButton("use default types", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                alertDialog2.setTitle("Default Types");
                                alertDialog2.setMessage("Types currently selected: \n" + viewModel.getAllTypesOrdered(typesUpdated).toString());
                                alertDialog2.setPositiveButton("CHANGE TYPES ANYWAY", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

//                                viewModel.setDefaultFreq();
                                        startActivity(new Intent(getApplicationContext(), settingsActivity.class));

                                        dialog.cancel();
                                    }
                                });

                                alertDialog2.setNegativeButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert1 = alertDialog2.create();

                                alert1.show();
                            }
                        });

                        AlertDialog alert = alertDialog.create();

                        alert.show();
                    }

                    //otherwise - ask user whether to use his previous types
                    else {
                        alertDialog.setTitle("hi again!");
                        alertDialog.setMessage("use conversion types from previous session?");
                        alertDialog.setPositiveButton("YES, SHOW ME SELECTED TYPES", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                alertDialog2.setTitle("TYPES FROM PREVIOUS SESSION");
                                alertDialog2.setMessage("Types currently selected: \n" + viewModel.getAllTypesOrdered(typesUpdated).toString());
                                alertDialog2.setPositiveButton("CHANGE TYPES ANYWAY", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

//                                viewModel.setDefaultFreq();
                                        startActivity(new Intent(getApplicationContext(), settingsActivity.class));

                                        dialog.cancel();
                                    }
                                });

                                alertDialog2.setNegativeButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert1 = alertDialog2.create();

                                alert1.show();
                            }
                        });

                        alertDialog.setNegativeButton("USE DIFFERENT TYPES", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getApplicationContext(), settingsActivity.class));
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert = alertDialog.create();

                        alert.show();

                    }




                }
                if ( item.component1().equals("Write Us")) {

                }



            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {

            }
        });

        setupWindowAnimations();
        setupToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }



    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


}
