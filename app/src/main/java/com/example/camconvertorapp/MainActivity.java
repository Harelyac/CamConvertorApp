package com.example.camconvertorapp;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.daimajia.androidanimations.library.Techniques;
import com.eftimoff.androipathview.PathView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import android.os.Handler;
import android.os.Looper;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.daimajia.androidanimations.library.YoYo;

import android.graphics.Typeface;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

//        PathView pathView= findViewById(R.id.pathView);
//
//        pathView.setSvgResource(R.raw.txtrecog);
//        pathView.getSequentialPathAnimator() .delay(500)
////                .duration(500)
//                .interpolator(new AccelerateDecelerateInterpolator()) .start();
//        pathView.useNaturalColors(); pathView.setFillAfter(true);

//
//        pathView.getSequentialPathAnimator()
//                .delay(100)
//                .duration(379)
//////                .listenerStart(new AnimationListenerStart())
//////                .listenerEnd(new AnimationListenerEnd())
//                .interpolator(new AccelerateDecelerateInterpolator())
//                .start();



        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setBackgroundResource(R.color.theme_yellow_accent);



        TextView text = findViewById(R.id.title1);


//        animate text

//        YoYo.with(Techniques.Tada)
//
//                .duration(1200)
//
//                .repeat(YoYo.INFINITE)
//
//                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
//
//                .interpolate(new AccelerateDecelerateInterpolator())
//
//
//
//                .playOn(text);

        //animate fab

        YoYo.with(Techniques.Tada)

                .duration(1200)

                .repeat(YoYo.INFINITE)

                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)

                .interpolate(new AccelerateDecelerateInterpolator())



                .playOn(fab);




//        PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
//        pulsator.start();

//        //todo here start the activity of the camera and text recognizer -->
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        LottieAnimationView animate = findViewById(R.id.av_from_code);
        KonfettiView konfettiView = findViewById(R.id.viewKonfetti);
        BubblePicker bubblePicker = findViewById(R.id.picker);
//        animate.setAnimation("drink.json");
//        animate.playAnimation();
//        animate.loop(true);




//        konfettiView.build()
//                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
//                .setDirection(0.0, 359.0)
//                .setSpeed(1f, 5f)
//                .setFadeOutEnabled(false)
//                .setTimeToLive(20000000L)
//                .addShapes(Shape.RECT, Shape.CIRCLE)
//                .addSizes(new Size(15, 7))
//                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
//                .stream(300, 500000L);

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
//                item.setTypeface(mediumTypeface);
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
