package com.lionas.ruwn.fairy;

import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;

/**
 * Created by ruwn on 2017-03-09.
 */

public class LionaWeather {
    private String strCity;
    private String strTemp;

    public String get_City()
    {
        return strCity;
    }
    public String get_Temp()
    {
        return strTemp;
    }
    public void set_City(String strCity)
    {
        this.strCity = strCity;
    }
    public void set_Temp(String strTemp)
    {
        this.strTemp = strTemp;
    }
    public void getWeather ()
    {
        try {
            URL url = new URL("http://www.kma.go.kr/XML/weather/sfc_web_map.xml");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(url.openStream(), "utf-8");
            String Item = "";
            String ItemName = "";
            String ItemContents = "";
            boolean bSet = false;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if (tag.equals("local")) {
                            ItemContents = "";
                            String state = parser.getAttributeValue(null,"desc");
                            String temperature = parser.getAttributeValue(null,"ta");
                            temperature += "℃";
                            ItemContents = state + ", " + temperature + "  ";
                            bSet = true;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        if (bSet) {
                            ItemName = "";
                            String region = parser.getText();
                            if (region.contains("서울"))
                            {
                                //strCity = region;
                                set_City(region);
                                //strTemp = ItemContents;
                                set_Temp(ItemContents);
                            }
                            ItemName += region + "   - ";
                            Item += ItemName + ItemContents;
                            Item += "\n";
                            bSet = false;
                        }
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            //Toast.makeText(this. e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
