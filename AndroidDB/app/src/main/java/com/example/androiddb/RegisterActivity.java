package com.example.androiddb;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AsyncTask<String, Void, String> {
    // AsyncTask는 추상클래스로 반드시 상속(extends)을 받아야 한다
    String sendMsg, receiveMsg;

    @Override
    protected String doInBackground(String... strings) {
        // 비동기실행으로 메인스레드가 아닌 백그라운드스레드에서 실행을 한다
        // 메인스레드에서 실행이 완료되기를 기다리지 않고 바로 다음 코드를 실행
        try {
            String str;
            
            URL url = new URL("http://192.168.219.109:8090/Android_Web/androidDB.jsp");
            //안드로이드에서 받을 서버주소
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // url 주소를 읽어 커넥션으로 변환하여 conn에 저장
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //key=value&key=value 형식
            // request 처리하는 서버에서는 request body를 읽어 Map형태로 변환하고
            // Content-Type Header 에 추가하여 post의 바디에 추가한다
            // 반드시 body의 Encoding 을 추가해야 한다
            // 대용량 바이너리 데이터를 전송하기에는 비효율적이다.

            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            //url로 데이터를 넘겨준다.

            // 전송할 데이터. GET 방식으로 작성
            sendMsg = "id=" + strings[0] + "&pw=" + strings[1] + "&email=" + strings[2];
            // 위에 선언한 컨텐츠 타입으로 전송
            osw.write(sendMsg); // sendMsg의 문자를 기억한다
            osw.flush(); // 버퍼에 있는 문자를 출력한다

            //jsp와 통신 성공 시 수행
            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                // url로부터 데이터를 읽어온다.
                BufferedReader reader = new BufferedReader(tmp);
                // 읽어올 데이터를 모아서 보내줄 버퍼를 생성
                StringBuffer buffer = new StringBuffer();
                // 스트링버퍼 객체 생성, 초기 문자열이 없고 16개의 문자를 저장할 수 있는 버퍼

                while ((str = reader.readLine()) != null) { // str에 입력한 문자열을 받을 수 있게 reader를 넣고
                                                            // 이 값이 null이 아닐 경우
                    buffer.append(str);  // 버퍼에 str 문자열을 추가한다
                }
                receiveMsg = buffer.toString(); // 버퍼의 문자를 스트링 형식으로 변환
            } else {
                // 통신 실패
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //jsp로부터 받은 리턴 값
        return receiveMsg;
    }

}