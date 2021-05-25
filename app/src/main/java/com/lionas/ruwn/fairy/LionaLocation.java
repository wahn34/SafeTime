package com.lionas.ruwn.fairy;

/**
 * Created by ruwn on 2017-03-09.
 */

public class LionaLocation {

    private int _id = 0;

    private String liona_time;
    private String liona_accuracy;
    private String liona_address;

    public int get_id(){
        return _id;
    }

    public String getTime() {
        return liona_time;
    }

    public String getAccuracy() {
        return liona_accuracy;
    }

    public String getAddress() {
        return liona_address;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setTime(String liona_date) {
        this.liona_time = liona_date;
    }

    public void setAccuracy(String liona_do){
        this.liona_accuracy = liona_do;
    }
    public void setAddress(String liona_location){
        this.liona_address = liona_location;
    }

}
