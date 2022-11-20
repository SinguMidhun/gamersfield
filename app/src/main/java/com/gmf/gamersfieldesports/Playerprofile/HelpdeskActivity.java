package com.gmf.gamersfieldesports.Playerprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gmf.gamersfieldesports.R;

public class HelpdeskActivity extends AppCompatActivity {

    private Button mailbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpdesk);

        String[] mail = "help.gamersfield@gmail.com,help.gamersfield@gmail.com".split(",");

        //referance
        mailbtn = findViewById(R.id.rulesmailbtn);
        mailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setData(Uri.parse("mailto:"));
                mailIntent.setType("text/plain");
                mailIntent.putExtra(Intent.EXTRA_EMAIL, mail);
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Queries");
                mailIntent.putExtra(Intent.EXTRA_TEXT, "Please write your query here");
                startActivity(Intent.createChooser(mailIntent, "Send Email"));
            }
        });
    }
}