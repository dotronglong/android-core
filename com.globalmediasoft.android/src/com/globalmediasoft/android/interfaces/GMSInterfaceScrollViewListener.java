package com.globalmediasoft.android.interfaces;

import com.globalmediasoft.android.widget.GMSWidgetScrollView;

public interface GMSInterfaceScrollViewListener {
	void onScrollChanged(GMSWidgetScrollView scrollView, int x, int y, int oldx, int oldy);
	void onScrollStopped();
}