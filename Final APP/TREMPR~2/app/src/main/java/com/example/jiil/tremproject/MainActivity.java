package com.example.jiil.tremproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int degInt;
    private int humInt;
    private int degData;//기준
    private int humData;//기준
    private String currDegree;
    private String currHumidity;
    private TextView text_deg;
    private TextView text_hum;
    private Timer timer_deg = new Timer();
    private Vibrator v;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 파스 시작
        Parse.initialize(this, "u2Z03LI8SBDu7Rwv7irJtCMdZqaDyzvE07PQwKvU", "h6xEHnAdTkgwfTKY65LD5VkeCGWXgNRxEtCq6cPP");
        new RemoteDataTask().execute();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_slideshow) { // 식물 도감
            Intent intent_guide = new Intent(getApplicationContext(), PlantguideActivity.class);
            startActivity(intent_guide);
        } else if (id == R.id.nav_manage) {
            Context mContext = getApplicationContext();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_setting,(ViewGroup) findViewById(R.id.layout_root));

            android.app.AlertDialog.Builder aDialog = new android.app.AlertDialog.Builder(MainActivity.this);
            aDialog.setTitle("설정 입력");
            aDialog.setView(layout);

            final EditText Degree = (EditText) layout.findViewById(R.id.Degree);
            final EditText Humidity = (EditText) layout.findViewById(R.id.Humidity);

            aDialog.setPositiveButton("취소", null)
                    .setNegativeButton("변경", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    degData = Integer.parseInt(Degree.getText().toString());
                    humData = Integer.parseInt(Humidity.getText().toString());
                }
            });
            android.app.AlertDialog ad = aDialog.create();
            ad.show();
        } else if(id == R.id.set_name_date_species) { //여기부분이 이름 입력하는 부분인데 왜 오류가 나는지 모르겠따...별지랄 다해봄
            final Context mContext = getApplicationContext();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_setdata,(ViewGroup) findViewById(R.id.layout_root));

            android.app.AlertDialog.Builder aDialog = new android.app.AlertDialog.Builder(MainActivity.this);
            aDialog.setTitle("식물 데이터 입력");
            aDialog.setView(layout);

            final EditText edit_species = (EditText) layout.findViewById(R.id.species_editText);
            final EditText edit_date = (EditText) layout.findViewById(R.id.date_editText);
            final TextView text_species = (TextView) findViewById(R.id.species_textView);
            final TextView text_date = (TextView) findViewById(R.id.date_textView);
            final EditText edit =(EditText) layout.findViewById(R.id.name_editText);
            final TextView re = (TextView) findViewById(R.id.name_textView);

            aDialog.setPositiveButton("취소", null)
                    .setNegativeButton("변경", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            String date, species, namedata;
                            species = edit_species.getText().toString();
                            date = edit_date.getText().toString();
                            namedata = edit.getText().toString();

                            if(namedata.getBytes().length <= 0||species.getBytes().length <= 0 || date.getBytes().length <= 0) {
                                Toast.makeText(MainActivity.this,"값을 입력하세요." ,Toast.LENGTH_SHORT).show();;
                            } else {
                                species = "품종: "+species;
                                date = "심은 날짜: "+date;
                                namedata = "이름: " +namedata;
                                re.setText(namedata);
                                text_species.setText(species);
                                text_date.setText(date);
                            }
                        }
                    });
            android.app.AlertDialog ad2 = aDialog.create();
            ad2.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onclicktalk(View v) {
        Intent intent_02 = new Intent(getApplicationContext(), ChatBubbleActivity.class);
        if(degData != 0 && humData != 0) {
            intent_02.putExtra("currDeg", currDegree);
            intent_02.putExtra("currHum", currHumidity);
            intent_02.putExtra("desiDeg", degData);
            intent_02.putExtra("desiHum", humData);

            startActivity(intent_02);
        } else{
            Toast.makeText(getApplicationContext(),"설정에서 식물의 기준 온도와 습도를 입력해주세요.",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setTitle("종료")
                        .setMessage("종료 하시겠습니까?")
                        .setNegativeButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setPositiveButton("아니오", null).show();
                return false;
            default:
                return false;
        }
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the class table named "Country" in Parse.com
            timer_deg.schedule(new TimerTask() {
                @Override
                public void run() {
                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Temperature");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            ParseObject parse_deg = null;
                            if (e == null) {
                                Iterator iterator_deg = objects.listIterator();
                                text_deg = (TextView) findViewById(R.id.textView2);
                                while (iterator_deg.hasNext())
                                    parse_deg = (ParseObject) iterator_deg.next();
                                if (parse_deg != null) {
                                    currDegree = parse_deg.get("temperature").toString();
                                    text_deg.setText("" + currDegree);
                                } else
                                    text_deg.setText("Temperature Error");
                            }
                        }
                    });
                    ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Humidity");
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            ParseObject parse_deg = null;
                            if (e == null) {
                                Iterator iterator_deg = objects.listIterator();
                                text_hum = (TextView) findViewById(R.id.textView3);
                                while (iterator_deg.hasNext())
                                    parse_deg = (ParseObject) iterator_deg.next();
                                if (parse_deg != null) {
                                    currHumidity = parse_deg.get("Humidity").toString();
                                    text_hum.setText("" + currHumidity);
                                } else
                                    text_hum.setText("Temperature Error");
                            }
                        }
                    });
                    if(degData != 0 && humData != 0){
                        degInt = Integer.parseInt(currDegree);
                        humInt = Integer.parseInt(currHumidity);
                        if(degInt > degData+5){
                            PushAlarm_HighDeg();
                        } else if(degInt < degData-5){
                            PushAlarm_LowDeg();
                        }

                        if(humInt > humData+5){
                            PushAlarm_HighHum();
                        } else if(humInt < humData-5){
                            PushAlarm_LowHum();
                        }
                    }
                }
            }, 0, 60000);
            return null;
        }
    }

    private void PushAlarm_LowDeg(){
        Intent intent = new Intent();
        PendingIntent pIntetn = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        Notification noti = new Notification.Builder(MainActivity.this)
                .setTicker("i-Pot")
                .setContentTitle("i-Pot Push")
                .setContentText("식물이 추워서 덜덜 떨고 있습니다.")
                .setSmallIcon(R.drawable.sun)
                .setContentIntent(pIntetn).getNotification();

        noti.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0,noti);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
    }

    private void PushAlarm_HighDeg(){
        Intent intent = new Intent();
        PendingIntent pIntetn = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        Notification noti = new Notification.Builder(MainActivity.this)
                .setTicker("i-Pot")
                .setContentTitle("i-Pot Push")
                .setContentText("식물이 더워서 땀을 흘리고 있습니다.")
                .setSmallIcon(R.drawable.sun)
                .setContentIntent(pIntetn).getNotification();

        noti.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0,noti);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
    }

    private void PushAlarm_LowHum(){
        Intent intent = new Intent();
        PendingIntent pIntetn = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        Notification noti = new Notification.Builder(MainActivity.this)
                .setTicker("i-Pot")
                .setContentTitle("i-Pot Push")
                .setContentText("식물의 목이 활활 타고 있습니다.")
                .setSmallIcon(R.drawable.sun)
                .setContentIntent(pIntetn).getNotification();

        noti.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0,noti);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
    }

    private void PushAlarm_HighHum(){
        Intent intent = new Intent();
        PendingIntent pIntetn = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        Notification noti = new Notification.Builder(MainActivity.this)
                .setTicker("i-Pot")
                .setContentTitle("i-Pot Push")
                .setContentText("식물의 배가 터질려고 합니다.")
                .setSmallIcon(R.drawable.sun)
                .setContentIntent(pIntetn).getNotification();

        noti.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0,noti);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
    }
}
