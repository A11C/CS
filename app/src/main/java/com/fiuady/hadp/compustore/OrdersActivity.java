package com.fiuady.hadp.compustore;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.fiuady.MultiSelectionSpinner;
import com.fiuady.StateC;
import com.fiuady.db.Assembly;
import com.fiuady.db.AssemblyProduct;
import com.fiuady.db.Category;
import com.fiuady.db.Client;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {


    private static boolean datei, datef;
    private Spinner spinneror, spinnercl;
    private CheckBox chkdi, chkdf;
    private TextView datefin, dateini;
    private static String string;
    private CompuStore compustore;
    private RecyclerView recyclerview;
    private OrderAdapter adapter;

    private class OrderHolder extends RecyclerView.ViewHolder {

        private TextView idOrtext, Statustext, CustomerIdtext, dateOrtext;


        public OrderHolder(View itemView) {
            super(itemView);

            idOrtext = (TextView) itemView.findViewById(R.id.id_Or_text);
            Statustext = (TextView) itemView.findViewById(R.id.estado_text);
            CustomerIdtext = (TextView) itemView.findViewById(R.id.idclient_text);
            dateOrtext = (TextView) itemView.findViewById(R.id.fecha_text);

        }

        private void bindOrder(final Order order) {

            idOrtext.setText(Integer.toString(order.getId()));
            Statustext.setText(Integer.toString(order.getStatus_id()));
            CustomerIdtext.setText(Integer.toString(order.getCustomer_id()));
            dateOrtext.setText(order.getDate());

         itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  final PopupMenu menu = new PopupMenu(OrdersActivity.this, itemView);
                  menu.getMenuInflater().inflate(R.menu.menu_option_assembly, menu.getMenu());


                  menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                      @Override
                      public boolean onMenuItemClick(MenuItem item) {

                          if (item.getTitle().equals(menu.getMenu().getItem(1).getTitle())) {
                              Intent i = new Intent(OrdersActivity.this, ModOrderActivity.class);
                              startActivity(i);
                          }
                          else{
                              final PopupMenu menu2 = new PopupMenu(OrdersActivity.this, itemView);
                              menu2.getMenuInflater().inflate(R.menu.menu_option_estadoassembly,menu2.getMenu());
                              menu2.show();
                          }
                          return true;
                      }
                  });
                  menu.show();
              }
          });

        }
    }
        private class OrderAdapter extends RecyclerView.Adapter<OrdersActivity.OrderHolder> {

            private List<Order> orders;

            public OrderAdapter(List<Order> orders) {
                this.orders = orders;
            }

            @Override
            public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.list_orders, parent, false);
                return new OrderHolder(view);
            }

            @Override
            public void onBindViewHolder(OrderHolder holder, int position) {
                holder.bindOrder(orders.get(position));
            }

            @Override
            public int getItemCount() {
                return orders.size();
            }

        }

        public static class DatePickerFragment extends DialogFragment
                implements DatePickerDialog.OnDateSetListener {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the current date as the default date in the picker
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Create a new instance of DatePickerDialog and return it
                return new DatePickerDialog(getActivity(), this, year, month, day);
            }

            public void onDateSet(DatePicker view, int year, int month, int day) {
                //this.getContext();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formatDate = DateFormat.getDateInstance(DateFormat.LONG).format(new Date());
                string = formatDate;

            }

        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_orders);
            //  spinnercl = (Spinner) findViewById(R.id.spinnerC);
            //  spinneror = (Spinner) findViewById(R.id.spinnerOr);
            dateini = (TextView) findViewById(R.id.dateini);
            chkdf = (CheckBox) findViewById(R.id.orderFf_checkbox);
            chkdi = (CheckBox) findViewById(R.id.orderFi_checkbox);
            datefin = (TextView) findViewById(R.id.datefin);


            final String[] select_qualification = {
                    "Filtrar por:", "Pendiente", "Cancelado", "Cofirmado", "En tránsito", "Finalizado"};
            Spinner spinner = (Spinner) findViewById(R.id.spinnerOr);

            ArrayList<StateC> listC = new ArrayList<>();

            for (int i = 0; i < select_qualification.length; i++) {
                StateC stateC = new StateC();
                stateC.setTitle(select_qualification[i]);
                stateC.setSelected(false);
                listC.add(stateC);
            }

            MultiSelectionSpinner multiSelectionSpinner = new MultiSelectionSpinner(OrdersActivity.this, 0,
                    listC);
            spinner.setAdapter(multiSelectionSpinner);


     /*  setContentView(R.layout.checkbox_orders);
        spinneror = (Spinner) findViewById(R.id.spinnerOr);

    /*    ArrayAdapter<String> adapter = ArrayAdapter.createFromResource(this,R.layout.checkbox_orders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
            // spinneror.setAdapter(adapter);

//        ArrayAdapter arrayAdapter=new ArrayAdapter(this, R.layout.checkbox_clients);
//        arrayAdapter.getContext(R.layout.checkbox_orders);

//        spinneror.setAdapter(arrayAdapter);
//        spinneror = (Spinner) findViewById(R.id.spinnerOr);

            //   ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
            // spinneror.setAdapter(arrayAdapter);


//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
//       arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinneror.setAdapter(arrayAdapter);
//        spinneror.setAdapter(ArrayAdapter.createFromResource(R.layout.checkbox_clients));
            spinnercl = (Spinner) findViewById(R.id.spinnerC);
            compustore = new CompuStore(this);

            recyclerview = (RecyclerView) findViewById(R.id.orders_rv);
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
            adapter = new OrderAdapter(compustore.getAllOrders());
            recyclerview.setAdapter(adapter);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
            spinnercl.setAdapter(arrayAdapter);

            arrayAdapter.add("Todos");
            final List<Client> clients = compustore.getAllClients();
            for (Client client : clients) {
                arrayAdapter.add(client.getFirst_name() + " " + client.getLast_name());
            }


            chkdf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // datef = true;
                        // datei = false;
                        DialogFragment newFragment = new DatePickerFragment();
                        newFragment.show(getSupportFragmentManager(), "datePicker2");
                        datefin.setText(string);
                    }
                }
            });

            chkdi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // datei = true;
                        // datef = false;
                        DialogFragment newFragment = new DatePickerFragment();
                        newFragment.show(getSupportFragmentManager(), "datePicker");
                        dateini.setText(string);
                    }

                }
            });

            // if (Build.VERSION.SDK_INT >= 21) {
            //    getWindow().setNavigationBarColor(getResources().getColor(R.color.colorOrders));
            //    getWindow().setStatusBarColor(getResources().getColor(R.color.colorOrders));
            //}

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.agregar, menu);
            return true;
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            Intent i = new Intent(OrdersActivity.this, AddOrderActivity.class);
            startActivity(i);
            return super.onOptionsItemSelected(item);
        }
    }

