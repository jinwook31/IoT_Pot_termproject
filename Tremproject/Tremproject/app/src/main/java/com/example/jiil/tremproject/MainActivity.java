package com.example.jiil.tremproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView text_deg;
    TextView text_hum;
    TextView text_test;
    Timer timer_deg = new Timer();
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if (id == R.id.nav_gallery) {
           Intent intent_manage = new Intent(getApplicationContext(),plant_manage.class);
           startActivity(intent_manage);
        } else if (id == R.id.nav_slideshow) {//화분관리
            Intent intent_guide = new Intent(getApplicationContext(),plantguide.class);
            startActivity(intent_guide);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onclicktalk(View v) {
        Intent intent_02 = new Intent(getApplicationContext(),ChatBubbleActivity.class);
        startActivity(intent_02);
    }


    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event) {
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
                        .setPositiveButton("아니오",null).show();
                return false;
            default:
                return false;
        }
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Parse 데이터 가져오는 중...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
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
                        public void done(List<ParseObject> objects, com.parse.ParseException e) {
                            ParseObject parse_deg = null;
                            if (e == null) {
                                Iterator iterator_deg = objects.listIterator();
                                text_deg = (TextView) findViewById(R.id.textView2);
                                while (iterator_deg.hasNext())
                                    parse_deg = (ParseObject) iterator_deg.next();
                                if (parse_deg != null) {
                                    text_deg.setText(""+parse_deg.get("temperature").toString()+"&apos;c");
                                } else
                                    text_deg.setText("Temperature Error");
                            }
                        }
                    });
                    ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Humidity");
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, com.parse.ParseException e) {
                            ParseObject parse_deg = null;
                            if (e == null) {
                                Iterator iterator_deg = objects.listIterator();
                                text_hum = (TextView) findViewById(R.id.textView3);
                                while (iterator_deg.hasNext())
                                    parse_deg = (ParseObject) iterator_deg.next();
                                if (parse_deg != null) {
                                    text_hum.setText(""+parse_deg.get("Humidity").toString());
                                } else
                                    text_hum.setText("Temperature Error");
                            }
                        }
                    });
                }
            }, 0, 10000);
            return null;
        }
    }
}
