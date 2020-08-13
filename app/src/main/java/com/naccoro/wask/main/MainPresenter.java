package com.naccoro.wask.main;


import android.content.Context;

import com.naccoro.wask.preferences.NotificationPreferenceManager;
import com.naccoro.wask.utils.AlarmUtil;
import com.naccoro.wask.utils.DateUtils;

import android.util.Log;

import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;

public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = "MainPresenter";

    private ReplacementHistoryRepository replacementHistoryRepository;

    private boolean isNoData = true;

    private boolean isChanged = false;

    MainContract.View mainView;

    MainPresenter(MainContract.View mainView, ReplacementHistoryRepository replacementHistoryRepository) {
        this.mainView = mainView;
        this.replacementHistoryRepository = replacementHistoryRepository;
    }

    /**
     * MainView에서 보여줘야 할 Data를 가져오는 함수
     */
    @Override
    public void start() {
        int period;

        if (isChanged) {
            //오늘 교체했다면 1일 째 사용 중
            period = 1;
        } else {
            //오늘 교체하지 않았다면 연속 사용 일자 계산
            period = getMaskPeriod();
        }
        mainView.setPeriodTextValue(period);

        if (period == 0) {
            //교체 기록이 없음
            mainView.showNoReplaceData();
            mainView.enableReplaceButton();
        } else if (period > 1) {
            //교체한지 하루 이상 지남
            mainView.showBadMainView();
        } else {
            //교체한 당일
            isChanged = true;
            mainView.showGoodMainView();
            mainView.disableReplaceButton();
        }
    }

    /**
     * 첫 번째 교체인지 확인 후 멘트 변경
     */
    private void checkIsFirstReplacement() {
        if (isNoData) {
            mainView.changeUsePeriodMessage();
            isNoData = false;
        }
    }

    @Override
    public void clickSettingButton() {
        mainView.showSettingView();
    }

    @Override
    public void clickCalendarButton() {
        mainView.showCalendarView();
    }

    /**
     * 사용자가 교체하기 버튼을 누를 경우 호출되는 함수
     */
    @Override
    public void changeMask(Context context) {

        if (!isChanged) {
            checkIsFirstReplacement();

            replacementHistoryRepository.insertToday(new ReplacementHistoryRepository.InsertHistoryCallback() {
                @Override
                public void onSuccess() {
                    mainView.showReplaceToast();
                    mainView.enableReplaceButton();
                    isChanged = true;

                    start();

                    setMaskReplaceNotification(context);
                }

                @Override
                public void onDuplicated() {
                    Log.d(TAG, "onDuplicated: true");
                }
            });
        } else {
            mainView.showCancelDialog();
        }
    }

    /**
     * 교체 취소 다이얼로그에서 확인을 눌렀을 때 호출되는 함수
     */
    @Override
    public void cancelChanging(Context context) {
        replacementHistoryRepository.deleteToday();
        isChanged = false;

        //메인화면 갱신
        start();

        setMaskReplaceNotification(context);
    }

    /**
     * 등록되어 있는 알람을 종료하고 새로운 교체하기 알람을 등록한다.
     */
    private void setMaskReplaceNotification(Context context) {
        //남아있던 alarm 을 종료한다.
        AlarmUtil.cancelReplacementCycleAlarm(context);
        AlarmUtil.cancelReplaceLaterAlarm(context);

        //혹여나 핸드폰이 종료되어 BootReceiver 가 작동되어도 ReplaceLater Alarm 을 작동되지 않게 하기 위해 0 을 넣는다.
        NotificationPreferenceManager.setReplaceLaterDate(0);

        int todayDate = DateUtils.getToday();

        //교체하기 Date 를 등록한다. BootReceiver 가 작동되어도 등록한 날짜 기준으로 period 후에 alarm 이 동작하게 만든다.
        NotificationPreferenceManager.setReplacementCycleDate(todayDate);

        AlarmUtil.setReplacementCycleAlarm(context);
    }

    /**
     * WaskDatabase에서 현재 마스크 교체 상태를 가져오는 함수
     *
     * @return [오늘 날짜 - 마지막 교체 일자 + 1]
     */
    private int getMaskPeriod() {
        int lastReplacement = replacementHistoryRepository.getLastReplacement();
        if (lastReplacement == -1) {
            //교체 기록이 없을 경우
            isNoData = true;
            return 0;
        }
        return DateUtils.calculateDateGapWithToday(lastReplacement) + 1;
    }
}
