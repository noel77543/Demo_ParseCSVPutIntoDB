package com.noel.sung.tw.demo_csvparser.model;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by noel on 2018/4/23.
 */

public class Tradition {

//    流水號
//    許可證字號
//    藥品名稱
//    劑型與類別
//    適應症及效能
//    成份內容
//    禁用物質分類
//    禁用物質名
//    使用規範

    private int sid;
    private String licenseNumber;
    private String name;
    private String dosageForm;
    private String indications;
    private String mainIngredient;
    private String category;
    private String substance;
    private String specification;


    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubstance() {
        return substance;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public String getMainIngredient() {
        return mainIngredient;
    }

    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }


    //資料存取
    public void fromCursor(Cursor c) {
        sid = c.getInt(c.getColumnIndex("sid"));
        licenseNumber = c.getString(c.getColumnIndex("licenseNumber"));
        name = c.getString(c.getColumnIndex("name"));
        dosageForm = c.getString(c.getColumnIndex("dosageForm"));
        indications = c.getString(c.getColumnIndex("indications"));
        mainIngredient = c.getString(c.getColumnIndex("mainIngredient"));
        category = c.getString(c.getColumnIndex("category"));
        substance = c.getString(c.getColumnIndex("substance"));
    }

    //========================================================================

    public ContentValues toContentValues() {
        ContentValues ret = new ContentValues();
        ret.put("licenseNumber", getLicenseNumber());
        ret.put("name", getName());
        ret.put("dosageForm", getDosageForm());
        ret.put("indications", getIndications());
        ret.put("mainIngredient", getMainIngredient());
        ret.put("category", getCategory());
        ret.put("substance", getSubstance());
        return ret;
    }
}
