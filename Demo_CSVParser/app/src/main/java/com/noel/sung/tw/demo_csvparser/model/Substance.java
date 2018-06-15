package com.noel.sung.tw.demo_csvparser.model;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by noel on 2018/4/18.
 */

public class Substance {
    String substanceName;
    String category;
    String substance;
    String rules;
    String manual;
    int sid;

    public int getSid() {
        return sid;
    }

    public String getSubstanceName() {
        return substanceName;
    }

    public String getCategory() {
        return category;
    }

    public String getSubstance() {
        return substance;
    }

    public String getRules() {
        return rules;
    }

    public String getManual() {
        return manual;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setSubstanceName(String substanceName) {
        this.substanceName = substanceName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public void setManual(String manual) {
        this.manual = manual;
    }


    //---------

    /***
     *  取出
     * @param c
     */
    public void fromCursor(Cursor c) {
        sid = c.getInt(c.getColumnIndex("sid"));
        substanceName = c.getString(c.getColumnIndex("SubstanceName"));
        category = c.getString(c.getColumnIndex("Category"));
        substance = c.getString(c.getColumnIndex("Substance"));
        rules = c.getString(c.getColumnIndex("Rules"));
        manual = c.getString(c.getColumnIndex("Manual"));
    }

    //---------

    /***
     * 存入
     * @return
     */
    public ContentValues toContentValues() {
        ContentValues ret = new ContentValues();
        ret.put("substanceName", getSubstanceName());
        ret.put("category", getCategory());
        ret.put("substance", getSubstance());
        ret.put("rules", getRules());
        ret.put("manual", getManual());
        return ret;
    }
}
