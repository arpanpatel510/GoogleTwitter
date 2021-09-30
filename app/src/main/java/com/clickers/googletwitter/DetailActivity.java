package com.clickers.googletwitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {


    TextView tvId,tvname,tvTag,tvTwitt;

    String id,name,tag,twitt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_detail);

        tvId = findViewById(R.id.tvId);
        tvname = findViewById(R.id.tvname);
        tvTag = findViewById(R.id.tvTag);
        tvTwitt = findViewById(R.id.tvTwitt);


        id = getIntent().getStringExtra("id");
        twitt = getIntent().getStringExtra("tweet");
        name = getIntent().getStringExtra("country");
        tag = getIntent().getStringExtra("tag");
        Log.d("dataaaaa::::---",id+"   "+twitt+"   "+name+"  "+tag);

        tvId.setText(id);
        tvname.setText(name);
        tvTag.setText(tag);
        tvTwitt.setText(twitt);

    }




}