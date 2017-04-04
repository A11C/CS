package com.fiuady.hadp.compustore;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Category;
import com.fiuady.db.CompuStore;
import com.fiuady.db.CompuStoreHelper;

import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

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
                    popup.getMenuInflater().inflate(R.menu.option2_menu, popup.getMenu());

                    if (compustore.deleteCategory(category.getId(), false)) {
                        popup.getMenu().removeItem(R.id.menu_item2);
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals(popup.getMenu().getItem(0).getTitle())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CategoriesActivity.this);
                                final View view = getLayoutInflater().inflate(R.layout.dialog_add, null);
                                TextView txtTitle = (TextView) view.findViewById(R.id.add_title);
                                final EditText txtAdd = (EditText) view.findViewById(R.id.add_text);

                                txtTitle.setText(R.string.Edita_categoria);

                                builder.setCancelable(false);
                                builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (compustore.updateCategory(txtAdd.getText().toString(), category.getId())) {
                                            Toast.makeText(CategoriesActivity.this, R.string.add_msg, Toast.LENGTH_SHORT).show();
                                            adapter = new CategoryAdapter(compustore.getAllCategories());
                                            categoryRV.setAdapter(adapter);
                                        } else {
                                            Toast.makeText(CategoriesActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                                builder.setView(view);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {
                                AlertDialog.Builder build = new AlertDialog.Builder(CategoriesActivity.this);
                                build.setCancelable(false);
                                build.setTitle(getString(R.string.category_delete));
                                build.setMessage(R.string.sure_text);

                                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(CategoriesActivity.this, R.string.add_msg, Toast.LENGTH_SHORT).show();
                                        compustore.deleteCategory(category.getId(), true);
                                        adapter = new CategoryAdapter(compustore.getAllCategories());
                                        categoryRV.setAdapter(adapter);
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
    private RecyclerView categoryRV;
    private CategoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        compustore = new CompuStore(this);

        categoryRV = (RecyclerView) findViewById(R.id.activity_categories);
        categoryRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter(compustore.getAllCategories());


        categoryRV.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_add, null);
        TextView txtTitle = (TextView) view.findViewById(R.id.add_title);
        final EditText txtAdd = (EditText) view.findViewById(R.id.add_text);
        txtTitle.setText(R.string.category_add);
        builder.setCancelable(false);

        builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AlertDialog.Builder build = new AlertDialog.Builder(CategoriesActivity.this);
                build.setCancelable(false);
                build.setTitle(getString(R.string.category_add));
                build.setMessage(R.string.sure_text);

                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Toast.makeText(CategoriesActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (compustore.insertCategory(txtAdd.getText().toString())) {
                            Toast.makeText(CategoriesActivity.this, R.string.add_msg, Toast.LENGTH_SHORT).show();
                            adapter = new CategoryAdapter(compustore.getAllCategories());
                            categoryRV.setAdapter(adapter);
                        } else {
                            Toast.makeText(CategoriesActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                build.create().show();
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        return super.onOptionsItemSelected(item);

    }
}

