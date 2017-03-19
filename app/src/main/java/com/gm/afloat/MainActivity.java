package com.gm.afloat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gm.afloat.widget.ProgressCountBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private HomeReceiver receiver;
    private ProgressCountBar bar;
    private TextView tickTv;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tickTv = (TextView) findViewById(R.id.tick_tv);
        assert tickTv != null;
        tickTv.setOnClickListener(this);
        bar = (ProgressCountBar) findViewById(R.id.progress_count_bar);

        bar.setProgressUpdateListener(new ProgressCountBar.ProgressUpdateListener() {
            @Override
            public void onTick(int duration) {

            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "onFinish", Toast.LENGTH_SHORT).show();
            }
        });

        receiver = new HomeReceiver();
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        registerReceiver(receiver, homeFilter);

    }

    @Override
    public void onClick(View v) {
        if (count == 0 ) {
            count++;
            bar.start();
        }
    }

    private class HomeReceiver extends BroadcastReceiver {

        private final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);

                if (reason == null)
                    return;

                // Home键
                if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    Toast.makeText(getApplicationContext(), "按了Home键", Toast.LENGTH_SHORT).show();
                }

                // 最近任务列表键
                if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                    Toast.makeText(getApplicationContext(), "按了最近任务列表", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

}
