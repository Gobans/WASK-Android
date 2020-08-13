package com.naccoro.wask.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.naccoro.wask.preferences.NotificationPreferenceManager;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.replacement.model.Injection;
import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.DateUtils;

import java.util.Calendar;

public class ReplaceMaskReceiver extends BroadcastReceiver {


    /**
     * Notification에서 교체하기 버튼을 눌렀을 때 교체하기 기능을 수행한다.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO: foreground 사용일자를 변경하는 작업도 할 수 있을 것 같다.

        int notificationId = intent.getIntExtra("notificationId", -1);
        if (notificationId != -1) {
            //알람 종료
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }

        insertMaskChangeHistory(context);

    }

    /**
     * Notification으로 사용자가 교체하기를 작동
     */
    private void insertMaskChangeHistory(Context context) {
        ReplacementHistoryRepository replacementHistoryRepository = Injection.replacementHistoryRepository(context);

        replacementHistoryRepository.insertToday(new ReplacementHistoryRepository.InsertHistoryCallback() {
            @Override
            public void onSuccess() {
                //혹여나 핸드폰이 종료되어 BootReceiver 가 작동되어도 ReplaceLater Alarm 을 작동되지 않게 하기 위해 0 을 넣는다.
                NotificationPreferenceManager.setReplaceLaterDate(0);

                //나중에 교체하기 알람을 지운다.
                AlarmUtil.cancelReplaceLaterAlarm(context);

                int todayDate = DateUtils.getToday();
                //교체하기 Date 를 등록한다. BootReceiver 가 작동되어도 등록한 날짜 기준으로 period 후에 alarm 이 동작하게 만든다.
                NotificationPreferenceManager.setReplacementCycleDate(todayDate);
            }

            @Override
            public void onDuplicated() {

            }
        });
    }

}