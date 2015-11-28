package term.grow.com.term;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    // Declare Variables
    int test_data = 0;
    TextView text_deg;
    TextView text_hum;
    TextView text_test;
    Timer timer_deg = new Timer();
    Timer timer_hum = new Timer();

    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.activity_main);
        // Execute RemoteDataTask AsyncTask
        Parse.initialize(this, "u2Z03LI8SBDu7Rwv7irJtCMdZqaDyzvE07PQwKvU", "h6xEHnAdTkgwfTKY65LD5VkeCGWXgNRxEtCq6cPP");

        new RemoteDataTask().execute();
    }

    // RemoteDataTask AsyncTask
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
                                text_deg = (TextView) findViewById(R.id.textview);
                                while (iterator_deg.hasNext())
                                    parse_deg = (ParseObject) iterator_deg.next();
                                if (parse_deg != null) {
                                    text_deg.setText("온도 :" + parse_deg.get("temperature").toString());
                                } else
                                    text_deg.setText("Temperature Error");
                            }
                        }
                    });

                    ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Country");
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, com.parse.ParseException e) {
                            ParseObject parse_deg = null;
                            if (e == null) {
                                Iterator iterator_deg = objects.listIterator();
                                test_data++;
                                text_hum = (TextView) findViewById(R.id.textview2);
                                text_test = (TextView) findViewById(R.id.textview3);
                                text_test.setText(""+test_data);
                                while (iterator_deg.hasNext())
                                    parse_deg = (ParseObject) iterator_deg.next();
                                if (parse_deg != null) {
                                    text_hum.setText("나라 :" + parse_deg.get("name").toString());
                                } else
                                    text_hum.setText("Temperature Error");
                            }
                        }
                    });


                }
            }, 0, 1000);
            return null;
        }
    }
}

