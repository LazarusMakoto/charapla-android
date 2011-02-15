package net.charapla.android.horoclock;

import net.charapla.android.horoclock.PreferencesUtil;
import android.app.Activity;
import android.os.Bundle;

public class HoroClockActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		PreferencesUtil.setPreferences(getApplicationContext(), "char", "init");
		// AppWidget数をインクリメント
		PreferencesUtil.countUpPreferences(getApplicationContext(), "count", 0, 1);
	}
}