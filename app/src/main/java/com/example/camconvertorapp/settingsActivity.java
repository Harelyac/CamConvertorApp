package com.example.camconvertorapp;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Triple;

public class settingsActivity extends FragmentActivity implements EffectAdapter.AdapterClickCallback {

    private RecyclerView mListView;
    private EffectAdapter mAdapter;

    private Spinner mTarget;
    private Spinner mTarget2;
    private YoYo.YoYoString rope;

    int flags[] = {R.drawable.weight_icon, R.drawable.currency_icon, R.drawable.temperature_icon, R.drawable.length_icon, R.drawable.volume_icon, R.drawable.pressure_icon,R.drawable.time_icon,R.drawable.speed,R.drawable.angle_icon};
    String typesForConversionList[] = {"Weight", "Currency", "Temperature", "Length", "volume" , "pressure", "time", "speed", "angle" };
    public AppDatabase db;
    public TheViewModel viewModel;
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


        // start with initializing local App DB
        db = AppDatabase.getDatabase(this);

        viewModel = ViewModelProviders.of(this).get(TheViewModel.class);


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
        layoutManager = new LinearLayoutManager(this ,RecyclerView.VERTICAL,
                false);
        mListView.setLayoutManager(layoutManager);

        // specify an adapter

        EffectAdapter customAdapter = new EffectAdapter(typesForConversionList, flags);
        mListView.setAdapter(customAdapter);
        customAdapter.callback = this;






        Button but = findViewById(R.id.btnSubmit);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Pair<String, String>> typesUpdated =  viewModel.getAllTypesStored();

                Snackbar.make(v, "Types currently selected: \n " + getAllTypesOrdered(typesUpdated).toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();



//                Toast.makeText(settingsActivity.this,
//                        "Types currently selected: \n " + getAllTypesOrdered(typesUpdated).toString(),
//                        Toast.LENGTH_LONG).show();

                //now look for all frequencies which have not been initialized explicitly and set for them DEFAULT values:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(settingsActivity.this);

                Boolean hasNotInit = viewModel.checkIfNotAllTypesSelectd();
                if(hasNotInit == false && !viewModel.frequenciesMap.isEmpty())
                {
                    //the user didnt define all types conversions - notice him:
                    alertDialog.setTitle("popup message");
                    alertDialog.setMessage("you did not set all conversion types");
                    alertDialog.setPositiveButton("USE DEFAULT TYPES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            viewModel.setDefaultFreq();
                            dialog.cancel();

                        }
                    });

                    alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert = alertDialog.create();

                    alert.show();
                }
            }
        });


    }

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

    public void setTypesSelected(final String type, TextView textView1, TextView textView2, final Frequency newFrequency){
        /** this function get the relevant frequencies which were set by the user via Spinner1 and Spinner2
         * and save it in local Room DB**/
        int array = 0;

        if(type.equals("Currency")){
            array = R.array.currencies;
            newFrequency.type = "Currency";

        }

        if(type.equals("Weight")){
            array = R.array.weights;
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

        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(getApplicationContext(),array, android.R.layout.simple_spinner_item);

//        ArrayAdapter adapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,array){
//            @Override
//            public boolean isEnabled(int position){
//                if(position == 0)
//                {
//                    // Disable the first item from Spinner
//                    // First item will be use for hint
//                    return false;
//                }
//                else
//                {
//                    return true;
//                }
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view;
//                if(position == 0){
//                    // Set the hint text color gray
//                    tv.setTextColor(Color.GRAY);
//                }
//                else {
//                    tv.setTextColor(Color.BLACK);
//                }
//                return view;
//            }
//        };
//
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        mTarget.setAdapter(adapter1);
//


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mTarget.setAdapter(adapter);



        //stop the animation
        if (rope != null) {
            rope.stop(true);
        }
//        if (rope != null) {
//            rope.stop(true);
//        }

        mTarget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //first - delete the previous type was stored
//                deleteAll(type);

                String currency_selected_source = parent.getItemAtPosition(position).toString();
                if(currency_selected_source.equals("Select an Item...") || currency_selected_source.equals("update")){
                    Toast.makeText(settingsActivity.this,"Select source type! ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Toast.makeText(settingsActivity.this, "Selected source type: " + currency_selected_source, Toast.LENGTH_SHORT).show();
                    //save in ROOM sqlite as default currency TODO
                    newFrequency.source = currency_selected_source;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(settingsActivity.this,"Select source type! ", Toast.LENGTH_SHORT).show();

            }

        });

        //now deal with target currency and save on room

        textView2.setText("choose the target " + type + " type:");//TODO maybe change instruction

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),array, android.R.layout.simple_spinner_item);


//        ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,array){
//            @Override
//            public boolean isEnabled(int position){
//                if(position == 0)
//                {
//                    // Disable the first item from Spinner
//                    // First item will be use for hint
//                    return false;
//                }
//                else
//                {
//                    return true;
//                }
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view;
//                if(position == 0){
//                    // Set the hint text color gray
//                    tv.setTextColor(Color.GRAY);
//                }
//                else {
//                    tv.setTextColor(Color.BLACK);
//                }
//                return view;
//            }
//        };
//
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        mTarget2.setAdapter(adapter2);



        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mTarget2.setAdapter(adapter2);

        mTarget2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //first - delete the previous type was stored
//                deleteMe(type);
                String currency_selected_target = parent.getItemAtPosition(position).toString();
                if(currency_selected_target.equals("Select an Item...") || currency_selected_target.equals("update")){
                    Toast.makeText(settingsActivity.this,"Select target type! ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Toast.makeText(settingsActivity.this, "Selected target type: " + currency_selected_target, Toast.LENGTH_SHORT).show();
                    //save in ROOM sqlite as default target currency TODO
                    newFrequency.target = currency_selected_target;
                    if (newFrequency.source != null && newFrequency.target != null)
                        insertToLocalDB(newFrequency);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(settingsActivity.this,"Select target type! ", Toast.LENGTH_SHORT).show();

            }

        });



    }


    /***these methods manage the insertion and deletion from room db**/
    void insertToLocalDB(Frequency newFrequency) {
        new insertLocalAsyncTask(db.freqDao()).execute(newFrequency);
    }

//    void deleteMe(String type){
//        new deleteALLAsyncTask(db.freqDao()).execute(type);
//    }

    void deleteAll(){
        new deleteALLAsyncTask(db.freqDao()).execute();
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (hasFocus) {

            rope = YoYo.with(Techniques.Tada).duration(1000).playOn(mTarget);// after start,just click mTarget view, rope is not init
            rope = YoYo.with(Techniques.Tada).duration(1000).playOn(mTarget2);// after start,just click mTarget view, rope is not init
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

           // startActivity(new Intent(this, editActivity.class));

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
        rope = YoYo.with(technique.Tada)
                .duration(1200)
                .repeat(2)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(mTarget);



        rope = YoYo.with(technique.Tada)
                .duration(1200)
                .repeat(2)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(mTarget2);


        if (typesForConversionList[(int) id].equals("Currency")) {
            setTypesSelected("Currency", textView, textView2, newFrequency );
        }
        if (typesForConversionList[(int) id].equals("Weight")) {
            setTypesSelected("Weight", textView, textView2, newFrequency );
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


    public String getTableAsString(AppDatabase db, String tableName) {
        Log.d("DBtable", "getTableAsString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.query("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }
        Log.d("DBTABLE ***************", tableString);

        return tableString;
    }

}