package com.noel.sung.tw.demo_csvparser;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.noel.sung.tw.demo_csvparser.database.DAO;
import com.noel.sung.tw.demo_csvparser.model.Commodity;
import com.noel.sung.tw.demo_csvparser.model.Substance;
import com.noel.sung.tw.demo_csvparser.model.Tradition;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ProgressBar progressBar;
    private int totalSize;
    private int progress;

    private final String CSV_FILE_SUBSTANCE = "substance.csv";
    private final String CSV_FILE_COMMODITY = "commodity.csv";
    private final String CSV_FILE_TRADITION = "tradition.csv";

    //複製出來的檔案名稱
    private final String SD_FILE_NAME = "backupName.db";


    private ArrayList<String[]> substances;
    private ArrayList<String[]> commodities;
    private ArrayList<String[]> traditions;


    private DAO dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        textView = (TextView) findViewById(R.id.tv_progress);

        MainActivityPermissionsDispatcher.allowedToOutPutDataWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void allowedToOutPutData() {
        dao = new DAO(this);
        commodities = getParseData(CSV_FILE_COMMODITY);
        substances = getParseData(CSV_FILE_SUBSTANCE);
        traditions = getParseData(CSV_FILE_TRADITION);

        new SaveData().execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    //----------------

    /***
     * 將從csv解析出來的資料存進DB
     */
    private class SaveData extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //每一個類別的第一項都不需要(因為是title)所以總尺寸會各-1
            totalSize = commodities.size() - 1 + substances.size() - 1 + traditions.size() - 1;
            progress = 0;
            progressBar.setMax(totalSize);

            progressBar.setProgress(progress);
            textView.setText(String.format(getString(R.string.progress), progress, totalSize));

        }

        @Override
        protected Void doInBackground(Void... voids) {

            putCommodityDataToDB();
            putSubstanceDataToDB();
            putTraditionDataToDB();
            return null;
        }

        //全都放至DB後將檔案 copy到外部資料夾
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("success", "Save");

            copyDataToSD(DAO._DB);
        }
    }

    //----------------

    /***
     * 複製db檔至外部資料夾
     */
    private void copyDataToSD(String strFileName) {

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                //欲複製的檔案
                String currentDBPath = "//data//" + getPackageName() + "//databases//" + strFileName + "";

                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, SD_FILE_NAME);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("success", "Output");
    }

    //----------------

    /***
     * 將Tradition放入
     */
    private void putTraditionDataToDB() {
        Tradition tradition;
        for (int i = 1; i < traditions.size(); i++) {//第0比不用因為是標題
            tradition = new Tradition();
            //禁用物質名
            tradition.setSubstance(traditions.get(i)[0]);
            //禁用物質分類
            tradition.setCategory(traditions.get(i)[1]);
            //使用規範
            tradition.setSpecification(traditions.get(i)[2]);
            //許可證字號
            tradition.setLicenseNumber(traditions.get(i)[3]);
            //藥品名稱
            tradition.setName(traditions.get(i)[4]);
            //劑型與類別
            tradition.setDosageForm(traditions.get(i)[5]);
            //適應症及效能
            tradition.setIndications(traditions.get(i)[6]);
            //成份內容
            tradition.setMainIngredient(traditions.get(i)[7]);

            Log.e("tradition" + i, traditions.get(i)[4]);

            //一批批放進去
            dao.insertData(DAO._TABLE_TRADITION, tradition.toContentValues());

            updateLoadingProgress();
        }
    }


    //----------------

    /***
     * 將Substance放入資料庫
     */
    private void putSubstanceDataToDB() {
        Substance substance;
        for (int i = 1; i < substances.size(); i++) {//第0比不用因為是標題
            substance = new Substance();
            substance.setSubstanceName(substances.get(i)[0]);
            substance.setCategory(substances.get(i)[1]);
            substance.setSubstance(substances.get(i)[2]);
            substance.setRules(substances.get(i)[3]);
            substance.setManual(substances.get(i)[4]);

            Log.e("Substance" + i, substances.get(i)[0]);

            //一批批放進去
            dao.insertData(DAO._TABLE_SUBSTANCE, substance.toContentValues());

            updateLoadingProgress();
        }
    }


    //----------------

    /***
     * 將Commodity放入資料庫
     */
    private void putCommodityDataToDB() {
        Commodity commodity;
        //commodity,第一行不用 因為都是標題
        for (int i = 1; i < commodities.size(); i++) {
            commodity = new Commodity();
            // 禁用物質名,(行)[列]
            commodity.setSubstance(commodities.get(i)[0]);
            //禁用物質分類,(行)[列]
            commodity.setCategory(commodities.get(i)[1]);
            //使用規範,(行)[列]
            commodity.setRules(commodities.get(i)[2]);
            //許可證字號,(行)[列]
            commodity.setLicenseNumber(commodities.get(i)[3]);
            //中文品名,(行)[列]
            commodity.setChineseName(commodities.get(i)[4]);
            //英文品名,(行)[列]
            commodity.setEnglishName(commodities.get(i)[5]);
            //劑型,(行)[列]
            commodity.setDosageForm(commodities.get(i)[6]);
            //主成分略述,(行)[列]
            commodity.setMainIngredient(commodities.get(i)[7]);
            //申請商名稱,(行)[列]
            commodity.setApplicant(commodities.get(i)[8]);
            //製造商名稱,(行)[列]
            commodity.setManufacturer(commodities.get(i)[9]);

            Log.e("Commodity" + i, commodities.get(i)[4]);
            //一批批放進去
            dao.insertData(DAO._TABLE_COMMODITY, commodity.toContentValues());

            updateLoadingProgress();
        }
    }
    //----------------

    /***
     *  更新進度條
     */
    private void updateLoadingProgress() {
        progress++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(String.format(getString(R.string.progress), progress, totalSize));
                progressBar.setProgress(progress);
            }
        });
    }


    //----------------

    /***
     * 解析csv
     */
    private ArrayList<String[]> getParseData(String csvFile) {
        ArrayList<String[]> data = new ArrayList<>();
        boolean isNext = true;
        int emptyTime = 0;

        try {
            //取檔
            InputStream inputStream = getAssets().open(csvFile);
            //buffer 預留
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            //csvReader套件
            CSVReader csvReader = new CSVReader(bufferedReader);

            //持續讀檔
            while (isNext) {
                //一整行每一個字詞裝入String[]中
                String[] textArray = csvReader.readNext();
                //當空行
                if (textArray == null) {
                    //計算次數
                    emptyTime++;
                    //連續空行次數滿兩行則結束,避免資料有空一行的情形發生
                    if (emptyTime == 2) {
                        Log.e("isStop", "stop");
                        isNext = false;
                    }
                }
                //其他
                else {
                    //每當不是空行則將累積次數歸零
                    emptyTime = 0;
                    //將資料加入ArrayList<String[]>
                    data.add(textArray);
                }
            }

            //關閉所有串流
            inputStream.close();
            bufferedReader.close();
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
