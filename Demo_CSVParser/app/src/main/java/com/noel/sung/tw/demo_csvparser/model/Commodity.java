package com.noel.sung.tw.demo_csvparser.model;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by noel on 2018/4/18.
 */

public class Commodity {


    private int sid;
    private String substance;
    private String category;
    private String rules;
    private String licenseNumber;
    private String chineseName;
    private String englishName;
    private String dosageForm;
    private String mainIngredient;
    private String applicant;
    private String manufacturer;
    //    private String note;
    private String engToLowerCase;

    public String getEngToLowerCase() {
        return engToLowerCase;
    }

    public void setEngToLowerCase(String engToLowerCase) {
        this.engToLowerCase = engToLowerCase;
    }

    public int getSid() {
        return sid;
    }

    public String getSubstance() {
        return substance;
    }

    public String getCategory() {
        return category;
    }

    public String getRules() {
        return rules;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getChineseName() {
        return chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public String getMainIngredient() {
        return mainIngredient;
    }

    public String getApplicant() {
        return applicant;
    }

    public String getManufacturer() {
        return manufacturer;
    }


//    public String getNote() {
//        return note;
//    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }


//    public void setNote(String note) {
//        this.note = note;
//    }

    //----------

    /***
     *  取出
     * @param c
     */
    public void fromCursor(Cursor c) {
        sid = c.getInt(c.getColumnIndex("sid"));
        substance = c.getString(c.getColumnIndex("Substance"));
        category = c.getString(c.getColumnIndex("Category"));
        rules = c.getString(c.getColumnIndex("Rules"));
        licenseNumber = c.getString(c.getColumnIndex("LicenseNumber"));
        chineseName = c.getString(c.getColumnIndex("ChineseName"));
        englishName = c.getString(c.getColumnIndex("EnglishName"));
        dosageForm = c.getString(c.getColumnIndex("DosageForm"));
        mainIngredient = c.getString(c.getColumnIndex("MainIngredient"));
        applicant = c.getString(c.getColumnIndex("Applicant"));
        manufacturer = c.getString(c.getColumnIndex("Manufacturer"));
//        note = c.getString(c.getColumnIndex("Note"));
        engToLowerCase = englishName.toLowerCase();
    }

    //----------
    /***
     *  存入
     */
    public ContentValues toContentValues() {
        ContentValues ret = new ContentValues();
        ret.put("Substance", getSubstance());
        ret.put("Category", getCategory());
        ret.put("Rules", getRules());
        ret.put("LicenseNumber", getLicenseNumber());
        ret.put("ChineseName", getChineseName());
        ret.put("EnglishName", getEnglishName());
        ret.put("DosageForm", getDosageForm());
        ret.put("MainIngredient", getMainIngredient());
        ret.put("Applicant", getApplicant());
        ret.put("Manufacturer", getManufacturer());
//        ret.put("Note",getNote());
        return ret;
    }
}
