package com.fiuady.hadp.compustore;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

       // if (Build.VERSION.SDK_INT >= 21) {
        //    getWindow().setNavigationBarColor(getResources().getColor(R.color.colorOrders));
        //    getWindow().setStatusBarColor(getResources().getColor(R.color.colorOrders));
        //}
    }
}
