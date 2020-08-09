package com.naccoro.wask.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.naccoro.wask.R;
import com.naccoro.wask.setting.SettingActivity;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, MainContract.View {
    MainPresenter presenter;

    ImageView settingButton;
    ImageView calendarButton;
    ImageView emotionImageView;
    TextView cardMessageTextView;
    TextView usePeriodTextView;
    Button changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this);
        initView();
    }

    private void initView() {
        settingButton = findViewById(R.id.imageview_setting);
        calendarButton = findViewById(R.id.imageview_calendar);
        emotionImageView = findViewById(R.id.imageview_emotion);
        cardMessageTextView = findViewById(R.id.textview_card_message);
        usePeriodTextView = findViewById(R.id.textview_use_period);
        changeButton = findViewById(R.id.button_change);

        settingButton.setOnClickListener(this);
        calendarButton.setOnClickListener(this);
        changeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageview_setting:
                // 셋팅 액티비티로 이동
                presenter.clickSettingButton();
                break;
            case R.id.imageview_calendar:
                // 캘린더 액티비티로 이동
                break;
            case R.id.button_change:
                // '교체하기' 버튼을 눌렀을 때
                break;
        }
    }

    @Override
    public void showSettingView() {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
    }
}