package com.example.thanh.mobilefinal;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class food_avg extends Activity {



        TextView avgtext;
        TextView dayText;
        TextView calText;
        FoodDatabase f_db;

        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_food_avg);

            avgtext = (TextView) findViewById(R.id.avaragevalue);
            dayText = (TextView) findViewById(R.id.dayvalue);
            calText = (TextView) findViewById(R.id.yesterdayvalue);

      f_db = new FoodDatabase(this);
        f_db.setWritable();

        String avarage =  f_db.getSUM()+"";
            avgtext.setText(avarage);

        String Day = f_db.getday()+"";
        dayText.setText(Day);

        String yes = f_db.getYesterday()+"";
        calText.setText(yes);

        }

        }

