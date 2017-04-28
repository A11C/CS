package com.fiuady.hadp.compustore;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.MultiSelectionSpinner;
import com.fiuady.db.Client;
import com.fiuady.db.CompuStore;
import com.fiuady.StateC;

import java.util.ArrayList;
import java.util.List;

public class ClientsActivity extends AppCompatActivity {

    private String Description;
    private int[] I;

    public static class mDialogFragment extends DialogFragment {

        private int Num, Id;
        private String Fn, Ln, Dir, P1, P2, P3, Email;
        private CompuStore compustore;
        private EditText nomtxt, lnomtxt,
                dirtxt, lad1txt, lad2txt, lad3txt,
                p1txt, p2txt, p3txt, emailtxt;

        static mDialogFragment newInstance(int num) {
            mDialogFragment dF = new mDialogFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            dF.setArguments(args);
            return dF;
        }

        static mDialogFragment newInstance2(int num, int id, String fn, String ln, String dir, String p1, String p2, String p3, String email) {
            mDialogFragment dF = new mDialogFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            args.putInt("id",id);
            args.putString("fn",fn);
            args.putString("ln",ln);
            args.putString("dir",dir);
            args.putString("p1",p1);
            args.putString("p2",p2);
            args.putString("p3",p3);
            args.putString("email",email);
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
                    Fn = getArguments().getString("fn");
                    Ln = getArguments().getString("ln");
                    Dir = getArguments().getString("dir");
                    P1 = getArguments().getString("p1");
                    P2 = getArguments().getString("p2");
                    P3 = getArguments().getString("p3");
                    Email = getArguments().getString("email");
                    return DialogMod();
            }
            return super.onCreateDialog(savedInstanceState);
        }

        public AlertDialog DialogAdd() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.new_client, null);

            nomtxt = (EditText) view.findViewById(R.id.nombreCln_text);
            lnomtxt = (EditText) view.findViewById(R.id.apellidoCln_text);
            dirtxt = (EditText) view.findViewById(R.id.direcCln_text);
            lad1txt = (EditText) view.findViewById(R.id.ladat1_text);
            lad2txt = (EditText) view.findViewById(R.id.ladat2_text);
            lad3txt = (EditText) view.findViewById(R.id.ladat3_text);
            p1txt = (EditText) view.findViewById(R.id.phonet1_text);
            p2txt = (EditText) view.findViewById(R.id.phonet2_text);
            p3txt = (EditText) view.findViewById(R.id.phonet3_text);
            emailtxt = (EditText) view.findViewById(R.id.emailCln_text);
            compustore = new CompuStore(getActivity());

            builder.setCancelable(false);

            builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.texto_guardar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if ((!nomtxt.getText().toString().isEmpty()) && (!lnomtxt.getText().toString().isEmpty()) && (!dirtxt.getText().toString().isEmpty())) {
                        compustore.InsertClient(nomtxt.getText().toString(), lnomtxt.getText().toString(), dirtxt.getText().toString(),
                                lad1txt.getText().toString() + "-" + p1txt.getText().toString(),
                                lad2txt.getText().toString() + "-" + p2txt.getText().toString(),
                                lad3txt.getText().toString() + "-" + p3txt.getText().toString(),
                                emailtxt.getText().toString());
                        ((ClientsActivity) getActivity()).UpdateAdapter();
                        Toast.makeText(getActivity(), R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.InvalidClient, Toast.LENGTH_SHORT).show();
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
            View view = inflater.inflate(R.layout.new_client, null);

            nomtxt = (EditText) view.findViewById(R.id.nombreCln_text);
            nomtxt.setText(Fn);
            lnomtxt = (EditText) view.findViewById(R.id.apellidoCln_text);
            lnomtxt.setText(Ln);
            dirtxt = (EditText) view.findViewById(R.id.direcCln_text);
            dirtxt.setText(Dir);
            lad1txt = (EditText) view.findViewById(R.id.ladat1_text);
            p1txt = (EditText) view.findViewById(R.id.phonet1_text);
            if(P1!=null) {
                if (P1.length() == 11) {
                    lad1txt.setText(P1.substring(0, 3));
                    p1txt.setText(P1.substring(4, 11));
                }
            }
            lad2txt = (EditText) view.findViewById(R.id.ladat2_text);
            p2txt = (EditText) view.findViewById(R.id.phonet2_text);
            if(P2!=null) {
                if (P2.length() == 11) {
                    lad2txt.setText(P2.substring(0, 3));
                    p2txt.setText(P2.substring(4, 11));
                }
            }
            lad3txt = (EditText) view.findViewById(R.id.ladat3_text);
            p3txt = (EditText) view.findViewById(R.id.phonet3_text);
            if(P3!=null) {
                if (P3.length() == 11 && P3 != null) {
                    lad3txt.setText(P3.substring(0, 3));
                    p3txt.setText(P3.substring(4, 11));
                }
            }
            emailtxt = (EditText) view.findViewById(R.id.emailCln_text);
            emailtxt.setText(Email);
            compustore = new CompuStore(getActivity());

            builder.setCancelable(false);

            builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.texto_guardar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if ((!nomtxt.getText().toString().isEmpty()) && (!lnomtxt.getText().toString().isEmpty()) && (!dirtxt.getText().toString().isEmpty())) {
                        compustore.UpdateClient(Id, nomtxt.getText().toString(), lnomtxt.getText().toString(), dirtxt.getText().toString(),
                                lad1txt.getText().toString() + "-" + p1txt.getText().toString(),
                                lad2txt.getText().toString() + "-" + p2txt.getText().toString(),
                                lad3txt.getText().toString() + "-" + p3txt.getText().toString(),
                                emailtxt.getText().toString());
                        ((ClientsActivity) getActivity()).UpdateAdapter();
                        Toast.makeText(getActivity(), R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.InvalidClient, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), R.string.Error_operacion, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setView(view);
            return builder.create();
        }
    }

    private EditText editText;
    private TextView dialogTitle;
    private Spinner spinneradd;

    private class ClientHolder extends RecyclerView.ViewHolder {

        private TextView idtext, nombtext,
                lnombtext, directext, tel1text,
                tel2text, tel3text, emailtext;

        public ClientHolder(View itemView) {
            super(itemView);
            idtext = (TextView) itemView.findViewById(R.id.idCl_text);
            nombtext = (TextView) itemView.findViewById(R.id.nombreCl_text);
            lnombtext = (TextView) itemView.findViewById(R.id.apellidoCl_text);
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
                    popup.getMenuInflater().inflate(R.menu.menu_option_catassem, popup.getMenu());

                    if (!compustore.ClientHaveOrder(client.getId())) {
                        popup.getMenu().removeItem(R.id.menu_1);
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals(popup.getMenu().getItem(0).getTitle())) {
                                mDialogFragment fragment = mDialogFragment.newInstance2(1, client.getId(), client.getFirst_name(),
                                        client.getLast_name(), client.getAddress(), client.getPhone1(), client.getPhone2(), client.getPhone3(), client.getE_mail());
                                fragment.show(getFragmentManager(), "ModDialog");
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
    private EditText searchtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);

        // if (Build.VERSION.SDK_INT >= 21) {
        //    getWindow().setNavigationBarColor(getResources().getColor(R.color.colorClientes));
        //     getWindow().setStatusBarColor(getResources().getColor(R.color.colorClientes));
        // }

        spinner = (Spinner) findViewById(R.id.spinner_clients);
        search = (ImageButton) findViewById(R.id.buscarCl_button);
        searchtext = (EditText) findViewById(R.id.busqueda_text);
        compustore = new CompuStore(this);

        recyclerview = (RecyclerView) findViewById(R.id.clients_rv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        UpdateAdapter();

        final String[] select_qualification = {
                "Filtrar por:", "Nombre", "Apellido", "Dirección", "Teléfono", "E-mail"};
        spinner = (Spinner) findViewById(R.id.spinner_clients);

        final ArrayList<StateC> listC = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            StateC stateC = new StateC();
            stateC.setTitle(select_qualification[i]);
            stateC.setSelected(false);
            listC.add(stateC);
        }

        MultiSelectionSpinner multiSelectionSpinner = new MultiSelectionSpinner(ClientsActivity.this, 0,
                listC);
        spinner.setAdapter(multiSelectionSpinner);
        // Spinner spinner=(Spinner)findViewById(R.id.input1);

        //List<String> list = new ArrayList<String>();
        //list.add("List1");
        //list.add("List2");
        //spinner.setOnItemClickListener();


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
                for(int i=1; i<listC.size();i++){
                    if(listC.get(i).isSelected()){
                        Toast.makeText(ClientsActivity.this, "Elemento "+i+" seleccionado",Toast.LENGTH_SHORT).show();
                    }
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
        mDialogFragment fragment = mDialogFragment.newInstance(0);
        fragment.show(getFragmentManager(), "AddDialog");
        return super.onOptionsItemSelected(item);
    }

    public void UpdateAdapter() {
        adapter = new ClientAdapter(compustore.getClients(Description, I));
        recyclerview.setAdapter(adapter);
    }
}
