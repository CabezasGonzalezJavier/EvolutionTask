package com.thedeveloperworldisyours.evaluationtask.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thedeveloperworldisyours.evaluationtask.R;
import com.thedeveloperworldisyours.evaluationtask.models.Item;
import com.thedeveloperworldisyours.evaluationtask.utils.Constants;
import com.thedeveloperworldisyours.evaluationtask.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class DetailActivity extends ActionBarActivity {

    private ProgressDialog mProgress;
    private String mId;
    private TextView mTitleView;
    private TextView mSubTitleView;
    private TextView mDateView;
    private TextView mBodyView;
    private Item mItem;
    StringBuilder mStringBuilderNameFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mProgress = new ProgressDialog(this, R.style.Transparent);
        mTitleView = (TextView) findViewById(R.id.activity_detail_title);
        mSubTitleView = (TextView) findViewById(R.id.activity_detail_subtitle);
        mDateView = (TextView) findViewById(R.id.activity_detail_date);
        mBodyView = (TextView) findViewById(R.id.activity_detail_body);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        mId = extras.getString(Constants.INTENT);

        mStringBuilderNameFile = new StringBuilder();
        mStringBuilderNameFile.append(mId);
        mStringBuilderNameFile.append(Constants.NAME_EXTENSION);
        getData();
    }

    /**
     * Get data
     */
    public void getData() {

        if (Utils.readFromFile(this, mStringBuilderNameFile.toString()).equals("")) {
            refresh();
        } else {
            buildView();
        }
    }

    /**
     * Get data from internet
     */
    public void refresh() {
        if (Utils.isOnline(this)) {
            RequestTask task = new RequestTask(this);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Constants.URL);
            stringBuilder.append(Constants.CONTENT);
            stringBuilder.append(mId);
            stringBuilder.append(Constants.JSON);
            task.execute(stringBuilder.toString());

        } else {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Built View
     */
    public void buildView() {
        covertJSON(Utils.readFromFile(this, mStringBuilderNameFile.toString()));

    }

    /**
     * Covert data of String to object java
     *
     * @param json
     */
    public void covertJSON(String json) {
        try {
            JSONObject itemFatherJSON = new JSONObject(json);
            JSONObject itemJSON = new JSONObject(itemFatherJSON.get("item").toString());

            mItem = new Item(itemJSON.get("id").toString(), itemJSON.get("title").toString(), itemJSON.get("subtitle").toString(), itemJSON.get("body").toString(), itemJSON.get("date").toString());
            showData();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * Show the data
     */
    public void showData() {
        getSupportActionBar().setTitle(mItem.getTitle());
        getSupportActionBar().setSubtitle(mItem.getSubtitle());
        mTitleView.setText(mItem.getTitle());
        mSubTitleView.setText(mItem.getSubtitle());
        mDateView.setText(mItem.getDate());
        mBodyView.setText(mItem.getBody());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Add a REST interface serving JSON
     */
    public class RequestTask extends AsyncTask<String, String, String> {


        private ProgressDialog mDialog;
        private Activity mActivity;

        public RequestTask(Activity activity) {
            mDialog = new ProgressDialog(activity);
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            mDialog.setMessage(mActivity.getString(R.string.request_task_loading));
            mDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mDialog.dismiss();

            Utils.writeToFile(result, mStringBuilderNameFile.toString(), mActivity);
            buildView();
        }
    }
    /**
     * Created for animation between activities
     */
    public void cameback()
    {
        finish();
        overridePendingTransition(R.anim.right, R.anim.left);
    }

    @Override
    public void onBackPressed()
    {
        cameback();
    }
}

