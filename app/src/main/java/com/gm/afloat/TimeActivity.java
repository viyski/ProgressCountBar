package com.gm.afloat;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgm on 2017/3/21.
 */
public class TimeActivity extends AppCompatActivity {

    private ListView listView;

    private List<String> list;

    private ListItemAdapter.ViewHolder holder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = new ListView(this);
        setContentView(listView);

        list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            list.add("Item :" + i);
        }
        listView.setAdapter(new ListItemAdapter());
    }

    private class ListItemAdapter extends BaseAdapter {

        private boolean isPlaying;
        private TimeCount timeCount;

        private long time;

        private int position;

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                holder = new ViewHolder();
                if (position == 0) {
                    convertView = LayoutInflater.from(TimeActivity.this).inflate(R.layout.list_item_time, parent, false);
                    holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
                    holder.btn = (Button) convertView.findViewById(R.id.btn);
                }else{
                    convertView = new TextView(TimeActivity.this);
                    holder.text = (TextView) convertView;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            this.position = position;
            if (holder.btn != null) {
                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isPlaying = !isPlaying;
                        if (isPlaying) {
                            time = time == 0 ? 40 * 1000 : time;
                            timeCount = new TimeCount(time, 1000, holder.timeTv);
                            timeCount.start();
                        } else {
                            if (timeCount != null) timeCount.cancel();
                        }
                    }
                });
            }
            if (position != 0) {
                holder.text.setText(list.get(position - 1));
            }

            return convertView;
        }

        class ViewHolder {

            TextView timeTv;
            Button btn;

            TextView text;
        }

        class TimeCount extends CountDownTimer {

            private TextView textView;
            public TimeCount(long millisInFuture, long countDownInterval,TextView textView) {
                super(millisInFuture, countDownInterval);
                this.textView = textView;
            }

            @Override
            public void onTick(long millisUntilFinished) {
                time = millisUntilFinished;
                textView.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                time = 0;
                isPlaying = false;
                textView.setText("00:00");
            }
        }
    }
}
