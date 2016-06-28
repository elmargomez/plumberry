package com.elmargomez.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.elmargomez.plumberry.PlumBerryContextMenu;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void whenClicked(View v) {
        PlumBerryContextMenu contextMenu = new PlumBerryContextMenu(this);
        contextMenu.setMenu(R.menu.context_menu);
        contextMenu.anchor(v);
    }

}
