package com.fiuady.hadp.compustore;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
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

    public static class mDialogFragment extends DialogFragment {

        private int Num, Id;
        private String Desc;
        private EditText editText;
        private TextView dialogTitle;
        private CompuStore compustore;

        static mDialogFragment newInstance(int num) {
            mDialogFragment dF = new mDialogFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            dF.setArguments(args);
            return dF;
        }

        static mDialogFragment newInstance2(int num, int id, String desc) {
            mDialogFragment dF = new mDialogFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            args.putInt("id", id);
            args.putString("desc", desc);
            dF.setArguments(args);
            return dF;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Num = getArguments().getInt("num");
            switch (Num) {
                case 0:
                    return DialogAdd();
                case 1:
                    Id = getArguments().getInt("id");
                    Desc = getArguments().getString("desc");
                    return DialogMod();
                case 2:
                    Id = getArguments().getInt("id");
                    Desc = getArguments().getString("desc");
                    return DialogErase();
            }
            return super.onCreateDialog(savedInstanceState);
        }

        public AlertDialog DialogAdd() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.agregar_categoria, null);
            dialogTitle = (TextView) view.findViewById(R.id.dialog_tittleCat);
            editText = (EditText) view.findViewById(R.id.dialog_text);
            dialogTitle.setText(R.string.Agrega_categoria);
            compustore = new CompuStore(getActivity());
            builder.setCancelable(false);

            builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.texto_guardar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if ((editText.getText() != null) && (!compustore.CategoryDescriptionExists(editText.getText().toString()))) {
                        compustore.InsertCategory(editText.getText().toString());
                        Toast.makeText(getActivity(), R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.Error_operacion, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setView(view);
            return builder.create();
        }

        public AlertDialog DialogMod() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.agregar_categoria, null);
            dialogTitle = (TextView) view.findViewById(R.id.dialog_tittleCat);
            editText = (EditText) view.findViewById(R.id.dialog_text);
            editText.setText(Desc);
            dialogTitle.setText(R.string.Edita_categoria);
            compustore = new CompuStore(getActivity());

            builder.setCancelable(false);
            builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.texto_modificar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if ((editText.getText() != null) && (!compustore.CategoryDescriptionExists(editText.getText().toString()))) {
                        compustore.updateCategory(editText.getText().toString(), Id);
                        Toast.makeText(getActivity(), R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.Error_operacion, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setView(view);
            return builder.create();
        }

        public AlertDialog DialogErase() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setTitle(getString(R.string.Elimina_categoria));
            builder.setMessage("La siguiente categoría será eliminada: " + Desc);
            compustore = new CompuStore(getActivity());

            builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.texto_eliminar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    compustore.CategoryDelete(Id);
                    Toast.makeText(getActivity(), R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                }
            });

            return builder.create();
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ((CategoriesActivity) getActivity()).updateAdapter();
        }
    }

    private class CategoryHolder extends RecyclerView.ViewHolder {

        private TextView descriptioncategory;

        public CategoryHolder(View itemView) {
            super(itemView);
            descriptioncategory = (TextView) itemView.findViewById(R.id.category_description);
        }

        public void bindCategory(final Category category) {
            descriptioncategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu menu = new PopupMenu(CategoriesActivity.this, descriptioncategory);
                    menu.getMenuInflater().inflate(R.menu.menu_option_catassem, menu.getMenu());

                    if (compustore.CategoryInProduct(category.getId())) {
                        menu.getMenu().removeItem(R.id.menu_1);
                    }

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals(menu.getMenu().getItem(0).getTitle())) {
                                mDialogFragment fragment = mDialogFragment.newInstance2(1, category.getId(), category.getDescription());
                                fragment.show(getFragmentManager(), "DialogMod");
                            } else {
                                mDialogFragment fragment = mDialogFragment.newInstance2(2, category.getId(), category.getDescription());
                                fragment.show(getFragmentManager(), "DialogErase");
                            }
                            return true;
                        }
                    });
                    menu.show();
                }
            });
            descriptioncategory.setText(category.getDescription());
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private List<Category> categories;

        public CategoryAdapter(List<Category> categories) {
            this.categories = categories;
        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_categories, parent, false);
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
    private CategoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        //if (Build.VERSION.SDK_INT >= 21){
        //   getWindow().setNavigationBarColor(getResources().getColor(R.color.colorCategories));
        //   getWindow().setStatusBarColor(getResources().getColor(R.color.colorCategories));
        //}

        compustore = new CompuStore(this);

        recyclerview = (RecyclerView) findViewById(R.id.activity_categories);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter(compustore.getAllCategories());

        recyclerview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mDialogFragment fragment = mDialogFragment.newInstance(0);
        fragment.show(getFragmentManager(), "DialogAdd");

        return super.onOptionsItemSelected(item);

    }

    public void updateAdapter() {
        adapter = new CategoryAdapter(compustore.getAllCategories());
        recyclerview.setAdapter(adapter);
    }
}

