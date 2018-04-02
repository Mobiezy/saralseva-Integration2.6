package mobi.app.saralseva.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import mobi.app.saralseva.R;
import mobi.app.saralseva.activities.MainActivity;
import mobi.app.saralseva.data.DataOperation;
import mobi.app.saralseva.models.Notification;

/**
 * Created by kumardev on 2/12/2017.
 */

public class FirebaseMsgService extends FirebaseMessagingService {

    String message="";
    String imageUri="";
    String notificationText="";
    String notificationTitle="";
    Bitmap bitmap=null;

    Notification notification;
    DataOperation dataOperation;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
       // Log.d("Msg Received",""+remoteMessage);





        Random rand = new Random();
        int randNum = rand.nextInt(10000);

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy_HH:mm");
        String currentDateandTime = sdf.format(new Date());


        notification= new Notification();
        notification.setNotificationId(randNum);
//        notification.setNotificationText(notificationText);
        notification.setNotificationTime(currentDateandTime);

        if(remoteMessage.getNotification()!=null) {
            notificationText = remoteMessage.getNotification().getBody();
            notificationTitle = remoteMessage.getNotification().getTitle();
            notification.setNotificationText(notificationText);
        }

        if(remoteMessage.getData().size()>0){
            message=remoteMessage.getData().get("message");
            imageUri=remoteMessage.getData().get("image");
            bitmap=getBitmapFromUrl(imageUri);
            notification.setNotificationText(message);

            if(bitmap!=null)
                sendNotification(notificationText,message,bitmap);
            else
                sendNotificationText(message,notificationText);

        }else{
            sendNotificationText(notificationText,notificationTitle);
        }



    }

    private void sendNotificationText(String notificationText, String notificationTitle) {

        int notId=(int) System.currentTimeMillis();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("notificationFrag", notificationText);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 11,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notifysmallogo)
                .setContentTitle("Saral Seva ")
                .setContentText(notificationText)
                .setAutoCancel(true)
                .setSound(alarmSound)
                //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .setContentIntent(pendingIntent);

        dataOperation=new DataOperation(getApplicationContext());
        dataOperation.addNotification(notification);
        System.out.print("");


        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);



        notificationManager.notify(11, notificationBuilder.build());



    }

    private Bitmap getBitmapFromUrl(String imageUri) {
        try {
            URL url = new URL(imageUri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    private void sendNotification(String body, String message, Bitmap bitmap) {
        int notId=(int) System.currentTimeMillis();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notificationFrag", message);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, notId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notifysmallogo)
                .setContentTitle("Saral Seva")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        dataOperation=new DataOperation(getApplicationContext());
        dataOperation.addNotification(notification);
        System.out.print("");

        notificationManager.cancelAll();
        notificationManager.notify(notId, notificationBuilder.build());
    }
}
