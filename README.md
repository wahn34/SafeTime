
# SafeTime

하루 24시간, 대부분의 시간을 스마트 폰과 함께 하고 있습니다.
SafeTime은 스마트 폰의 사용패턴을 통해 유용한 기능을 제공하자는 취지로 시작되었습니다.

수집하여 활용한 패턴은 크게 2가지이고, 점차 늘려갈 계획입니다.

## 패턴1 이동패턴
위치 패턴을 통해 사용자의 위치를 예측하여 유용한 정보를 제공할 수 있습니다.

## 패턴2 사용패턴
사용 패턴을 통해 사용자의 수면 시간을 예측할 수 있습니다.


## 주요 기능
 + 음성 명령
 + 원격 조종
 + 날씨 알림
 + 위치기반 SNS


### 구조
 + 20개의 Activity
 + 17개의 Class (Database, Service, UI, 패턴, 데이터)
 + 3개의 내장 Database(이동패턴, 사용패턴, 음성대화)
 + 3개의 서버 Database(사용자 정보, 사용자간 대화, 데이터분석)
 + 2개의 Lib


## 사용 외부 라이브러리(API)

사용자 보도 이용 경로 제공을 위한 TmapAPI가 사용되었습니다.  [Tmap API][tmaplink]

사용자 수면 패턴 그래프를 제공하기 위한 GraphView가 사용되었습니다.  [GraphView][graphview]

[tmaplink]: https://tmapapi.sktelecom.com/
[graphview]: http://www.android-graphview.org/simple-graph/



# 서비스 실행 필요
몇 기능은 백그라운드에서 실행되어야 정상적으로 동작합니다. (사용자 사용패턴 분석, 사용자 이동 패턴 분석, 문자 당겨받기, 사용자 위치 추적 등)

방법1
```java
Intent intent = new Intent(this, Service.class);
startService(intent);
```
방법2
```java
if (isServiceRunning("com.lionas.ruwn.fairy.LionaService")) {}
else
{
	Intent intent = new Intent(this, LionaService.class);
	startService(intent);
}

SafeDb safeDb = new SafeDb();
if (safeDb == null) {
	safeDb = new DBHelper(Activity.this, “DB",null,1);
}
```


기기 이벤트 수신 (BroadcastReceiver)
```java
public void onReceive(Context context, Intent intent) {
	if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {/*부팅 완료*/}
	if (Intent.ACTION_SCREEN_ON == intent.getAction()) {/*스크린 ON*/}
	if (Intent.ACTION_SCREEN_OFF == intent.getAction()) {/*스크린 OFF*/}
	if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {/*문자 수신*/}
}
```

기기 이벤트 등록 (BroadcastReceiver)
```java
BroadcastReceiver stReceiver = new Broadcast(); //Broadcast.java
IntentFilter itFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
itFilter.addAction(Intent.ACTION_SCREEN_OFF);
itFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
itFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
registerReceiver(stReceiver, itFilter);
}
```



## 권한 확인
```java
ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.READ_CONTACTS, MODE_PRIVATE);
int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);
if(permissionCheck== PackageManager.PERMISSION_DENIED){
	// 권한 없음
}else{
	// 권한 있음
}
```



## 내장 DB(SQLite)

생성
```java
StringBuffer sb = new StringBuffer();
sb.append(" CREATE TABLE SAFET_TABLE (");
sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
sb.append(" TYPE TEXT, ");
sb.append(" DETAIL TEXT, ");
sb.append(" WORD TEXT ) ");
db.execSQL(sb.toString());
Toast.makeText(context, "Table 생성", Toast.LENGTH_SHORT).show();
```
쓰기
```java
SQLiteDatabase db = getWritableDatabase();
StringBuffer sb = new StringBuffer();
sb.append(" INSERT INTO SAFET_TABLE ( TYPE, DETAIL,
                WORD ) ");
sb.append(" VALUES ( ?, ?, ? ) ");
db.execSQL(sb.toString(),
new Object[]{
safeData.getType(),
safeData.getDetail(),
safeData.getWords()});
```
읽기
```java
StringBuffer sb = new StringBuffer();
sb.append(" SELECT _ID, TYPE, DETAIL, WORD FROM SAFET_TABLE");
SQLiteDatabase db = getReadableDatabase();
Cursor cursor = db.rawQuery(sb.toString(), null);
SafeData safeData = null;
while (cursor.moveToNext()) {
  safeData = new SafeData();
  safeData.set_id(cursor.getInt(0));
  safeData.setType(cursor.getString(1));
  safeData.setDetail(cursor.getString(2));
  safeData.setWord(cursor.getString(3));
}
```
데이터 Class
```java
private int _id = 0;
private String liona_type;
private String liona_detail;
private String liona_word;
public int get_id(){return _id;}
public void set_id(int _id) {this._id = _id;} 
```

## 외부 DB
PHP코드
```php
<?php  
$con=mysqli_connect(“서버IP",“관리자ID",“SQL관리자 PW",“DB명");  
mysqli_set_charset($con,"utf8");
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  
$sid= $_POST[‘sid'];  
$smessage = $_POST[‘smessege'];  
$result = mysqli_query($con,"insert into 테이블명 (sid,smessage)
      values ('$lionaid','$lionarequest')");  
  if($result){  
    echo 'success';  
  }  
  else{  
    echo 'failure';  
  }  
mysqli_close($con);  
?> 
```
Android 코드
```java
private void insertToDatabase(String name, String address) {
	class InsertData extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
		try {
			String name = (String) params[0];
			String request = (String) params[1];
			String link = “주소";
			String data = URLEncoder.encode(“sid ", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
			data += "&" + URLEncoder.encode(“smessage"UTF-8") + "=" + URLEncoder.encode(request, "UTF-8");
			URL url = new URL(link);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;// Read Server Response
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				break;
			}                
			return sb.toString();
			} catch (Exception e) {
				return new String("Exception: " + e.getMessage());
			}        
		}    
	}    
	InsertData task = new InsertData();    
	task.execute(name, address);
}
```


# Changelog

#### 1.0.0
 + initial commit
