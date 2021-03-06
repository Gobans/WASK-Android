package com.naccoro.wask.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreference를 데이터 타입에 따라 관리하는 매니저
 * 싱글톤으로 구현
 */
public class SharedPreferenceManager {
    private final String SHARED_PREFERENCES_FILE_NAME = "wask_preference"; // 파일명

    private volatile static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    /**
     * getInstance()를 통해서만 객체가 생성되도록 private 설정
     */
    private SharedPreferenceManager() {
    }

    private static class LazyHolder {
        private static final SharedPreferenceManager instance = new SharedPreferenceManager();
    }

    /**
     * instance, editor 초기화
     *
     * @param context
     */
    public void initInstance(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * LazyHolder.instance 를 참조하는 순간 Class가 로딩되고 초기화가 진행
     *
     * @return SharedPreferenceManager 객체 (온리원!)
     */
    public static SharedPreferenceManager getInstance() {
        return LazyHolder.instance;
    }

    /**
     * String 값 저장
     *
     * @param key   value에 대응하는 key 문자열
     * @param value 저장할 String
     */
    public void setString(String key, String value) {
        editor.putString(key, value).apply();
    }

    /**
     * String 값 로드
     *
     * @param key          value에 대응하는 key 문자열
     * @param defaultValue key에 대응하는 value가 없으면 반환될 문자열
     * @return key에 해당하는 String or defaultValue
     */
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * int 값 저장
     *
     * @param key   value에 대응하는 key 문자열
     * @param value 저장할 int
     */
    public void setInt(String key, int value) {
        editor.putInt(key, value).apply();
    }

    /**
     * int 값 로드
     *
     * @param key          value에 대응하는 key 문자열
     * @param defaultValue key에 대응하는 값이 없을 경우 반환될 값
     * @return key에 해당하는 int or default value
     */
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * boolean 값 저장
     *
     * @param key   value에 대응하는 key 문자열
     * @param value 저장할 boolean
     */
    public void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    /**
     * boolean 값 로드
     *
     * @param key          value에 대응하는 key 문자열
     * @param defaultValue key에 대응하는 value가 없을경우 반환될 값
     * @return key에 해당하는 boolean or defaultValue
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 모든 저장 데이터 삭제
     *
     * @return 성공여부
     */
    public boolean clear() {
        return editor.clear().commit();
    }


}
