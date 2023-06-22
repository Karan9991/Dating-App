package com.delhi.dating.IntroScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.delhi.dating.R;
import com.delhi.dating.ui.Activity.MainActivity;

public class SplashScreen extends AppCompatActivity {
    TextView l1, l2;
    Animation uptodown,downtoup;

    SharedPreferences sharedPref;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference dfOnline;
    DatabaseReference dfOnline2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPref = getSharedPreferences("UserType", Context.MODE_PRIVATE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);

        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
           downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
           l1.setAnimation(uptodown);
           l2.setAnimation(downtoup);
//        getSupportActionBar().hide();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
//                    Intent intent = new Intent(SplashScreen.this, SignIn.class);
//                    startActivity(intent);
                   // findUT();
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }
            }
        };
        timerThread.start();
    }

//   public void findUT(){
//       //If User already signed in
//       if (firebaseUser!=null&&firebaseAuth.getCurrentUser().isEmailVerified()){
//           dfOnline = FirebaseDatabase.getInstance().getReference();
//           dfOnline2 = FirebaseDatabase.getInstance().getReference();
//
//           Query myTopPostsQuery = dfOnline.child("Customer").orderByChild("email").equalTo(firebaseUser.getEmail());
//           Query myTopPostsQuery2 = dfOnline2.child("Seller").orderByChild("email").equalTo(firebaseUser.getEmail());
//           if (sharedPref.getString("UT",null).equals("Customer")){
//               startActivity(new Intent(SplashScreen.this, Customer.class));
//                           finish();
////               myTopPostsQuery.addValueEventListener(new ValueEventListener() {
////                   @Override
////                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                       Log.i("qqqqqqqqqqqqqqqqqqqqqqqqqsplashscreen","q"+dataSnapshot.getKey());
////                       if (dataSnapshot.getKey().equals("Customer")){
////                           dfOnline.child("Customer").orderByChild("email").equalTo(firebaseUser.getEmail()).removeEventListener(this);
////                           startActivity(new Intent(SplashScreen.this, Customer.class));
////                           finish();
////                       }
////                   }
////
////                   @Override
////                   public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                   }
////               });
//           }else if (sharedPref.getString("UT",null).equals("Seller")) {
//               startActivity(new Intent(SplashScreen.this, AddView.class));
//                           finish();
////               myTopPostsQuery2.addValueEventListener(new ValueEventListener() {
////                   @Override
////                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                       if (dataSnapshot.getKey().equals("Seller")) {
////
////                           dfOnline2.child("Seller").orderByChild("email").equalTo(firebaseUser.getEmail()).removeEventListener(this);
////                           startActivity(new Intent(SplashScreen.this, AddView.class));
////                           finish();
////                       }
////                   }
////
////                   @Override
////                   public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                   }
////               });
//           }
//       }
//       else
//       {
//           startActivity(new Intent(SplashScreen.this, SignIn.class));
//       }
//   }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}