package com.lionas.ruwn.fairy;

/**
 * Created by ruwn on 2017-02-26.
 */

public class LionaCommunication {

    private int _id = 0;

    private String liona_type;
    private String liona_detail;
    private String liona_word;

    public int get_id(){
        return _id;
    }

    public String getType() {
        return liona_type;
    }

    public String getDetail() {
        return liona_detail;
    }

    public String getWords() {
        return liona_word;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setType(String liona_type) {
        this.liona_type = liona_type;
    }

    public void setDetail(String liona_detail){
        this.liona_detail = liona_detail;
    }
    public void setWord(String liona_word){
        this.liona_word = liona_word;
    }
}
