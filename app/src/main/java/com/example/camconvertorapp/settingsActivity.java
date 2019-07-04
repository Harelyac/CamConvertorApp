package com.example.camconvertorapp;

import android.animation.Animator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.lang.reflect.Array;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class settingsActivity extends FragmentActivity implements EffectAdapter.AdapterClickCallback {

    private RecyclerView mListView;
    private EffectAdapter mAdapter;

    private Spinner mTarget;
    private Spinner mTarget2;
    private YoYo.YoYoString rope;

    int flags[] = {R.drawable.weight_icon, R.drawable.currency_icon, R.drawable.temperature_icon, R.drawable.length_icon, R.drawable.volume_icon, R.drawable.pressure_icon,R.drawable.time_icon,R.drawable.speed,R.drawable.angle_icon};
    String typesForConversionList[] = {"Weight", "Currency", "Temperature", "Length", "volume" , "pressure", "time", "speed", "angle" };
    public AppDatabase db;
    public FrequenciesViewModel viewModel;
    private RecyclerView.LayoutManager layoutManager;
    TextView textView ;
    TextView textView2 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_ac);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        //if its the first time the user come in - help pop up

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(settingsActivity.this);
        alertDialog.setTitle("Hi! Here we Go!");
        alertDialog.setMessage("select conversion types from the list\n then choose the source and target sign");
        alertDialog.setPositiveButton("Got It", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });

        alertDialog.setNegativeButton("Help", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO send the user to Help Activity for further explanations and use cases
                dialog.cancel();
            }
        });

        AlertDialog alert = alertDialog.create();

        alert.show();




        //start with initializing local App DB -->
        db = AppDatabase.getDatabase(this);

        viewModel = ViewModelProviders.of(this).get(FrequenciesViewModel.class);


        //noticing the viewModel all the frequencies which have been already stored
        db.freqDao().getAll().observe(this, new Observer<List<Frequency>>() {
            @Override
            public void onChanged(@Nullable final List<Frequency> frequencies) {

                viewModel.setFrequenciesMap(frequencies);
            }
        });

        mListView = (RecyclerView) findViewById(R.id.list_items);


        // use spinners for choosing signs
        mTarget = (Spinner) findViewById(R.id.spinner1);
        mTarget2 = (Spinner) findViewById(R.id.spinner2);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mListView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)

        EffectAdapter customAdapter = new EffectAdapter(typesForConversionList, flags);
        mListView.setAdapter(customAdapter);




        //now look for all frequencies which have not been initialized explicitly and set for them DEFAULT values:
        Boolean hasNotInit = viewModel.setDefaultFreq();
        if(hasNotInit == true)
        {
            //the user didnt define all types conversions - notice him:
            alertDialog.setTitle("popup message");
            alertDialog.setMessage("you did not set all conversion types");
            alertDialog.setPositiveButton("USE DEFAULT TYPES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                }
            });

            alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

             alert = alertDialog.create();

            alert.show();


        }

    }

    public void setTypesSelected(final String type, TextView textView1, TextView textView2, final Frequency newFrequency){
        /** this function get the relevant frequencies which were set by the user via Spinner1 and Spinner2
         * and save it in local Room DB**/
        int array = 0;

        if(type.equals("Currency")){
            array = R.array.currencies;
            newFrequency.type = "Currency";

        }

        if(type.equals("Weight")){
            array = R.array.currencies;
            newFrequency.type = "Weight";

        }
        if(type.equals("Temperature")){
            array = R.array.temperature;
            newFrequency.type = "Temperature";

        }
        if(type.equals("Length")){
            array = R.array.length;
            newFrequency.type = "Length";


        }
        if(type.equals("volume")){
            array = R.array.volume;
            newFrequency.type = "volume";

        }
        if(type.equals("pressure")){
            array = R.array.pressure;
            newFrequency.type = "pressure";

        }
        if(type.equals("time")){
            array = R.array.time;
            newFrequency.type = "time";
        }

        if(type.equals("speed")){
            array = R.array.speed;
            newFrequency.type = "speed";
        }
        if(type.equals("angle")){
            array = R.array.angle;
            newFrequency.type = "angle";

        }
        //select source currency and save on room
        textView1.setText("choose the source " + type + " type:" );//TODO maybe change instruction

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mTarget.setAdapter(adapter);

        mTarget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //first - delete the previous type was stored
//                deleteAll(type);
                String currency_selected_source = parent.getItemAtPosition(position).toString();
                Toast.makeText(settingsActivity.this, currency_selected_source, Toast.LENGTH_SHORT).show();
                //save in ROOM sqlite as default currency TODO
                newFrequency.source = currency_selected_source;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        //now deal with target currency and save on room

        textView2.setText("choose the target " + type + " type:");//TODO maybe change instruction

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mTarget2.setAdapter(adapter2);

        mTarget2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //first - delete the previous type was stored
//                deleteMe(type);
                String currency_selected_target = parent.getItemAtPosition(position).toString();
                Toast.makeText(settingsActivity.this, currency_selected_target, Toast.LENGTH_SHORT).show();
                //save in ROOM sqlite as default target currency TODO
                newFrequency.target = currency_selected_target;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        insertToLocalDB(newFrequency);

    }


    /***these methods manage the insertion and deletion from room db**/
    void insertToLocalDB(Frequency newFrequency) {
        new insertLocalAsyncTask(db.freqDao()).execute(newFrequency);
    }

//    void deleteMe(String type){
//        new deleteALLAsyncTask(db.freqDao()).execute(type);
//    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (hasFocus) {

            rope = YoYo.with(Techniques.FadeIn).duration(1000).playOn(mTarget);// after start,just click mTarget view, rope is not init
        }
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

        if (id == R.id.action_settings) {

            startActivity(new Intent(this, editActivity.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void AdapterClickCallback(String[] freq, int id,  View view) {

        Frequency newFrequency = new Frequency();
        //animate both spinners for user to notice there are fields to choose at top of screen!!!!
        if (rope != null) {
            rope.stop(true);
        }
        Techniques technique = (Techniques) view.getTag();
        rope = YoYo.with(technique)
                .duration(1200)
                .repeat(YoYo.INFINITE)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(mTarget);

        if (rope != null) {
            rope.stop(true);
        }

        rope = YoYo.with(technique)
                .duration(1200)
                .repeat(YoYo.INFINITE)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(mTarget2);


        if (typesForConversionList[(int) id].equals("Currency")) {
            setTypesSelected("Currency", textView, textView2, newFrequency );
        }
        if(typesForConversionList[(int) id].equals("Temperature")) {

            setTypesSelected("Temperature", textView, textView2, newFrequency );

        }
        if(typesForConversionList[(int) id].equals("Length")) {

            setTypesSelected("Length", textView, textView2, newFrequency );

        }
        if(typesForConversionList[(int) id].equals("volume")) {

            setTypesSelected("volume", textView, textView2, newFrequency );

        }
        if(typesForConversionList[(int) id].equals("pressure")) {

            setTypesSelected("pressure", textView, textView2, newFrequency );

        }
        if(typesForConversionList[(int) id].equals("time")) {

            setTypesSelected("time", textView, textView2, newFrequency );

        }
        if(typesForConversionList[(int) id].equals("speed")) {

            setTypesSelected("speed", textView, textView2, newFrequency );

        }
        if(typesForConversionList[(int) id].equals("angle")) {

            setTypesSelected("angle", textView, textView2, newFrequency );

        }
    }
}