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
    private Spinner spinneradd;
    Category category;

    private class ProductHolder extends RecyclerView.ViewHolder {

        private TextView idtag, idtext, catidtag, catidtext, desctag2, desctext2, pricetag2, pricetext2, qtytext2, qtytag2;

        public ProductHolder(View itemView) {
            super(itemView);
            idtag = (TextView) itemView.findViewById(R.id.id_tag);
            idtext = (TextView) itemView.findViewById(R.id.id_text);
            catidtag = (TextView) itemView.findViewById(R.id.categoryID_tag);
            catidtext = (TextView) itemView.findViewById(R.id.categoryID_text);
            desctag2 = (TextView) itemView.findViewById(R.id.description_tag);
            desctext2 = (TextView) itemView.findViewById(R.id.description_text);
            pricetag2 = (TextView) itemView.findViewById(R.id.price_tag);
            pricetext2 = (TextView) itemView.findViewById(R.id.price_text);
            qtytext2 = (TextView) itemView.findViewById(R.id.qty_text);
            qtytag2 = (TextView) itemView.findViewById(R.id.qty_tag);
        }

        public void bindProduct(final Product product) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(ProductsActivity.this, itemView);
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
                            } else {
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
            idtext.setText(Integer.toString(product.getId()));
//            category = compustore.getOneCategory(product.getCategory_id());
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

    private EditText desctext, pricetext, qtytext;
    private TextView cattag, desctag, pricetag, qtytag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);


        //    if(Build.VERSION.SDK_INT >= 21){
        //      getWindow().setNavigationBarColor(getResources().getColor(R.color.colorProducts));
        //    getWindow().setStatusBarColor(getResources().getColor(R.color.colorProducts));
        // }

        spinner = (Spinner) findViewById(R.id.spinner);
        compustore = new CompuStore(this);

        recyclerview = (RecyclerView) findViewById(R.id.productos_rv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        spinner.setAdapter(arrayAdapter);

        arrayAdapter.add("Todos");
        List<Category> categories = compustore.getAllCategories();
        for (Category category : categories) {
            arrayAdapter.add(category.getDescription());
        }

        adapter = new ProductAdapter(compustore.getAllProducts());

        recyclerview.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add_product, null);

        dialogTitle = (TextView) view.findViewById(R.id.title_product);
        cattag = (TextView) view.findViewById(R.id.category_tag);
        desctag = (TextView) view.findViewById(R.id.description_tag);
        pricetag = (TextView) view.findViewById(R.id.precio_tag);
        qtytag = (TextView) view.findViewById(R.id.cantidad_tag);
        desctext = (EditText) view.findViewById(R.id.description_text);
        pricetext = (EditText) view.findViewById(R.id.precio_text);
        qtytext = (EditText) view.findViewById(R.id.cantidad_text);
        spinneradd = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        spinneradd.setAdapter(arrayAdapter);

        List<Category> categories = compustore.getAllCategories();
        for (Category category : categories) {
            arrayAdapter.add(category.getDescription());
        }

        builder.setCancelable(false);

        builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.texto_guardar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //   if (compustore.insertProduct(desctext.getText().toString(),)) {
                //       Toast.makeText(ProductsActivity.this, R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                //       adapter = new ProductAdapter(compustore.getAllProducts());
                //       recyclerview.setAdapter(adapter);
                //   } else {
                //      Toast.makeText(ProductsActivity.this, R.string.Error_operacion, Toast.LENGTH_SHORT).show();
                //   }
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        return super.onOptionsItemSelected(item);
    }
}