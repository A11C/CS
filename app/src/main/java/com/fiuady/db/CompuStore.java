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

    public Category getCategory(){
        Cursor cursor = getWrappedCursor();
        return new Category(cursor.getInt(cursor.getColumnIndex(InventoryCSDbSchema.CategoriesTable.Columns.ID)),
                cursor.getString(cursor.getColumnIndex(InventoryCSDbSchema.CategoriesTable.Columns.DESCRIPTION)));
    }
}

class ProductCursor extends CursorWrapper {
    public ProductCursor(Cursor cursor) {
        super(cursor);
    }

    public Product getProduct(){
        Cursor cursor = getWrappedCursor();
        return new Product(cursor.getInt(cursor.getColumnIndex(InventoryCSDbSchema.ProductsTable.Columns.ID)),
                cursor.getInt(cursor.getColumnIndex(ProductsTable.Columns.CATEGORY_ID)),
                cursor.getString(cursor.getColumnIndex(InventoryCSDbSchema.ProductsTable.Columns.DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(ProductsTable.Columns.PRICE)),
                cursor.getInt(cursor.getColumnIndex(ProductsTable.Columns.QUANTITY)));
    }
}

class AssemblyCursor extends CursorWrapper{
    public AssemblyCursor(Cursor cursor) {super(cursor);}

    public Assembly getAssembly()
    {
        Cursor cursor = getWrappedCursor();
        return new Assembly(cursor.getInt(cursor.getColumnIndex(InventoryCSDbSchema.AssembliesTable.Columns.ID)),
                cursor.getString(cursor.getColumnIndex(AssembliesTable.Columns.DESCRIPTION)));
    }
}

class AssemblyProductCursor extends CursorWrapper{
    public AssemblyProductCursor(Cursor cursor){super(cursor);}

    public AssemblyProduct getAssemblyProduct(){
        Cursor cursor = getWrappedCursor();
        return new AssemblyProduct(cursor.getInt(cursor.getColumnIndex(AssemblyProductsTable.Columns.ID)),
                cursor.getInt(cursor.getColumnIndex(AssemblyProductsTable.Columns.PRODUCT_ID)),
                cursor.getInt(cursor.getColumnIndex(AssemblyProductsTable.Columns.QUANTITY)));
    }
}

class ClientCursor extends CursorWrapper{
    public ClientCursor(Cursor cursor){super(cursor);}

    public Client getClient(){
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


public final class CompuStore {
    private InventoryCSHelper inventoryCSHelper;
    private SQLiteDatabase db;

    public CompuStore(Context context) {
        inventoryCSHelper = new InventoryCSHelper(context);
        db = inventoryCSHelper.getWritableDatabase();
        inventoryCSHelper.backupDatabasefile(context);
    }

    public List<Category> getAllCategories() {
        ArrayList<Category> list = new ArrayList<>();

        CategoryCursor cursor = new CategoryCursor(db.rawQuery("SELECT * FROM product_categories ORDER BY id", null));
        while(cursor.moveToNext()){
            list.add(cursor.getCategory());
        }
        cursor.close();

        return list;
    }

    public Category getOneCategory(int id){
        Category category;
        CategoryCursor cursor = new CategoryCursor(db.rawQuery("SELECT * FROM product_categories WHERE id =" + id, null));
        category = cursor.getCategory();
        cursor.close();

        return category;
    }

    public boolean updateCategory(String des, int id) {
        boolean b = true;
        List<Category> a = getAllCategories();

        if (des.isEmpty()) {
            b = false;
        }

        for(Category category : a) {
            if (category.getDescription().toUpperCase().equals(des.toUpperCase())) {
                b = false;
            }
        }

        if (b) {
            ContentValues values = new ContentValues();
            values.put(CategoriesTable.Columns.DESCRIPTION, des);

            db.update(CategoriesTable.NAME,
                    values,
                    CategoriesTable.Columns.ID+ "= ?",
                    new String[] {Integer.toString(id)});
        }

        return b;
    }

    public boolean InsertCategory(String text) {
        boolean b = true;
        List<Category> categories = getAllCategories();
        ContentValues values = new ContentValues();

        if (text.isEmpty()) {
            b = false;
        }

        for(Category category : categories) {
            if (category.getDescription().toUpperCase().equals(text.toUpperCase())) {

                b = false;
            }
        }

        if (b) {
            values.put(CategoriesTable.Columns.DESCRIPTION, text);

            db.insert(CategoriesTable.NAME, null, values);
        }

        return b;
    }

    public boolean categorydelete(int id, boolean dlt) {
        boolean c = false;
        boolean d = true;
        boolean e = true;
        List<Category> a = getAllCategories();
        List<Product> b = getAllProducts();

        for(Category category : a) {
            if (e) {
                if (category.getId() == id) {
                    e = false;
                    if (d) {
                        for(Product product : b) {
                            if (product.getCategory_id() == id) {
                                c = true;
                                d = false;
                            }
                            else {
                                if (dlt){
                                    db.delete(CategoriesTable.NAME, CategoriesTable.Columns.ID + "= ?", new String[] {Integer.toString(id)});
                                }
                            }
                        }
                    }
                }
            }
        }

        return  c;
    }

    public List<Product> getAllProducts() {
        ArrayList<Product> list = new ArrayList<>();

        ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products ORDER BY description", null));
        while(cursor.moveToNext()) {
            list.add(cursor.getProduct());
        }
        cursor.close();

        return list;
    }

   public List<Product> getOneCategoryProduct(int id){
        ArrayList<Product> list = new ArrayList<>();

        ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products WHERE category_id = " + id + " ORDER BY description", null));
        while(cursor.moveToNext()) {
           list.add(cursor.getProduct());
        }
        cursor.close();

        return list;
    }

    public List<Product> getProductName(String name){
        ArrayList<Product> list = new ArrayList<>();

        ProductCursor cursor = new ProductCursor(db.rawQuery("SELECT * FROM products WHERE description LIKE '%" + name + "%' ORDER BY description", null));
        while(cursor.moveToNext()) {
            list.add(cursor.getProduct());
        }
        cursor.close();

        return list;
    }

    public List<Assembly> getAllAssemblies(){
        ArrayList<Assembly> list = new ArrayList<>();

        AssemblyCursor cursor = new AssemblyCursor(db.rawQuery("SELECT * FROM assemblies ORDER BY description",null));
        while (cursor.moveToNext()) {
            list.add(cursor.getAssembly());
        }
        cursor.close();
        return list;
    }


    public List<AssemblyProduct> getAllAssemblyProductsid(){
        ArrayList<AssemblyProduct> list = new ArrayList<>();

        AssemblyProductCursor cursor = new AssemblyProductCursor(db.rawQuery("SELECT * FROM assembly_products ORDER BY id",null));
        while (cursor.moveToNext()){
            list.add(cursor.getAssemblyProduct());
        }
        cursor.close();

        return list;
    }

    public List<Client> getAllClients(){
        ArrayList<Client> list = new ArrayList<>();

        ClientCursor cursor = new ClientCursor(db.rawQuery("SELECT * FROM customers ORDER BY last_name",null));
        while (cursor.moveToNext()) {

            list.add(cursor.getClient());
        }
        cursor.close();

        return list;
    }

    public List<Client> getClientName(String name){
        ArrayList<Client> list = new ArrayList<>();

        ClientCursor cursor = new ClientCursor(db.rawQuery("SELECT * FROM customers WHERE first_name LIKE '%" + name + "%' ORDER BY description", null));
        while(cursor.moveToNext()) {
            list.add(cursor.getClient());
        }
        cursor.close();

        return list;
    }

}
