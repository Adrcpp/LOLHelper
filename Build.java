package com.example.adrien.lolhelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Adrien CESARO on 10/03/2016.
 */
public class Build extends Activity {
    EditText champSelect;
    Button button;

    @Override
    protected void onCreate(Bundle savedBundle){
        super.onCreate(savedBundle);
        setContentView(R.layout.build_layout);

        champSelect = (EditText) findViewById(R.id.champSelect_build);
        button = (Button) findViewById(R.id.button_build);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = champSelect.getText().toString();
            }
        });
    }

}
