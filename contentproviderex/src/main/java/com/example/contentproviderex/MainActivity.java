package com.example.contentproviderex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {

    Button btnCall, btnAddress;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCall=findViewById(R.id.btncall);
        btnAddress=findViewById(R.id.btnAddress);
        tvResult=findViewById(R.id.tvResult);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText(getCallHistory());
            }
        });
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText(getAddress());
            }
        });
        AutoPermissions.Companion.loadAllPermissions(this, 100);
    }
    String getCallHistory(){
        String[] callSet=new String[] {CallLog.Calls.DATE, CallLog.Calls.TYPE,
                CallLog.Calls.NUMBER, CallLog.Calls.DURATION}; // 날짜, 발/수신, 전화번호, 통화시간(초)
        Cursor cursor=getContentResolver().query(CallLog.Calls.CONTENT_URI,callSet,
                null, null, null);
        if(cursor==null){
            return "통화기록 없음";
        }else{
            StringBuffer callBuff=new StringBuffer();
            callBuff.append("날짜     구분      전화번호        통화시간\n");
            while(cursor.moveToNext()){
                long callDate=cursor.getLong(0);
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                String dateStr=dateFormat.format(new Date(callDate));
                callBuff.append(dateStr + " ");
                if(cursor.getInt(1)== CallLog.Calls.INCOMING_TYPE){
                    callBuff.append("수신 : ");
                } else{
                    callBuff.append("발신 : ");
                }
                callBuff.append(cursor.getString(2) + "  ");
                callBuff.append(cursor.getString(3) + "초\n");
            }
            cursor.close();
            return  callBuff.toString();
        }
    }

    //통화기록 가져오는 메소드
    String getAddress(){
        Cursor cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null,null);
        String strAddress="이름       전화번호\n";
        String name, phoneNumber;
        while(cursor.moveToNext()){
            name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)); //주소록 이름 가져오기
            phoneNumber=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); //주소록 번호 가져오기
            strAddress+=name+"      "+ phoneNumber+"\n";
        }
        cursor.close();
        return strAddress;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, 100, permissions,this);
    }

    @Override
    public void onDenied(int i, String[] strings) {

    }

    @Override
    public void onGranted(int i, String[] strings) {

    }
}