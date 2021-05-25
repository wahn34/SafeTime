package com.lionas.ruwn.fairy;

/**
 * Created by ruwn on 2017-03-12.
 */

public class LionaSetData {
    static Boolean lionaLock = false;
    static String lionaPhone = "123465789";
    static String lionaCode = "LIONALOCKSETTING";
    static String lionaHome = "LIONAHOMESTRING";
    static String lionaID = "LIONAIDENTITY";
    static String lionaPW = "LIONAPASSWORD";

    public String getLionaPhone()
    {
        return lionaPhone;
    }
    public Boolean getLionaLock()
    {
        return lionaLock;
    }
    public String getLionaCode()
    {
        return lionaCode;
    }
    public String getLionaHome()
    {
        return lionaHome;
    }
    public String getLionaID()
    {
        return lionaID;
    }
    public String getLionaPW()
    {
        return lionaPW;
    }
    public void setLionaPhone(String lionaPhone)
    {
        this.lionaPhone = lionaPhone;
    }
    public void setLionaLock(Boolean lionaLock)
    {
        this.lionaLock = lionaLock;
    }
    public void setLionaCode(String lionaCode)
    {
        this.lionaCode = lionaCode;
    }
    public void setLionaHome(String lionaHome)
    {
        this.lionaHome = lionaHome;
    }
    public void setLionaID(String lionaID)
    {
        this.lionaID = lionaID;
    }
    public void setLionaPW(String lionaPW)
    {
        this.lionaPW = lionaPW;
    }
}

