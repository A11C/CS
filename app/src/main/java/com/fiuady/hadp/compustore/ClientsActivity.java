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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.fiuady.db.Client;
import com.fiuady.db.CompuStore;

import java.util.List;

public class ClientsActivity extends AppCompatActivity {

    private EditText editText;
    private TextView dialogTitle;
    private Spinner spinneradd;
    Client client;

    private class ClientHolder extends RecyclerView.ViewHolder {

        private TextView idtext, nombtext,
                lnombtext, directext, tel1text,
                tel2text, tel3text, emailtext;

        public ClientHolder(View itemView) {
            super(itemView);
            idtext = (TextView) itemView.findViewById(R.id.id_text);
            nombtext = (TextView) itemView.findViewById(R.id.nombre_text);
            lnombtext = (TextView) itemView.findViewById(R.id.apellido_text);
            directext = (TextView) itemView.findViewById(R.id.direc_text);
            tel1text = (TextView) itemView.findViewById(R.id.tel1_text);
            tel2text = (TextView) itemView.findViewById(R.id.tel2_text);
            tel3text = (TextView) itemView.findViewById(R.id.tel3_text);
            emailtext = (TextView) itemView.findViewById(R.id.email_text);
        }

        public void bindClient(final Client client) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(ClientsActivity.this, itemView);
                    popup.getMenuInflater().inflate(R.menu.menu_option_products, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals(popup.getMenu().getItem(0).getTitle())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ClientsActivity.this);
                                View view = getLayoutInflater().inflate(R.layout.agregar_categoria, null);
                                dialogTitle = (TextView) view.findViewById(R.id.dialog_tittle);
                                editText = (EditText) view.findViewById(R.id.dialog_text);

                                dialogTitle.setText(R.string.title_AddProducto);

                                builder.setCancelable(false);
                                builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.texto_guardar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AlertDialog.Builder build = new AlertDialog.Builder(ClientsActivity.this);
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
                                AlertDialog.Builder build = new AlertDialog.Builder(ClientsActivity.this);
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
            idtext.setText(Integer.toString(client.getId()));
            nombtext.setText(client.getFirst_name());
            lnombtext.setText(client.getLast_name());
            directext.setText(client.getAddress());
            tel1text.setText(client.getPhone1());
            tel2text.setText(client.getPhone2());
            tel3text.setText(client.getPhone3());
            emailtext.setText(client.getE_mail());
        }
    }

    private class ClientAdapter extends RecyclerView.Adapter<ClientHolder> {

        private List<Client> clients;

        public ClientAdapter(List<Client> clients) {
            this.clients = clients;
        }

        @Override
        public ClientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_clients, parent, false);
            return new ClientHolder(view);
        }

        @Override
        public void onBindViewHolder(ClientHolder holder, int position) {
            holder.bindClient(clients.get(position));
        }

        @Override
        public int getItemCount() {
            return clients.size();
        }
    }

    private CompuStore compustore;
    private RecyclerView recyclerview;
    private ClientAdapter adapter;
    private Spinner spinner;
    private ImageButton search;

   //private EditText desctext, pricetext, qtytext, searchtext;
   //private TextView cattag, desctag, pricetag, qtytag;

    private EditText searchtext, nomtxt, lnomtxt,
                    dirtxt, lad1txt, lad2txt, lad3txt,
                    p1txt, p2txt, p3txt, emailtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);

        // if (Build.VERSION.SDK_INT >= 21) {
        //    getWindow().setNavigationBarColor(getResources().getColor(R.color.colorClientes));
        //     getWindow().setStatusBarColor(getResources().getColor(R.color.colorClientes));
        // }

        spinner = (Spinner) findViewById(R.id.spinner_clients);
        search = (ImageButton) findViewById(R.id.buscar_button);
        searchtext = (EditText) findViewById(R.id.busqueda_text);
        compustore = new CompuStore(this);

        recyclerview = (RecyclerView) findViewById(R.id.clients_rv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.datos, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.checkbox_clients);
        spinner.setAdapter(arrayAdapter);


        adapter = new ClientAdapter(compustore.getAllClients());
        recyclerview.setAdapter(adapter);

        //spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        //   @Override
        //    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //        if (spinner.getSelectedItem().toString() != "Todas"){
        //           adapter = new ClientAdapter(compustore.getOneCategoryProduct(position-1));
        //           recyclerview.setAdapter(adapter);
        //       }else{
        //           adapter = new ClientAdapter(compustore.getAllProducts());
        //           recyclerview.setAdapter(adapter);
        //       }

        //   }

        //   @Override
        //   public void onNothingSelected(AdapterView<?> parent) {

        //   }


        //}

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchtext.getFreezesText() != true) {
                    adapter = new ClientAdapter(compustore.getClientName(String.valueOf(searchtext.getText())));
                    recyclerview.setAdapter(adapter);
                    searchtext.setHint(String.valueOf(searchtext.getText()));
                    searchtext.setText(null);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.new_client, null);

        nomtxt = (EditText) view.findViewById(R.id.nombre_text);
        lnomtxt = (EditText) view.findViewById(R.id.apellido_text);
        dirtxt = (EditText) view.findViewById(R.id.direc_text);
        lad1txt = (EditText) view.findViewById(R.id.ladat1_text);
        lad2txt = (EditText) view.findViewById(R.id.ladat2_text);
        lad3txt = (EditText) view.findViewById(R.id.ladat3_text);
        p1txt = (EditText) view.findViewById(R.id.phonet1_text);
        p2txt = (EditText) view.findViewById(R.id.phonet2_text);
        p3txt = (EditText) view.findViewById(R.id.phonet3_text);
        emailtxt = (EditText) view. findViewById(R.id.email_text);

       //List<Client> clients = compustore.getAllCategories();
        //for (Client client : clients) {
        //    arrayAdapter.add(client.getDescription());
        //}

        builder.setCancelable(false);

        builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.texto_guardar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //   if (compustore.insertProduct(desctext.getText().toString(),)) {
                //       Toast.makeText(ProductsActivity.this, R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                //       adapter = new ClientAdapter(compustore.getAllProducts());
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