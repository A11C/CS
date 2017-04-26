package com.fiuady.hadp.compustore;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Assembly;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ModAssemblyActivity extends AppCompatActivity {

    public static final String EXTRA_IDASSEMBLY = " com.fiuady.hadp.compustore.extra_id";
    public static final String EXTRA_DESCASSEMBLY = " com.fiuady.hadp.compustore.extra_description";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_ID = "id";
    public static final int REQUESTCODE = 0;
    private int Id;

    public static class mDialogFragment extends DialogFragment {

        private int Num, Qty, Id, Pid;
        private String Desc;
        private NumberPicker picker;
        // private CompuStore compustore;

        static mDialogFragment newInstance(int num, int qty, int pid) {
            mDialogFragment dF = new mDialogFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            args.putInt("pid", pid);
            args.putInt("qty", qty);
            dF.setArguments(args);
            return dF;
        }

        static mDialogFragment newInstance2(int num, String description, int pid) {
            mDialogFragment dF = new mDialogFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            args.putString("desc", description);
            args.putInt("pid", pid);
            dF.setArguments(args);
            return dF;
        }




        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Num = getArguments().getInt("num");
            switch (Num) {
                case 0:
                    Qty = getArguments().getInt("qty");
                    Pid = getArguments().getInt("pid");
                    return DialogMod();
                case 1:
                    Desc = getArguments().getString("desc");
                    Pid = getArguments().getInt("pid");
                    return DialogErase();
            }

            return super.onCreateDialog(savedInstanceState);
        }

        public AlertDialog DialogMod() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.stock));
            picker = new NumberPicker(getActivity());
            picker.setMinValue(1);
            picker.setMaxValue(Qty + 10);
            picker.setValue(Qty + 1);
            //compustore = new CompuStore(getActivity());

            builder.setCancelable(false);
            builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.texto_guardar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ((ModAssemblyActivity) getActivity()).UpdateProduct(Pid, picker.getValue());
                    //compustore.AddQtyProductInAssembly(Pid, Id, picker.getValue());
                    ((ModAssemblyActivity) getActivity()).UpdateAdapter();
                    Toast.makeText(getActivity(), R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                }
            });
            builder.setView(picker);
            return builder.create();
        }

        public AlertDialog DialogErase() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setTitle(getString(R.string.title_EliminarProducto));
            builder.setMessage("El siguiente producto será eliminado del ensamble: " + Desc);
            //compustore = new CompuStore(getActivity());

            builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.texto_eliminar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ((ModAssemblyActivity) getActivity()).DeleteProduct(Pid);
                    //compustore.DeleteProductInAssembly(Pid,Id);
                    ((ModAssemblyActivity) getActivity()).UpdateAdapter();
                    Toast.makeText(getActivity(), R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                }
            });
            return builder.create();
        }
    }

    private class ProductHolder extends RecyclerView.ViewHolder {

        private TextView idtext, catidtext, desctext, pricetext, qtytext, qtytag;

        public ProductHolder(View itemView) {
            super(itemView);
            idtext = (TextView) itemView.findViewById(R.id.idPr_text);
            catidtext = (TextView) itemView.findViewById(R.id.categoryID_text);
            desctext = (TextView) itemView.findViewById(R.id.descriptionPr_text);
            pricetext = (TextView) itemView.findViewById(R.id.pricePr_text);
            qtytag = (TextView) itemView.findViewById(R.id.qty_tag);
            qtytext = (TextView) itemView.findViewById(R.id.qty_text);
        }

        private void bindProduct(final Product product) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(ModAssemblyActivity.this, itemView);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_option_catassem, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals(popupMenu.getMenu().getItem(0).getTitle())) {
                                mDialogFragment fragment = mDialogFragment.newInstance(0, product.getQuantity(), product.getId());
                                fragment.show(getFragmentManager(), "QtyDialog");
                            } else {
                                mDialogFragment fragment = mDialogFragment.newInstance2(1, product.getDescription(), product.getId());
                                fragment.show(getFragmentManager(), "EraseDialog");
                            }

                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });

            idtext.setText(String.valueOf(product.getId()));
            catidtext.setText(String.valueOf(product.getCategory_id()));
            pricetext.setText(String.valueOf(product.getPrice()));
            qtytext.setText(String.valueOf(product.getQuantity()));
            desctext.setText(product.getDescription());
            qtytag.setText("Cantidad Requerida: ");
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
    private EditText desctext;
    private CompuStore compustore;
    private ProductAdapter adapter;
    private Button btnsave, btncancel;
    private ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assembly);
        desctext = (EditText) findViewById(R.id.descass_text);
        btnsave = (Button) findViewById(R.id.Btn_guardar);
        btncancel = (Button) findViewById(R.id.Btn_cancelar);
        final String description;
        compustore = new CompuStore(this);

        if(savedInstanceState != null)
        {
            description = savedInstanceState.getString(KEY_DESCRIPTION);
            desctext.setText(description);
            Id = savedInstanceState.getInt(KEY_ID);
            products = new ArrayList<Product>(compustore.RestoreProducts());
        }else{
            Intent i = getIntent();
            Id = i.getIntExtra(EXTRA_IDASSEMBLY, 0);
            products = new ArrayList<Product>(compustore.getAllProductsInAssembly(Id));
            description = i.getStringExtra(EXTRA_DESCASSEMBLY);
            desctext.setText(description);
        }


        recyclerview = (RecyclerView) findViewById(R.id.add_productrv);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
        }else{
            recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        }

        UpdateAdapter();

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (desctext.getText().toString().toUpperCase().equals(description.toUpperCase())) {
                    UpdateAssembly();
                } else if (!desctext.getText().toString().isEmpty() && !compustore.AssemblyExists(desctext.getText().toString())) {
                    UpdateAssembly();
                } else {
                    Toast.makeText(ModAssemblyActivity.this, R.string.InvalidAssembly, Toast.LENGTH_SHORT).show();
                }
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

        Intent i = new Intent(ModAssemblyActivity.this, AddProductToAssemblyActivity.class);
        startActivityForResult(i, AddProductToAssemblyActivity.CODE_REQUEST);

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //Intent i = getIntent();
            //Product product = compustore.getProductById(i.getIntExtra(AddProductToAssemblyActivity.EXTRA_PID, -1));
            //Toast.makeText(ModAssemblyActivity.this, String.valueOf(data.getIntExtra(AddProductToAssemblyActivity.EXTRA_PID,-1)), Toast.LENGTH_SHORT).show();
            boolean repeated = false;
            for (Product product : products) {
                if (product.getId() == data.getIntExtra(AddProductToAssemblyActivity.EXTRA_PID, -1)) {
                    Toast.makeText(ModAssemblyActivity.this, "El producto ya se encuentra en el ensamble", Toast.LENGTH_SHORT).show();
                    repeated = true;
                    break;
                }
            }
            if (!repeated) {
                for (Product product : compustore.getAllProducts()) {
                    if (product.getId() == data.getIntExtra(AddProductToAssemblyActivity.EXTRA_PID, -1)) {
                        product.setQuantity(1);
                        products.add(product);
                        break;
                    }
                }
            }
            UpdateAdapter();

        }
        compustore.RestoreProducts();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        compustore.SaveProducts(products);
        outState.putString(KEY_DESCRIPTION,desctext.getText().toString());
        outState.putInt(KEY_ID,Id);
        super.onSaveInstanceState(outState);
    }

    public void UpdateAdapter() {
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o1.getDescription().compareTo(o2.getDescription());
            }
        });
        adapter = new ProductAdapter(products);
        recyclerview.setAdapter(adapter);
    }

    public void DeleteProduct(int pid) {
        int erase = -1;
        for (Product product : products) {
            if (product.getId() == pid) {
                erase = products.indexOf(product);
            }
        }
        products.remove(erase);
    }

    public void UpdateProduct(int pid, int qty) {
        for (Product product : products) {
            if (product.getId() == pid) {
                product.setQuantity(qty);
            }
        }
    }

    public void UpdateAssembly() {
        boolean repeated = true;
        compustore.UpdateAssembly(desctext.getText().toString(), Id);
        for (Product product1 : compustore.getAllProductsInAssembly(Id)) {
            for (Product product : products) {
                if ((product1.getId() == product.getId()) && (product1.getQuantity() != product.getQuantity())) {
                    compustore.UpdateQtyProductInAssembly(product1.getId(), Id, product.getQuantity());
                    repeated = true;
                } else {
                    repeated = false;
                }
            }
            if (!repeated || products.isEmpty()) {
                compustore.DeleteProductInAssembly(product1.getId(), Id);
            }
        }
        for (Product product : products) {
            for (Product product1 : compustore.getAllProductsInAssembly(Id)) {
                if (product1.getId() == product.getId()) {
                    repeated = true;
                } else {
                    repeated = false;
                }
            }
            if (!repeated || compustore.getAllProductsInAssembly(Id).isEmpty()){
                compustore.InsertAssemblyProduct(Id, product.getId(), product.getQuantity());
            }
        }

        setResult(RESULT_OK);
        finish();
    }

}