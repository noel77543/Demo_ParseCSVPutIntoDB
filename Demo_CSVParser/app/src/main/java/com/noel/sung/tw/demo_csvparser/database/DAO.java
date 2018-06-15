package com.noel.sung.tw.demo_csvparser.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.noel.sung.tw.demo_csvparser.model.Commodity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by noel on 2018/4/18.
 */

public class DAO {

    public static final String _TABLE_COMMODITY = "AllMedicines";
    public static final String _TABLE_SUBSTANCE = "SubstanceNameSearch";
    public static final String _TABLE_TRADITION = "Tradition";

    public static final String _DB = "illeagalDrug.db";


    private Context context;

    public DAO(Context context) {
        this.context = context;
    }

    //--------

    /**
     * 打開讀取DB
     *
     * @return
     */
    private SQLiteDatabase openReadDatabase() {

        File dbFile = context.getDatabasePath(_DB);
        if (!dbFile.exists()) {
            try {
                File parentDir = new File(dbFile.getParent());
                if (!parentDir.exists()) {
                    parentDir.mkdir();
                }
                copyDatabase(dbFile);
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);
    }

    //--------

    /**
     * 打開寫入DB
     *
     * @return
     */
    private SQLiteDatabase openWriteDatabase() {

        File dbFile = context.getDatabasePath(_DB);
        try {
            if (!dbFile.exists()) {
                File parentDir = new File(dbFile.getParent());
                if (!parentDir.exists()) {
                    parentDir.mkdir();
                }
                copyDatabase(dbFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating source database", e);
        }


        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    //--------

    /**
     * AllMedicines
     * SubstanceNameSearch
     * 在tableName中新增
     */
    public void insertData(String tableName, ContentValues contentValues) {
        SQLiteDatabase db = openWriteDatabase();
        try {
            db.insert(tableName, null, contentValues);
        } catch (Exception ex) {
            Log.e("SQLITE", ex.getMessage());
        }
        db.close();
    }

    //--------

    /**
     * 複製DB
     *
     * @param dbFile
     * @throws IOException
     */
    private void copyDatabase(File dbFile) throws IOException {

        InputStream is = context.getAssets().open(_DB);
        OutputStream os = new FileOutputStream(dbFile);

        byte[] buffer = new byte[1024];
        int read = is.read(buffer);
        while (read != -1) {
            os.write(buffer, 0, read);
            read = is.read(buffer);
        }
        os.flush();
        os.close();
        is.close();
    }

    //--------

    /**
     * 取得符合帶入Sid值的藥物
     *
     * @param sid
     * @return
     */
    public Commodity getCommodityBySid(int sid) {
        SQLiteDatabase db = openReadDatabase();
        String sql = "SELECT * FROM AllMedicines WHERE sid = " + sid;
        Cursor c = db.rawQuery(sql, null);
        Commodity commodity = new Commodity();
        if (c.moveToFirst()) {
            commodity.fromCursor(c);
        }
        c.close();
        db.close();
        return commodity;
    }
    //--------

    /**
     * 模糊搜尋 EnglishName 並依照sid排序
     *
     * @param englishName
     * @return
     */
    public ArrayList<Commodity> getDatasByEnglishName(String englishName) {
        SQLiteDatabase db = openReadDatabase();
        //對多個欄位進行模糊搜尋   避免單引號搜尋
        englishName = englishName.replace("'", "''");

        String sql = "SELECT * FROM AllMedicines WHERE  EnglishName LIKE '%" + englishName + "%' ORDER BY sid";
        Cursor c = db.rawQuery(sql, null);
        ArrayList<Commodity> commodities = new ArrayList<Commodity>();
        if (c.moveToFirst()) {
            do {
                Commodity commodity = new Commodity();
                commodity.fromCursor(c);
                commodities.add(commodity);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return commodities;
    }


    //--------

    /**
     * 清除此ＤＢ的AllMedicines中的所有資料
     */
    public void delete(String tableName) {

        SQLiteDatabase db = openWriteDatabase();
        try {
            db.execSQL("DELETE FROM `%" + tableName + "%`");
        } catch (Exception ex) {
            Log.e("SQLITE", ex.getMessage());
        }
        db.close();
    }
}
