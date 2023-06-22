package com.delhi.dating.Notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

//import com.example.torontodating.Chat.MessageActivity;
//import com.example.torontodating.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.delhi.dating.Application;
import com.delhi.dating.R;
import com.delhi.dating.model.ChatUser_DataModel;
import com.delhi.dating.ui.Activity.AccountsActivity;
import com.delhi.dating.ui.Activity.MainActivity;
import com.delhi.dating.ui.Activity.MessageActivity;

import javax.annotation.Nullable;
//import com.example.torontodating.Seller.addCustomers.map.deliveryNotification.MainActivity;

//import android.support.v4.app.NotificationCompat;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    NotificationChannel mChannel;
    NotificationManager notifManager;
    String title, message;
    Application application = new Application();
    //
    public static final String CHANNEL_ID_1 = "New Matches";
    public static final String CHANNEL_NAME_1 = "New Matches";
    public static final String CHANNEL_GROUP_1 = "New Matches";
    public static final String CHANNEL_DETAIL_1 = "Check when you have new Match";
    public static final String CHANNEL_ID_2 = "Messages";
    public static final String CHANNEL_NAME_2 = "Messages";
    public static final String CHANNEL_GROUP_2 = "Messages";
    public static final String CHANNEL_DETAIL_2 = "Check new and unread messages";
    public static final String CHANNEL_ID_3 = "New Likes";
    public static final String CHANNEL_NAME_3 = "New Likes";
    public static final String CHANNEL_GROUP_3 = "New Likes";
    public static final String CHANNEL_DETAIL_3 = "Check out who is into you";
    public static final String CHANNEL_ID_4 = "Super Likes";
    public static final String CHANNEL_NAME_4 = "Super Likes";
    public static final String CHANNEL_GROUP_4 = "Super Likes";
    public static final String CHANNEL_DETAIL_4 = "Check who has crush on you";
    public static final String CHANNEL_ID_5 = "New Visits";
    public static final String CHANNEL_NAME_5 = "New Visits";
    public static final String CHANNEL_GROUP_5 = "New Visits";
    public static final String CHANNEL_DETAIL_5 = "Check who is stalking you";
    public static final String CHANNEL_ID_6 = "New Feeds";
    public static final String CHANNEL_NAME_6 = "New Feeds";
    public static final String CHANNEL_GROUP_6 = "New Feeds";
    public static final String CHANNEL_DETAIL_6 = "Check out the recent uploads";
    public static final String CHANNEL_ID_7 = "Uncategorised";
    public static final String CHANNEL_NAME_7 = "Uncategorised";
    public static final String CHANNEL_GROUP_7 = "Uncategorised";
    public static final String CHANNEL_DETAIL_7 = "Check out other notifications";
    public static NotificationManagerCompat notificationManagerCompat;
   // public static boolean appRunning = false;
    static String user_unread = "0";
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    int add;
    int news;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //appRunning = true;

//        Intent intent = new Intent(this, FirebaseService.class);
//        startService(intent);

        NotificationChannels();
//        StartNotificationMatch();
        Log.e("Chat"," "+remoteMessage.getData().get("body"));
        if (remoteMessage.getData().get("body") != null){
            if (remoteMessage.getData().get("body").equals("chat")){
                StartNotificationChats();
            }
            else if (remoteMessage.getData().get("body").equals("match")){
                StartNotificationMatch();
            }
            else if (remoteMessage.getData().get("body").equals("like")){
                StartNotificationLikes();
            } else if (remoteMessage.getData().get("body").equals("super")){
                StartNotificationSuper();
                Log.e("test","super");
            }
            else if (remoteMessage.getData().get("body").equals("visit")){
                StartNotificationVisits();
                Log.e("test","visit");
            }
        }
//        StartNotificationLikes();
//        StartNotificationSuper();
//        StartNotificationVisits();
        //

        //application.NotificationChannels();
       // application.StartNotificationChats();
        title=remoteMessage.getData().get("Title");
        message=remoteMessage.getData().get("Message");

        String sented = remoteMessage.getData().get("sented");
        String user = remoteMessage.getData().get("user");

        SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        String currentUser = preferences.getString("currentuser", "none");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.i("iddddddddddddddd"," "+firebaseUser.getUid());
        if (title!=null && message!=null){
          //  deliveryTiffinNotification(title, message);
        }
        if (sented!=null && user !=null && firebaseUser != null && sented.equals(firebaseUser.getUid())){
            if (!currentUser.equals(user)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 //  StartNotificationChats();
                   Log.e("a","a");
                    // sendOreoNotification(remoteMessage);
                } else {
                   // sendNotification(remoteMessage);
                }
            }
        }
    }

    private void sendOreoNotification(RemoteMessage remoteMessage){
        Log.i("ooooooooooooooooo","oreo");
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_IMMUTABLE);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
                defaultSound, icon);

        int i = 0;
        if (j > 0){
            i = j;
        }

        oreoNotification.getManager().notify(i, builder.build());

    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if (j > 0){
            i = j;
        }

        noti.notify(i, builder.build());
    }

//    private void deliveryTiffinNotification(String title, String description) {
//
//        if (notifManager == null) {
//            notifManager = (NotificationManager) getSystemService
//                    (Context.NOTIFICATION_SERVICE);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationCompat.Builder builder;
//            Intent intent = new Intent(this, com.example.torontodating.Chat.MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent;
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            if (mChannel == null) {
//                mChannel = new NotificationChannel
//                        ("0", title, importance);
//                mChannel.setDescription(description);
//                mChannel.enableVibration(true);
//                notifManager.createNotificationChannel(mChannel);
//            }
//            builder = new NotificationCompat.Builder(this, "0");
//
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT);
//            builder.setContentTitle(title)
//                    .setSmallIcon(getNotificationIcon()) // required
//                    .setContentText(description)  // required
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setAutoCancel(true)
//                    .setLargeIcon(BitmapFactory.decodeResource
//                            (getResources(), R.drawable.background_left))
//                    .setContentIntent(pendingIntent)
//                    .setSound(RingtoneManager.getDefaultUri
//                            (RingtoneManager.TYPE_NOTIFICATION));
//            Notification notification = builder.build();
//            notifManager.notify(0, notification);
//        } else {
//
//            Intent intent = new Intent(this, com.example.torontodating.Chat.MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = null;
//
//            pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT);
//
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setContentTitle(title)
//                    .setContentText(description)
//                    .setAutoCancel(true)
//                    .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary))
//                    .setSound(defaultSoundUri)
//                    .setSmallIcon(getNotificationIcon())
//                    .setContentIntent(pendingIntent)
//                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(description));
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(1251, notificationBuilder.build());
//        }
//    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.notify_icon : R.drawable.notify_icon;
    }
    //
    public void NotificationChannels() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //Match Notification Channel
            NotificationChannel channelMatch = new NotificationChannel(
                    CHANNEL_ID_1,
                    CHANNEL_NAME_1,
                    NotificationManager.IMPORTANCE_HIGH);
            channelMatch.setDescription(CHANNEL_DETAIL_1);
            channelMatch.enableLights(true);
            channelMatch.enableVibration(true);

            //Chats Notification Channel
            NotificationChannel channelChats = new NotificationChannel(
                    CHANNEL_ID_2,
                    CHANNEL_NAME_2,
                    NotificationManager.IMPORTANCE_HIGH);
            channelChats.setDescription(CHANNEL_DETAIL_2);
            channelChats.enableLights(true);
            channelChats.enableVibration(true);

            //Likes Notification Channel
            NotificationChannel channelLikes = new NotificationChannel(
                    CHANNEL_ID_3,
                    CHANNEL_NAME_3,
                    NotificationManager.IMPORTANCE_HIGH);
            channelLikes.setDescription(CHANNEL_DETAIL_3);
            channelLikes.enableLights(true);
            channelLikes.enableVibration(true);

            //Super Notification Channel
            NotificationChannel channelSuper = new NotificationChannel(
                    CHANNEL_ID_4,
                    CHANNEL_NAME_4,
                    NotificationManager.IMPORTANCE_HIGH);
            channelSuper.setDescription(CHANNEL_DETAIL_4);
            channelSuper.enableLights(true);
            channelSuper.enableVibration(true);

            //Visits Notification Channel
            NotificationChannel channelVisits = new NotificationChannel(
                    CHANNEL_ID_5,
                    CHANNEL_NAME_5,
                    NotificationManager.IMPORTANCE_HIGH);
            channelVisits.setDescription(CHANNEL_DETAIL_5);
            channelVisits.enableLights(true);
            channelVisits.enableVibration(true);

            //Feeds Notification Channel
            NotificationChannel channelFeeds = new NotificationChannel(
                    CHANNEL_ID_6,
                    CHANNEL_NAME_6,
                    NotificationManager.IMPORTANCE_HIGH);
            channelFeeds.setDescription(CHANNEL_DETAIL_6);
            channelFeeds.enableLights(true);
            channelFeeds.enableVibration(true);

            //Other Notification Channel
            NotificationChannel channelOther = new NotificationChannel(
                    CHANNEL_ID_7,
                    CHANNEL_NAME_7,
                    NotificationManager.IMPORTANCE_HIGH);
            channelOther.setDescription(CHANNEL_DETAIL_7);
            channelOther.enableLights(true);
            channelOther.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channelMatch);
            notificationManager.createNotificationChannel(channelChats);
            notificationManager.createNotificationChannel(channelLikes);
            notificationManager.createNotificationChannel(channelSuper);
            notificationManager.createNotificationChannel(channelVisits);
            notificationManager.createNotificationChannel(channelFeeds);
            notificationManager.createNotificationChannel(channelOther);
        }

    }

    /**
     * send matched profile notifications
     */
    private void StartNotificationMatch() {
        if (firebaseUser != null) {
            final String currentUser = firebaseUser.getUid();
            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .collection("matches")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (queryDocumentSnapshots != null) {
                                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_match").equals("yes")) {
                                                              //  if (!Application.appRunning) {
                                                                    ShowNotificationMatch();
                                                                //}
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                    if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_match").equals("yes")) {
                                                                if (!Application.appRunning) {
                                                                    ShowNotificationMatch();
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });
        }
    }


    /**
     * show the matched profile notification
     */
    private void ShowNotificationMatch() {
        Intent intent = new Intent(this, AccountsActivity.class);
        intent.putExtra("tab_show", "tab_matches");
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Bitmap bitmapLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        notificationManagerCompat = NotificationManagerCompat.from(this);

        Notification newMessageNotificationMatch = new NotificationCompat.Builder(this, CHANNEL_ID_1)
                .setSmallIcon(setNotificationIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setColor(getResources().getColor(R.color.colorPink))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("You have new match")
                .setContentText("Someone matched you! Tap here to find out who!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(CHANNEL_GROUP_1)
                .build();


        notificationManagerCompat.notify(1, newMessageNotificationMatch);

    }


    /**
     * send like notifcation
     */
    private void StartNotificationLikes() {
        if (firebaseUser != null) {
            final String currentUser = firebaseUser.getUid();
            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .collection("likes")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (queryDocumentSnapshots != null) {
                                for (final DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_likes").equals("yes")) {
                                                              //  if (!Application.appRunning) {
                                                                String user_super = doc.getDocument().getString("user_super");
                                                                if (user_super == null || user_super.equals("no")) {
                                                                    //  if (!appRunning) {
                                                                    ShowNotificationLikes();
                                                                    //}
                                                                }                                                                //}
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                    if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_likes").equals("yes")) {
                                                               // if (!Application.appRunning) {
                                                                String user_super = doc.getDocument().getString("user_super");
                                                                if (user_super == null || user_super.equals("no")) {
                                                                    //  if (!appRunning) {
                                                                    ShowNotificationLikes();
                                                                    //}
                                                                }                                                                //}
                                                            }
                                                        }
                                                    }
                                                });

                                    }
                                }
                            }
                        }
                    });
        }
    }


    /**
     * show liks notification
     */
    private void ShowNotificationLikes() {
        Intent intent = new Intent(this, AccountsActivity.class);
        intent.putExtra("tab_show", "tab_likes");
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        Bitmap bitmapLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        notificationManagerCompat = NotificationManagerCompat.from(this);

        Notification newMessageNotificationLikes = new NotificationCompat.Builder(this, CHANNEL_ID_3)
                .setSmallIcon(setNotificationIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setColor(getResources().getColor(R.color.colorPink))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("You have new likes")
                .setContentText("Someone liked you! Tap here to find out who!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(CHANNEL_GROUP_3)
                .build();

        notificationManagerCompat.notify(3, newMessageNotificationLikes);
    }


    /**
     * send supper like notification
     */
    private void StartNotificationSuper() {
        Log.e("test","super1");

        if (firebaseUser != null) {
            final String currentUser = firebaseUser.getUid();
            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .collection("likes")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (queryDocumentSnapshots != null) {
                                for (final DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_super").equals("yes")) {
                                                                String user_super = doc.getDocument().getString("user_super");
                                                                if (user_super != null && user_super.equals("yes")) {
                                                                   // if (!Application.appRunning) {
                                                                    Log.e("test","super2");

                                                                    ShowNotificationSuper();
                                                                   // }
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                    if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_super").equals("yes")) {
                                                                String user_super = doc.getDocument().getString("user_super");
                                                                if (user_super != null && user_super.equals("yes")) {
                                                                    Log.e("test","super2a");

                                                                    //  if (!Application.appRunning) {
                                                                        ShowNotificationSuper();
                                                                   // }
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });
        }
    }


    /**
     * show supper like notification
     */
    private void ShowNotificationSuper() {
        Log.e("test","super3");

        Intent intent = new Intent(this, AccountsActivity.class);
        intent.putExtra("tab_show", "tab_likes");
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        Bitmap bitmapLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        notificationManagerCompat = NotificationManagerCompat.from(this);

        Notification newMessageNotificationSuper = new NotificationCompat.Builder(this, CHANNEL_ID_4)
                .setSmallIcon(setNotificationIcon())
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setColor(getResources().getColor(R.color.colorPink))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("You have new super like")
                .setContentText("Someone super liked you! Tap here to find out!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(CHANNEL_GROUP_4)
                .build();

        notificationManagerCompat.notify(4, newMessageNotificationSuper);
    }


    /**
     * send notification for visits
     */
    private void StartNotificationVisits() {
        if (firebaseUser != null) {
            final String currentUser = firebaseUser.getUid();
            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .collection("visits")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (queryDocumentSnapshots != null) {
                                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_visits").equals("yes")) {
//                                                                if (!Application.appRunning) {
                                                                    ShowNotificationVisits();
                                                               // }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                    if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getString("alert_visits").equals("yes")) {
//                                                                if (!Application.appRunning) {
                                                                    ShowNotificationVisits();
                                                               // }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });
        }
    }


    /**
     * show notification for visits
     */
    private void ShowNotificationVisits() {
        Intent intent = new Intent(this, AccountsActivity.class);
        intent.putExtra("tab_show", "tab_visitors");
        // @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        Bitmap bitmapLogo = BitmapFactory.decodeResource(getResources(), R.drawable.button_back);
        notificationManagerCompat = NotificationManagerCompat.from(this);

        Notification newMessageNotificationVisits = new NotificationCompat.Builder(this, CHANNEL_ID_5)
                .setSmallIcon(setNotificationIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setColor(getResources().getColor(R.color.colorPink))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("You have new visitor")
                .setContentText("Someone visited you! Tap here to find out who!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(CHANNEL_GROUP_5)
                .build();

        notificationManagerCompat.notify(5, newMessageNotificationVisits);
    }

    /**
     * send notifcation for Chats
     */
    public void StartNotificationChats() {
        Log.e("a","b");

        if (firebaseUser != null) {

            final String currentUser = firebaseUser.getUid();

            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .collection("chats")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (queryDocumentSnapshots != null) {

                                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {

                                        ChatUser_DataModel chatUserDataModel = doc.getDocument().toObject(ChatUser_DataModel.class);

                                        if (!chatUserDataModel.getUser_unread().equals("0")) {

                                            String user_message = chatUserDataModel.getUser_message();
                                            String user_receiver = chatUserDataModel.getUser_receiver();
                                            String user_unread_new = chatUserDataModel.getUser_unread();

                                            CheckNotificationsChats(currentUser, user_message, user_receiver);
                                        }
                                    }
                                    if (doc.getType() == DocumentChange.Type.MODIFIED) {

                                        ChatUser_DataModel chatUserDataModel = doc.getDocument().toObject(ChatUser_DataModel.class);

                                        if (!chatUserDataModel.getUser_unread().equals("0")) {

                                            String user_message = chatUserDataModel.getUser_message();
                                            String user_receiver = chatUserDataModel.getUser_receiver();
                                            String user_unread_new = chatUserDataModel.getUser_unread();

                                            Log.e("a","c");

                                            CheckNotificationsChats(currentUser, user_message, user_receiver);
                                        }


                                    }
                                }
                            }
                        }
                    });
        }

    }

    /**
     * @param currentUser   current User
     * @param user_message  user message
     * @param user_receiver user receiver
     */
    private void CheckNotificationsChats(final String currentUser, final String user_message, String user_receiver) {
        Log.e("a","d");

        firebaseFirestore.collection("users")
                .document(user_receiver)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String user_status = task.getResult().getString("user_status");
                            String user_name = task.getResult().getString("user_name");
                            final String user_uid = task.getResult().getString("user_uid");

                            String[] splitName = user_name.split(" ");
                            final String userTitle = splitName[0];

                         // String removenonnumeric =  user_uid.replaceAll("[^\\d]", "");
                            String uids = user_uid.replaceAll("\\D", "");


//                            String uids = user_uid.replaceAll("[^0-9]", "");
                            final long uid = Long.valueOf(uids);
                            Log.e("appRunning", " "+Application.appRunning);

                           if (!MessageActivity.isChatScreenActive) {
                                Log.e("a","apprunning"+Application.appRunning);

                                firebaseFirestore.collection("users")
                                        .document(currentUser)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().getString("alert_chats").equals("yes")) {
                                                        ShowNotificationChats(userTitle, user_message, user_uid, uid, MessageActivity.class);
                                                        Log.e("a","e");

                                                    }
                                                }
                                            }
                                        });

                          }

                        }
                    }
                });
    }


    /**
     * @param stringTitle
     * @param stringContent
     * @param user_uid
     * @param uid
     * @param classActivity
     */
    private void ShowNotificationChats(String stringTitle, String stringContent,
                                       String user_uid, Long uid, Class classActivity) {
        Log.e("a","f");

        Intent intent = new Intent(this, classActivity);
        intent.putExtra("user_uid", user_uid);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Intent intentChat = new Intent(this, MainActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent chatPending = PendingIntent.getActivity(this, 0, intentChat, PendingIntent.FLAG_IMMUTABLE);

        // Bitmap bitmapLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap bitmapLogo = BitmapFactory.decodeResource(getResources(), R.drawable.button_back);

        notificationManagerCompat = NotificationManagerCompat.from(this);

        Notification newMessageNotificationChats = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(setNotificationIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setColor(getResources().getColor(R.color.colorPink))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(stringTitle)
                .setContentText(stringContent)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(CHANNEL_GROUP_2)
                .build();

        Notification summaryNotificationChats = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(setNotificationIcon())
                .setStyle(new NotificationCompat.InboxStyle()
                        .setBigContentTitle("New Chat Messages")
                        .setSummaryText("New Chat Messages"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.colorPink))
                .setGroup(CHANNEL_GROUP_2)
                .setGroupSummary(true)
                .setAutoCancel(true)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setContentIntent(chatPending)
                .build();

        int notificationId = Long.valueOf(uid).hashCode();

        notificationManagerCompat.notify(notificationId, newMessageNotificationChats);
        notificationManagerCompat.notify(2, summaryNotificationChats);

    }

    /**
     * @return set notification icon
     */
    private int setNotificationIcon() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return R.drawable.notify_icon;
        } else {
            return R.drawable.notify_icon;
        }
    }

    /**
     * @param bitmap convert bitmap to cercle
     * @return
     */
    private Bitmap CircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(5,
                5, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, 5, 5);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(5 / 2, 5 / 2,
                5 / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 110;
        int targetHeight = 110;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0,110,
                        110),
                new Rect(0, 0, targetWidth, targetHeight), new Paint(Paint.FILTER_BITMAP_FLAG));
        return targetBitmap;
    }

}
