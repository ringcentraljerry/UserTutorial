package com.ringcentral.usertutorial;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ringcentral.usertutorial.activity.TutorialActivityInterface;

import java.lang.reflect.Field;


/**
 * @author Jerry Cai
 */
public class TipView extends ViewGroup implements TipViewInterface, View.OnTouchListener {
    private static final String FIELD_BITMAP = "mBitmap";
    private static final long ANIMATION_DURATION = 400;
    private static final int LINE_DEVIATION = 1;
    private Activity mTargetAct = null;
    private TargetView mTargetView = null;
    private View mTipView;
    private boolean mIsShowing = false;
    private boolean mIsHoverTarget = true;
    private boolean mIsTutorialTip = true;
    private boolean mIsShowArrow = true;
    private Paint mHotSpotPaint = getHotSpotPaint();
    private Path mClipPath = null;
    private int mBackgroundColor = Color.TRANSPARENT;
    private Rect mHotArea, mOldHotArea;
    private Bitmap mHotBitmap = null;
    private BitmapDrawable mTipArrowDownDrawable = null;
    private BitmapDrawable mTipArrowUpDrawable = null;
    private Bitmap mTipViewBitmap = null;
    private int mTipHorizontalMargin = 0;
    private int mTipHotPadding = 0;
    private boolean mIsHotZoomIn = false;
    private int mTipCentralX, mTipCentralY;

    private UpdateOnGlobalLayout mUpdateOnGlobalLayout;


    private TipViewEventTrace mTipViewEventTrace = null;

    private AnimationInterface mAnimation = null;

    private static final int ANIMATION_MAX_OFFSET_DEFAULT = 10;
    private static final int ANIMATION_PER_OFFSET = 1;
    private static final int ANIMATION_TIME = 60;
    private static final int ANIMATION_TIME_HARDWARE = 100;
    private Handler mHandler = null;
    private AnimationCallback mAnimationCallback = null;
    private int mAnimationOffset = 0;
    private int mAnimationTimer = ANIMATION_TIME;
    private int mAnimationMaxOffset = ANIMATION_MAX_OFFSET_DEFAULT;
    private boolean mAnimationUp = false;

    private WindowsFocusChange mFocusListener = new WindowsFocusChange();
    private boolean mPreWindowFocusStatus = true;
    private TipDismissListener mTipDismissListener;

    public interface TipDismissListener {
        void onTipDismiss();
    }

    private class AnimationCallback implements Runnable {
        @Override
        public void run() {

            if (mAnimationUp) {
                if (mAnimationOffset < mAnimationMaxOffset) {
                    mAnimationOffset += ANIMATION_PER_OFFSET;
                }

                if (mAnimationOffset >= mAnimationMaxOffset) {
                    mAnimationOffset = mAnimationMaxOffset;
                    mAnimationUp = false;
                }
            } else {
                if (mAnimationOffset > (-mAnimationMaxOffset)) {
                    mAnimationOffset -= ANIMATION_PER_OFFSET;
                }

                if (mAnimationOffset <= (-mAnimationMaxOffset)) {
                    mAnimationOffset = (-mAnimationMaxOffset);
                    mAnimationUp = true;
                }
            }
            TipView.this.requestLayout();
            TipView.this.invalidate();

            mHandler.postDelayed(this, mAnimationTimer);

        }
    }

    public TipView(Activity targetAct, Fragment targetFragment, Tip tip) {
        super(targetAct);
        init(targetAct, targetFragment, tip);
    }

    public TipView setMaskColor(int color) {
        this.mBackgroundColor = color;
        return this;
    }

    public TipView setHoverTarget(boolean hoverTarget) {
        this.mIsHoverTarget = hoverTarget;
        return this;
    }

    public TipView setTutorialTip(boolean isTutorialTip) {
        this.mIsTutorialTip = isTutorialTip;
        return this;
    }

    public TipView setShowArrow(boolean showArrow) {
        mIsShowArrow = showArrow;
        return this;
    }

    public TipView setTipDismissListener(TipDismissListener listener) {
        mTipDismissListener = listener;
        return this;
    }

    @Override
    public void remove() {
        mIsShowing = false;
        mHandler.removeCallbacks(mAnimationCallback);
        mHandler.removeCallbacks(mFocusListener);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(mUpdateOnGlobalLayout);
        }else {
            getViewTreeObserver().removeGlobalOnLayoutListener(mUpdateOnGlobalLayout);
        }

        if (mTargetAct instanceof TutorialActivityInterface) {
            TutorialActivityInterface ta = (TutorialActivityInterface) mTargetAct;
            ta.getViewRootForTip().removeView(this);
        }

        if (mHotBitmap != null && !mHotBitmap.isRecycled()) {
            mHotBitmap.recycle();
            mHotBitmap = null;
        }

        if (mTipViewBitmap != null && !mTipViewBitmap.isRecycled()) {
            mTipViewBitmap.recycle();
            mTipViewBitmap = null;
        }

        if(mTipDismissListener != null) {
            mTipDismissListener.onTipDismiss();
        }
    }

    @Override
    public boolean isShowing() {
        return mIsShowing;
    }

    @Override
    public void show() {
        if (mIsShowing) {
            return;
        }

        mIsShowing = true;

        if (isSupportAnimation()) {
            mAnimation.fadeIn(this, ANIMATION_DURATION, new AnimationInterface.AnimationStartListener() {
                @Override
                public void onAnimationStart() {
                    if (mIsShowing) {
                        //basically the animation should start the view is visible.
                        mHandler.removeCallbacks(mAnimationCallback);
                        mHandler.postDelayed(mAnimationCallback, ANIMATION_TIME);
                        setVisibility(View.VISIBLE);
                        if (mTipViewEventTrace != null) {
                            mTipViewEventTrace.onEventShow();
                        }
                    }

                }
            });

        } else {
            if (mIsShowing) {
                setVisibility(View.VISIBLE);
                if (mTipViewEventTrace != null) {
                    mTipViewEventTrace.onEventShow();
                }
            }
        }
    }

    protected boolean isSupportAnimation(){
        return true;
    }

    @Override
    public void hide() {
        if(!mIsShowing) {
            return;
        }

        mIsShowing = false;
        this.setVisibility(View.GONE);

        if (mTipViewEventTrace != null) {
            mTipViewEventTrace.onEventHide();
        }
    }

    public void setTipViewEventTrace(TipViewEventTrace listener) {
        mTipViewEventTrace = listener;
    }

    private void init(Activity targetAct, Fragment targetFragment, Tip tip) {
        mTargetAct = targetAct;
        mHotArea = new Rect();
        mOldHotArea = new Rect();
        mAnimation = new AnimationImpl();

        final Resources resources = targetAct.getResources();

        mTipArrowDownDrawable = (BitmapDrawable) resources.getDrawable(R.drawable.tip_arrow_down);
        mTipArrowUpDrawable = (BitmapDrawable) resources.getDrawable(R.drawable.tip_arrow_up);
        mTipHorizontalMargin = resources.getDimensionPixelSize(R.dimen.tutorial_tip_horizontal_margin);
        mBackgroundColor = resources.getColor(R.color.transparent_mask2);
        if(tip instanceof TipInfo) {
            boolean isNeedToStrFormat = false;
            String contentFormatStr = "";
            int contextTextId = resources.getIdentifier(tip.getText(), "string", targetAct.getPackageName());
            int hotPaddingDimenId = resources.getIdentifier(tip.getHotPadding(), "dimen", targetAct.getPackageName());
            mTipHotPadding = resources.getDimensionPixelSize(hotPaddingDimenId);
            mIsHotZoomIn = tip.isHotZoomIn();

            mTargetView = new TargetView(tip.getViewId(), targetAct, targetFragment, tip.isFragment());

            TextTipView tipView = new TextTipView(targetAct, null);
            if(!isNeedToStrFormat) {
                tipView.setText(contextTextId);
            }else {
                tipView.setText(contentFormatStr);
            }
            tipView.setCloseTipListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTipViewEventTrace != null) {
                        mTipViewEventTrace.onEventTapClose();
                    }
                }
            });

            tipView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTipViewEventTrace != null) {
                        mTipViewEventTrace.onEventTapTip();
                    }
                }
            });

            mTipView = tipView;

        } else if(tip instanceof PopupItem) {
            PopupItem popupItem = (PopupItem)tip;
            mTargetView = new TargetView(popupItem.getTargetView());
            mTipView = new CustomContentView(targetAct, popupItem.getContentLayout());
            updatePosition();
        }

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.addView(mTipView, layoutParams);

        setOnTouchListener(this);

        mUpdateOnGlobalLayout = new UpdateOnGlobalLayout();

        getViewTreeObserver().addOnGlobalLayoutListener(mUpdateOnGlobalLayout);

        //turn OFF hw acc
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        this.setVisibility(View.GONE);
        this.mIsShowing = false;

        mHandler = new Handler();
        mAnimationCallback = new AnimationCallback();
        mAnimationOffset = 0;
        mAnimationMaxOffset = resources.getDimensionPixelSize(R.dimen.tutorial_tip_animation_max_offset);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(!this.mIsTutorialTip) {
            if(isShowing()) {
                this.hide();
                this.remove();
                return true;
            }
        }

        return mIsHoverTarget? !mHotArea.contains((int) motionEvent.getX(), (int) motionEvent.getY()) : true;
    }

    private void calculateTipPosition() {
        int textTipViewHeight = mTipView.getMeasuredHeight();
        int textTipViewWidth = mTipView.getMeasuredWidth();
        final int targetCentralX = mHotArea.centerX();
        final int targetCentralY = mHotArea.centerY();

        int centralX = targetCentralX;
        int centralY = targetCentralY;

        int halfTextTipWidth = textTipViewWidth >> 1;
        int halfTextTipHeight = textTipViewHeight >> 1;

        int offsetLeft = centralX - halfTextTipWidth - mTipHorizontalMargin;


        //calculate centralX
        if (offsetLeft >= 0) {
            int offsetRight = this.getMeasuredWidth() - (centralX + halfTextTipWidth + mTipHorizontalMargin);
            if (offsetRight <= 0) {
                centralX += offsetRight;
                centralX -= mTipHorizontalMargin;
            }
        } else {
            centralX -= offsetLeft;
            centralX += mTipHorizontalMargin;
        }

        //calculate centralY
        if (centralY > (this.getMeasuredHeight() >> 1)) {
            centralY = mHotArea.top - halfTextTipHeight - mTipArrowDownDrawable.getIntrinsicHeight();
        } else {
            centralY = mHotArea.bottom + halfTextTipHeight + mTipArrowUpDrawable.getIntrinsicHeight();
        }

        mTipCentralX = centralX;
        mTipCentralY = centralY;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        calculateTipPosition();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int halfTextTipWidth = mTipView.getMeasuredWidth() >> 1;
        int halfTextTipHeight = mTipView.getMeasuredHeight() >> 1;

        //calculate animation
        int centralY = mTipCentralY + mAnimationOffset;
        mTipView.layout(mTipCentralX - halfTextTipWidth, centralY - halfTextTipHeight, mTipCentralX + halfTextTipWidth, centralY + halfTextTipHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < this.getChildCount(); ++i) {
            View item = this.getChildAt(i);
            this.measureChild(item, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        boolean ac = isHardwareAccelerated(canvas);
        updateBitmap(ac);
        if (!ac) {
            mAnimationTimer = ANIMATION_TIME;
            if (mHotBitmap == null) {
                super.dispatchDraw(canvas);
                return;
            }

            drawBackground(canvas);

            if(mIsHoverTarget) {
                drawHotSpot(canvas);
            }

            if(mIsShowArrow) {
                drawArrow(canvas);
            }

            super.dispatchDraw(canvas);
        } else {
            mAnimationTimer = ANIMATION_TIME_HARDWARE;
            if (mTipViewBitmap == null) {
                super.dispatchDraw(canvas);
                return;
            }

            canvas.save();

            drawBackground(mTipViewBitmap);

            if(mIsHoverTarget) {
                drawHotSpot(mTipViewBitmap);
            }

            if(mIsShowArrow) {
                drawArrow(mTipViewBitmap);
            }

            canvas.drawBitmap(mTipViewBitmap, this.getLeft(), this.getTop(), null);

            canvas.restore();

            super.dispatchDraw(canvas);
        }
    }

    private boolean isHardwareAccelerated(Canvas canvas) {
        boolean result = true;//canvas.isHardwareAccelerated();
        try {
            Class<?> obj = canvas.getClass();
            Class<?> superObj = obj.getSuperclass();
            if((superObj != null)
                    && (superObj.getName().equals(Canvas.class.getName()))) {
                obj = superObj;
            }

            Field field = obj.getDeclaredField(FIELD_BITMAP);
            field.setAccessible(true);
            Bitmap bitmap = (Bitmap)field.get(canvas);
            if(bitmap != null) {
                result = false;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Throwable th){
            th.printStackTrace();
        }

        return result;
    }

    private class WindowsFocusChange implements Runnable {
        @Override
        public void run() {
            if (!mTargetView.isVisible()) {
                hide();
            }else {
                if(!mTargetAct.hasWindowFocus()) {
                    hide();
                }else {
                    show();
                }
            }
        }
    }

    private void onLayoutChange(int w, int h) {
        if (w <= 0 || h <= 0) {
            return;
        }

        updatePosition();


        boolean currentFocus = mTargetAct.hasWindowFocus();
        if(currentFocus != mPreWindowFocusStatus) {
            mPreWindowFocusStatus = currentFocus;
            mHandler.removeCallbacks(mFocusListener);
        }

        mHandler.postDelayed(mFocusListener, 0);


        if (mOldHotArea.top != mHotArea.top
                || mOldHotArea.left != mHotArea.left
                || mOldHotArea.bottom != mHotArea.bottom
                || mOldHotArea.right != mHotArea.right) {
            mOldHotArea.set(mHotArea);
            calculateTipPosition();
            requestLayout();
            invalidate();
        }
    }

    private void updatePosition() {
        View targetView = mTargetView.getView();
        int targetViewWidth = targetView.getMeasuredWidth();
        int targetViewHeight = targetView.getMeasuredHeight();
        int[] location = new int[2];
        targetView.getLocationInWindow(location);
        if (mIsHotZoomIn) {
            mHotArea.set(location[0] - mTipHotPadding, location[1] - mTipHotPadding, location[0] + targetViewWidth + mTipHotPadding, location[1] + targetViewHeight + mTipHotPadding);
        } else {
            mHotArea.set(location[0] + mTipHotPadding, location[1] + mTipHotPadding, location[0] + targetViewWidth - mTipHotPadding, location[1] + targetViewHeight - mTipHotPadding);
        }
    }

    private void updateBitmap(boolean isHwAcSupport) {
        if (!isHwAcSupport) {
            int width = mHotArea.width();
            int height = mHotArea.height();
            if (width == 0 || height == 0) {
                return;
            }

            if (mTipViewBitmap != null) {
                mTipViewBitmap.recycle();
                mTipViewBitmap = null;
            }

            if (mHotBitmap != null) {
                if (mHotBitmap.getWidth() == width
                        && mHotBitmap.getHeight() == height) {
                    return;
                }

                mHotBitmap.recycle();
            }

            try {
                mHotBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            } catch (Throwable t) {
                t.printStackTrace();
                mHotBitmap = null;
            }
        } else {
            int width = this.getWidth();
            int height = this.getHeight();
            if (width == 0 || height == 0) {
                return;
            }

            if (mHotBitmap != null) {
                mHotBitmap.recycle();
                mHotBitmap = null;
            }

            if (mTipViewBitmap != null) {
                if (mTipViewBitmap.getWidth() == width
                        && mTipViewBitmap.getHeight() == height) {
                    return;
                }

                mTipViewBitmap.recycle();
            }

            try {
                mTipViewBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            } catch (Throwable t) {
                t.printStackTrace();
                mTipViewBitmap = null;
            }
        }
    }

    private Paint getHotSpotPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        return paint;
    }

    /**o
     * internal drawing methods
     */
    private void drawBackground(Canvas canvas) {
        canvas.save();
        if(mIsHoverTarget) {
            if(mClipPath == null) {
                mClipPath = new Path();
            }
            mClipPath.reset();
            mClipPath.addRect(mHotArea.left, mHotArea.top, mHotArea.right, mHotArea.bottom, Path.Direction.CW);
            canvas.clipPath(mClipPath, Region.Op.DIFFERENCE);
        }
        canvas.drawColor(mBackgroundColor, PorterDuff.Mode.OVERLAY);
        canvas.restore();
    }

    private void drawBackground(Bitmap destination) {
        destination.eraseColor(mBackgroundColor);
    }

    private void drawHotSpot(Canvas canvas) {
        canvas.save();
        mHotBitmap.eraseColor(mBackgroundColor);
        Canvas temp = new Canvas(mHotBitmap);
        float halfWidth = mHotArea.width() >> 1;
        float halfHeight = mHotArea.height() >> 1;
        temp.drawCircle(halfWidth, halfHeight, ((halfWidth > halfHeight) ? halfHeight : halfWidth) - mHotSpotPaint.getStrokeWidth(), mHotSpotPaint);
        canvas.drawBitmap(mHotBitmap, mHotArea.left, mHotArea.top, null);
        canvas.restore();
    }

    private void drawHotSpot(Bitmap destination) {
        Canvas canvas = new Canvas(destination);
        float halfWidth = mHotArea.width() >> 1;
        float halfHeight = mHotArea.height() >> 1;
        canvas.drawCircle(mHotArea.centerX(), mHotArea.centerY(), ((halfWidth > halfHeight) ? halfHeight : halfWidth) - mHotSpotPaint.getStrokeWidth(), mHotSpotPaint);
    }

    private void drawArrow(Canvas canvas) {
        int positionY;
        BitmapDrawable arrowDrawable = mTipArrowUpDrawable;
        if (mHotArea.centerY() > (this.getHeight() >> 1)) {
            arrowDrawable = mTipArrowDownDrawable;
            positionY = mHotArea.top - arrowDrawable.getIntrinsicHeight() - LINE_DEVIATION;
        } else {
            positionY = mHotArea.bottom + LINE_DEVIATION;
        }

        //calculate animation
        positionY += mAnimationOffset;
        canvas.drawBitmap(arrowDrawable.getBitmap(), mHotArea.centerX() - (arrowDrawable.getIntrinsicWidth() >> 1), positionY, null);
    }

    private void drawArrow(Bitmap destination) {
        Canvas canvas = new Canvas(destination);
        drawArrow(canvas);
    }


    private class UpdateOnGlobalLayout implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            onLayoutChange(getWidth(), getHeight());
        }
    }


    private class TextTipView extends RelativeLayout {
        private TextView mTextView;
        private View mCloseBtn;

        public TextTipView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
            View mainView = inflate(context, R.layout.tip, this);
            mTextView = (TextView) mainView.findViewById(R.id.tvText);
            mCloseBtn = mainView.findViewById(R.id.btnClose);
        }

        public void setText(int resId) {
            mTextView.setText(resId);
        }

        public void setText(String text) {
            mTextView.setText(text);
        }

        public void setCloseTipListener(OnClickListener listener) {
            mCloseBtn.setOnClickListener(listener);
        }
    }

    private class CustomContentView extends RelativeLayout {
        public CustomContentView(Context context, @LayoutRes int contentLayout) {
            super(context, null);
            init(context, contentLayout);
        }

        private void init(Context context, @LayoutRes int contentLayout) {
            inflate(context, contentLayout, this);
        }
    }

    public static class Builder {
        private TipView mTipView;
        private Activity mTargetActivity;
        private Fragment mTargetFragment;
        private Tip mTip;
        private TipViewEventTrace mTipViewEventTrace;

        public Builder(Activity target, Fragment targetFragment, Tip tip) {
            mTargetActivity = target;
            mTargetFragment = targetFragment;
            mTip = tip;
        }

        public Builder setTipViewEventTrace(TipViewEventTrace listener) {
            mTipViewEventTrace = listener;
            return this;
        }

        public TipView build() {
            mTipView = new TipView(mTargetActivity, mTargetFragment, mTip);
            mTipView.setTipViewEventTrace(mTipViewEventTrace);
            if (mTargetActivity instanceof TutorialActivityInterface) {
                TutorialActivityInterface ta = (TutorialActivityInterface) mTargetActivity;
                ta.getViewRootForTip().addView(mTipView);
            }
            return mTipView;
        }
    }

}
