package com.naeemdev.tinder.ui.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.naeemdev.tinder.R;
import com.naeemdev.tinder.model.Feeds_DataModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    String currentUser;

    Toolbar toolbarSettingsToolbar;

    LinearLayout linearLayoutAgeRange;
    LinearLayout linearLayoutGender;
    LinearLayout linearLayoutLocation;
    LinearLayout linearLayoutRelationship;
    LinearLayout linearLayoutSexual;
    LinearLayout linearLayoutSeeking;


    LinearLayout linearLayoutSettingsAccount;
    LinearLayout linearLayoutSettingsSupport;
    LinearLayout linearLayoutSettingsNotify;

    Button buttonSettingsAccountLogout;
    Button buttonSettingsDeleteAccount;

    CrystalRangeSeekbar seekbarSettingsAgeRange;
    TextView textViewSettingsGender;
    TextView textViewSettingsLocation;
    TextView textViewSettingsRelationship;
    TextView textViewSettingsSexual;
    TextView textViewSettingsSeeking;


    TextView textViewSettingsAgeRangeMin;
    TextView textViewSettingsAgeRangeMax;

    String[] string_array_show_sexual;
    String[] string_array_show_lookingfor;
    String[] string_array_show_seekingfor;
    String[] string_array_show_relationship;
    String[] string_array_show_lookingin;

    String stringSeekbarMinimum;
    String stringSeekbarMaximum;

    String user_city;
    String user_state;
    String user_country;

    Switch switchSettingsFeeds;
    Switch switchSettingsProfile;
    Switch switchSettingsStatus;
    Switch switchSettingsMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUser = firebaseUser.getUid();


        toolbarSettingsToolbar = findViewById(R.id.toolbarSettingsToolbar);
        setSupportActionBar(toolbarSettingsToolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarSettingsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linearLayoutAgeRange = findViewById(R.id.linearLayoutAgeRange);
        linearLayoutGender = findViewById(R.id.linearLayoutGender);
        linearLayoutLocation = findViewById(R.id.linearLayoutLocation);
        linearLayoutRelationship = findViewById(R.id.linearLayoutRelationship);
        linearLayoutSexual = findViewById(R.id.linearLayoutSexual);
        linearLayoutSeeking = findViewById(R.id.linearLayoutSeeking);


        linearLayoutSettingsAccount = findViewById(R.id.linearLayoutSettingsAccount);
        linearLayoutSettingsSupport = findViewById(R.id.linearLayoutSettingsSupport);
        linearLayoutSettingsNotify = findViewById(R.id.linearLayoutSettingsNotify);

        buttonSettingsAccountLogout = findViewById(R.id.buttonSettingsAccountLogout);
        buttonSettingsDeleteAccount = findViewById(R.id.buttonSettingsDeleteAccount);


        seekbarSettingsAgeRange = findViewById(R.id.seekbarSettingsAgeRange);
        textViewSettingsGender = findViewById(R.id.textViewSettingsGender);
        textViewSettingsLocation = findViewById(R.id.textViewSettingsLocation);
        textViewSettingsRelationship = findViewById(R.id.textViewSettingsRelationship);
        textViewSettingsSexual = findViewById(R.id.textViewSettingsSexual);
        textViewSettingsSeeking = findViewById(R.id.textViewSettingsSeeking);


        textViewSettingsAgeRangeMin = findViewById(R.id.textViewSettingsAgeRangeMin);
        textViewSettingsAgeRangeMax = findViewById(R.id.textViewSettingsAgeRangeMax);

        string_array_show_sexual = getResources().getStringArray(R.array.string_array_show_sexual);
        string_array_show_lookingfor = getResources().getStringArray(R.array.string_array_show_gender);
        string_array_show_lookingin = new String[4];
        string_array_show_seekingfor = getResources().getStringArray(R.array.string_array_show_seeking);
        string_array_show_relationship = getResources().getStringArray(R.array.string_array_show_marital);

        switchSettingsFeeds = findViewById(R.id.switchSettingsFeeds);
        switchSettingsProfile = findViewById(R.id.switchSettingsProfile);
        switchSettingsStatus = findViewById(R.id.switchSettingsStatus);
        switchSettingsMatch = findViewById(R.id.switchSettingsMatch);


        switchSettingsFeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchSettingsFeeds.isChecked()) {

                    Map<String, Object> mapUserFeeds = new HashMap<>();
                    mapUserFeeds.put("show_feeds", "yes");
                    firebaseFirestore.collection("users")
                            .document(currentUser)
                            .update(mapUserFeeds)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SettingsActivity.this,
                                                "You are sharing your feeds now!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                    firebaseFirestore.collection("feeds")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot querySnapshot : task.getResult()) {

                                        Feeds_DataModel feedsDataModel = querySnapshot.toObject(Feeds_DataModel.class);

                                        if (feedsDataModel.getFeed_user().equals(currentUser)) {

                                            Map<String, Object> mapUserFeeds = new HashMap<>();
                                            mapUserFeeds.put("feed_show", "yes");

                                            firebaseFirestore.collection("feeds")
                                                    .document(querySnapshot.getId())
                                                    .update(mapUserFeeds);

                                        }
                                    }
                                }
                            });

                } else {

                    Map<String, Object> mapUserFeeds = new HashMap<>();
                    mapUserFeeds.put("show_feeds", "no");
                    firebaseFirestore.collection("users")
                            .document(currentUser)
                            .update(mapUserFeeds)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SettingsActivity.this,
                                                "Feeds sharing stopped!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                    firebaseFirestore.collection("feeds")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot querySnapshot : task.getResult()) {

                                        Feeds_DataModel feedsDataModel = querySnapshot.toObject(Feeds_DataModel.class);

                                        if (feedsDataModel.getFeed_user().equals(currentUser)) {

                                            Map<String, Object> mapUserFeeds = new HashMap<>();
                                            mapUserFeeds.put("feed_show", "no");

                                            firebaseFirestore.collection("feeds")
                                                    .document(querySnapshot.getId())
                                                    .update(mapUserFeeds);

                                        }
                                    }
                                }
                            });

                }
            }
        });


        switchSettingsProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivacyProfile(switchSettingsProfile,
                        "show_profile",
                        "You are public now!",
                        "You are on stealth mode now!");
            }
        });

        switchSettingsStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivacyProfile(switchSettingsStatus,
                        "show_status",
                        "You are online!",
                        "You are now ninja mode!");
            }
        });

        switchSettingsMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivacyProfile(switchSettingsMatch,
                        "show_match",
                        "Match Mode on! Swipe to connect!",
                        "Match mode deactive!");
            }
        });


        linearLayoutSettingsAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingsActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        linearLayoutSettingsNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingsActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });


        linearLayoutSettingsSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingsActivity.this, SupportActivity.class);
                startActivity(intent);
            }
        });


        buttonSettingsAccountLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();

                Toast.makeText(SettingsActivity.this,
                        "You are logged out!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        buttonSettingsDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Confirm Account Deletion")
                        .setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAccount();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked the "Cancel" button, do nothing
                            }
                        })
                        .show();
            }
        });


        seekbarSettingsAgeRange.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                stringSeekbarMinimum = String.valueOf(minValue);
                stringSeekbarMaximum = String.valueOf(maxValue);

                String stringLookage = stringSeekbarMinimum + " - " + stringSeekbarMaximum;
                if (stringSeekbarMaximum.equals("60")) {
                    textViewSettingsAgeRangeMin.setText(stringSeekbarMinimum);
                    textViewSettingsAgeRangeMax.setText(stringSeekbarMaximum + "+");
                } else {
                    textViewSettingsAgeRangeMin.setText(stringSeekbarMinimum);
                    textViewSettingsAgeRangeMax.setText(stringSeekbarMaximum);
                }

                ProfileUpdate("show_ages", stringLookage);
            }
        });

        linearLayoutGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LookingForDialog();
                ProfileDialogRadio(string_array_show_lookingfor,
                        textViewSettingsGender,
                        "show_gender",
                        "Gender");
            }
        });

        linearLayoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                string_array_show_lookingin[0] = "Anywhere";
                string_array_show_lookingin[1] = user_city;
                string_array_show_lookingin[2] = user_state;
                string_array_show_lookingin[3] = user_country;

                //LookingInDialog();
                ProfileDialogRadio(string_array_show_lookingin,
                        textViewSettingsLocation,
                        "show_location",
                        "Location");
            }
        });
        linearLayoutRelationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LookingForDialog();
                ProfileDialogRadio(string_array_show_relationship,
                        textViewSettingsRelationship,
                        "show_marital",
                        "Marital Status");
            }
        });
        linearLayoutSexual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LookingForDialog();
                ProfileDialogRadio(string_array_show_sexual,
                        textViewSettingsSexual,
                        "show_sexual",
                        "Sexual Orientation");
            }
        });
        linearLayoutSeeking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LookingForDialog();
                ProfileDialogRadio(string_array_show_seekingfor,
                        textViewSettingsSeeking,
                        "show_seeking",
                        "I am Seeking for");
            }
        });

    }


    private void PrivacyProfile(Switch switchProfile, String switchString,
                                final String switchToastOn, final String switchToastOff) {

        if (switchProfile.isChecked()) {

            Map<String, Object> mapUserProfile = new HashMap<>();
            mapUserProfile.put(switchString, "yes");
            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .update(mapUserProfile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SettingsActivity.this,
                                        switchToastOn, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {

            Map<String, Object> mapUserProfile = new HashMap<>();
            mapUserProfile.put(switchString, "no");
            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .update(mapUserProfile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SettingsActivity.this,
                                        switchToastOff, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }


    private void ProfileDialogRadio(
            final String[] dialogArray,
            final TextView dialogTextView,
            final String dialogUser,
            final String dialogTitle) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle(dialogTitle);

        String dialogTextString = dialogTextView.getText().toString();
        int checkedPosition = new ArrayList<String>(Arrays.asList(dialogArray)).indexOf(dialogTextString);

        // add a radio button list
        int checkedItem = checkedPosition; // cow
        builder.setSingleChoiceItems(dialogArray, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int selectedWhich = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                dialogTextView.setText(dialogArray[selectedWhich]);
                ProfileUpdate(dialogUser, dialogArray[selectedWhich]);
            }
        });

        builder.setNegativeButton("Cancel", null);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void ProfileUpdate(final String user_key, final String user_value) {

        final Map<String, Object> mapProfileUpdate = new HashMap<>();
        mapProfileUpdate.put(user_key, user_value);

        firebaseFirestore.collection("users")
                .document(currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            firebaseFirestore.collection("users")
                                    .document(currentUser)
                                    .update(mapProfileUpdate);
                        } else {
                            firebaseFirestore.collection("users")
                                    .document(currentUser)
                                    .set(mapProfileUpdate);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String currentUser = firebaseUser.getUid();

        firebaseFirestore.collection("users")
                .document(currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot documentSnapshot = task.getResult();

                            user_city = documentSnapshot.getString("user_city");
                            user_state = documentSnapshot.getString("user_state");
                            user_country = documentSnapshot.getString("user_country");
                            String show_seeking = documentSnapshot.getString("show_seeking");
                            String show_sexual = documentSnapshot.getString("show_sexual");
                            String show_gender = documentSnapshot.getString("show_gender");
                            String show_location = documentSnapshot.getString("show_location");
                            String show_ages = documentSnapshot.getString("show_ages");
                            String show_marital = documentSnapshot.getString("show_marital");

                            String show_feeds = documentSnapshot.getString("show_feeds");
                            String show_profile = documentSnapshot.getString("show_profile");
                            String show_status = documentSnapshot.getString("show_status");
                            String show_match = documentSnapshot.getString("show_match");


                            if (show_sexual != null) {
                                textViewSettingsSexual.setText(show_sexual);
                            }
                            if (show_seeking != null) {
                                textViewSettingsSeeking.setText(show_seeking);
                            }
                            if (show_gender != null) {
                                textViewSettingsGender.setText(show_gender);
                            }
                            if (show_location != null) {
                                textViewSettingsLocation.setText(show_location);
                            }
                            if (show_marital != null) {
                                textViewSettingsRelationship.setText(show_marital);
                            }
                            if (show_ages != null) {
                                String[] splitAges = show_ages.split(" - ");

                                textViewSettingsAgeRangeMin.setText(splitAges[0]);
                                textViewSettingsAgeRangeMax.setText(splitAges[1]);

                                seekbarSettingsAgeRange.setMinStartValue(Float.valueOf(splitAges[0]));
                                seekbarSettingsAgeRange.setMaxStartValue(Float.valueOf(splitAges[1]));

                                seekbarSettingsAgeRange.apply();

                            }

                            if (show_feeds != null && show_feeds.equals("yes")) {
                                switchSettingsFeeds.setChecked(true);
                            } else {
                                switchSettingsFeeds.setChecked(false);
                            }
                            if (show_profile != null && show_profile.equals("yes")) {
                                switchSettingsProfile.setChecked(true);
                            } else {
                                switchSettingsProfile.setChecked(false);
                            }
                            if (show_status != null && show_status.equals("yes")) {
                                switchSettingsStatus.setChecked(true);
                            } else {
                                switchSettingsStatus.setChecked(false);
                            }
                            if (show_match != null && show_match.equals("yes")) {
                                switchSettingsMatch.setChecked(true);
                            } else {
                                switchSettingsMatch.setChecked(false);
                            }


                        }
                    }
                });

    }

    private void deleteAccount() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Delete the Firestore data
            FirebaseFirestore.getInstance().collection("users")
                    .document(user.getUid())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Firestore data deleted successfully, now delete the user account
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Account deleted successfully
                                                Toast.makeText(SettingsActivity.this, "Account deleted successfully.", Toast.LENGTH_SHORT).show();
                                                navigateToLoginActivity();
                                                // Perform any additional actions or navigation as needed
                                            } else {
                                                // Failed to delete the account
                                                Toast.makeText(SettingsActivity.this, "Failed to delete account.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to delete Firestore data
                            Toast.makeText(SettingsActivity.this, "Failed to delete Firestore data.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
