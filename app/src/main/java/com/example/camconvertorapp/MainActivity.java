package com.example.camconvertorapp;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;

import com.eftimoff.androipathview.PathView;

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

import android.transition.Slide;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.michaldrabik.tapbarmenulib.TapBarMenu;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    public ViewModel viewModel;
    public AppDatabase db;


    private static final String TEXT_DETECTION = "Text Detection";
    private static final String BARCODE_DETECTION = "Barcode Detection";

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



        final TapBarMenu tapBarMenu = findViewById(R.id.tapBarMenu);

        tapBarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapBarMenu.toggle();
            }
        });

        final ImageView convertor = findViewById(R.id.item1);
        final ImageView barcode = findViewById(R.id.item2);

        convertor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if room db has not be initialized
                boolean hasNotInit = viewModel.checkIfNotAllTypesSelected();

                if (hasNotInit) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    //the user didnt define all types conversions - notice him:
                    alertDialog.setTitle("Hi mate!");
                    alertDialog.setMessage("you did not set all conversion types");
                    alertDialog.setPositiveButton("ALRIGHT", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // viewModel.setDefaultFreq();
                            startActivity(new Intent(MainActivity.this, settingsActivity.class));
                        }
                    });

                    AlertDialog alert1 = alertDialog.create();
                    alert1.show();
                }
                else
                {
                    cameraActivity.model = TEXT_DETECTION;
                    startActivity(new Intent(MainActivity.this, cameraActivity.class));
                }

            }
        });

        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraActivity.model = BARCODE_DETECTION;
                startActivity(new Intent(MainActivity.this, cameraActivity.class));
            }
        });





        // start of animations
        Toolbar toolbar = findViewById(R.id.toolbar);
        PathView pathView= findViewById(R.id.path);

        pathView.getSequentialPathAnimator()
                .delay(100)
                .interpolator(new AccelerateDecelerateInterpolator())
                .start();

        setSupportActionBar(toolbar);

        BubblePicker bubblePicker = findViewById(R.id.picker);

        final String[] titles = getResources().getStringArray(R.array.title);
        final TypedArray colors = getResources().obtainTypedArray(R.array.colors);
        final TypedArray images = getResources().obtainTypedArray(R.array.images);

        bubblePicker.setAdapter(new BubblePickerAdapter()
        {
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

                item.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_blue_dark));
                item.setTextSize(60);
                item.setBackgroundImage(ContextCompat.getDrawable(MainActivity.this, images.getResourceId(position, 0)));

                Typeface typeface = Typeface.create("serif", Typeface.BOLD);
                item.setTypeface(typeface);
                return item;
            }
        });

        bubblePicker.setCenterImmediately(true);
        bubblePicker.setBubbleSize(100);

        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {
                if ( item.component1().equals("Settings")){
                    startActivity(new Intent(getApplicationContext(), settingsActivity.class));
                }
                if ( item.component1().equals("Chosen Types")) {
                    final HashMap<String, Pair<String, String>> typesUpdated = viewModel.getAllTypesStored();

                    // if the view model is not empty
                    if (!typesUpdated.isEmpty())
                    {
                        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
                        alertDialog2.setTitle("TYPES FROM PREVIOUS SESSION");
                        alertDialog2.setMessage("Types currently selected: \n" + viewModel.getAllTypesOrdered(typesUpdated).toString());

                        alertDialog2.setPositiveButton("CHANGE TYPES ANYWAY", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getApplicationContext(), settingsActivity.class));
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
                    else
                    {
                        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
                        alertDialog2.setTitle("No types were selected yet!");
                        alertDialog2.setMessage("You can go and select them now \n");
                        alertDialog2.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getApplicationContext(), settingsActivity.class));
                                dialog.cancel();
                            }
                        });

                        alertDialog2.setNegativeButton("LATER", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert1 = alertDialog2.create();
                        alert1.show();
                    }

                }

                if (item.component1().equals("Write Us"))
                {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Hi there!");
                    alert.setMessage("Got a question? we'd love to hear from you. Send us a message and we'll respond ASAP");

                    LinearLayout layout = new LinearLayout(MainActivity.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final EditText titleBox = new EditText(MainActivity.this);
                    titleBox.setHint("Title");
                    layout.addView(titleBox);

                    final EditText descriptionBox = new EditText(MainActivity.this);
                    descriptionBox.setHint("Description");
                    layout.addView(descriptionBox);

                    alert.setView(layout);
                    alert.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // sending the mail from user to harel's mail address
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("message/rfc822");
                            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"harelyac@gmail.com"});
                            i.putExtra(Intent.EXTRA_SUBJECT, titleBox.getText());
                            i.putExtra(Intent.EXTRA_TEXT   , descriptionBox.getText());
                            try {
                                startActivity(Intent.createChooser(i, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    alert.create();
                    alert.show();
                }

                if (item.component1().equals("About Us"))
                {
                    final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
                    alertDialog2.setTitle("Hi There!");
                    alertDialog2.setMessage("A bit info about the developers who build this app: \n \n"
                    + "Harel Yacovian - third year student in CS at the Hebrew University\n \n" +
                                            "Daniel Hazan - Junior Full Stack Developer at Freelance");


                    alertDialog2.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert1 = alertDialog2.create();
                    alert1.show();
                }

                if (item.component1().equals("Help"))
                {
                    final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
                    alertDialog2.setTitle("Hi There!");
                    alertDialog2.setMessage("Wonder How to use this app? \n" +
                    "Hopefully you are holding any product attached with Numerical/Barcode \n\n" +
                    "1.First, decide what function you want to use - " +
                            "our Barcode Scanner or Price Convertor \n \n" +
                    "2.If you use the Convertor (left side) - firstly, you may need to go to " +
                            "'Settings' and fill in all the relevant fields; e.g , if you want to convert price from Euro to" +
                            " Dollar - then select in the 'Currency' field the source and target signs." +
                            "\n Then click  on 'Submit' button and your selection will be saved for later use \n \n" +
                    "3.Now you are ready to convert the types you've selected \n \n" +
                    "--------------------------------------------- \n \n" +
                    "4.If you use the Barcode reader (right side) - just look for the barcode " +
                            "on the item that you want to detect, if the items exist on google good chance you will get an image and its \n" +
                                    "current price on market");


                    alertDialog2.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert1 = alertDialog2.create();
                    alert1.show();
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


    // not used at the moment
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


}
