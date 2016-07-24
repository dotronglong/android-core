package com.globalmediasoft.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

public class GMSWidgetListView extends ListView {
	protected onOverScrollListener mOverScrollListener;
	
	public interface onOverScrollListener {
		public void onScrollOverFooter();
		public void onScrollOverHeader();
	}

	public GMSWidgetListView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	public GMSWidgetListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	
	public GMSWidgetListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				//Log.d("DEBUG", String.valueOf(getY()));
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	public int getVerticalScrollOffset () {
    	return super.computeVerticalScrollOffset();
    }
    
    public int getVerticalScrollExtent() {
    	return super.computeVerticalScrollExtent();
    }
    
    public int getVerticalScrollRange() {
    	return super.computeVerticalScrollRange();
    }

	public void setOnOverScrollListener(onOverScrollListener l) {
		mOverScrollListener = l;
	}
	
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		// TODO Auto-generated method stub
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

		int verticalScrollExtent = getVerticalScrollExtent();
		int verticalScrollOffset = getVerticalScrollOffset();
		int verticalScrollRange  = getVerticalScrollRange();
		
		if (verticalScrollExtent + verticalScrollOffset >= verticalScrollRange) {
			if (mOverScrollListener != null) {
				mOverScrollListener.onScrollOverFooter();
			}
		} else if (verticalScrollOffset == 0) {
			if (mOverScrollListener != null) {
				mOverScrollListener.onScrollOverHeader();
			}
		}
	}
}
