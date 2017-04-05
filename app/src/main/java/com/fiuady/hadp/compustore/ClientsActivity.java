package com.fiuady.hadp.compustore;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ClientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorClientes));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorClientes));
        }
    }
}
