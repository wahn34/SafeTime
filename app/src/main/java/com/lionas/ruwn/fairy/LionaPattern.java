package com.lionas.ruwn.fairy;

/**
 * Created by ruwn on 2017-03-09.
 */

public class LionaPattern {

    private int _id = 0;

    private String liona_date;
    private String liona_do;
    private String liona_location;

    public int get_id(){
        return _id;
    }

    public String getDate() {
        return liona_date;
    }

    public String getDo() {
        return liona_do;
    }

    public String getLocation() {
        return liona_location;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setDate(String liona_date) {
        this.liona_date = liona_date;
    }

    public void setDo(String liona_do){
        this.liona_do = liona_do;
    }
    public void setLocation(String liona_location){
        this.liona_location = liona_location;
    }

}
