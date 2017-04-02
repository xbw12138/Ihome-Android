package cn.ilell.ihome.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xubowen on 16/10/2.
 */

public class SharedPreference {

	private static final String SHAREDPREFERENCES_NAME = "KEEP_AP";
	private static final String KEEP_AP1= "AP1";
	private static final String KEEP_BAP1= "BAP1";
	private static final String ISKEEP_AP1= "ISAP1";
	private static final String KEEP_AP2 = "AP2";
	private static final String KEEP_BAP2= "BAP2";
	private static final String ISKEEP_AP2= "ISAP2";

	private static final String HN1="HN1";
	private static final String HP1="HP1";
	private static final String HN2="HN2";
	private static final String HP2="HP2";
	private static final String HN3="HN3";
	private static final String HP3="HP3";
	private static final String HN4="HN4";
	private static final String HP4="HP4";
	private static final String HN5="HN5";
	private static final String HP5="HP5";
	private static final String HN6="HN6";
	private static final String HP6="HP6";
	private static final String HN7="HN7";
	private static final String HP7="HP7";
	private Context context;

	public SharedPreference(Context context) {
		this.context = context;
	}

	public void KeepAP1(String SSID,String BSSID) {
		SharedPreferences settings = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(KEEP_AP1, SSID);
		editor.putString(KEEP_BAP1, BSSID);
		//editor.putString(ISKEEP_AP1, "1");
		editor.commit();
	}
	public void KeepAP2(String SSID,String BSSID) {
		SharedPreferences settings = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(KEEP_AP2, SSID);
		editor.putString(KEEP_BAP2, BSSID);
		//editor.putString(ISKEEP_AP2, "2");
		editor.commit();
	}
	public void KeepFinish(){
		SharedPreferences settings = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(ISKEEP_AP1, "1");
		editor.putString(ISKEEP_AP2, "2");
		editor.commit();
	}
	public void reKeep(){
		SharedPreferences settings = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(ISKEEP_AP1, "0");
		editor.putString(ISKEEP_AP2, "0");
		editor.commit();
	}
	public boolean isKeep(String className) {
		if (context == null || className == null
				|| "".equalsIgnoreCase(className))
			return false;
		String mResultStr1 = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				ISKEEP_AP1, "");
		String mResultStr2 = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				ISKEEP_AP2, "");
		if (mResultStr1.equals("1")&&mResultStr2.equals("2"))
			return true;
		else
			return false;
	}
	public String getAP1() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				KEEP_AP1, "");
		return mResultStr;
	}
	public String getBAP1() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				KEEP_BAP1, "");
		return mResultStr;
	}
	public String getAP2() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				KEEP_AP2, "");
		return mResultStr;
	}
	public String getBAP2() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				KEEP_BAP2, "");
		return mResultStr;
	}
	public void HOME1(String HN,String HP) {
		SharedPreferences settings = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(HN1, HN);
		editor.putString(HP1, HP);
		editor.commit();
	}
	public void HOME2(String HN,String HP) {
		SharedPreferences settings = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(HN2, HN);
		editor.putString(HP2, HP);
		editor.commit();
	}
	public void HOME3(String HN,String HP) {
		SharedPreferences settings = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(HN3, HN);
		editor.putString(HP3, HP);
		editor.commit();
	}
	public void HOME4(String HN,String HP) {
		SharedPreferences settings = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(HN4, HN);
		editor.putString(HP4, HP);
		editor.commit();
	}
	public void HOME5(String HN,String HP) {
		SharedPreferences settings = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(HN5, HN);
		editor.putString(HP5, HP);
		editor.commit();
	}
	public void HOME6(String HN,String HP) {
		SharedPreferences settings = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(HN6, HN);
		editor.putString(HP6, HP);
		editor.commit();
	}
	public void HOME7(String HN,String HP) {
		SharedPreferences settings = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(HN7, HN);
		editor.putString(HP7, HP);
		editor.commit();
	}

	//1111111111111111
	public String getHN1() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				HN1, "");
		return mResultStr;
	}
	public String getHP1() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				HP1, "");
		return mResultStr;
	}
	//2222222222222222
	public String getHN2() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				HN2, "");
		return mResultStr;
	}
	public String getHP2() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				HP2, "");
		return mResultStr;
	}
	//3333333333333333333
	public String getHN3() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				HN3, "");
		return mResultStr;
	}
	public String getHP3() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				HP3, "");
		return mResultStr;
	}
	//4444444444444444
	public String getHN4() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				HN4, "");
		return mResultStr;
	}
	public String getHP4() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				HP4, "");
		return mResultStr;
	}
	//555555555555555
	public String getHN5() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				HN5, "");
		return mResultStr;
	}
	public String getHP5() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				HP5, "");
		return mResultStr;
	}
	//6666666666
	public String getHN6() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				HN6, "");
		return mResultStr;
	}
	public String getHP6() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				HP6, "");
		return mResultStr;
	}
	//77777777777
	public String getHN7() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				HN7, "");
		return mResultStr;
	}
	public String getHP7() {
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				HP7, "");
		return mResultStr;
	}

}
