package com.fiuady.hadp.compustore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fiuady.db.CompuStore;
import com.fiuady.db.Product;

import java.util.List;


public class ModAssemblyActivity extends AppCompatActivity {

    public static final String IdAssembly = "id";
    public static final String DescAssembly ="description";
    private int Id;

    private class ProductHolder extends RecyclerView.ViewHolder {

        private TextView idtext, catidtext, desctext2, pricetext2, qtytext2;

        public ProductHolder(View itemView) {
            super(itemView);
            idtext = (TextView) itemView.findViewById(R.id.idPr_text);
            catidtext = (TextView) itemView.findViewById(R.id.categoryID_text);
            desctext2 = (TextView) itemView.findViewById(R.id.descriptionPr_text);
            pricetext2 = (TextView) itemView.findViewById(R.id.pricePr_text);
            qtytext2 = (TextView) itemView.findViewById(R.id.qty_text);
        }

        private void bindProduct(final Product product) {

            idtext.setText(String.valueOf(product.getId()));
            catidtext.setText(String.valueOf(product.getCategory_id()));
            pricetext2.setText(String.valueOf(product.getPrice()));
            qtytext2.setText(String.valueOf(product.getQuantity()));
            desctext2.setText(product.getDescription());
        }
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {

        private List<Product> products;

        public ProductAdapter(List<Product> products) {
            this.products = products;
        }

        @Override
        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_products, parent, false);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductHolder holder, int position) {
            holder.bindProduct(products.get(position));
        }

        @Override
        public int getItemCount() {
            return products.size();
        }
    }


    private RecyclerView recyclerview;
    private EditText desctext, pricetext, qtytext;
    private CompuStore compustore;
    private ProductAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assembly);
        desctext = (EditText) findViewById(R.id.descass_text);

        Intent i = new Intent();
        Id = i.getIntExtra("id", 0);
        desctext.setText(i.getStringExtra("desc"));
        compustore = new CompuStore(this);
        recyclerview = (RecyclerView) findViewById(R.id.add_productrv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        UpdateAdapter();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i = new Intent(ModAssemblyActivity.this, AddProductToAssemblyActivity.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    public void UpdateAdapter(){
        adapter = new ProductAdapter(compustore.getAllProductsInAssembly(Id));
        recyclerview.setAdapter(adapter);
    }
}