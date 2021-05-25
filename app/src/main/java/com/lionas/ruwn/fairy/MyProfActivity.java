package com.lionas.ruwn.fairy;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyProfActivity extends AppCompatActivity {

    private static final int ROTATE_VALUE = 90;
    ImageView imgMyProf;
    private int currentAngle = 0;
    private Bitmap mbitMap;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_prof);
        try{
            imgMyProf = (ImageView)findViewById(R.id.imgMyProf);
            String imgpath = "data/data/com.lionas.ruwn.fairy/files/test.png";
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            imgMyProf.setImageBitmap(bm);
        }
        catch(Exception e){
            Snackbar.make(getWindow().getDecorView().getRootView(), "저장된 프로필 이미지가 없습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        Button btnChgProf = (Button)findViewById(R.id.btnChgProf);
        btnChgProf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });
        Button btnRotate = (Button)findViewById(R.id.btnRotate);
        btnRotate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                int width = imgMyProf.getWidth();
                int height = imgMyProf.getHeight();
                currentAngle = ROTATE_VALUE + (currentAngle % 360);
                Bitmap resize = getImageProcess(mbitMap, currentAngle, width, height);
                imgMyProf.setImageBitmap(resize);
            }
        });
        Button btnBlur = (Button)findViewById(R.id.btnBlur);
        btnBlur.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                BlurMaskFilter blurFilter = new BlurMaskFilter(30, BlurMaskFilter.Blur.OUTER);
                Paint shadowPaint = new Paint();
                shadowPaint.setMaskFilter(blurFilter);

                int[] offsetXY = new int[2];
                Drawable d = imgMyProf.getDrawable();
                Bitmap btMap = ((BitmapDrawable)d).getBitmap();
                Bitmap shadowImage = btMap.extractAlpha(shadowPaint, offsetXY);
                Bitmap shadowImage32 = shadowImage.copy(Bitmap.Config.ARGB_8888, true);
                Canvas c = new Canvas(shadowImage32);
                c.drawBitmap(btMap, -offsetXY[0], -offsetXY[1], null);

                imgMyProf.setImageBitmap(shadowImage32);
            }
        });
        Button btnBright = (Button)findViewById(R.id.btnBright);
        btnBright.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Bitmap bmpGrayscale = Bitmap.createBitmap(imgMyProf.getWidth(), imgMyProf.getHeight(), Bitmap.Config.RGB_565);
                Canvas c = new Canvas(bmpGrayscale);
                Paint paint = new Paint();
                ColorMatrix cm = new ColorMatrix();

                cm.setSaturation(0);
                ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);

                Drawable d = imgMyProf.getDrawable();
                Bitmap btMap = ((BitmapDrawable)d).getBitmap();

                paint.setColorFilter(f);
                c.drawBitmap(btMap, 0, 0, paint);

                imgMyProf.setImageBitmap(bmpGrayscale);
            }
        });
        Button btnSaveImg = (Button)findViewById(R.id.btnSaveImg);
        btnSaveImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Drawable d = imgMyProf.getDrawable();
                Bitmap btMap = ((BitmapDrawable)d).getBitmap();

                try{

                    File file = new File("test.png");
                    FileOutputStream fos = openFileOutput("test.png" , 0);
                    btMap.compress(Bitmap.CompressFormat.PNG, 100 , fos);
                    fos.flush();
                    fos.close();

                }catch(Exception e) {

                }
            }
        });
    }

    private Bitmap getImageProcess(Bitmap bmp, int nRotate, int viewW, int viewH){

        Matrix matrix = new Matrix();

        // 이미지의 해상도를 줄인다.
        /*float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight);*/

        matrix.postRotate(nRotate); // 회전
        // 이미지를 회전시킨다
        Bitmap rotateBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

        // View 사이즈에 맞게 이미지를 조절한다.
        Bitmap resize = Bitmap.createScaledBitmap(rotateBitmap, viewW, viewH, true);

        return resize;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Toast.makeText(getBaseContext(), "resultCode : "+resultCode,Toast.LENGTH_SHORT).show();

        if(requestCode == 100)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    mbitMap= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    imgMyProf = (ImageView)findViewById(R.id.imgMyProf);

                    //배치해놓은 ImageView에 set
                    imgMyProf.setImageBitmap(mbitMap);


                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgName;
    }
}
