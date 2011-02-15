package net.charapla.android.horoclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class HoroClockProvider extends AppWidgetProvider {
	private static final String TAG = "[HoroClockProvider]";
	private static final String PACKAGE_NAME = "net.charapla.android.horoclock";

	private static final String ACTION_ALARM = PACKAGE_NAME + ".HoroClockWidget.ACTION_ALARM";
	private static final String APP_WIDGET_ID_COUNT = "count";
	private static final long INTERVAL = 1000 * 60;
	private static boolean mIsUpdate = true;

	@Override
	public void onEnabled(Context context) {
		Log.d(TAG, "onEnabled");
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onRecieve");
		String action = intent.getAction();
		Log.d(TAG, "Action=[" + action + "]");
		if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)) {
			PreferencesUtil.setPreferences(context, "char", "init");
			// AppWidget数をインクリメント
			PreferencesUtil.countUpPreferences(context, "count", 0, 1);
		}
		if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action) ||
			Intent.ACTION_SCREEN_ON.equals(action) ||
			Intent.ACTION_SCREEN_OFF.equals(action) ||
			ACTION_ALARM.equals(action) ) {
			updateClock(context, intent.getAction(), intent.getData());
		}
		super.onReceive(context, intent);
	}

	/**
	 * 時計更新
	 */
	public static void updateClock(Context context, String action, Uri uri) {
		Log.d(TAG, "updateClock");
		if (Intent.ACTION_SCREEN_OFF.equals(action)) {
			mIsUpdate = false;
			return;
		} else if (Intent.ACTION_SCREEN_ON.equals(action)) {
			mIsUpdate = true;
		} else if (!mIsUpdate) {
			return;
		}

		// Manager情報取得
		AppWidgetManager manager = AppWidgetManager.getInstance(context);

		// 初回起動時にチェック
		int appWidgetId = 0;
		Log.d(TAG, "Uri=[" + uri + "]");
		if (uri != null) {
			appWidgetId = (int) ContentUris.parseId(uri);
			Log.d(TAG, "AppWidgetId=[" + appWidgetId + "]");
			if (manager.getAppWidgetInfo(appWidgetId) == null) {
				// AppWidgetIdの配置数を減算設定
				PreferencesUtil.countUpPreferences(context, APP_WIDGET_ID_COUNT, 0, -1);
				return;
			}
		}

		// 更新対象のAppWidgetを取得(AppWidgetを初めて設置した時 or 二回目以降の初回処理でない場合)
		int count = PreferencesUtil.getPreferences(context, APP_WIDGET_ID_COUNT, 0);
		Log.d(TAG, "Count=[" + count + "]");
		if (count == 1 || (count > 1 && appWidgetId == 0)) {
			setAlarm(context);
		}

		// 時計の更新はServiceで行う
		Intent intent = new Intent(context, HoroClockService.class);
		intent.setAction(action);
		intent.putExtra("count", count);
		context.startService(intent);
	}

	/**
	 * アラームは1分後に設定
	 */
	private static void setAlarm(Context context) {
		Intent intent = new Intent(context, HoroClockProvider.class);
		intent.setAction(ACTION_ALARM);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		long now = System.currentTimeMillis() + 1;
		long oneMinutesAfter = now + INTERVAL - now % (INTERVAL);
		alarmManager.set(AlarmManager.RTC_WAKEUP, oneMinutesAfter, pendingIntent);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);

		// AppWidgetIdの配置数を減算設定
		PreferencesUtil.countUpPreferences(context, APP_WIDGET_ID_COUNT, 0, -1);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);

		// 一応削除しておく
		PreferencesUtil.removePreferences(context, "char");
	}

}
