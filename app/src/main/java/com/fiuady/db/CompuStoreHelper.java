package com.fiuady.db;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;

public final class CompuStoreHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "CompuStore.db";
    public static final int SCHEMA_VERSION = 1;

    private Context context;

    public CompuStoreHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);

        this.context = context;

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){ //codigo solo sirve antes de jelly bean
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("PRAGMA foreign_keys = ON");
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onConfigure(SQLiteDatabase db) { //Es el mismo codigo pero para versiones mayores a jelly bean
        super.onConfigure(db);

        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        applySQLFile(db, "schema.sql");
        applySQLFile(db, "initial-data.sql");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void applySQLFile(SQLiteDatabase db, String filename){

        BufferedReader reader = null;
        try{
            InputStream is = context.getAssets().open(filename);
            reader = new BufferedReader(new InputStreamReader(is));

            StringBuilder statemnt = new StringBuilder();
            for (String line; (line = reader.readLine()) != null;){
                if (!TextUtils.isEmpty(line) && !line.startsWith("--")) {
                    statemnt.append(line.trim());
                }

                if (line.endsWith(";")){
                    db.execSQL(statemnt.toString());
                    statemnt.setLength(0);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (reader != null){
                try{
                    reader.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void backupDatabasefile(Context context){

        try{
            File sd = Environment.getExternalStorageDirectory();
            if(sd.canWrite()){
                String currentDBPath = "/data/data/" +
                        context.getPackageName() + "/databases/inventory.db";
                String backupDbPath = "inventory-bk.db";
                File currenDB = new File(currentDBPath);
                File backupDB = new File(backupDbPath);

                if (currenDB.exists()){
                    FileChannel src = new FileInputStream(currenDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();

                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        }catch (IOException e){
        }
    }
}
