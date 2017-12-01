package org.pctechtips.george.randomquotes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class QuotesActivity extends AppCompatActivity {

    FetchQuote getQuoteTask;
    String randQuote = "";
    String author;
    TextView quoteTextView;
    //    ImageView bgImageView;
    ImageView nextQuoteImageView;
    ImageView shareQuoteImageView;
    String url = "http://quotesondesign.com/wp-json/posts?filter[orderby]=rand&filter[posts_per_page]=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quote_activity);

        //setting the ActionBar for the activity
//        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);

        quoteTextView = (TextView) findViewById(R.id.quote_text);
        nextQuoteImageView = (ImageView) findViewById(R.id.next_quote);
        shareQuoteImageView = (ImageView) findViewById(R.id.share_quote);

        //executing asynctask when activity loads
        getQuoteTask = new FetchQuote();
        getQuoteTask.execute();

        /*onclick listener for next quote*/
        nextQuoteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchQuote fetchQuote = new FetchQuote();
                fetchQuote.execute();
            }
        });

        /*onclick listener for share quote*/
        shareQuoteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, randQuote +" --" + author);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    /*
    * async class to get random quote in background
    */
    class FetchQuote extends AsyncTask<Void, Void, Void> {
        String quote = "";
        //regex pattern for ' single quote conversion to &#8217;
        String regexStr = "&#[0-9]+;";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Toast.makeText(MainActivity.this,"Fetching Quote!",Toast.LENGTH_SHORT).show();
        }

        /*
        * making connection and parsing json data
        */
        @Override
        protected Void doInBackground(Void... voids) {
            Log.v("url", url);
            try {
                URL uri = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = bf.readLine()) != null) {
                    Log.v("line: ", line);
                    quote += line;
                }
                bf.close();
            }
            catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }

            //json parsing
            try {
                JSONArray ja = new JSONArray(quote);
                JSONObject jo = (JSONObject) ja.get(0);
                /*stripping html tags and fixing single quote regex*/
                randQuote = jo.get("content").toString().replaceAll("\\<[^>]*>","");
                randQuote = randQuote.replaceAll(regexStr, "'");
                author = jo.get("title").toString();
                Log.v("QUOTE", randQuote+" "+author);
            }
            catch (JSONException ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            Log.v("Post Exec", randQuote);
            quoteTextView.setText(randQuote +"\n"+author);
        }
    }

}
