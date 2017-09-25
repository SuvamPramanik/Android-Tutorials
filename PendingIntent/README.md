## What is PendingIntent?

[PendingIntent](https://developer.android.com/reference/android/app/PendingIntent.html) is an object that is wrapped on top of [Intent](https://developer.android.com/reference/android/content/Intent.html) and it specifies an action to be taken place in future. In other words, PendingIntent lets us pass a future Intent to another application and allow that application to execute that Intent as if it had the same permissions as our application, whether or not our application is still around when the Intent is eventually invoked.

A PendingIntent is very useful when you want to execute an [AlarmManager](https://developer.android.com/reference/android/app/AlarmManager.html) or create a new [Notification](https://developer.android.com/reference/android/app/NotificationManager.html) (refer to the code for more details about how to implement them). A PendingIntent provides a means for applications to work, even after their process exits.

For security reasons, the base Intent that is supplied to the PendingIntent must have the component name explicitly set to ensure it is ultimately sent there and nowhere else. Each explicit intent is supposed to be handled by a specific app component like Activity, BroadcastReceiver or a Service. Hence PendingIntent uses the following methods to handle the different types of intents:

  1. **PendingIntent.getActivity()** : Retrieve a PendingIntent to start an Activity
  2. **PendingIntent.getBroadcast()** : Retrieve a PendingIntent to perform a Broadcast
  3. **PendingIntent.getService()** : Retrieve a PendingIntent to start a Service
  
#### How to send pending intent?

First, in one of your component, in activity, service or broadcast receiver, where you need pending intent, you need to create Intent object with information required to start an activity in your application. Then using the intent object you can create pending intent object. Then the created PendingIntent object can be passed to target application using target application service from your application. On occurrence of an event, in the target application, which pending intent is tied to, target application sends the intent exists in pending intent, by calling send on PendingIntent.

An example implementation of PendingIntent is given below.

```
Intent intent = new Intent(this, SomeActivity.class);
 
// Creating a pending intent and wrapping our intent
PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
try {
    // Perform the operation associated with our pendingIntent
    pendingIntent.send();
} catch (PendingIntent.CanceledException e) {
    e.printStackTrace();
}
```

The operation associated with the pendingIntent is executed using the send() method.

The parameters inside the getActivity() method and their usages are described below :

 1.  **this (context)** : This is the context in which the PendingIntent starts the activity
 2.  **requestCode** : “1” is the private request code for the sender used in the above example. Using it later with the same method again will get back the same pending intent. Then we can do various things like cancelling the pending intent with cancel(), etc.
 3.  **intent** : Explicit intent object of the activity to be launched
 4.  **flag** : One of the PendingIntent flag that we’ve used in the above example is FLAG_UPDATE_CURRENT. This one states that if a previous PendingIntent already exists, then the current one will update it with the latest intent. There are many other flags like **FLAG_CANCEL_CURRENT** etc.

Now let's use PendingIntent with AlarmManager and NotificationManager.

#### Creating Android Notification using PendingIntent

First of all, create an intent and pass that intent to the PendingIntent as shown below.

```
Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SuvamPramanik/Android-Tutorials/tree/master/PendingIntent/"));
        // Creating a pending intent and wrapping our intent
PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
```

Now, create a notification using [NotificationCompat.Builder](https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html).

```
//Create a notification
NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
builder.setSmallIcon(android.R.drawable.ic_dialog_alert)
       .setContentIntent(pendingIntent)
       .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
       .setContentTitle("Notification Title")
       .setSubText("Tap to open athe tutorial")
       .setContentText("Put your notification content here.");

NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
manager.notify(NOTIFICATION_ID, builder.build());
```
The method **setContentIntent(pendingIntent)** is used to pass our pendingIntent to the NotificationManager. The NOTIFICATION_ID is set to 1 and it's used to build the notification and cancel it. On running the code, you will get something like this.

[![Watch the Output](https://github.com/SuvamPramanik/Android-Tutorials/edit/master/PendingIntent/asset/android-notifications-output.gif)]

#### Using PendingIntent wiht AlarmManager

Using pending intent with Alarm Manager is very similar to that of NotificationManager. First create an intent and wrap it using PendingIntent. 

Below code shows how to create an alarm manager and pass a pending intent to it.

```
Intent intent = new Intent(this, MyReceiver.class);
PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//calls the alarm
AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//trigger after 1 second
alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
            + (1000), pendingIntent);
```

_**Note:** I am passing the pendingIntent as an argument to alarmManager.set() function._

Now, create a broadcastreceiver class _**MyReceiver.java**_ which should have the following content.

```
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm triggered!!!", Toast.LENGTH_LONG).show();
    }

}
```

The above code snippets will show a Toast message "Alarm triggered!!!" after a second of clicking the Start button.

[![Output](https://github.com/SuvamPramanik/Android-Tutorials/edit/master/PendingIntent/asset/android-alarmmanager-output.png)]


**To get a hands-on experience, checkout this repo using git clone command shown below. And then, launch the PendingIntent project in Android Studio.**

```
git clone https://github.com/SuvamPramanik/Android-Tutorials.git
```

_**Note:** To create AlarmManager using PendingIntent, please open **AndroidManifest.xml** and change the activity android:name to ".SecondActivity"._

```
<activity android:name=".SecondActivity">
```
