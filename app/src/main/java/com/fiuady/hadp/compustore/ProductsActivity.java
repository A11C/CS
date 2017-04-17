package com.fiuady.hadp.compustore;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Category;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Product;

import java.util.List;

public class ProductsActivity extends AppCompatActivity {


    public static class mDialogFragment extends DialogFragment {

        private int Num, Qty;
        private String Desc;
        private EditText editText, precio;
        private TextView dialogTitle;
        private Spinner spinner;
        private NumberPicker picker;
        private CompuStore compustore;

        static mDialogFragment newInstance(int num) {
            mDialogFragment dF = new mDialogFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            dF.setArguments(args);
            return dF;
        }

        static mDialogFragment newInstance2(int num, int qty) {
            mDialogFragment dF = new mDialogFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            args.putInt("qty", qty);
            dF.setArguments(args);
            return dF;
        }

        static mDialogFragment newInstance3(int num, String description) {
            mDialogFragment dF = new mDialogFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            args.putString("description", description);
            dF.setArguments(args);
            return dF;
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Num = getArguments().getInt("num");
            switch (Num) {
                case 0:
                    return DialogMod();
                case 1:
                    Qty = getArguments().getInt("qty");
                    return DialogStock();
                case 2:
                    Desc = getArguments().getString("description");
                    return DialogErase();
                case 3:
                    return DialogAdd();
            }
            return super.onCreateDialog(savedInstanceState);
        }

        public AlertDialog DialogMod() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.add_product, null);
            dialogTitle = (TextView) view.findViewById(R.id.title_product);
            editText = (EditText) view.findViewById(R.id.description_text);
            precio = (EditText) view.findViewById(R.id.precio_text);
            spinner = (Spinner) view.findViewById(R.id.spinnerPr);
            dialogTitle.setText(R.string.title_ModProducto);
            compustore = new CompuStore(getActivity());

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);

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
                    dialog.dismiss();
                }
            });
            builder.setView(view);
            return builder.create();
        }
        public AlertDialog DialogErase(){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setTitle(getString(R.string.title_EliminarProducto));
            builder.setMessage("El siguiente producto será eliminado: " + Desc);

            builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.texto_eliminar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                }
            });
            return builder.create();
        }

        public AlertDialog DialogStock(){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.stock));
            picker = new NumberPicker(getActivity());
            picker.setMinValue(Qty);
            picker.setValue(Qty);
            picker.setMaxValue(Qty+100);

            builder.setCancelable(false);
            builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.texto_guardar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            builder.setView(picker);
            return builder.create();
        }

        public AlertDialog DialogAdd(){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.add_product, null);
            editText = (EditText) view.findViewById(R.id.description_text);
            precio = (EditText) view.findViewById(R.id.precio_text);
            spinner = (Spinner) view.findViewById(R.id.spinnerPr);
            compustore = new CompuStore(getActivity());

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);

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
                    dialog.dismiss();
                }
            });
            builder.setView(view);
            return builder.create();
        }
    }

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



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(ProductsActivity.this, itemView);
                    popup.getMenuInflater().inflate(R.menu.menu_option_products, popup.getMenu());

                    if (compustore.ProductInAssembly(product.getId())) {
                        popup.getMenu().removeItem(R.id.menu_2);
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals(popup.getMenu().getItem(0).getTitle())) {
                                mDialogFragment fragment = mDialogFragment.newInstance(0);
                                fragment.show(getFragmentManager(),"ModFragment");
                            } else if(item.getTitle().equals(popup.getMenu().getItem(1).getTitle())){
                                mDialogFragment fragment = mDialogFragment.newInstance2(1, product.getQuantity());
                                fragment.show(getFragmentManager(),"StockFragment");
                            } else{
                                mDialogFragment fragment = mDialogFragment.newInstance3(2, product.getDescription());
                                fragment.show(getFragmentManager(),"EraseFragment");
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });
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

    private CompuStore compustore;
    private RecyclerView recyclerview;
    private ProductAdapter adapter;
    private Spinner spinner;
    private ImageButton search;

    private EditText desctext, pricetext, qtytext, searchtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);


        //    if(Build.VERSION.SDK_INT >= 21){
        //      getWindow().setNavigationBarColor(getResources().getColor(R.color.colorProducts));
        //    getWindow().setStatusBarColor(getResources().getColor(R.color.colorProducts));
        // }

        spinner = (Spinner) findViewById(R.id.spinnerCat);
        search = (ImageButton) findViewById(R.id.buscarCat_button);
        searchtext = (EditText) findViewById(R.id.busquedaCat_text);
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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchtext.getText()!= null){
                    if (spinner.getSelectedItemPosition() != 0){
                        adapter = new ProductAdapter(compustore.getProductByName(spinner.getSelectedItemPosition()-1, String.valueOf(searchtext.getText())));
                        recyclerview.setAdapter(adapter);
                    }else{
                        adapter = new ProductAdapter(compustore.getAllProductsByName(String.valueOf(searchtext.getText())));
                        recyclerview.setAdapter(adapter);
                    }
                }
                else{
                    if (spinner.getSelectedItemPosition() != 0){
                        adapter = new ProductAdapter(compustore.getOneCategoryProduct(spinner.getSelectedItemPosition()-1));
                        recyclerview.setAdapter(adapter);
                    }else{
                        adapter = new ProductAdapter(compustore.getAllProducts());
                        recyclerview.setAdapter(adapter);
                    }


                }
                searchtext.setHint(String.valueOf(searchtext.getText()));
                searchtext.setText(null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mDialogFragment fragment = mDialogFragment.newInstance(3);
        fragment.show(getFragmentManager(),"AddFragment");
        return super.onOptionsItemSelected(item);
    }

}