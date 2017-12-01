package org.pctechtips.george.randomquotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.pctechtips.george.randomquotes.*;


public class MainActivity extends AppCompatActivity {

    TextView titleTextView;
    ImageView mainImageView;
    Button getQuoteBotton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleTextView = (TextView) findViewById(R.id.main_title);
        mainImageView = (ImageView) findViewById(R.id.main_image);
        getQuoteBotton = (Button) findViewById(R.id.get_quote_btn);

        getQuoteBotton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quoteActivityIntent = new Intent(getApplicationContext(), QuotesActivity.class);
                startActivity(quoteActivityIntent);
            }
        });

    }

}
