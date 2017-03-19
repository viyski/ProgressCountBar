# ProgressCountBar
倒计时圆形进度条

>xml

    <com.gm.afloat.widget.ProgressCountBar
            android:id="@+id/progress_count_bar"
            android:layout_width="120dp"
            android:layout_height="120dp"
            app:circleColor="@android:color/white"
            app:circleRadius="48dp"
            app:progressColor="@android:color/holo_blue_dark"
            android:layout_gravity="center"
            app:progressDuration="100"
            app:strokeWidth="6dp"
            app:countTextSize="16sp"/>
            
          
>java
  
  // 开始倒计时
  bar.start();
  // 倒计时进度监听
  bar.setProgressUpdateListener(new ProgressCountBar.ProgressUpdateListener() {
            @Override
            public void onTick(int duration) {

            }
            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "onFinish", Toast.LENGTH_SHORT).show();
            }
        });
