package com.example.camconvertorapp;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.camconvertorapp.locationModule.IpApi;
import com.example.camconvertorapp.locationModule.Response;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Thread.sleep;

public class settingsActivity extends FragmentActivity implements EffectAdapter.AdapterClickCallback {

    private RecyclerView mListView;
    private EffectAdapter mAdapter;

    private Spinner mTarget;
    private Spinner mTarget2;
    private YoYo.YoYoString rope;

    int flags[] = {R.drawable.currency_icon, R.drawable.weight_icon, R.drawable.temperature_icon, R.drawable.length_icon, R.drawable.volume_icon};
    String typesForConversionList[] = {"Currency", "Weight" , "Length", "Volume" };
    public  AppDatabase db;
    public ViewModel viewModel;
    private RecyclerView.LayoutManager layoutManager;
    TextView textView ;
    TextView textView2 ;
    public Response IpResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_ac);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        // start with initializing local App DB
        db = AppDatabase.getDatabase(this);

        // init view model
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        // noticing the viewModel all the frequencies which have been already stored
        db.freqDao().getAll().observe(this, new Observer<List<Frequency>>() {
            @Override
            public void onChanged(@Nullable final List<Frequency> frequencies) {

                viewModel.setFrequenciesMap(frequencies);
            }
        });

        // if its the first time the user come in - help pop up
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(settingsActivity.this);
        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(settingsActivity.this);
        alertDialog.setTitle("Hi! Here we Go!");
        alertDialog.setMessage("Select conversion types from the list\nthen choose the source and target sign");
        alertDialog.setPositiveButton("Got It", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });



        if(!ViewModel.frequenciesMap.isEmpty()) {

            final HashMap<String, Pair<String, String>> typesUpdated =  viewModel.getAllTypesStored();

            alertDialog.setNegativeButton("Show types from previous session", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    alertDialog2.setTitle("TYPES FROM PREVIOUS SESSION");
                    alertDialog2.setMessage("Types currently selected: \n" + viewModel.getAllTypesOrdered(typesUpdated).toString());
                    alertDialog2.setPositiveButton("CHANGE TYPES ANYWAY", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    alertDialog2.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert1 = alertDialog2.create();

                    alert1.show();
                }
            });
        }

        AlertDialog alert = alertDialog.create();
        alert.show();

        mListView = (RecyclerView) findViewById(R.id.list_items);

        // use spinners for choosing signs
        mTarget = (Spinner) findViewById(R.id.spinner1);
        mTarget2 = (Spinner) findViewById(R.id.spinner2);

        // set default values on spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.currencies, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.currencies, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTarget.setAdapter(adapter);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTarget2.setAdapter(adapter2);

        mTarget.setSelection(0);
        mTarget2.setSelection(0);

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


        // restful api for getting current location of phone for default types initialization
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
                setDefaultFreq(IpResponse.timezone, IpResponse.country);
                Toast.makeText(getApplicationContext(),"Succeeded to get response from server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed to get response from server", Toast.LENGTH_SHORT).show();
                Log.e("", "Exception: "+Log.getStackTraceString(t));
            }
        });



        // when submit is clicked
        Button but = findViewById(R.id.btnSubmit);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(settingsActivity.this);

                // check if room db has not be initialized
                boolean hasNotInit = viewModel.checkIfNotAllTypesSelected();

                if(hasNotInit)
                {
                    //the user didnt define all types conversions - notice him:
                    alertDialog.setTitle("Hi mate!");
                    alertDialog.setMessage("you did not set all conversion types");
                    alertDialog.setPositiveButton("ALRIGHT", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // viewModel.setDefaultFreq();
                            dialog.cancel();
                        }
                    });


                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }
                else
                {
                    Toast toast = new Toast(getApplicationContext());
                    TextView tv = new TextView(settingsActivity.this);
                    //tv.setBackgroundColor(Color.BLUE);
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(25);
                    Typeface typeface = Typeface.create("serif", Typeface.BOLD);
                    tv.setTypeface(typeface);
                    tv.setPadding(10,10,10,10);
                    tv.setText("Added Successfully!");
                    toast.setView(tv);
                    toast.show();

                }
            }
        });
    }


    // set default conversion types source unit based on location - metric or imperial base unit types
    // and currency of country / region only on conversion types that was not set yet

    public void setDefaultFreq(String timezone, String country){
        String [] parts = timezone.split("/",0);

        boolean isMetric = true;

        String value = country;

        // only in Europe get continent
        if (parts[0].equals("Europe"))
        {
            value = parts[0];
        }

        if (country.equals("GB"))
        {
            value = country;
        }

        if (value.equals("US"))
        {
            isMetric = false;
        }

        for(String type: typesForConversionList)
        {
            if(ViewModel.frequenciesMap == null)
            {
                return;
            }
            if (!ViewModel.frequenciesMap.containsKey(type))
            {
                Frequency newFreq = new Frequency();
                newFreq.target = "Select an Item...";


                if (isMetric)
                {
                    if(type.equals("Currency"))
                    {
                        newFreq.type = "Currency";
                        newFreq.source = getCurrency(value);
//                        frequenciesMap.put(type,new Pair<String, String>(getCurrency(value), "Select an Item..."));
                    }

                    if(type.equals("Weight")) {
                        newFreq.type = "Weight";
                        newFreq.source = "kilogram (kg)";
//                        frequenciesMap.put(type,new Pair<String, String>("kilogram (kg)", "Select an Item..."));
                    }
                    if(type.equals("Length")) {
                        newFreq.type = "Length";
                        newFreq.source = "meter (m)";
//                          frequenciesMap.put(type,new Pair<String, String>("meter (m)", "Select an Item..."));
                    }

                    if(type.equals("Volume")) {
                        newFreq.type = "Volume";
                        newFreq.source = "Liter (L)";
//                          frequenciesMap.put(type,new Pair<String, String>("Liter (L)", "Select an Item..."));
                    }


                }
                else
                {
                    if(type.equals("Currency")) {
                        newFreq.type = "Currency";

                        newFreq.source = getCurrency(value);
//                        frequenciesMap.put(type, new Pair<String, String>(getCurrency(value), "Select an Item..."));
                    }

                    if(type.equals("Weight")) {
                        newFreq.type = "Weight";

                        newFreq.source = "pound (lb)";
//                      frequenciesMap.put(type, new Pair<String, String>("pound (lb)", "Select an Item..."));
                    }

                    if(type.equals("Length")) {
                        newFreq.type = "Length";
                        newFreq.source = "Yard (yd)";
//                        requenciesMap.put(type, new Pair<String, String>("Yard (yd)", "Select an Item..."));
                    }

                    if(type.equals("Volume")) {
                        newFreq.type = "Volume";

                        newFreq.source = "gallon (gal)";
//                        frequenciesMap.put(type, new Pair<String, String>("gallon (gal)", "Select an Item..."));
                    }
                }

                insertToLocalDB(newFreq);

            }
        }
    }

    // get currency by given country name
    public String getCurrency(String country)
    {
        switch(country)
        {
            case "IL":
            {
                return "NIS (₪)";
            }
            case "US":
            {
                return "Dollar ($)";
            }
            case "EU":
            {
                return "Euro (€)";
            }
            case "GB":
            {
                return "Pound (£)";
            }
            case "JP":
            {
                return "Yen (¥)";
            }
            case "RU":
            {
                return "RUB (\u20BD)";
            }
            default:
                return "";
        }
    }

    // create a frequency after the user has pressed the type and then put
    public void setTypesSelected(final String type, TextView textView1, TextView textView2, final Frequency newFrequency) {
        int array = 0;

        if (type.equals("Currency")) {
            array = R.array.currencies;
            newFrequency.type = "Currency";
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), array, android.R.layout.simple_spinner_item);
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(), array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mTarget.setAdapter(adapter);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mTarget2.setAdapter(adapter2);
            if (ViewModel.frequenciesMap.containsKey("Currency")) {
                int spinnerPosition = adapter.getPosition(ViewModel.getSourceByFrequencyType("Currency"));
                mTarget.setSelection(spinnerPosition);

                int spinnerPosition2 = adapter.getPosition(ViewModel.getTargetByFrequencyType("Currency"));
                mTarget2.setSelection(spinnerPosition2);
            }

        }

        if (type.equals("Weight")) {
            array = R.array.weights;
            newFrequency.type = "Weight";

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), array, android.R.layout.simple_spinner_item);
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(), array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mTarget.setAdapter(adapter);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mTarget2.setAdapter(adapter2);
            if (ViewModel.frequenciesMap.containsKey("Weight")) {
                int spinnerPosition = adapter.getPosition(ViewModel.getSourceByFrequencyType("Weight"));
                mTarget.setSelection(spinnerPosition);

                int spinnerPosition2 = adapter.getPosition(ViewModel.getTargetByFrequencyType("Weight"));
                mTarget2.setSelection(spinnerPosition2);
            }

        }
        if (type.equals("Length")) {
            array = R.array.length;
            newFrequency.type = "Length";

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), array, android.R.layout.simple_spinner_item);
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(), array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mTarget.setAdapter(adapter);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mTarget2.setAdapter(adapter2);
            if (ViewModel.frequenciesMap.containsKey("Length")) {
                int spinnerPosition = adapter.getPosition(ViewModel.getSourceByFrequencyType("Length"));
                mTarget.setSelection(spinnerPosition);

                int spinnerPosition2 = adapter.getPosition(ViewModel.getTargetByFrequencyType("Length"));
                mTarget2.setSelection(spinnerPosition2);
            }


        }
        if (type.equals("Volume")) {
            array = R.array.volume;
            newFrequency.type = "Volume";

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), array, android.R.layout.simple_spinner_item);
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(), array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mTarget.setAdapter(adapter);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mTarget2.setAdapter(adapter2);
            if (ViewModel.frequenciesMap.containsKey("Volume")) {
                int spinnerPosition = adapter.getPosition(ViewModel.getSourceByFrequencyType("Volume"));
                mTarget.setSelection(spinnerPosition);

                int spinnerPosition2 = adapter.getPosition(ViewModel.getTargetByFrequencyType("Volume"));
                mTarget2.setSelection(spinnerPosition2);
            }

        }



        // now choosing the signs from Spinners
        mTarget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String currency_selected_source = parent.getItemAtPosition(position).toString();
                    Toast.makeText(settingsActivity.this, "Selected source type: " + currency_selected_source, Toast.LENGTH_SHORT).show();
                    newFrequency.source = currency_selected_source;
                    getTableAsString(db,"Frequency");
                    insertToLocalDB(newFrequency);
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(settingsActivity.this,"Select source type! ", Toast.LENGTH_SHORT).show();

            }

        });

        //now deal with target currency and save on room

        textView2.setText("choose the target " + type + " type:");//TODO maybe change instruction


        mTarget2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //first - delete the previous type was stored
//                deleteByType(type);
                String currency_selected_target = parent.getItemAtPosition(position).toString();
                Toast.makeText(settingsActivity.this, "Selected target type: " + currency_selected_target, Toast.LENGTH_SHORT).show();
                newFrequency.target = currency_selected_target;
                insertToLocalDB(newFrequency);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(settingsActivity.this,"Select target type! ", Toast.LENGTH_SHORT).show();
            }

        });

    }


    /**these methods manage the insertion and deletion from room db**/
    public void insertToLocalDB(Frequency newFrequency) {
        new insertLocalAsyncTask(db.freqDao()).execute(newFrequency);
    }

    //debug purposes
    void deleteAll(){
        new deleteALLAsyncTask(db.freqDao()).execute();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
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


    // set old (from room db) unit types on spinners after the type was selected by user
    @Override
    public void AdapterClickCallback(String[] freq, int id,  View view) {

        Frequency newFrequency = new Frequency();
        // animate both spinners for user to notice there are fields to choose at top of screen!

        if (rope != null) {
            rope.stop(true);
        }

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
        if(typesForConversionList[(int) id].equals("Volume")) {

            setTypesSelected("Volume", textView, textView2, newFrequency );

        }

    }

    // debug purposes - check current types stored on room db
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