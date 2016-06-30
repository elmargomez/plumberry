package com.elmargomez.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.elmargomez.plumberry.dialog.checkbox.PlumBerryCheckBox;

public class CheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void whenChecked(View view) {
        PlumBerryCheckBox checkBox = new PlumBerryCheckBox(this);
        checkBox.show();
    }

}
