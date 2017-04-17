package com.fiuady.hadp.compustore;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fiuady.db.Category;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Product;

import java.util.List;


public class AddProductToAssemblyActivity extends AppCompatActivity {

    private class ProductHolder extends RecyclerView.ViewHolder {

        private TextView idtext, catidtext, desctext2, pricetext2, qtytext2;

        public ProductHolder(View itemView) {
            super(itemView);
            idtext = (TextView) itemView.findViewById(R.id.id_text);
            catidtext = (TextView) itemView.findViewById(R.id.categoryID_text);
            desctext2 = (TextView) itemView.findViewById(R.id.description_text);
            pricetext2 = (TextView) itemView.findViewById(R.id.price_text);
            qtytext2 = (TextView) itemView.findViewById(R.id.qty_text);
        }

        private void bindProduct(final Product product) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(AddProductToAssemblyActivity.this, itemView);
                    popupMenu.getMenuInflater().inflate(R.menu.add_prodtoassem, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });
            idtext.setText(Integer.toString(product.getId()));
            catidtext.setText(Integer.toString(product.getCategory_id()));
            pricetext2.setText(Integer.toString(product.getPrice()));
            qtytext2.setText(Integer.toString(product.getQuantity()));
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

    private CompuStore compustore;
    private RecyclerView recyclerview;
    private ProductAdapter adapter;
    private Spinner spinner;
    private ImageButton search;
    private EditText searchtext;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_products);
        relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorAssembliesbg));

        spinner = (Spinner) findViewById(R.id.spinner);
        search = (ImageButton) findViewById(R.id.buscar_button);
        searchtext = (EditText) findViewById(R.id.busqueda_text);
        compustore = new CompuStore(this);

        recyclerview = (RecyclerView) findViewById(R.id.productos_rv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        arrayAdapter.add("Todas");
        final List<Category> categories = compustore.getAllCategories();
        for (Category category : categories) {
            arrayAdapter.add(category.getDescription());
        }

        adapter = new ProductAdapter(compustore.getAllProducts());
        recyclerview.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString() != "Todas"){
                    adapter = new ProductAdapter(compustore.getOneCategoryProduct(position-1));
                    recyclerview.setAdapter(adapter);
                }else{
                    adapter = new ProductAdapter(compustore.getAllProducts());
                    recyclerview.setAdapter(adapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchtext.getFreezesText()!=true){
                    adapter = new ProductAdapter(compustore.getProductName(String.valueOf(searchtext.getText())));
                    recyclerview.setAdapter(adapter);
                    searchtext.setHint(String.valueOf(searchtext.getText()));
                    searchtext.setText(null);

                }
            }
        });

    }


}
