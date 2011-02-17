package net.charapla.android.horoclock;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RemoteViews;

public class HoroClockService extends Service {
	private static final String TAG = "[HoroClockService]";
	private static final String PACKAGE_NAME = "net.charapla.android.horoclock";
	private static final int CLOCK_SIZE = 294;
	private static final int ICON_SIZE  = 19;
	private static final int NINE_POS   = -68;

	private static HoroClockProvider mReceiver;
	private static LocationManager mLocationManager;
	private static LocationListener mLocationListener;

	private static double mLatitude  = 0;
	private static double mLongitude = 0;

	private static int mAvailableWidth  = 0;
	private static int mAvailableHeight = 0;

	private static Time mCalendar;

	private static Drawable mHourHand;
	private static Drawable mMinuteHand;
	private static Drawable mDial;
	private static Drawable mPlanet[];
//	※カルディアンオーダー
//	土星	木星	火星	太陽	金星	水星	月
	private static int planet_res[][] = {	{6, R.drawable.saturn},	{5, R.drawable.jupiter},
											{4, R.drawable.mars},	{0, R.drawable.sun},
											{3, R.drawable.venus},	{2, R.drawable.mercury},
											{1, R.drawable.moon} };
//	private static int planet_res[][] = {	{1, R.drawable.moon},	{2, R.drawable.mercury},
//											{3, R.drawable.venus},	{0, R.drawable.sun},
//											{4, R.drawable.mars},	{5, R.drawable.jupiter},
//											{6, R.drawable.saturn} };

	private static float mHour;
	private static float mMinutes;
	private static boolean mChanged;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		String action = intent.getAction();
		Log.d(TAG, "{onStart} Action=[" + action + "]");
		Log.d(TAG, "{onStart} Count=[" + intent.getIntExtra("count", 0) + "]");
		if (intent.getIntExtra("count", 0) == 0) {
			unregistIntentFilter(getApplicationContext());
			mCalendar = null;
			stopSelf();
			return;
		} else {
			registIntentFilter(getApplicationContext(), action);
		}

		initialize(action);

		// 共通情報取得
		AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
		ComponentName thisWidget = new ComponentName(PACKAGE_NAME, PACKAGE_NAME + ".HoroClockProvider");
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.appwidget_layout);
		remoteViews.setImageViewBitmap(R.id.ImageView01, getClockBitmap(getApplicationContext()));

		manager.updateAppWidget(thisWidget, remoteViews);

		// 位置情報取得
		//initializeLocation();
	}

	// 初期読み込み
	private void initialize(String action) {
		if (mCalendar == null || AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
			String num = PreferencesUtil.getPreferences(getApplicationContext(), "char", "");
			if ("".equals(num)) {
				return;
			}
		}

		Context context = getApplicationContext();

		if (mDial == null)
			mDial = context.getResources().getDrawable(R.drawable.clock_bg);

		if (mHourHand == null)
			mHourHand = context.getResources().getDrawable(R.drawable.short_hand);

		if (mMinuteHand == null)
			mMinuteHand = context.getResources().getDrawable(R.drawable.long_hand);

		if (mCalendar == null)
			mCalendar = new Time();

		if (mAvailableWidth == 0 || mAvailableHeight == 0) {
			Display disp = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			mAvailableWidth  = disp.getWidth();
			mAvailableHeight = disp.getHeight();
			Log.d(TAG, "{Initialize}(0) AvailabelWidth=" + mAvailableWidth + " AvailableHeight=" + mAvailableHeight);
			Log.d(TAG, "{Initialize}(0) IntrinsicWidth=" + mDial.getIntrinsicWidth() + " IntrinsicHeight=" + mDial.getIntrinsicHeight());
			if (mAvailableWidth >= mDial.getIntrinsicWidth())
				mAvailableWidth  = CLOCK_SIZE;
				//mAvailableWidth  = mDial.getIntrinsicWidth();
			if (mAvailableHeight >= mDial.getIntrinsicHeight())
				mAvailableHeight = CLOCK_SIZE;
				//mAvailableHeight = mDial.getIntrinsicHeight();
			Log.d(TAG, "{Initialize}(1) AvailabelWidth=" + mAvailableWidth + " AvailableHeight=" + mAvailableHeight);
		}

		if (mPlanet == null) {
			mPlanet = new Drawable[planet_res.length];
			for (int i=0; i<mPlanet.length; i++) {
				mPlanet[i] = context.getResources().getDrawable(planet_res[i][1]);
			}
		}
	}


	private Bitmap getClockBitmap(Context context) {
		onTimeChanged();
		Bitmap rtn = Bitmap.createBitmap(mAvailableWidth, mAvailableHeight, Bitmap.Config.ARGB_8888);
		onDraw(new Canvas(rtn));
		return rtn;
	}

	private void onTimeChanged() {
		if (mCalendar == null)
			return;

		mCalendar.setToNow();

		int hour   = mCalendar.hour;
		int minute = mCalendar.minute;
		int second = mCalendar.second;

		mMinutes = minute + second / 60.0f;
		mHour = hour + mMinutes / 60.0f;
		mChanged = true;
	}

	private void onDraw(Canvas canvas) {

		boolean changed = mChanged;
		if (changed) {
			mChanged = false;
		}

		int x = mAvailableWidth / 2;
		int y = mAvailableHeight / 2;
		Log.d(TAG, "{onDraw} x=" + x + " y=" + y);

		final Drawable dial = mDial;
		//int w = dial.getIntrinsicWidth();
		//int h = dial.getIntrinsicHeight();
		int w = CLOCK_SIZE;
		int h = CLOCK_SIZE;
		Log.d(TAG, "{onDraw} w=" + w + " h=" + h);

		boolean scaled = false;

		if (mAvailableWidth < w || mAvailableHeight < h) {
			Log.d(TAG, "{onDraw} Scaling...");
			scaled = true;
			float scale = Math.min( (float) mAvailableWidth  / (float) w,
									(float) mAvailableHeight / (float) h);
			canvas.save();
			canvas.scale(scale, scale, x, y);
		}

		if (changed) {
			Log.d(TAG, "{onDraw} Changed...");
			dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
		}
		dial.draw(canvas);

		canvas.save();
		canvas.rotate(mHour / 12.0f * 360.0f, x, y);
		final Drawable hourHand = mHourHand;
		if (changed) {
			//w = hourHand.getIntrinsicWidth();
			//h = hourHand.getIntrinsicHeight();
			w = ICON_SIZE;
			h = CLOCK_SIZE;
			hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
		}
		hourHand.draw(canvas);
		canvas.restore();

		canvas.save();
		canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
		final Drawable minuteHand = mMinuteHand;
		if (changed) {
			//w = minuteHand.getIntrinsicWidth();
			//h = minuteHand.getIntrinsicHeight();
			w = ICON_SIZE;
			h = CLOCK_SIZE;
			minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
		}
		minuteHand.draw(canvas);
		canvas.restore();

		// draw planet sign
		onHoroDraw(canvas, changed);
		//testDraw(canvas);

		if (scaled) {
			canvas.restore();
		}
	}

	private void onHoroDraw(Canvas canvas, boolean changed) {
//		※カルディアンオーダー
//		土星	木星	火星	太陽	金星	水星	月
		PlanetCalc planetCalc = new PlanetCalc();
		double planet_pos[][] = null;
		//planetCalc.planet_pos(Calendar.getInstance(), PlanetCalc.DEFAULT_PLACE);
		Log.d(TAG, "{onHoloDraw} Lat=[" + mLatitude + "] Lon=["+ mLongitude + "]");
		if (mLatitude == 0 && mLongitude == 0) {
			planet_pos = planetCalc.planet_pos(Calendar.getInstance(), PlanetCalc.DEFAULT_PLACE);
		} else {
			planet_pos = planetCalc.planet_pos(Calendar.getInstance(), mLatitude, mLongitude);
		}

		int x = mAvailableWidth  / 2;
		int y = mAvailableHeight / 2;
		double radian = Math.PI / 180;

		ArrayList<Float> draw_pos = new ArrayList<Float>();
		final Drawable planet[] = new Drawable[mPlanet.length];
		for (int i=0; i<planet.length; i++) {
			canvas.save();
			int pla_no = planet_res[i][0];
			// 惑星の位置は、時計の９時から逆行
			float rot = -((float)planet_pos[pla_no][0] * 30 + (float)planet_pos[pla_no][1]);
			int shift = chaldaeanPos(draw_pos, rot);	// 描画位置が重なる場合シフトする
			draw_pos.add(rot);	// 描画位置の保存
			// canvas.rotate(rot, x -shift, y);
			planet[i] = mPlanet[i];
			if (changed) {
				//int w = planet[i].getIntrinsicWidth();
				//int h = planet[i].getIntrinsicHeight();
				int w = ICON_SIZE;
				int h = ICON_SIZE;
				int x0 = NINE_POS + shift;	// 9時の位置から
				int y0 = 0;
				//Log.d(TAG, "{onHoloDraw} i=[" + pla_no + "] w="+ w + " h=" + h);
				// setBounds(left, top, right, bottom)
				// planet[i].setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));

				// 回転移動の１次変換 http://www.geisya.or.jp/~mwm48961/kou2/linear_image3.html
				//Log.d(TAG, "{onHoloDraw} i=[" + pla_no + "] ROT="+ rot + " SI=" + planet_pos[pla_no][0] + " MM.DD=" + planet_pos[pla_no][1]);
				int _x = (int)Math.round(x0 * Math.cos(rot * radian) - y0 * Math.sin(rot * radian));
				int _y = (int)Math.round(x0 * Math.sin(rot * radian) + y0 * Math.cos(rot * radian));
				//Log.d(TAG, "{onHoloDraw} i=[" + pla_no + "] X="+ _x + " Y=" + _y);
				planet[i].setBounds(_x + x - (w / 2), _y + y - (h / 2), _x + x + (w / 2), _y + y + (h / 2));
				//Log.d(TAG, "{onHoloDraw} i=[" + pla_no + "] Left="+ (_x + x - (w / 2)) + " Top=" + (_y + y - (h / 2)));
			}
			planet[i].draw(canvas);
			canvas.restore();
		}
	}

	@SuppressWarnings("unused")
	private void testDraw(Canvas canvas) {
		int x = mAvailableWidth  / 2;
		int y = mAvailableHeight / 2;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(11);
		canvas.save();
		canvas.drawText("●", x, y, paint);
		canvas.restore();
		double radian = Math.PI / 180;
		double x0 = NINE_POS;	// 9時の位置から
		double y0 = 0;
		for (int i=0; i<=360; i+=30) {
			canvas.save();
			int _x = (int)(x0 * Math.cos((i * radian)) - y0 * Math.sin((i * radian)));
			int _y = (int)(x0 * Math.sin((i * radian)) + y0 * Math.cos((i * radian)));
			int g = i <= 180 ? i : 0;
			int b = i >  180 ? i : 0;
			paint.setColor(Color.argb(255, 255, g, b));
			Log.d(TAG, "{testDraw} R=[" + i + "] Left=["+ (_x + x) + "] Top=[" + (_y + y) + "]");
			canvas.drawText(String.valueOf(i), x + _x, y + _y, paint);
			canvas.restore();
		}
	}

	private final static float OVERLAP = 12;
	private int chaldaeanPos(ArrayList<Float> draw_pos, float rot) {
		int pos = 0;
		for (float f : draw_pos) {
			if (rot > f -OVERLAP && rot < f +OVERLAP) {
				pos += OVERLAP;
			}
		}
		return pos;
	}


	public static void registIntentFilter(Context context, String action) {
		if (mReceiver == null || action == null) {
			unregistIntentFilter(context);

			mReceiver = new HoroClockProvider();
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SCREEN_ON);
			filter.addAction(Intent.ACTION_SCREEN_OFF);
			context.registerReceiver(mReceiver, filter);
		}
	}

	private static void unregistIntentFilter(Context context) {
		if (mReceiver != null) {
			context.unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	@SuppressWarnings("unused")
	private void initializeLocation() {
		//位置情報サービスマネージャを取得
		mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		if (mLocationManager == null)
			return;

		final Criteria criteria = new Criteria();
		criteria.setBearingRequired(false);		// 方位不要
		criteria.setSpeedRequired(false);		// 速度不要
		criteria.setAltitudeRequired(false);	// 高度不要
		//使える中で最も条件にヒットする位置情報サービスを取得する
		final String bestProvider = mLocationManager.getBestProvider(criteria, true);
		//以前に取得した位置情報を取得
		final Location location = mLocationManager.getLastKnownLocation(bestProvider);
		//位置情報の取得
		setLocation(location);
		mLocationListener = new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {	}
			@Override
			public void onProviderEnabled(String provider) {	}
			@Override
			public void onProviderDisabled(String provider) {	}
			@Override
			public void onLocationChanged(Location location) {
				setLocation(location);
			}
		};
		//位置更新の際のリスナーを登録 (最少 1時間 1km の変化)
		mLocationManager.requestLocationUpdates(bestProvider, 1000, 1, mLocationListener);
	}

	private void setLocation(Location location) {
		if (location != null) {
			mLatitude  = location.getLatitude();
			mLongitude = location.getLongitude();
		}
	}
}
