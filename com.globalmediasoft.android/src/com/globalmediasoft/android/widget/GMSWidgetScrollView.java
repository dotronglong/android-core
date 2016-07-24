package com.globalmediasoft.android.widget;

import com.globalmediasoft.android.interfaces.GMSInterfaceScrollViewListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class GMSWidgetScrollView extends ScrollView {
	private View thumbView;
	private GMSInterfaceScrollViewListener scrollViewListener = null;
	private int scrollRange = 0;
	private int thumbRange = 0;
	private int thumbHeight = 0;
	private int fadeDelay = 500;
	
	private long scrollerTaskDelay = 500;
	private int lastCallScrollPositionY;
	private int lastScrollPositionY;
	private boolean onGlobalLayoutProcessed = false;
	public static int SCROLL_VERTICAL   = 1;
	public static int SCROLL_HORIZONTAL = 2;
	
	private Runnable scrollerTask = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			int currentSCrollPositionY = getScrollY();
			if (lastScrollPositionY == currentSCrollPositionY && lastScrollPositionY != lastCallScrollPositionY) {
				if (scrollViewListener != null) {
					scrollViewListener.onScrollStopped();
				}

				onScrollStopped();
				lastCallScrollPositionY = lastScrollPositionY;
			} else {
				lastScrollPositionY = currentSCrollPositionY;
			}
		}
	};

    public GMSWidgetScrollView(Context context) {
        super(context);
    }

    public GMSWidgetScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GMSWidgetScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    protected void onAttachedToWindow() {
    	// TODO Auto-generated method stub
    	super.onAttachedToWindow();
    	
    	final LinearLayout layout = (LinearLayout) this.getChildAt(0);
    	final ViewTreeObserver viewTreeObserver = layout.getViewTreeObserver();
    	viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				if (onGlobalLayoutProcessed) {
					return;
				}
				onGlobalLayoutProcessed = true;
				if (scrollRange == 0) {
					scrollRange = getVerticalScrollRange() - getVerticalScrollExtent();
				}
				thumbRange = getHeight();
				if (thumbView != null && thumbRange > 0) {
					float percentInView = (float) scrollRange / thumbRange;
					thumbHeight = (int) Math.ceil(1 / percentInView * thumbRange);
					ViewGroup.LayoutParams params = thumbView.getLayoutParams();
					params.height = thumbHeight;
				}
			}
		});
    }

    public void setScrollViewListener(GMSInterfaceScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
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
    
    public GMSWidgetScrollView setThumbRange(int thumbRange) {
    	this.thumbRange = thumbRange;
    	return this;
    }
	
    public GMSWidgetScrollView setThumbView(View v) {
    	thumbView = v;
    	return this;
    }
    
    private void setThumbY(int y) {
    	if (y < 0) y = 0;
    	if (thumbView != null && thumbRange > 0) {
    		ViewGroup.MarginLayoutParams params = (MarginLayoutParams) thumbView.getLayoutParams();
    		params.setMargins(params.leftMargin, y, params.rightMargin, params.bottomMargin);
    		thumbView.setLayoutParams(params);
		}
    }

	@Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        } else {
        	setThumbY((int) (((float) y / scrollRange) * (thumbRange - thumbHeight)));
        }
        
        if (thumbView.getVisibility() == View.GONE) {
        	thumbView.setVisibility(View.VISIBLE);
        }
        postDelayed(scrollerTask, scrollerTaskDelay);
    }
	
	public void setFadeDelay(int miliseconds) {
		this.fadeDelay = miliseconds;
	}
	
	public void onScrollStopped() {
		if (fadeDelay > 0) {
			postDelayed(new Runnable() {
				
				@Override
				public void run() {
					if (thumbView.getVisibility() == View.VISIBLE) {
						thumbView.setVisibility(View.GONE);
					}
				}
			}, fadeDelay);
		}
	}
}
