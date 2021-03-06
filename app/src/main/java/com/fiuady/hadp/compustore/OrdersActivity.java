package com.fiuady.hadp.compustore;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.MultiSelectionSpinner;
import com.fiuady.StateC;
import com.fiuady.db.Assembly;
import com.fiuady.db.AssemblyProduct;
import com.fiuady.db.Category;
import com.fiuady.db.Client;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Order;
import com.fiuady.db.OrderStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

public class OrdersActivity extends AppCompatActivity {


    private static boolean datei, datef;
    private Spinner spinneror, spinnercl;
    private CheckBox chkdi, chkdf;
    private TextView datefin, dateini;
    private static String string;
    private CompuStore compustore;
    private RecyclerView recyclerview;
    private OrderAdapter adapter;
    private List<OrderStatus> orderstatus;


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        private int Num;


        static DatePickerFragment newInstance(int num) {
            DatePickerFragment dF = new DatePickerFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            dF.setArguments(args);
            return dF;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Num = getArguments().getInt("num");
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

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date(year - 1900, month, day);
            String formatDate = sdf.format(date);
            string = formatDate;
            if (Num == 0) {
                ((OrdersActivity) getActivity()).SetDateIni();
                ((OrdersActivity) getActivity()).UpdateAdapter();
            } else {
                ((OrdersActivity) getActivity()).SetDateFin();
                ((OrdersActivity) getActivity()).UpdateAdapter();
            }
        }
    }

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

            idOrtext.setText(String.valueOf(order.getId()));
            for (OrderStatus orderStatus : orderstatus) {
                if (orderStatus.getId() == order.getStatus_id()) {
                    Statustext.setText(orderStatus.getDescription());
                    break;
                }
            }
            CustomerIdtext.setText(compustore.getClientName(order.getCustomer_id()));
            dateOrtext.setText(order.getDate());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu menu = new PopupMenu(OrdersActivity.this, itemView);
                    menu.getMenuInflater().inflate(R.menu.menu_option_assembly, menu.getMenu());

                    if(order.getStatus_id()!=0){
                        menu.getMenu().removeItem(R.id.menu_as_mod_or);
                    }

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals(menu.getMenu().getItem(1).getTitle())) {
                                Intent i = new Intent(OrdersActivity.this, ModOrderActivity.class);
                                i.putExtra(ModOrderActivity.EXTRA_CID, order.getCustomer_id());
                                i.putExtra(ModOrderActivity.EXTRA_OID, order.getId());
                                startActivityForResult(i,ModOrderActivity.REQUEST_CODE);
                            } else {
                                final PopupMenu menu2 = new PopupMenu(OrdersActivity.this, itemView);
                                menu2.getMenuInflater().inflate(R.menu.menu_option_estadoassembly, menu2.getMenu());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        //  spinnercl = (Spinner) findViewById(R.id.spinnerC);
        //  spinneror = (Spinner) findViewById(R.id.spinnerOr);
        chkdf = (CheckBox) findViewById(R.id.orderFf_checkbox);
        chkdi = (CheckBox) findViewById(R.id.orderFi_checkbox);
        dateini = (TextView) findViewById(R.id.dateini);
        datefin = (TextView) findViewById(R.id.datefin);
        compustore = new CompuStore(this);
        orderstatus = (compustore.getStatus());
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
        recyclerview = (RecyclerView) findViewById(R.id.orders_rv);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        }
        UpdateAdapter();


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        spinnercl.setAdapter(arrayAdapter);

        arrayAdapter.add("Todos");
        final List<Client> clients = compustore.getAllClients();
        for (Client client : clients) {
            arrayAdapter.add(client.getFirst_name() + " " + client.getLast_name());
        }


        chkdi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DialogFragment fragment = DatePickerFragment.newInstance(0);
                    fragment.show(getFragmentManager(), "datePicker1");
                }else{
                    UpdateAdapter();
                }

            }
        });

        chkdf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DialogFragment fragment = DatePickerFragment.newInstance(1);
                    fragment.show(getFragmentManager(), "datePicker2");
                }else{
                    UpdateAdapter();
                }

            }
        });

        // if (Build.VERSION.SDK_INT >= 21) {
        //    getWindow().setNavigationBarColor(getResources().getColor(R.color.colorOrders));
        //    getWindow().setStatusBarColor(getResources().getColor(R.color.colorOrders));
        //}


    }

    public void SetDateIni() {
        dateini.setText(string);
    }

    public void SetDateFin() {
        datefin.setText(string);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar, menu);
        return true;
    }

/*  @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
        }
        else {
            recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i = new Intent(OrdersActivity.this, AddOrderActivity.class);
        startActivityForResult(i, AddOrderActivity.REQUEST_CODE);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Toast.makeText(OrdersActivity.this, "Orden Agregada Correctamente", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 0 && resultCode == RESULT_OK) {
            Toast.makeText(OrdersActivity.this, "Orden Modificada Correctamente", Toast.LENGTH_SHORT).show();
        }
        UpdateAdapter();
    }

    public void UpdateAdapter() {
        List<Order> orders = compustore.getAllOrders();
        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                String f1 = o1.getDate();
                String f2 = o2.getDate();
                Date c1 = new Date();
                Date c2 = new Date();
                try {
                    SimpleDateFormat curFormarter = new SimpleDateFormat("dd-MM-yyyy");
                    c1 = curFormarter.parse(f1);
                    c2 = curFormarter.parse(f2);
                    //Toast.makeText(OrdersActivity.this, String.valueOf(newDate), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return c1.compareTo(c2);
            }
        });

        if (chkdi.isChecked() && chkdf.isChecked()){
            int or=-1;
            for(Order order : orders){
                String f1 = order.getDate();
                String f2 = dateini.getText().toString();
                String f3 = datefin.getText().toString();
                Date c1 = new Date();
                Date c2 = new Date();
                Date c3 = new Date();
                try {
                    SimpleDateFormat curFormarter = new SimpleDateFormat("dd-MM-yyyy");
                    c1 = curFormarter.parse(f1);
                    c2 = curFormarter.parse(f2);
                    c3 = curFormarter.parse(f3);
                    //Toast.makeText(OrdersActivity.this, String.valueOf(newDate), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(c2.after(c1) || c1.after(c3)){
                 //   orders.remove(order);
                }
            }
        }else if (chkdi.isChecked()){
            int or=-1;
            for(Order order : orders){
                String f1 = order.getDate();
                String f2 = dateini.getText().toString();
                Date c1 = new Date();
                Date c2 = new Date();
                try {
                    SimpleDateFormat curFormarter = new SimpleDateFormat("dd-MM-yyyy");
                    c1 = curFormarter.parse(f1);
                    c2 = curFormarter.parse(f2);
                    //Toast.makeText(OrdersActivity.this, String.valueOf(newDate), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(c2.after(c1)){
                 //   orders.remove(order);
                }
            }
        }else if (chkdf.isChecked()){
            int or=-1;
            for(Order order : orders){
                String f1 = order.getDate();
                String f3 = datefin.getText().toString();
                Date c1 = new Date();
                Date c3 = new Date();
                try {
                    SimpleDateFormat curFormarter = new SimpleDateFormat("dd-MM-yyyy");
                    c1 = curFormarter.parse(f1);
                    c3 = curFormarter.parse(f3);
                    //Toast.makeText(OrdersActivity.this, String.valueOf(newDate), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(c1.after(c3)){
                 //   orders.remove(order);
                }
            }
        }


        adapter = new OrderAdapter(orders);

        recyclerview.setAdapter(adapter);
    }

}

