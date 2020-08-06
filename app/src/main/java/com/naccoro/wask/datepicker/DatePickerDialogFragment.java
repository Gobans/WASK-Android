package com.naccoro.wask.datepicker;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naccoro.wask.R;
import com.naccoro.wask.datepicker.wheel.WheelDatePicker;

/**
 * @author jaeryo
 *@since 2020.08.06
 *
 * DatePicker 기능을 하는 Modal BottomSheet Dialog 입니다.
 * 사용하기 위해서는 아래와 같이 호출해주면 됩니다.
 * <pre>
 *     DatePickerDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class DatePickerDialogFragment extends BottomSheetDialogFragment {

    private OnDateChangedListener dateChangedListener;
    WheelDatePicker datePicker;
    int year = -1;
    int month = -1;
    int day = -1;

    // TODO: Customize parameters
    public static DatePickerDialogFragment newInstance() {
       return new DatePickerDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (!(dialog instanceof BottomSheetDialog)) return dialog;

        BottomSheetDialog sheetDialog = (BottomSheetDialog)dialog;

        //BottomSheet 상태관리하는 객체인 BottomSheetBehavior
        //아래로 스크롤 시 화면 아래로 숨겨지지 않게 설정
        BottomSheetBehavior behavior = sheetDialog.getBehavior();
        behavior.setHideable(false);
        return sheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker_dialog_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //corner style 적용하기
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_DatePicker_BottomSheetDialog);

        datePicker = view.findViewById(R.id.date_picker);
        if (year != -1 && month != -1 && day != -1) {
            datePicker.setDate(year, month, day);
        }

        view.findViewById(R.id.datePicker_xBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDismiss();
            }
        });

        view.findViewById(R.id.datePicker_okBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //날짜 반환
                if (dateChangedListener != null) {
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth();
                    int day = datePicker.getDay();
                    dateChangedListener.onDateChange(year, month, day);
                }
                datePickerDismiss();
            }
        });
    }

    public DatePickerDialogFragment setOnDateChangedListener(OnDateChangedListener dateChangedListener) {
        this.dateChangedListener = dateChangedListener;
        return this;
    }

    //사용자가 메인에서 보고있는 달력을 기준
    public DatePickerDialogFragment setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        return this;
    }

    /**
     * Dialog 종료
     */
    public void datePickerDismiss() {
        this.dismiss();
    }

    public interface OnDateChangedListener {
        public void onDateChange(int year, int month, int day);
    }
}