package com.lionas.ruwn.fairy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.*;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
/*
 * Created by ruwn on 2017-02-17.
 */

public class MatchCommnucation {

    public ArrayList<String> greets = new ArrayList<String>();
    public ArrayList<Integer> chkGreets = new ArrayList<Integer>();
    private DBHelper dbHelper;
    LionaCommunication lionaCommunication = new LionaCommunication();
    LionaWeather lionaWeather = new LionaWeather();
    private int lionaCur;
    private String strMessageBody="";
    public int ifTryLiona = 0;
    public static ArrayList<String> lionaType = new ArrayList<String>();
    public static ArrayList<String> lionaDetail = new ArrayList<String>();
    public static ArrayList<String> lionaWords = new ArrayList<String>();

    public static ArrayList<String> lionaPhoneName = new ArrayList<String>();
    public static ArrayList<String> lionaPhoneNumber = new ArrayList<String>();

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    //목적을 0번 인덱스로 합니다.
    //대상을 1번 인덱스로 합니다.
    //명령을 2번 인덱스로 합니다.
    //기타를 3번 인덱스로 합니다.
    //반응을 4번 인덱스로 합니다.

    //명령 개체 값 ord를 구분합니다.
    //구분지표
    //1일 경우 문자 메시지를 호출합니다.
    //2일 경우 전화 서비스를 호출합니다.
    //3일 경우 검색 서비스를 호출합니다.
    public String syncSentence(String[] array)
    {
        String strTmp = "";
        ArrayList<String> arrSentence = new ArrayList<String>();
        for (int i = 0; i < 5; i++)
        {
            arrSentence.add("");
        }

        for (int i = 0; i < array.length; i++)
        {
            for (int j = 0; j < lionaDetail.size(); j++)
            {
                System.out.println("Detail 값 확인 : " +lionaDetail.get(j));
                if(lionaDetail.get(j).matches("인사"))
                {
                    System.out.println("인사 값 확인 : " +lionaDetail.get(j));
                    System.out.println("Array값 확인 : " + array[i]);
                    if (array[i].contains(lionaWords.get(j)))
                    {
                        arrSentence.set(4,"인사");
                        System.out.println("성공 : " + arrSentence.get(4));
                    }
                }
            }
            //String strTemps = mcu.greets.get(i);
            if(array[i].contains("문자") || array[i].contains("전화") || array[i].contains("검색") || array[i].contains("날씨"))
            {
                if (array[i].contains("문자"))
                {
                    arrSentence.set(2, "문자");
                    array[i] = "";
                }
                else if (array[i].contains("전화"))
                {
                    arrSentence.set(2, "전화");
                    array[i] = "";
                }
                else if (array[i].contains("검색"))
                {
                    arrSentence.set(2, "검색");
                    array[i] = "";
                }
                else if (array[i].contains("날씨"))
                {
                    arrSentence.set(2, "날씨");
                    array[i] = "";
                }
                else System.out.println("오류 발생");
                System.out.println("명령 값 : " + arrSentence.get(2));
            }
            if(array[i].matches(".*"+"을") || array[i].matches(".*"+"를"))
            {
                System.out.println("목적 값: "+array[i]);
                array[i] = array[i].replace("을", "");
                array[i] = array[i].replace("를", "");
                arrSentence.set(0, array[i]);
                System.out.println("저장 값: "+arrSentence.get(0));
                //arrSentence.set(i, "테스트");
                //System.out.println("저장 값: "+arrSentence.get(i));

                //명령어 있을 경우 수행, 없을 경우 명령어 수신 재시도
            }
            if(array[i].matches(".*"+"에게") || array[i].matches(".*"+"한테"))
            {
                System.out.println("대상: "+array[i]);
                array[i] = array[i].replace("에게", "");
                array[i] = array[i].replace("한테", "");
                arrSentence.set(1, array[i]);
                System.out.println("저장 값: "+arrSentence.get(1));
                //주소록 / 번호입력 받기 구현부
            }
        }
        for (int i = 0; i < 5; i++)
        {
            if (arrSentence.get(i)=="")	arrSentence.set(i,"0");
        }

        for (int i = 0; i < array.length; i++){

            if (array[i].contains(arrSentence.get(0)) || array[i].contains(arrSentence.get(1)) || array[i].contains(arrSentence.get(2)) || array[i].contains(arrSentence.get(3)) || array[i].contains(arrSentence.get(4))) {
                System.out.println("통과 : " + array[i]);
                System.out.println(arrSentence.get(0));
                System.out.println(arrSentence.get(1));
                System.out.println(arrSentence.get(2));
                System.out.println(arrSentence.get(3));
                System.out.println(arrSentence.get(4));
            }
            else
            {
                //if (array[i].contains("보내줘") || (array[i].contains("해줘"))) ;

                //else {
                    array[i] = array[i].replace("라고", "");
                    strTmp = strTmp + array[i];
                    System.out.println("미통 : " + array[i]);
               //}
            }
        }
        arrSentence.set(3, strTmp);
        System.out.println("기타 값: "+arrSentence.get(3));

        //String strTmps = communicationLionaSystem(arrSentence);
        return communicationLionaSystem(arrSentence);
    }

    private String communicationLionaSystem(ArrayList<String> arrSentence){
        System.out.println("!!!!!!!!!!!");
        System.out.println("0번째 : " + arrSentence.get(0));
        System.out.println("1번째 : " + arrSentence.get(1));
        System.out.println("2번째 : " + arrSentence.get(2));
        System.out.println("3번째 : " + arrSentence.get(3));
        System.out.println("4번째 : " + arrSentence.get(4));
        if (arrSentence.isEmpty()) System.out.println("헐");
        String strResult = "인식하지 못하는 명령어입니다.";
        //목적을 0번 인덱스로 합니다.
        //대상을 1번 인덱스로 합니다.
        //명령을 2번 인덱스로 합니다.
        //기타를 3번 인덱스로 합니다.
        //반응을 4번 인덱스로 합니다.

        //명령 개체 값 ord를 구분합니다.
        //구분지표
        //1일 경우 문자 메시지를 호출합니다.
        //2일 경우 전화 서비스를 호출합니다.
        //3일 경우 검색 서비스를 호출합니다.
        if (arrSentence.get(2).contains("문자") || arrSentence.get(2).contains("검색"))
        {
            if (arrSentence.get(1) == "0")
            {
                //누구한테 보내야되는지 없음
                strResult = "누구에게 보내야될지 잘 모르겠어요";
            }
            else
            {
                lionaCur = 0;
                for (int x = 1; x <lionaPhoneName.size(); x++ )
                {
                    System.out.println(" 접근 " + lionaPhoneName.get(x));
                    if (lionaPhoneName.get(x).contains(arrSentence.get(1)))
                    {
                        System.out.println("찾음 : " + lionaPhoneName.get(x));
                        lionaCur = x;
                    }
                }
                if (lionaCur != 0)
                {
                    strMessageBody="";
                    strResult = arrSentence.get(1) + "(" + lionaPhoneNumber.get(lionaCur) + ")" + "에게 " + arrSentence.get(3) + "라고 " + arrSentence.get(2) + "(을)를 할까요?";
                    ifTryLiona = 1;
                    strMessageBody = arrSentence.get(3);
                    strMessageBody.replace("보내 줘","");
                }
                else strResult = "주소록에서 " + arrSentence.get(1) + "을(를) 찾을 수 없어 문자 발송에 실패했습니다.";
            }
        }
        else if (arrSentence.get(2).contains("날씨"))
        {
            MainActivity mainActivity = new MainActivity();
            //String strTemp = mainActivity.txtTemp.getText().toString();
            //[]arrTemp;
            //arrTemp = strTemp.split(",");
            lionaWeather.getWeather();
            strResult = lionaWeather.get_City() + "지역은 현재 " + lionaWeather.get_Temp() + "입니다.";
        }
        else if (arrSentence.get(2).contains("전화"))
        {
            if (arrSentence.get(1) == "0")
            {
                //누구한테 보내야되는지 없음
                strResult = "누구에게 보내야될지 잘 모르겠어요";
            }
            else
            {
                lionaCur = 0;
                for (int x = 1; x <lionaPhoneName.size(); x++ )
                {
                    System.out.println(" 접근 " + lionaPhoneName.get(x));
                    if (lionaPhoneName.get(x).contains(arrSentence.get(1)))
                    {
                        System.out.println("찾음 : " + lionaPhoneName.get(x));
                        lionaCur = x;
                    }
                }
                if (lionaCur != 0)
                {
                    strMessageBody="";
                    strResult = arrSentence.get(1) + "(" + lionaPhoneNumber.get(lionaCur) + ")" + "에게 " + arrSentence.get(2) + "를 할까요?";
                    ifTryLiona = 2;
                    strMessageBody = arrSentence.get(3);
                }
                else strResult = "주소록에서 " + arrSentence.get(1) + "을(를) 찾을 수 없어 전화 발신에 실패했습니다.";
            }
        }
        else if (arrSentence.get(4).contains("인사"))
        {
            System.out.println("wow");
            final ArrayList<String> rplGreets = new ArrayList<String>();
            rplGreets.add("네 안녕하세요");
            rplGreets.add("저는 테스트 중인 기능입니다.");
            rplGreets.add("안녕하세요. 반가워요");
            rplGreets.add("안녕하세요. 도움이 필요하신가요?");
            rplGreets.add("안녕하세요. 무엇을 도와드릴까요?");
            Random random = new Random();
            int ran = (int) (random.nextInt(5));
            strResult = rplGreets.get(ran);
        }
        else
        {
            System.out.println("뭐가 문제");
        }
        return strResult;
    }
    public String ifTrySendMessage(String[] array){
        String strReturn = "";
        for (int i = 0; i < array.length; i++)
        {
            if (array[i].contains("네") || array[i].contains("예") || array[i].contains("응") || array[i].contains("어") || array[i].contains("그래") || array[i].contains("보내"))
            {
                strReturn = "문자를 발송합니다";
                SmsManager sms = SmsManager.getDefault();
                System.out.println(lionaPhoneNumber.get(lionaCur));
                System.out.println(strMessageBody);
                sms.sendTextMessage(lionaPhoneNumber.get(lionaCur), null, strMessageBody, null, null);
            }
            else strReturn = "문자 발송을 취소합니다";
        }
        ifTryLiona = 0;
        return strReturn;
    }
    public String ifTryCallPhone(String[] array){
        String strReturn = "";
        for (int i = 0; i < array.length; i++)
        {
            if (array[i].contains("네") || array[i].contains("예") || array[i].contains("응") || array[i].contains("어") || array[i].contains("그래"))
            {
                strReturn = "발신을 합니다";
                //Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", lionaPhoneNumber.get(lionaCur), null));
                //startActivity(intent);
            }
            else strReturn = "발신을 취소합니다";
        }
        ifTryLiona = 0;
        return strReturn;
    }

}
