package com.fiuady.hadp.compustore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton btncategorias, btnproductos, btnensambles, btnclientes, btnordenes, btnreportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btncategorias = (ImageButton) findViewById(R.id.Button_Categorias);
        btnproductos = (ImageButton) findViewById(R.id.Button_Productos);
        btnensambles = (ImageButton) findViewById(R.id.Button_Ensambles);
        btnclientes = (ImageButton) findViewById(R.id.Button_Clientes);
        btnordenes = (ImageButton) findViewById(R.id.Button_Ordenes);
        btnreportes = (ImageButton) findViewById(R.id.Button_Reportes);

        btncategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CategoriesActivity.class);
                startActivity(i);
            }
        });
        btnproductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProductsActivity.class);
                startActivity(i);
            }
        });
        btnensambles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AssemblyActivity.class);
                startActivity(i);
            }
        });
        btnclientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ClientsActivity.class);
                startActivity(i);
            }
        });
        btnordenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, OrdersActivity.class);
                startActivity(i);
            }
        });
        btnreportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ReportsActivity.class);
                startActivity(i);
            }
        });
    }
}
