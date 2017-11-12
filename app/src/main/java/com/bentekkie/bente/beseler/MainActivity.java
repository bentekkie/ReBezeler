package com.bentekkie.bente.beseler;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Settings.canDrawOverlays(this)){
            Intent intent = new Intent(this, BeselService.class);
            startService(intent);
        }
        finish();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, BeselService.class);
        if (intent != null) {
            Log.e("hello","made it");
            this.startService(intent);
        }
    }
}
