package com.naccoro.wask.calendar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.naccoro.wask.replacement.repository.ReplacementHistoryRepository;
import com.naccoro.wask.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.naccoro.wask.R.*;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private static final String TAG = "CalendarAdapter";

    private ArrayList<CalendarItem> calendarList;
    private boolean isModifyMode;

    private ReplacementHistoryRepository replacementHistoryRepository;

    public CalendarAdapter(ArrayList<CalendarItem> calendarList, ReplacementHistoryRepository replacementHistoryRepository) {
        this.calendarList = calendarList;
        this.isModifyMode = false; // 무조건 실행했을때는 수정 불가 모드
        this.replacementHistoryRepository = replacementHistoryRepository;
    }

    public void setCalendarList(ArrayList<CalendarItem> calendarList) {
        this.calendarList = calendarList;
        notifyDataSetChanged();
    }

    public void setModifyMode(boolean isModifyMode) {
        this.isModifyMode = isModifyMode;
        notifyDataSetChanged();
    }

    /**
     * 아이템 뷰를 넣기 위한 view holder 생성
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public CalendarAdapter.CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(layout.calendar_item, parent, false) ;
        CalendarAdapter.CalendarViewHolder viewHolder = new CalendarAdapter.CalendarViewHolder(view) ;

        return viewHolder ;
    }

    /**
     * position에 해당하는 data를 view holder에 넣기
     *
     * @param calendarViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder calendarViewHolder, int position) {

        CalendarItem item = calendarList.get(position);
        calendarViewHolder.dateTextView.setText(item.getDate().get(Calendar.DAY_OF_MONTH) + "");

        decorateItem(calendarViewHolder, item, position);

        // 수정모드 설정
        if (isModifyMode) {
            calendarViewHolder.itemView.setOnClickListener(view -> onDayClick(calendarViewHolder, item));
        }
    }

    /**
     * 아이템(각 날짜) 꾸미기
     *
     * @param calendarViewHolder
     * @param item
     * @param position
     */
    private void decorateItem(CalendarViewHolder calendarViewHolder, CalendarItem item, int position) {

        View itemView = calendarViewHolder.getItemView();

        // 일요일은 빨간날
        if (position%7==0) {
            calendarViewHolder.dateTextView.setTextColor(itemView.getResources().getColor(color.waskRed));
        }

        // 오늘이면 동그라미 표시
        if (item.isSelect()) {
            calendarViewHolder.dateTextView.setTextColor(itemView.getResources().getColor(color.white));
            calendarViewHolder.dateBackgroundImageView.setVisibility(itemView.getVisibility());
        }

        //지난 달과 다음 달의 날짜는 흐리게
        if (!item.isCurrentMonth()) {
            calendarViewHolder.dateTextView.setAlpha(0.4f);
        } else {
            calendarViewHolder.dateTextView.setAlpha(1.0f);
        }

        // 마스크 교체한 날 표시
        if (item.isChangeMask()) {
            calendarViewHolder.changeImageView.setVisibility(itemView.getVisibility());
        }
    }

    /**
     * 날짜를 클릭했을 때 마스크교체여부가 바뀐다. (DB에도 바로 반영)
     *
     * @param calendarViewHolder
     * @param item
     */
    private void onDayClick(CalendarViewHolder calendarViewHolder, CalendarItem item) {

        View itemView = calendarViewHolder.getItemView();

        if (item.isChangeMask()) {
            item.setChangeMask(false);
            replacementHistoryRepository.delete(DateUtils.getDateFromGregorianCalendar(item.getDate()));
            calendarViewHolder.changeImageView.setVisibility(itemView.GONE);
        } else {
            item.setChangeMask(true);
            replacementHistoryRepository.insert(DateUtils.getDateFromGregorianCalendar(item.getDate()), new ReplacementHistoryRepository.InsertHistoryCallback() {
                @Override
                public void onSuccess() {
                    calendarViewHolder.changeImageView.setVisibility(itemView.getVisibility());
                }
                @Override
                public void onDuplicated() {
                    Log.d(TAG, "onDuplicated: true");
                }
            });
        }
    }

    /**
     * item 몇 개 생성되는지
     * 제대로 생성되지 않으면 오류가 난다고 합니다.
     *
     * @return item 개수 반환
     */
    @Override
    public int getItemCount() {
        if (calendarList != null) {
            return calendarList.size();
        }
        return 0;
    }

    /**
     * calendar view holder
     */
    public class CalendarViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private ImageView dateBackgroundImageView;
        private ImageView changeImageView;
        private View itemView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;

            initView(itemView);
        }

        public void initView(View v) {
            dateTextView = v.findViewById(id.textView_date);
            dateBackgroundImageView = v.findViewById(id.imageView_date_background);
            changeImageView = v.findViewById(id.imageView_change);
        }

        public View getItemView() {
            return itemView;
        }
    }
}
