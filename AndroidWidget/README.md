## [Android Widget](https://developer.android.com/reference/android/appwidget/AppWidgetProvider.html)

In this tutorial, we will create a widget which displays an image. This image is updated every 30 minutes. However, you can register _OnClickListener_ or create a custom button to update the widget whenever the user click it.

![Demo]()

#### 1. Home Screen Widgets

Home screen widgets are broadcast receivers which provide interactive components. They are primarily used on the Android home screen. They typically display some kind of data and allow the user to perform actions with them. For example, a widget can display a short summary of new emails and if the user selects an email, it could start the email application with the selected email.

A widget runs as part of the process of its host. This requires that the widget preserves the permissions of their application.

Widget use [RemoteViews](https://developer.android.com/reference/android/widget/RemoteViews.html) to create their user interface. A RemoteView can be executed by another process with the same permissions as the original application. This way the widget runs with the permissions of its defining application.

The user interface for a Widget is defined by a broadcast receiver. This receiver inflates its layout into an object of type RemoteViews. This object is delivered to Android, which hands it over the home screen application.

#### 2. Steps to create a Widget

To create a widget, you need to follow the below steps:

- [x] Define a layout file

- [x] Create an XML file (AppWidgetProviderInfo) which describes the properties of the widget, e.g. size or the fixed update frequency.

- [x] Create a BroadcastReceiver which is used to build the user interface of the widget.

- [x] Enter the Widget configuration in the AndroidManifest.xml file.

#### 3. How to find Optimal Widget size

Before Android 3.1 a widget always took a fixed amount of cells on the home screen. A cell is usually used to display the icon of one application. As a calculation rule you should define the size of the widget with the formula: **((Number of columns / rows) * 74) - 2**. These are device independent pixels and the -2 is used to avoid rounding errors.

#### 4. Using Views and Layouts
Widget layout can be defined in XML and can be saved in project’s res/layout/ directory. Currently App Widget layouts are based on RemoteViews and android currently supports limited View widget. Creating layout for the widget is same as creating the layout for other activity type. But to make a good widget we need to follow the android widget guidelines.

A RemoteViews object or App Widget currently supports the following layout classes

* FrameLayout
* LinearLayout
* RelativeLayout
* GridLayout

Supported widget classes are

* AnalogClock
* Button
* Chronometer
* ImageButton
* ImageView
* ProgressBar
* TextView
* ViewFlipper
* ListView
* GridView
* StackView
* AdapterViewFlipper

#### 5. Creating and Configuring Widget

To register a widget, you create a broadcast receiver with an intent filter for the **android.appwidget.action.APPWIDGET_UPDATE** action.

```
<receiver
       android:icon="@drawable/icon"
       android:label="Example Widget"
       android:name="MyWidgetProvider" >
       <intent-filter >
            <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
       </intent-filter>

       <meta-data
          android:name="android.appwidget.provider"
          android:resource="@xml/widget_info" />
</receiver>
```

The receiver can get a label and icon assigned. These are used in the list of available widgets in the Android launcher.

You also specify the meta-data for the widget via the **android:name="android.appwidget.provider** attribute. The configuration file referred by this metadata contains the configuration settings for the widget. It contains, for example, the update interface, the size and the initial layout of the widget.

```
<?xml version="1.0" encoding="utf-8"?>
<appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
    android:initialLayout="@layout/widget_layout"
    android:minHeight="72dp"
    android:minWidth="300dp"
    android:updatePeriodMillis="1800000"
    android:resizeMode="horizontal|vertical" >
</appwidget-provider>
```

#### 6. AppWidgetProvider Implementation

The [BroadcastReceiver](https://developer.android.com/reference/android/content/BroadcastReceiver.html) class typically extends the [AppWidgetProvider](https://developer.android.com/reference/android/appwidget/AppWidgetProvider.html) class.

You need to override the **onReceive()** which should contain the logic to update the app wodget. 

The AppWidgetProvider provides the following life cycle methods methods.


_Table 1: AppWidgetProvider Life Cycle methods_

Method | Description
--- | ---
onEnabled() | Called the first time an instance of your widget is added to the home screen.
onDisabled() | Called once the last instance of your widget is removed from the home screen.
onUpdate() | Called for every update of the widget. Contains the ids of appWidgetIds for which an update is needed. Note that this may be all of the AppWidget instances for this provider, or just a subset of them, as stated in the method’s JavaDoc. For example, if more than one widget is added to the home screen, only the last one changes (until reinstall).
onDeleted() | Widget instance is removed from the home screen.


***
 
 
## Implementation of Widget

#### 1. First. let's create a background for the widget. Create a new file _myshape.xml_ inside _res/drawable_ folder.

```
<?xml version="1.0" encoding="UTF-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle" >

    <stroke
        android:width="2dp"
        android:color="#FFFFFFFF" />

    <gradient
        android:angle="225"
        android:endColor="#DD2ECCFA"
        android:startColor="#DD000000" />

    <corners
        android:bottomLeftRadius="7dp"
        android:bottomRightRadius="7dp"
        android:topLeftRadius="7dp"
        android:topRightRadius="7dp" />

</shape>
```

#### 2. Now, define the layout for the AppWidget. Create _widget_layout.xml_ inside _res/layout_ directory.

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="wrap_content"
    android:layout_margin="5sp"
    android:background="@drawable/demo_shape"
    android:orientation="vertical" >

    <RelativeLayout
        android:id="@+id/buttonContainer"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true" >

        <Button
            android:id="@+id/sync_button"
            android:layout_width="30dp"
            android:layout_height="30dp"
            android:layout_centerInParent="true"
            android:background="@drawable/ic_sync_button"
            android:text="" />
    </RelativeLayout>

    <LinearLayout
        android:id="@+id/contentContainer"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:layout_above="@id/buttonContainer"
        android:layout_alignParentTop="true"
        android:orientation="vertical"
        android:padding="8dp" >

       <ImageView
            android:id="@+id/imageView"
            android:layout_width="fill_parent"
            android:layout_height="fill_parent"
            android:paddingBottom="5dp" />
    </LinearLayout>

</RelativeLayout>
```

#### 3. Create _AppWidgetProviderInfo_ in XML

This is used to describe the metadata for an App Widget, such as the App Widget’s layout, update frequency, and the AppWidgetProvider class. This is always defined as XML file. 

Create _demo_widget_provider.xml_ file inside _res/xml_ directory.

```
<?xml version="1.0" encoding="utf-8"?>
<appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
    android:initialLayout="@layout/widget_layout"
    android:minHeight="72dp"
    android:minWidth="300dp"
    android:updatePeriodMillis="1800000"
    android:resizeMode="horizontal|vertical" >
</appwidget-provider>
```

The minWidth and minHeight attribute is used to specify the minimum amount of space the App Widget consumes by default. The default Home screen positions App Widgets in its window based on a grid of cells that have a defined height and width. If the values for an App Widget’s minimum width or height don’t match the dimensions of the cells, then the App Widget dimensions round up to the nearest cell size.

To make your app widget portable across devices, your app widget’s minimum size should never be larger than 4 x 4 cells. Check out the below link for the App Widget Design Guidelines

http://developer.android.com/guide/practices/ui_guidelines/widget_design.html

The updatePeriodMillis attribute defines the update timer for the App Widget framework. It calls AppWidgetProvider by calling the onUpdate() callback method. This update timer is not guaranteed to refresh exactly on the same time.
Note: The lesser the refresh timer, consumes more battery. It is better to always provide user an option to select the refresh timer frequency.

The initialLayout attribute points to the layout resource that defines the App Widget layout.

As of Android 3.1 a widget can be flexible in size, e.g., the user can make it larger or smaller. To enable this for widget, you can use the **_android:resizeMode="horizontal|vertical"_** attribute in the XML configuration file for the widget.

#### 4. Implementing _MyWidgetProvider_ class

Now we need to add a class extending AppWidgetProvider which will be used to control the behavior of the widget. The onUpdate() call back method is used to change the content at runtime. This class is used to interface with the App Widget, based on broadcast events. Using AppWidgetProvider we can receive broadcast events while the App Widget state has been updated, enabled, disabled or deleted.

```
public class MyWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        // initializing widget layout
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        // register for button event
        remoteViews.setOnClickPendingIntent(R.id.sync_button,
                createPendingIntent(context));

        // updating view with initial data
        remoteViews.setImageViewBitmap(R.id.imageView, BitmapFactory.decodeResource(context.getResources(), R.drawable.image1));

        // request for widget update
        pushWidgetUpdate(context, remoteViews);
    }

    public static PendingIntent createPendingIntent(Context context) {
        ++MyWidgetReceiver.count;

        // initiate widget update request
        Intent intent = new Intent();
        intent.setAction(WidgetConsts.WIDGET_UPDATE_ACTION);
        return PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context,
                MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }
}
```

#### Step 5: Writing BroadcastReceiver for your widget

Now, let's write a BroadcastReciever to perform action on the button click. This implementation is pretty straight forward. The onReceive() method is requesting the app widget provider for updating the widget.

```
public class MyWidgetReceiver extends BroadcastReceiver {

    public static int count = 1;  //initialize it to 1 as the default image is R.drawable.image1
    //array to hold the images
    private int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5};

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(WidgetConsts.WIDGET_UPDATE_ACTION)) {
            updateWidgetContent(context, intent);
        }
    }

    private void updateWidgetContent(Context context, Intent intent) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        //updating remote view
        remoteViews.setImageViewBitmap(R.id.imageView, getImageBitmap(context));

        // re-registering for click listener
        remoteViews.setOnClickPendingIntent(R.id.sync_button,
                MyWidgetProvider.createPendingIntent(context));

        MyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
    }

    private Bitmap getImageBitmap(Context context) {
        if(count >= images.length)
            count = 0;
       return BitmapFactory.decodeResource(context.getResources(), images[count]);

    }
}
```

#### 6. Registering receiver in in Android Manifest

This is the most important step in creating a widget. For this, we need to register the app widget in your application Manifest file. To do this, you will use the tag. This block of XML should be placed inside the application tag in application Manifest.

```
<application
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:supportsRtl="true"
    android:theme="@style/AppTheme">

    <receiver android:name=".MyWidgetProvider" >
        <intent-filter>
            <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
        </intent-filter>
        <meta-data
            android:name="android.appwidget.provider"
            android:resource="@xml/demo_widget_provider" />
    </receiver>
    <receiver
        android:name=".MyWidgetReceiver"
        android:label="@string/app_name" >
        <intent-filter>
            <action android:name="com.androidwidget.intent.action.UPDATE_WIDGET" />
        </intent-filter>

        <meta-data
            android:name="android.appwidget.provider"
            android:resource="@xml/demo_widget_provider" />
    </receiver>
</application>
```

_**Note:** The receiver name is the name of your app widget provider class implementation. We add an intent filter for the UPDATE_WIDGET event such that your widget will update at regular intervals._

**To get a hands-on experience, checkout this repo using git clone command shown below. And then, launch the PendingIntent project in Android Studio.**

```
git clone https://github.com/SuvamPramanik/Android-Tutorials.git
```
