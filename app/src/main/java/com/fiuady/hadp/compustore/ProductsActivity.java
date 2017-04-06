package com.fiuady.hadp.compustore;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Category;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Product;

import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private EditText editText;
    private TextView dialogTitle;

    private class ProductHolder extends RecyclerView.ViewHolder {

        private TextView descriptionproduct;

        public ProductHolder(View itemView) {
            super(itemView);
            descriptionproduct = (TextView) itemView.findViewById(android.R.id.text1);
        }

        public void bindCategory(final Product product) {
            descriptionproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(ProductsActivity.this, descriptionproduct);
                    popup.getMenuInflater().inflate(R.menu.menu_option, popup.getMenu());

                    if (compustore.deleteProduct(product.getId(), false)) {
                        popup.getMenu().removeItem(R.id.menu_1);
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals(popup.getMenu().getItem(0).getTitle())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
                                View view = getLayoutInflater().inflate(R.layout.agregar_categoria, null);
                                dialogTitle = (TextView) view.findViewById(R.id.dialog_tittle);
                                editText = (EditText) view.findViewById(R.id.dialog_text);

                                editText.setText(R.string.edit_category);

                                builder.setCancelable(false);
                                builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.texto_guardar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AlertDialog.Builder build = new AlertDialog.Builder(ProductsActivity.this);
                                        build.setCancelable(false);
                                        build.setTitle(getString(R.string.edit_category));
                                        build.setMessage(R.string.sure_text);

                                        build.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        }).setPositiveButton(R.string.texto_guardar, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
//
                                            }
                                        });

                                        build.create().show();
                                    }
                                });
                                builder.setView(view);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            else{
                                AlertDialog.Builder build = new AlertDialog.Builder(ProductsActivity.this);
                                build.setCancelable(false);
                                build.setTitle(getString(R.string.delete_category));
                                build.setMessage(R.string.sure_text);

                                build.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.texto_guardar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
//
                                    }
                                });

                                build.create().show();
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });
            descriptionproduct.setText(product.getDescription());
        }
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductsActivity.ProductHolder> {

        private List<Product> products;

        public ProductAdapter(List<Product> products) {
            this.products = products;
        }

        @Override
        public ProductsActivity.ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.categories_list, parent, false);
            return new ProductsActivity.ProductHolder(view);
        }
        @Override
        public void onBindViewHolder(ProductsActivity.ProductHolder holder, int position) {
            holder.bindCategory(products.get(position));
        }
        @Override
        public int getItemCount() {
            return products.size();
        }
    }

    private CompuStore compustore;
    private RecyclerView recyclerview;
    private ProductAdapter adapter;
    private Spinner categoriesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);


    //    if(Build.VERSION.SDK_INT >= 21){
      //      getWindow().setNavigationBarColor(getResources().getColor(R.color.colorProducts));
        //    getWindow().setStatusBarColor(getResources().getColor(R.color.colorProducts));
       // }

        categoriesSpinner = (Spinner)findViewById(R.id.spinner);
        compustore = new CompuStore(this);

        recyclerview= (RecyclerView) findViewById(R.id.productos_rv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(arrayAdapter);

        arrayAdapter.add("Todos");
        List<Category> categories = compustore.getAllCategories();
        for(Category category :categories){
            arrayAdapter.add(category.getDescription());
        }

        recyclerview.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add_product, null);
        dialogTitle = (TextView) view.findViewById(R.id.dialog_tittle);
        editText = (EditText) view.findViewById(R.id.dialog_text);
        dialogTitle.setText(R.string.Agrega_categoria);
        builder.setCancelable(false);

        builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.texto_guardar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (compustore.InsertCategory(editText.getText().toString())) {
                    Toast.makeText(ProductsActivity.this, R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                    adapter = new ProductAdapter(compustore.getAllProducts());
                    recyclerview.setAdapter(adapter);
                } else {
                    Toast.makeText(ProductsActivity.this, R.string.Error_operacion, Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        return super.onOptionsItemSelected(item);
    }
}