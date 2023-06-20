package com.naeemdev.tinder.ui.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.naeemdev.tinder.R;
import com.naeemdev.tinder.ui.Fragments.DatePicker_Fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String string_city;
    String string_state;
    String string_country;
    String string_location;
    String stringLatitude;
    String stringLongitude;
    String stringLooking;
    LocationManager locationManager;
    ProgressDialog dialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private Button btnRegisterPageRegister;
    private Button btnRegisterPageLogin;
    private EditText editTextRegisterName;
    private EditText editTextRegisterEmail;
    private EditText editTextRegisterPassword;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGender;
    private TextView textViewRegisterBirthday;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private TextView termsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        termsTextView = findViewById(R.id.termsTextView);
        btnRegisterPageRegister = findViewById(R.id.btnRegisterPageRegister);
        btnRegisterPageLogin = findViewById(R.id.btnRegisterPageLogin);

        editTextRegisterName = findViewById(R.id.editTextRegisterName);
        editTextRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);

        textViewRegisterBirthday = findViewById(R.id.textViewRegisterBirthday);

        radioGroupRegisterGender = findViewById(R.id.radioGroupRegisterGender);


        dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationPremissionCheck();
        GooglePlayServiceCheck();
        GPSLocationServiceCheck();

        btnRegisterPageLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        textViewRegisterBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePicker_Fragments();
                datePicker.show(getSupportFragmentManager(), "Date Picker");

            }
        });

        btnRegisterPageRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radioButtonRegisterGender != null) {
                    if (string_city != null && !string_city.equals("city") &&
                            string_state != null && !string_state.equals("state") &&
                            string_country != null && !string_country.equals("country")) {


                        final String stringName = editTextRegisterName.getText().toString();
                        final String stringEmail = editTextRegisterEmail.getText().toString();
                        final String stringPassword = editTextRegisterPassword.getText().toString();
                        final String stringGender = radioButtonRegisterGender.getText().toString();
                        final String stringBirthday = textViewRegisterBirthday.getText().toString();

                        if (stringGender.equals("Male")) {
                            stringLooking = "Woman";
                        } else {
                            stringLooking = "Man";
                        }

                        if (!TextUtils.isEmpty(stringName) &&
                                !TextUtils.isEmpty(stringEmail) &&
                                !TextUtils.isEmpty(stringPassword) &&
                                !TextUtils.isEmpty(stringGender) &&
                                !TextUtils.isEmpty(stringBirthday)) {

                            dialog.show();


                            firebaseAuth.createUserWithEmailAndPassword(stringEmail, stringPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        String currentUser = firebaseUser.getUid();

                                        Map<String, Object> userProfile = new HashMap<>();
                                        userProfile.put("user_uid", currentUser);
                                        userProfile.put("user_email", stringEmail);
                                        userProfile.put("user_epass", stringPassword);
                                        userProfile.put("user_name", stringName);
                                        userProfile.put("user_gender", stringGender);
                                        userProfile.put("user_birthday", stringBirthday);
                                        userProfile.put("user_birthage", AgeUser(stringBirthday));
                                        userProfile.put("user_city", string_city);
                                        userProfile.put("user_state", string_state);
                                        userProfile.put("user_country", string_country);
                                        userProfile.put("user_location", string_location);
                                        userProfile.put("user_thumb", "thumb");
                                        userProfile.put("user_image", "image");
                                        userProfile.put("user_cover", "cover");
                                        userProfile.put("user_status", "offline");
                                        userProfile.put("user_looking", stringLooking);
                                        userProfile.put("user_about", "Hi! Everybody I am newbie here.");
                                        userProfile.put("user_latitude", stringLatitude);
                                        userProfile.put("user_longitude", stringLongitude);
                                        userProfile.put("user_online", Timestamp.now());
                                        userProfile.put("user_joined", Timestamp.now());

                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .set(userProfile)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                            dialog.dismiss();
                                                        } else {
                                                            Toast.makeText(RegisterActivity.this, "Something went wrong! Please try again!", Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                });

                                    } else {

                                        Toast.makeText(RegisterActivity.this, "Please check errors to proceed!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });


                        } else {

                            Toast.makeText(RegisterActivity.this, "Please Fill in all the details to proceed!", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        Toast.makeText(RegisterActivity.this, "Please turn on Location service to continue.", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "Please Fill in all the details to proceed!", Toast.LENGTH_SHORT).show();

                }

            }
        });


        String fullText = "By Signing up you agree with Privacy Policy and Terms of Use";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(fullText);

        // Set the ClickableSpan for the "privacy policy" text
        ClickableSpan privacyPolicyClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                openWebPage("https://doc-hosting.flycricket.io/delhi-dating-privacy-policy/4e1e9ec1-c0bf-4f30-b29c-27138acad037/privacy");
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false); // Remove underline
            }
        };
        int privacyPolicyStartIndex = fullText.indexOf("Privacy Policy");
        int privacyPolicyEndIndex = privacyPolicyStartIndex + "Privacy Policy".length();
        spannableStringBuilder.setSpan(privacyPolicyClickSpan, privacyPolicyStartIndex, privacyPolicyEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.main_orange_color)), privacyPolicyStartIndex, privacyPolicyEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), privacyPolicyStartIndex, privacyPolicyEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the ClickableSpan for the "terms of use" text
        ClickableSpan termsOfUseClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                openWebPage("https://doc-hosting.flycricket.io/delhi-dating-terms-of-use/73b7f589-5a29-48f7-8d7a-dedbe15335bb/terms");
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false); // Remove underline
            }
        };
        int termsOfUseStartIndex = fullText.indexOf("Terms of Use");
        int termsOfUseEndIndex = termsOfUseStartIndex + "Terms of Use".length();
        spannableStringBuilder.setSpan(termsOfUseClickSpan, termsOfUseStartIndex, termsOfUseEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.main_orange_color)), termsOfUseStartIndex, termsOfUseEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), termsOfUseStartIndex, termsOfUseEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsTextView.setText(spannableStringBuilder);
        termsTextView.setMovementMethod(LinkMovementMethod.getInstance());


    }

    /**
     * @param view
     */
    public void radioButtonRegisterGender(View view) {
        int radioId = radioGroupRegisterGender.getCheckedRadioButtonId();
        radioButtonRegisterGender = findViewById(radioId);

    }

    /**
     * response after date selection
     *
     * @param view       date picker
     * @param year       year
     * @param month      month
     * @param dayOfMonth day
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dayOfMonth + "-"+month+"-"+year+"";


        if (year > 2004) {
            Toast.makeText(this,
                    "Sorry! you dont meet our user registration minimum age limits policy now. Please register with us after some time. Thank you for trying our app now!",
                    Toast.LENGTH_LONG).show();
            textViewRegisterBirthday.setText("");

        } else {
            textViewRegisterBirthday.setText(strDate);
        }

    }


    /**
     * @param stringDateUser
     * @return
     */
    private String AgeUser(String stringDateUser) {

        String[] arrayDateUser = stringDateUser.split("-");
        int day = Integer.valueOf(arrayDateUser[0]);
        int month = Integer.valueOf(arrayDateUser[1]);
        int year = Integer.valueOf(arrayDateUser[2]);


        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }


    /**
     * check location permision Granted or not if its Granted than  get user current location else again check lotion
     * permission
     */
    private void LocationPremissionCheck() {

        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        String rationale = "Please provide location permission so that you can ...";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Location Permission")
                .setSettingsDialogTitle("Warning");
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                LocationRequest();

            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                LocationPremissionCheck();
            }
        });
    }


    /**
     * @param locationLatitude  user current lat
     * @param locationLongitude user current long
     */
    private void LocationRetreive(Double locationLatitude, Double locationLongitude) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(locationLatitude, locationLongitude, 1);
            if (addresses != null && addresses.size() > 0) {
                string_city = addresses.get(0).getLocality();
                string_state = addresses.get(0).getAdminArea();
                string_country = addresses.get(0).getCountryName();
                string_location = addresses.get(0).getAddressLine(0);


                if (string_country == null) {
                    if (string_state != null) {
                        string_country = string_state;
                    } else if (string_city != null) {
                        string_country = string_city;
                    } else {
                        string_country = "null";
                    }
                }
                if (string_city == null) {
                    if (string_state != null) {
                        string_city = string_state;
                    } else {
                        string_city = string_country;
                    }
                }
                if (string_state == null) {
                    if (string_city != null) {
                        string_state = string_city;
                    } else {
                        string_state = string_country;
                    }
                }
                if (string_location == null) {
                    string_location = "Null";
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *  get  user location
     */
    private void LocationRequest() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PermissionChecker.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PermissionChecker.PERMISSION_GRANTED) {


            fusedLocationProviderClient = new FusedLocationProviderClient(this);

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {


                    if (location != null) {

                        Double locationLatitude = location.getLatitude();
                        Double locationLongitude = location.getLongitude();

                        stringLatitude = locationLatitude.toString();
                        stringLongitude = locationLongitude.toString();

                        if (!stringLatitude.equals("0.0") && !stringLongitude.equals("0.0")) {

                            LocationRetreive(locationLatitude, locationLongitude);

                        } else {

                            Toast.makeText(RegisterActivity.this,
                                    "Please turn on any GPS or location service and restart to use the app", Toast.LENGTH_SHORT).show();

                        }


                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Please turn on any GPS or location service and restart to use the app", Toast.LENGTH_SHORT).show();
                    }

                }

            });


        } else {

            LocationPremissionCheck();

        }
    }

    public boolean GooglePlayServiceCheck() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    /**
     * check Gps enable or not
     */
    private void GPSLocationServiceCheck() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, enable it to use this app?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                            Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

//    private void openWebPage(String url) {
//        Log.e("ooooopen web page", "oooooopen");
//        Uri webpage = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }
private void openWebPage(String url) {
    Log.d("openWebPage", "Opening web page: " + url);
    Uri webpage = Uri.parse(url);
    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
    startActivity(intent);

//    if (intent.resolveActivity(getPackageManager()) != null) {
//        Log.d("openWebPage", "Starting activity to open web page.");
//        startActivity(intent);
//    } else {
//        Log.d("openWebPage", "No activity found to handle the web page intent.");
//    }
}

    @Override
    protected void onStart() {
        super.onStart();


        GPSLocationServiceCheck();


    }
}
