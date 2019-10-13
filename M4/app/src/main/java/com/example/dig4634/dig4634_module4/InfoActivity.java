package com.example.dig4634.dig4634_module4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        Bundle extras = getIntent().getExtras();
        if(extras !=null){

            String text = extras.getString("text");
            TextView myTextField=findViewById(R.id.myTextField);
            myTextField.setText(text);

            String button_text = extras.getString("button");

            Button myButton=findViewById(R.id.myButton);
            myButton.setText(button_text);

            int image_id = extras.getInt("image");
            if(image_id!=0) {
                ImageView myFrame = findViewById(R.id.myFrame);
                myFrame.setImageResource(image_id);
            }
        }

    }

    public void onClick(View view){
        finish();
    }
}
