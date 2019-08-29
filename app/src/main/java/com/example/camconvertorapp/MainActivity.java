package com.example.camconvertorapp;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;


import com.daimajia.androidanimations.library.Techniques;
import com.eftimoff.androipathview.PathView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import nl.dionsegijn.konfetti.KonfettiView;

import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.daimajia.androidanimations.library.YoYo;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

                if ( item.component1().equals("settings")){
                    startActivity(new Intent(getApplicationContext(), settingsActivity.class));
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
