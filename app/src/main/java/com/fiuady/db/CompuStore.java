package com.fiuady.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.fiuady.db.InventoryCSDbSchema.*;

class CategoryCursor extends CursorWrapper {
    public CategoryCursor(Cursor cursor) {
        super(cursor);
    }

    public Category getCategory() {
        Cursor cursor = getWrappedCursor();
        return new Category(cursor.getInt(cursor.getColumnIndex(InventoryCSDbSchema.CategoriesTable.Columns.ID)),
                cursor.getString(cursor.getColumnIndex(InventoryCSDbSchema.CategoriesTable.Columns.DESCRIPTION)));
    }
}

class ProductCursor extends CursorWrapper {
    public ProductCursor(Cursor cursor) {
        super(cursor);
    }

    public Product getProduct() {
        Cursor cursor = getWrappedCursor();
        return new Product(cursor.getInt(cursor.getColumnIndex(InventoryCSDbSchema.ProductsTable.Columns.ID)),
                cursor.getInt(cursor.getColumnIndex(ProductsTable.Columns.CATEGORY_ID)),
                cursor.getString(cursor.getColumnIndex(InventoryCSDbSchema.ProductsTable.Columns.DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(ProductsTable.Columns.PRICE)),
                cursor.getInt(cursor.getColumnIndex(ProductsTable.Columns.QUANTITY)));
    }
}

class AssemblyCursor extends CursorWrapper {
    public AssemblyCursor(Cursor cursor) {
        super(cursor);
    }

    public Assembly getAssembly() {
        Cursor cursor = getWrappedCursor();
        return new Assembly(cursor.getInt(cursor.getColumnIndex(InventoryCSDbSchema.AssembliesTable.Columns.ID)),
                cursor.getString(cursor.getColumnIndex(AssembliesTable.Columns.DESCRIPTION)));
    }
}

class AssemblyProductCursor extends CursorWrapper {
    public AssemblyProductCursor(Cursor cursor) {
        super(cursor);
    }

    public AssemblyProduct getAssemblyProduct() {
        Cursor cursor = getWrappedCursor();
        return new AssemblyProduct(cursor.getInt(cursor.getColumnIndex(AssemblyProductsTable.Columns.ID)),
                cursor.getInt(cursor.getColumnIndex(AssemblyProductsTable.Columns.PRODUCT_ID)),
                cursor.getInt(cursor.getColumnIndex(AssemblyProductsTable.Columns.QUANTITY)));
    }
}

class ClientCursor extends CursorWrapper {
    public ClientCursor(Cursor cursor) {
        super(cursor);
    }

    public Client getClient() {
        Cursor cursor = getWrappedCursor();
        return new Client(cursor.getInt(cursor.getColumnIndex(CustomersTable.Columns.ID)),
                cursor.getString(cursor.getColumnIndex(CustomersTable.Columns.FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(CustomersTable.Columns.LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(CustomersTable.Columns.ADDRESS)),
                cursor.getString(cursor.getColumnIndex(CustomersTable.Columns.PHONE1)),
                cursor.getString(cursor.getColumnIndex(CustomersTable.Columns.PHONE2)),
                cursor.getString(cursor.getColumnIndex(CustomersTable.Columns.PHONE3)),
                cursor.getString(cursor.getColumnIndex(CustomersTable.Columns.E_MAIL)));
    }

}

class OrderCursor extends CursorWrapper {
    public OrderCursor(Cursor cursor) {
        super(cursor);
    }

    public Order getOrder(){
        Cursor cursor = getWrappedCursor();
        return new Order(cursor.getInt(cursor.getColumnIndex(OrdersTable.Columns.ID)),
                cursor.getInt(cursor.getColumnIndex(OrdersTable.Columns.STATUS_ID)),
                cursor.getInt(cursor.getColumnIndex(OrdersTable.Columns.CUSTOMER_ID)),
                cursor.getString(cursor.getColumnIndex(OrdersTable.Columns.DATE)),
                cursor.getString(cursor.getColumnIndex(OrdersTable.Columns.CHANGE_LOG)));
    }
}

class OrderStatusCursor extends CursorWrapper{
    public OrderStatusCursor(Cursor cursor){super (cursor);}

    public OrderStatus getOrderStatus(){
        Cursor cursor = getWrappedCursor();
        return new OrderStatus(cursor.getInt(cursor.getColumnIndex(OrderStatusTable.Columns.ID)),
                cursor.getString(cursor.getColumnIndex(OrderStatusTable.Columns.DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(OrderStatusTable.Columns.EDITABLE)),
                cursor.getString(cursor.getColumnIndex(OrderStatusTable.Columns.PREVIOUS)),
                cursor.getString(cursor.getColumnIndex(OrderStatusTable.Columns.NEXT)));
    }
}

class OrderAssemblyCursor extends CursorWrapper{
    public OrderAssemblyCursor(Cursor cursor){
        super (cursor);
    }

    public OrderAssembly getOrderAssembly(){
        Cursor cursor = getWrappedCursor();
        return new OrderAssembly(cursor.getInt(cursor.getColumnIndex(OrderAssembliesTable.Columns.ID)),
                cursor.getInt(cursor.getColumnIndex(OrderAssembliesTable.Columns.ASSEMBLY_ID)),
                cursor.getInt(cursor.getColumnIndex(OrderAssembliesTable.Columns.QUANTITY)));
    }
}

public final class CompuStore {

    private SQLiteDatabase db;

    public CompuStore(Context context) {
        InventoryCSHelper inventoryCSHelper;
        inventoryCSHelper = new InventoryCSHelper(context);
        db = inventoryCSHelper.getWritableDatabase();
        inventoryCSHelper.backupDatabasefile(context);
    }

    public List<Category> getAllCategories() {
        ArrayList<Category> list = new ArrayList<>();

        CategoryCursor cursor = new CategoryCursor(db.rawQuery("SELECT * FROM product_categories ORDER BY description", null));
        while (cursor.moveToNext()) {
            list.add(cursor.getCategory());
        }
        cursor.close();

        return list;
    }

    public List<Category> getAllCategoriesById() {
        ArrayList<Category> list = new ArrayList<>();

        CategoryCursor cursor = new CategoryCursor(db.rawQuery("SELECT * FROM product_categories ORDER BY id", null));
        while (cursor.moveToNext()) {
            list.add(cursor.getCategory());
        }
        cursor.close();

        return list;
    }

    public int getOneCategory(String description) {

        List<Category> list = getAllCategoriesById();
        int  category_id=-1;
        for (Category category : list){
           if(category.getDescription().equals(description)){
               category_id = category.getId();
               break;
           }
        }

        return category_id;
    }

    public boolean CategoryInProduct(int id) {
        boolean match = true;

        List<Product> products = getAllProductsById(id);

        if (products.isEmpty()) {
            match = false;
        }

        return match;
    }

    public void InsertCategory(String text) {
        ContentValues values = new ContentValues();
        values.put(CategoriesTable.Columns.DESCRIPTION, text);
        db.insert(CategoriesTable.NAME, null, values);
    }

    public void UpdateCategory(String description, int id) {
        ContentValues values = new ContentValues();
        values.put(CategoriesTable.Columns.DESCRIPTION, description);
        db.update(CategoriesTable.NAME, values, CategoriesTable.Columns.ID + "= ?", new String[]{Integer.toString(id)});
    }

    public void CategoryDelete(int id) {
        db.delete(CategoriesTable.NAME, CategoriesTable.Columns.ID + "= ?", new String[]{Integer.toString(id)});
    }

    public boolean CategoryExists(String description) {
        boolean match = false;

        List<Category> categories = getAllCategories();
        for (Category category : categories) {
            if (category.getDescription().toUpperCase().equals(description.toUpperCase())) {
                match = true;
                break;
            }
        }
        return match;
    }

    public List<Product> getAllProducts() {
        ArrayList<Product> list = new ArrayList<>();

        ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products ORDER BY description", null));
        while (cursor.moveToNext()) {
            list.add(cursor.getProduct());
        }
        cursor.close();

        return list;
    }

    public List<Product> getAllProductsById(int id) {
        ArrayList<Product> list = new ArrayList<>();

        ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products WHERE category_id = " + id + " ORDER BY description", null));
        while (cursor.moveToNext()) {
            list.add(cursor.getProduct());
        }
        cursor.close();

        return list;
    }

    public List<Product> getAllProductsByName(String name) {
        ArrayList<Product> list = new ArrayList<>();

        ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products ORDER BY description", null));
        while (cursor.moveToNext()) {
            list.add(cursor.getProduct());
        }
        cursor.close();

        ArrayList<Product> list_filter = new ArrayList<>();
        for (Product product : list) {
            if (product.getDescription().contains(name)) {
                list_filter.add(product);
            }
        }
        if (list_filter.isEmpty()) {
            return list;
        } else {
            return list_filter;
        }
    }

    public List<Product> getProductByName(int id, String name) {
        ArrayList<Product> list = new ArrayList<>();

        //ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products WHERE description LIKE '%" + name + "%' ORDER BY description", null));
        ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products WHERE category_id = " + id + " ORDER BY description", null));
        while (cursor.moveToNext()) {
            list.add(cursor.getProduct());
        }
        cursor.close();

        ArrayList<Product> list_filter = new ArrayList<>();
        for (Product product : list) {
            if (product.getDescription().contains(name)) {
                list_filter.add(product);
            }
        }
        if (list_filter.isEmpty()) {
            return list;
        } else {
            return list_filter;
        }
    }

    public List<Product> getAllProductsInAssembly(int id){
        ArrayList<Product> list = new ArrayList<>();

        ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT p.id, p.category_id, p.description, p.price, ap.qty FROM products p " +
                "INNER JOIN assembly_products ap ON (ap.product_id = p.id) WHERE ap.id = " + id + " ORDER BY p.description",null));
        while (cursor.moveToNext()){
            list.add(cursor.getProduct());
        }
        cursor.close();

        return list;
    }

    public boolean ProductInAssembly(int id) {
        boolean match = true;

        List<AssemblyProduct> assemblies = getAllAssemblyProductsById(id);

        if (assemblies.isEmpty()) {
            match = false;
        }

        return match;
    }

    public boolean ProductExists(String description) {
        boolean match = false;

        List<Product> products = getAllProducts();
        for (Product product : products) {
            if (product.getDescription().toUpperCase().equals(description.toUpperCase())) {
                match = true;
                break;
            }
        }
        return match;
    }

    public void UpdateProduct(int catid, String description, int precio, int id) {
        ContentValues values = new ContentValues();
        values.put(ProductsTable.Columns.DESCRIPTION, description);
        values.put(ProductsTable.Columns.CATEGORY_ID, catid);
        values.put(ProductsTable.Columns.PRICE, precio);
        db.update(ProductsTable.NAME, values, ProductsTable.Columns.ID + "= ?", new String[]{Integer.toString(id)});
    }

    public void DeleteProduct(int id) {
        db.delete(ProductsTable.NAME, CategoriesTable.Columns.ID + "= ?", new String[]{Integer.toString(id)});
    }

    public void InsertProduct(int catid, String description, int precio) {
        ContentValues values = new ContentValues();
        values.put(ProductsTable.Columns.DESCRIPTION, description.toUpperCase());
        values.put(ProductsTable.Columns.CATEGORY_ID, catid);
        values.put(ProductsTable.Columns.PRICE, precio);
        values.put(ProductsTable.Columns.QUANTITY, 0);
        db.insert(ProductsTable.NAME, null, values);
    }

    public void AddStockProduct(int id, int qty){
        ContentValues values = new ContentValues();
        values.put(ProductsTable.Columns.QUANTITY,qty);
        db.update(ProductsTable.NAME, values, ProductsTable.Columns.ID + "= ?", new String[]{Integer.toString(id)});
    }

    public void SaveProducts(ArrayList<Product> products){

        db.execSQL("CREATE TABLE [products_aux]( " +
                " [id] INTEGER PRIMARY KEY, " +
                " [category_id] INTEGER NOT NULL REFERENCES product_categories([id]), " +
                " [description] TEXT NOT NULL, " +
                " [price] INTEGER NOT NULL, " +
                " [qty] INTEGER NOT NULL, " +
                " CHECK(price >= 0), " +
                " CHECK(qty >= 0));");

        ContentValues values = new ContentValues();
        for (Product product : products)
        {
            values.put(ProductsTable.Columns.DESCRIPTION, product.getDescription());
            values.put(ProductsTable.Columns.CATEGORY_ID, product.getCategory_id());
            values.put(ProductsTable.Columns.ID, product.getId());
            values.put(ProductsTable.Columns.QUANTITY, product.getQuantity());
            values.put(ProductsTable.Columns.PRICE, product.getPrice());
            db.insert("products_aux",null,values);
            values.clear();
        }

    }

    public List<Product> RestoreProducts(){
        ArrayList<Product> list = new ArrayList<>();

        ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products_aux ORDER BY description",null));
        while (cursor.moveToNext()){
            list.add(cursor.getProduct());
        }
        cursor.close();
        db.execSQL("DROP TABLE IF EXISTS [products_aux]");

        return list;
    }

    public List<Assembly> getAllAssemblies() {
        ArrayList<Assembly> list = new ArrayList<>();

        AssemblyCursor cursor = new AssemblyCursor(db.rawQuery("SELECT * FROM assemblies ORDER BY description", null));
        while (cursor.moveToNext()) {
            list.add(cursor.getAssembly());
        }
        cursor.close();
        return list;
    }

    public List<AssemblyProduct> getAllAssemblyProducts() {
        ArrayList<AssemblyProduct> list = new ArrayList<>();

        AssemblyProductCursor cursor = new AssemblyProductCursor(db.rawQuery("SELECT * FROM assembly_products ORDER BY id", null));
        while (cursor.moveToNext()) {
            list.add(cursor.getAssemblyProduct());
        }
        cursor.close();

        return list;
    }

    public List<AssemblyProduct> getAllAssemblyProductsById(int id) {
        ArrayList<AssemblyProduct> list = new ArrayList<>();

        AssemblyProductCursor cursor = new AssemblyProductCursor(db.rawQuery("SELECT * FROM assembly_products WHERE product_id = " + id + " ORDER BY id", null));
        while (cursor.moveToNext()) {
            list.add(cursor.getAssemblyProduct());
        }
        cursor.close();

        return list;
    }

    public void DeleteAssembly (int id){
        DeleteAllProductsInAssembly(id);
        db.delete(AssembliesTable.NAME, AssembliesTable.Columns.ID + "= ?", new String[]{Integer.toString(id)});
    }

    public void InsertAssembly (String description){
        ContentValues values = new ContentValues();
        values.put(AssembliesTable.Columns.DESCRIPTION, description);
        db.insert(AssembliesTable.NAME, null, values);
    }

    public boolean AssemblyInOrder(int id){
        boolean match = true;

        List<OrderAssembly> orderAssemblies = getAllOrderAssemblyById(id);

        if (orderAssemblies.isEmpty()) {
            match = false;
        }

        return match;
    }

    public void UpdateQtyProductInAssembly(int pid, int id, int qty){
        ContentValues values = new ContentValues();
        values.put(AssemblyProductsTable.Columns.QUANTITY,qty);
        db.update(AssemblyProductsTable.NAME, values, AssemblyProductsTable.Columns.PRODUCT_ID + " = " + pid + " AND " + AssemblyProductsTable.Columns.ID + " = " + id, null);
    }

    public void DeleteProductInAssembly(int pid, int id){
        db.delete(AssemblyProductsTable.NAME, AssemblyProductsTable.Columns.PRODUCT_ID + " = " + pid + " AND " + AssemblyProductsTable.Columns.ID + " = " + id, null);
    }

    public void DeleteAllProductsInAssembly(int id){
        db.delete(AssemblyProductsTable.NAME, AssemblyProductsTable.Columns.ID + " = " + id, null);
    }

    public boolean AssemblyExists(String description){
        boolean match = false;

        List<Assembly> assemblies = getAllAssemblies();
        for (Assembly assembly : assemblies){
            if(assembly.getDescripcion().toUpperCase().equals(description.toUpperCase())){
                match = true;
                break;
            }
        }

        return match;
    }

    public void UpdateAssembly(String description, int id){
        ContentValues values = new ContentValues();
        values.put(AssembliesTable.Columns.DESCRIPTION, description);
        db.update(AssembliesTable.NAME, values, AssembliesTable.Columns.ID + " = ?", new String[]{Integer.toString(id)});
    }

    public void InsertAssemblyProduct(int id, int pid, int qty){
        ContentValues values = new ContentValues();
        values.put(AssemblyProductsTable.Columns.ID, id);
        values.put(AssemblyProductsTable.Columns.PRODUCT_ID, pid);
        values.put(AssemblyProductsTable.Columns.QUANTITY, qty);

        db.insert(AssemblyProductsTable.NAME, null, values);
    }

    public List<Client> getClients(String description,int i[]) {
        ArrayList<Client> list = new ArrayList<>();

        ClientCursor cursor = new ClientCursor(db.rawQuery("SELECT * FROM customers ORDER BY last_name", null));
        while (cursor.moveToNext()) {

            list.add(cursor.getClient());
        }
        cursor.close();

        return list;
    }

    public List<Client> getAllClients() {
        ArrayList<Client> list = new ArrayList<>();

        ClientCursor cursor = new ClientCursor(db.rawQuery("SELECT * FROM customers ORDER BY last_name", null));
        while (cursor.moveToNext()) {

            list.add(cursor.getClient());
        }
        cursor.close();

        return list;
    }

    public String getClientName (int id){
        List<Client> list = getAllClients();
        String name = "";
        for (Client client : list){
            if(client.getId()==id){
                name = client.getLast_name() +" "+ client.getFirst_name();
                break;
            }
        }

        return name;
    }

    public void InsertClient(String fn, String ln, String dir, String p1, String p2, String p3, String email){
        ContentValues values = new ContentValues();

        values.put(CustomersTable.Columns.FIRST_NAME, fn);
        values.put(CustomersTable.Columns.LAST_NAME, ln);
        values.put(CustomersTable.Columns.ADDRESS, dir);
        values.put(CustomersTable.Columns.PHONE1, p1);
        values.put(CustomersTable.Columns.PHONE2, p2);
        values.put(CustomersTable.Columns.PHONE3, p3);
        values.put(CustomersTable.Columns.E_MAIL, email);

        db.insert(CustomersTable.NAME, null, values);
    }

    public void UpdateClient(int id, String fn, String ln, String dir, String p1, String p2, String p3, String email){
        ContentValues values = new ContentValues();

        values.put(CustomersTable.Columns.FIRST_NAME, fn);
        values.put(CustomersTable.Columns.LAST_NAME, ln);
        values.put(CustomersTable.Columns.ADDRESS, dir);
        values.put(CustomersTable.Columns.PHONE1, p1);
        values.put(CustomersTable.Columns.PHONE2, p2);
        values.put(CustomersTable.Columns.PHONE3, p3);
        values.put(CustomersTable.Columns.E_MAIL, email);

        db.update(CustomersTable.NAME, values, CustomersTable.Columns.ID + "= ?", new String[]{Integer.toString(id)});
    }

    public boolean ClientHaveOrder(int id){
        boolean match = true;

        List<Order> orders = getAllOrders();
        for (Order order : orders){
            if (order.getCustomer_id() == id){
                match = false;
                break;
            }
        }
        return match;
    }

    public List<Order> getAllOrders(){
        ArrayList<Order> list = new ArrayList<>();

        OrderCursor cursor = new OrderCursor(db.rawQuery("SELECT * FROM orders ORDER BY id",null));
        while (cursor.moveToNext()){
            list.add(cursor.getOrder());
        }
        cursor.close();

        return list;
    }

    public List<OrderStatus> getStatus(){
        ArrayList<OrderStatus> list = new ArrayList<>();

        OrderStatusCursor cursor = new OrderStatusCursor(db.rawQuery("SELECT * FROM order_status ORDER BY id",null));
        while (cursor.moveToNext()){
            list.add(cursor.getOrderStatus());
        }
        cursor.close();

        return list;
    }

    public List<OrderAssembly> getAllOrderAssemblyById(int id){
        ArrayList<OrderAssembly> list = new ArrayList<>();

        OrderAssemblyCursor cursor = new OrderAssemblyCursor(db.rawQuery("SELECT * FROM order_assemblies WHERE assembly_id = " + id + " ORDER BY id",null));
        while (cursor.moveToNext()){
            list.add(cursor.getOrderAssembly());
        }
        cursor.close();

        return list;
    }

}
