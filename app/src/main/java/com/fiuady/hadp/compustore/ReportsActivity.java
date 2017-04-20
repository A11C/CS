package com.fiuady.hadp.compustore;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ReportsActivity extends AppCompatActivity {
private ImageButton  btnproductos, btnconfirma, btnresumen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        btnproductos=(ImageButton)findViewById(R.id.Button_Faltantes);
        btnconfirma=(ImageButton)findViewById(R.id.Button_Confirmacion);
        btnresumen=(ImageButton)findViewById(R.id.Button_Ventas);

        btnproductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportsActivity.this, ReportsProductsActivity.class);
                startActivity(i);
            }
        });
        btnconfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportsActivity.this, ReportsOrdersActivity.class);
                startActivity(i);
            }
        });
        btnresumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportsActivity.this, ReportsResumeActivity.class);
                startActivity(i);
            }
        });
    }
}
