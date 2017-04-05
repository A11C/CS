package com.fiuady.hadp.compustore;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.fiuady.db.Category;
import com.fiuady.db.CompuStore;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private Spinner categorispinner;
    private CompuStore compuStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorProducts));
        }

        categorispinner = (Spinner)findViewById(R.id.spinner);
        compuStore = new CompuStore(getApplicationContext());

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        categorispinner.setAdapter(adapter);

        adapter.add("Todos");
        List<Category> categories = compuStore.getAllCategories();
        for(Category category :categories){
            adapter.add(category.getDescription());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}