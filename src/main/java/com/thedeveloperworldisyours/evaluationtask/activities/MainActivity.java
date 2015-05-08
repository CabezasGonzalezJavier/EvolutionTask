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
import android.widget.Toast;

import com.thedeveloperworldisyours.evaluationtask.R;
import com.thedeveloperworldisyours.evaluationtask.adapters.ListAdapter;
import com.thedeveloperworldisyours.evaluationtask.models.Article;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ProgressDialog mProgress;
    private ListView mListView;
    private List<Item> mItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgress = new ProgressDialog(this, R.style.Transparent);
        mListView = (ListView) findViewById(R.id.activity_main_listView);
        mItemList = new ArrayList<Item>();

        getData();
    }

    public void getData() {
        if (Utils.isOnline(MainActivity.this)) {
            RequestTask task = new RequestTask(MainActivity.this);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Constants.URL);
            stringBuilder.append(Constants.CONTENT_LIST);
            task.execute(stringBuilder.toString());

        } else {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
    }

    public void buildListView() {
        returnRequest(this);

        mListView.setAdapter(new ListAdapter(MainActivity.this, 0, mItemList));
//        mListView.setOnItemClickListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public void returnRequest(Activity activity){
        String json = Utils.readFromFile(activity);

        try {

            JSONObject obj = new JSONObject(json);
            JSONArray array = new JSONArray();
            array = (JSONArray) obj.get("items");
            getListData(array);
            Log.d("My App", array.toString());
//            json.get("name")

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
        }
    }

    public void getListData(JSONArray itemsArray) throws JSONException {
        if(itemsArray!=null && itemsArray.length()>0){

            JSONObject itemJSON = new JSONObject();

            for (int i=0; i <itemsArray.length();i++){
                itemJSON = itemsArray.getJSONObject(i);
                Item item = new Item(itemJSON.get("id").toString(),itemJSON.get("title").toString(),itemJSON.get("subtitle").toString(),itemJSON.get("date").toString());
                mItemList.add(item);
            }
        }
    }

    public class RequestTask extends AsyncTask<String, String, String> {


        private ProgressDialog mDialog;
        private Activity mActivity;

        public RequestTask(MainActivity activity) {
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
            Log.d("RequestTask", result.toString());
            Utils.writeToFile(result, mActivity);
            buildListView();
        }
    }

}
