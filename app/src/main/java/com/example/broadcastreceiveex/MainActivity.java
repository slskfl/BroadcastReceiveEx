package com.example.broadcastreceiveex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView imgBattery;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgBattery=findViewById(R.id.imgBattery);
        tvResult=findViewById(R.id.tvResult);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(br, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    BroadcastReceiver br=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(Intent.ACTION_BATTERY_CHANGED)){
                int remain=intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
                tvResult.setText("현재 배터리 : " + remain + "%\n");
                if(remain>=90){
                    imgBattery.setImageResource(R.drawable.bettery90);
                }else if(remain>=70){
                    imgBattery.setImageResource(R.drawable.bettery70);
                }else if(remain>=40){
                    imgBattery.setImageResource(R.drawable.bettery40);
                }else if(remain>=10){
                    imgBattery.setImageResource(R.drawable.bettery10);
                }else if(remain>=0){
                    imgBattery.setImageResource(R.drawable.bettery0);
                }
                int plug=intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
                switch (plug){
                    case 0:
                        tvResult.append("전원 연결 : 해제 ");
                        break;
                    case BatteryManager.BATTERY_PLUGGED_AC: //220V 충전기 연결
                        tvResult.append("전원 연결 : 충전기 연결");
                        break;
                    case BatteryManager.BATTERY_PLUGGED_USB: //
                        tvResult.append("전원 연결 : USB 연결");
                        break;
                }
            }
        }
    };
}