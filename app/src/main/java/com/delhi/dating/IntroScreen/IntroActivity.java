package com.delhi.dating.IntroScreen;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.delhi.dating.R;
import com.delhi.dating.ui.Activity.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    int fvalue = -1;
    Button btnGetStarted;
    Animation btnAnim ;
    TextView tvSkip;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // when this activity is about to be launch we need to check if its openened before or not

        fetchDataFromFirebase();

        if (restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(), LoginActivity.class );
            startActivity(mainActivity);
            finish();

        }
        setContentView(R.layout.activity_intro);
        // hide the action bar
//       getSupportActionBar().hide();

        // ini views
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.intro_button_animation);
    //    tvSkip = findViewById(R.id.tv_skip);

        // fill list screen

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Welcome to Delhi Dating","Date in your local Delhi area around you",R.drawable.dd3));
        mList.add(new ScreenItem("Discover to Perfect Match","Get ready to discover your ideal partner!",R.drawable.dd2));
        mList.add(new ScreenItem("LuvLink to Eternal Bonds","Meaningful connections start with great conversations",R.drawable.dd1));

        // setup viewpager
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // setup tablayout with viewpager

        tabIndicator.setupWithViewPager(screenPager);




        // next button click Listner

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                }

                if (position == mList.size()-1) { // when we rech to the last screen
                    // TODO : show the GETSTARTED Button and hide the indicator and the next button
                    loaddLastScreen();
                }



            }
        });

        // tablayout add change listener


        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1) {
                    loaddLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        // Get Started button click listener

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("ffff value", " "+fvalue);
               if (fvalue == 1){
                    Log.e("iiif","iiif");
                    ActivityCompat.requestPermissions(IntroActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                }
                else {
                    Log.e("eeelse","else");
                   Intent mainActivity = new Intent(getApplicationContext(), LoginActivity.class);
                   startActivity(mainActivity);
                   savePrefsData();
                   finish();
                }
            }
        });
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;
    }
    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
    //    tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted.setAnimation(btnAnim);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch user's location
                fetchUserLocation();
            } else {
                // Permission denied, show dialog or toast informing the user about the location requirement
                showLocationPermissionDeniedDialog();
            }
        }
    }
    private void fetchUserLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Get the last known location
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    // Check if the user's location is within the specific circle of the target region (Halifax)
                    if (isLocationInHalifax(latitude, longitude)) {
                        // User is in Halifax, proceed with sign up
                        Intent mainActivity = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(mainActivity);
                        savePrefsData();
                        finish();
                       // Toast.makeText(getApplicationContext(), "User is in Halifax", Toast.LENGTH_SHORT).show();
                      //  showSignUpActivity();
                    } else {
                        // User is not in Halifax, show dialog or toast informing the user about the restriction
                        showLocationRestrictionDialog();
                    }
                } else {
                    // Location not available, show dialog or toast informing the user about the location unavailability
                    showLocationUnavailableDialog();
                }
            } else {
                // Location permission not granted, show dialog or toast informing the user about the permission requirement
                showLocationPermissionDeniedDialog();
            }
        }
    }
    private boolean isLocationInHalifax(double latitude, double longitude) {
        double halifaxLatitude = 28.653625; // Latitude of Halifax city  44.644674, -63.594469
        double halifaxLongitude = 77.141284; // Longitude of Halifax city //delhi lat lng 28.653625   77.141284


        double radius = 25.0; // Radius of the circle in kilometers

        // Calculate the distance between the user's location and the center of the circle
        float[] distance = new float[1];
        Location.distanceBetween(latitude, longitude, halifaxLatitude, halifaxLongitude, distance);
        double distanceInKm = distance[0] / 1000.0; // Convert distance to kilometers

        // Check if the distance is within the radius of the circle
        return distanceInKm <= radius;
    }

    private void showLocationRestrictionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("This App is not available in your area")
                .setMessage("This app is only available for Delhi users.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showLocationPermissionDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Permission Denied")
                .setMessage("This app requires location permission to determine your eligibility. Please grant location permission to sign up.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showLocationUnavailableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Unavailable")
                .setMessage("Unable to retrieve your location. Please make sure location services are enabled on your device.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void fetchDataFromFirebase() {
        // Get a reference to the Firebase database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // Specify the path to the value you want to retrieve
        databaseReference = firebaseDatabase.getReference("loc");

        // Add a listener to fetch the value from the database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve the value from the dataSnapshot
                Integer value = dataSnapshot.getValue(Integer.class);

                fvalue = value;

                Log.e("vvvvvv"," "+value);

                // Check the value and perform actions based on the condition
                if (value != null && value == 0) {
                    // Value is 1, proceed with your logic here
                    // ...
                } else {
                    // Value is not 1, handle the condition as per your requirement
                    // ...
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur during the database operation
                // ...
            }
        });
    }

}
