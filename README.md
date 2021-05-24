
# SafeTime

하루 24시간, 대부분의 시간을 스마트 폰과 함께 하고 있습니다.
SafeTime은 스마트 폰의 사용패턴을 통해 유용한 기능을 제공하자는 취지로 시작되었습니다.

수집하여 활용한 패턴은 크게 2가지이고, 점차 늘려갈 계획입니다.

## 패턴1 이동패턴
위치 패턴을 통해 사용자의 위치를 예측하여 유용한 정보를 제공할 수 있습니다.

## 패턴2 사용패턴
사용 패턴을 통해 사용자의 수면 시간을 예측할 수 있습니다.


## 사용 API

사용자 보도 이용 경로 제공을 위한 TmapAPI가 사용되었습니다.  [Tmap API][tmaplink]

사용자 수면 패턴 그래프를 제공하기 위한 TmapAPI가 사용되었습니다.  [Tmap API][tmaplink]

[tmaplink]: https://tmapapi.sktelecom.com/

### 쉬운 사용
```
  띄어쓰기 교정
  단어 맞춤법 교정
  클립보드 복사
```

### 빠른 사용
```
  틀렸던 단어 즉시 교정
  터치하여 바로 교정
  알림 영역에서 터치하여 팝업 실행
```

### 정확한 사용
```
  직접 터치하여 원하는 부분만 수정
  교정 대상, 결과 확인
  교정된 이유 확인
```
<img src="/images/main.jpg" width="180px" height="370px" title="메인" alt="main"></img>
<img src="/images/sit2.jpg" width="180px" height="370px" title="사용2" alt="situation2"></img>
<img src="/images/history1.jpg" width="180px" height="370px" title="기록1" alt="history1"></img>
<img src="/images/pop.jpg" width="180px" height="370px" title="팝업1" alt="pop"></img>
<br/>


# 사용 라이브러리
https://github.com/blackfizz/EazeGraph

# 수정 필요
사용자 데이터는 내부 SQLite와 AWS EC2 환경의 Oracle DB에 저장되도록 설정되어있습니다.  
이 부분을 수정하거나 주석 후 사용해야 합니다.
```java
@Override
protected ArrayList<String> doInBackground( String... params){
	wrdLst.clear();
	ResultSet reset = null;
	Connection conn = null;
	try {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		conn = DriverManager.getConnection("jdbc:oracle:thin:@orclelec.cfvmyazpemfk.us-east-1.rds.amazonaws.com:1521:orcl","rywn34","myelectric");
		Statement stmt = conn.createStatement();
		reset = stmt.executeQuery(query);
		while(reset.next()){
			if ( isCancelled() ) break;
			final String str = reset.getString(1)+"<1>"+reset.getString(2);
			wrdLst.add(str);
		}
	conn.close();
	}
	catch (Exception e) {}
	return wrdLst;
}
```

# Changelog

#### 1.0.0
 + initial commit
#### 1.0.1
 + 불필요한 이미지 제거
