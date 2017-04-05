package com.fiuady.hadp.compustore;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Category;
import com.fiuady.db.CompuStore;

import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    EditText txtAdd;

    private class CategoryHolder extends RecyclerView.ViewHolder {

        private TextView txtDescription;


        public CategoryHolder(View itemView) {
            super(itemView);
            txtDescription = (TextView) itemView.findViewById(android.R.id.text1);
        }

        public void bindCategory(final Category category) {
            txtDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(CategoriesActivity.this, txtDescription);
                    popup.getMenuInflater().inflate(R.menu.menu_option, popup.getMenu());

                    if (compustore.deleteCategory(category.getId(), false)) {
                        popup.getMenu().removeItem(R.id.menu_1);
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals(popup.getMenu().getItem(0).getTitle())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CategoriesActivity.this);
                                View view = getLayoutInflater().inflate(R.layout.agregar_categoria, null);
                                TextView txtTitle = (TextView) view.findViewById(R.id.dialog_tittle);
                                txtAdd = (EditText) view.findViewById(R.id.dialog_text);

                                txtTitle.setText(R.string.Edita_categoria);

                                builder.setCancelable(false);
                                builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.texto_modificar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (compustore.updateCategory(txtAdd.getText().toString(), category.getId())) {
                                            Toast.makeText(CategoriesActivity.this, R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                                            categoryadapter = new CategoryAdapter(compustore.getAllCategories());
                                            recyclerview.setAdapter(categoryadapter);
                                        } else {
                                            Toast.makeText(CategoriesActivity.this, R.string.Error_operacion, Toast.LENGTH_SHORT).show();


                                        }
                                    }
                                });
                                builder.setView(view);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {
                                AlertDialog.Builder build = new AlertDialog.Builder(CategoriesActivity.this);
                                build.setCancelable(false);
                                build.setTitle(getString(R.string.Elimina_categoria));
                                build.setMessage("La siguiente categoría será eliminada" + ": " + category.getDescription());

                                build.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.texto_eliminar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(CategoriesActivity.this, R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                                        compustore.deleteCategory(category.getId(), true);
                                        categoryadapter = new CategoryAdapter(compustore.getAllCategories());
                                        recyclerview.setAdapter(categoryadapter);
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
            txtDescription.setText(category.getDescription());
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private List<Category> categories;

        public CategoryAdapter(List<Category> categories) {
            this.categories = categories;
        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            return new CategoryHolder(view);

        }

        @Override
        public void onBindViewHolder(CategoryHolder holder, int position) {
            holder.bindCategory(categories.get(position));
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }


    }

    private CompuStore compustore;
    private RecyclerView recyclerview;
    private CategoryAdapter categoryadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        if (Build.VERSION.SDK_INT >= 21){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorCategories));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorCategories));
        }

        compustore = new CompuStore(this);

        recyclerview= (RecyclerView) findViewById(R.id.activity_categories);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        categoryadapter = new CategoryAdapter(compustore.getAllCategories());

        recyclerview.setAdapter(categoryadapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.agregar_categoria, null);
        TextView txtTitle = (TextView) view.findViewById(R.id.dialog_tittle);
        txtAdd = (EditText) view.findViewById(R.id.dialog_text);
        txtTitle.setText(R.string.Agrega_categoria);
        builder.setCancelable(false);

        builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.texto_guardar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (compustore.InsertCategory(txtAdd.getText().toString())) {
                    Toast.makeText(CategoriesActivity.this, R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                    categoryadapter = new CategoryAdapter(compustore.getAllCategories());
                    recyclerview.setAdapter(categoryadapter);
                } else {
                    Toast.makeText(CategoriesActivity.this, R.string.Error_operacion, Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        return super.onOptionsItemSelected(item);

    }
}

