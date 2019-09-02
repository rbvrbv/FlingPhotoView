package su.rbv.flingPhotoView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.daasuu.ei.Ease;
import com.daasuu.ei.EasingInterpolator;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

public class FlingPhotoViewAttacher extends PhotoViewAttacher {

    private Context ctx;

    private Runnable mOnFlingReturnStartListener;
    private Runnable mOnFlingReturnFinishListener;
    private Runnable mOnFlingDoneStartListener;
    private Runnable mOnFlingDoneFinishListener;
    private Runnable mOnFlingDragStartListener;
    private FlingDragRunnable mOnFlingDragListener;

    private View savedView = null;

    private FlingType flingType;                // fling to top, bottom or to rect
    private int flingPhotoReturnDuration = 100; // return photo duration to initial pos in ms
    private int flingPhotoDoneDuration = 300;   // fling photo duration in ms
    private float endScale = 1;                 // scale of photo when fling ended
    private float flingDoneDeltaY_Threshold;  // distance in "px" to fling photo for done
    private float flingStartDeltaY_Threshold; // distance in "px" to start flinging photo
    private static final int default_flingDoneDeltaY_Threshold = 40;  // default distance in "dp" to fling photo for done
    private static final int default_flingStartDeltaY_Threshold = 13; // default distance in "dp" to start flinging photo
    private boolean isNeedBackground = true;    // if true, use background fading
    private View viewBackground = null; // background view, for adjust transparency during flinging picture
    private int refViewBackground = -1; // background view id from attributes. if <0, will get default background (parent of picture)

    private float savedAlpha;           // initial alpha of view
    private float savedBackgroundAlpha; // initial alpha of background
    private float savedScaleX;
    private float savedScaleY;
    private float savedY = 0;           // initial touch Y coordinate
    private float deltaY = 0;           // delta between savedY and current touch Y coordinate
    private boolean isTouched = false;  // =true, if touched, and ready to fling
    private boolean isFlinging = false; // =true, if flinging in progress

    private int iX, iY, iSizeX, iSizeY;
    private float curX, curY, curSizeX, curSizeY;


    public FlingPhotoViewAttacher(ImageView imageView, Context ctx, AttributeSet attr) {
        super(imageView);

        this.ctx = ctx;
        TypedArray a = ctx.obtainStyledAttributes(attr, R.styleable.FlingPhotoView);
        try {
            flingPhotoReturnDuration   = a.getInteger(R.styleable.FlingPhotoView_return_duration, flingPhotoReturnDuration);
            flingPhotoDoneDuration     = a.getInteger(R.styleable.FlingPhotoView_done_duration, flingPhotoDoneDuration);
            flingDoneDeltaY_Threshold  = a.getDimension(R.styleable.FlingPhotoView_done_deltaY_threshold,
                    dpToPx(default_flingDoneDeltaY_Threshold));
            flingStartDeltaY_Threshold = a.getDimension(R.styleable.FlingPhotoView_start_deltaY_threshold,
                    dpToPx(default_flingStartDeltaY_Threshold));
            flingType = FlingType.values()[a.getInteger(R.styleable.FlingPhotoView_fling_type, 0)];
            endScale  = a.getFloat(R.styleable.FlingPhotoView_end_scale, endScale);
            isNeedBackground = a.getBoolean(R.styleable.FlingPhotoView_need_background, isNeedBackground);
            refViewBackground = a.getResourceId(R.styleable.FlingPhotoView_background, -1);
        } finally {
            a.recycle();
        }
    }

    private int dpToPx(int dp) {
        return Math.round((float) dp * ctx.getResources().getDisplayMetrics().density);
    }

    public void setViewBackground(View viewBackground) {
        this.viewBackground = viewBackground;
    }

    public View getViewBackground() {
        return viewBackground;
    }

    public void setFlingResultRect(int iX, int iY, int iSizeX, int iSizeY) {
        this.iX = iX;
        this.iY = iY;
        this.iSizeX = iSizeX;
        this.iSizeY = iSizeY;
    }

    public void setFlingResultRect(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        iX = location[0];
        iY = location[1];
        iSizeX = v.getWidth();
        iSizeY = v.getHeight();
    }

    public void setFlingType(FlingType flingType) {
        this.flingType = flingType;
    }

    public FlingType getFlingType() {
        return flingType;
    }

    public void setNeedBackground(boolean needBackground) {
        isNeedBackground = needBackground;
    }

    public boolean getNeedBackground() {
        return isNeedBackground;
    }

    public int getFlingPhotoReturnDuration() {
        return flingPhotoReturnDuration;
    }

    public int getFlingPhotoDoneDuration() {
        return flingPhotoDoneDuration;
    }

    public float getEndScale() {
        return endScale;
    }

    public float getFlingDoneDeltaY_Threshold() {
        return flingDoneDeltaY_Threshold;
    }

    public float getFlingStartDeltaY_Threshold() {
        return flingStartDeltaY_Threshold;
    }

    public void setFlingPhotoReturnDuration(int flingPhotoReturnDuration) {
        this.flingPhotoReturnDuration = flingPhotoReturnDuration;
    }

    public void setFlingPhotoDoneDuration(int flingPhotoDoneDuration) {
        this.flingPhotoDoneDuration = flingPhotoDoneDuration;
    }

    public void setEndScale(float endScale) {
        this.endScale = endScale;
    }

    public void setFlingDoneDeltaY_Threshold(float flingDoneDeltaY_Threshold) {
        this.flingDoneDeltaY_Threshold = flingDoneDeltaY_Threshold;
    }

    public void setFlingStartDeltaY_Threshold(float flingStartDeltaY_Threshold) {
        this.flingStartDeltaY_Threshold = flingStartDeltaY_Threshold;
    }

    public void setOnFlingReturnStartListener(Runnable r) {
        this.mOnFlingReturnStartListener = r;
    }

    public void setOnFlingReturnFinishListener(Runnable r) {
        this.mOnFlingReturnFinishListener = r;
    }

    public void setOnFlingDoneStartListener(Runnable r) {
        this.mOnFlingDoneStartListener = r;
    }

    public void setOnFlingDoneFinishListener(Runnable r) {
        this.mOnFlingDoneFinishListener = r;
    }

    public void setOnFlingDragStartListener(Runnable r) {
        this.mOnFlingDragStartListener = r;
    }

    public void setOnFlingDragListener(FlingDragRunnable r) {
        this.mOnFlingDragListener = r;
    }


    public void restoreView() {
        if (savedView != null) {
            savedView.setX(0);
            savedView.setY(0);
            savedView.setAlpha(savedAlpha);
            savedView.setScaleX(savedScaleX);
            savedView.setScaleY(savedScaleY);
        }
        if (isNeedBackground && viewBackground != null) {
            viewBackground.setAlpha(savedBackgroundAlpha);
        }
    }

    private void flingOutBackground() {
        if (isNeedBackground && viewBackground != null)
            viewBackground.animate()     // fade out the background
                .alpha(0.0f)
                .setDuration(flingPhotoDoneDuration)
                .setInterpolator(new EasingInterpolator(Ease.QUAD_OUT));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        return handleFlingGesture(v, ev) || super.onTouch(v, ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    private boolean handleFlingGesture(View v, MotionEvent event) {
        if (getScale() == 1) {          // if photo not scaled, then we can fling it
            if (event.getAction() == MotionEvent.ACTION_DOWN) {     // initial touch
                RectF Rect = getDisplayRect();
                if (Rect != null) {
                    isTouched = true;
                    curX = Rect.left;
                    curY = Rect.top;
                    curSizeX = Rect.right - curX;
                    curSizeY = Rect.bottom - curY;
                }
                if (isNeedBackground && viewBackground == null) {
                    if (refViewBackground >= 0)
                        setViewBackground(((View) v.getParent()).findViewById(refViewBackground));
                    else setViewBackground((View) v.getParent());
                }
                savedY = event.getRawY();
                savedAlpha = v.getAlpha();
                savedScaleX = v.getScaleX();
                savedScaleY = v.getScaleY();
                savedView = v;
                if (viewBackground != null) {
                    savedBackgroundAlpha = viewBackground.getAlpha();
                }
            }
            if (isTouched && event.getAction() == MotionEvent.ACTION_UP) {
                isTouched = isFlinging = false;
                if (savedY - event.getRawY() < flingDoneDeltaY_Threshold) { // Flinging return
                    if (mOnFlingReturnStartListener != null) mOnFlingReturnStartListener.run();
                    if (isNeedBackground && viewBackground != null)
                        viewBackground.animate()  // restore transparency of the background
                                .alpha(savedBackgroundAlpha)
                                .setDuration(flingPhotoReturnDuration);
                    v.animate()        // restore transparency and position of the picture
                        .alpha(savedAlpha)
                        .setDuration(flingPhotoReturnDuration)
                        .setInterpolator(new EasingInterpolator(Ease.QUAD_OUT))
                        .yBy(deltaY)
                        .withEndAction(() -> {
                            if (mOnFlingReturnFinishListener != null) mOnFlingReturnFinishListener.run();
                        });
                } else {            // fling distance is above threshold
                    int parentX = 0;
                    int parentY = 0;
                    if (v.getParent() != null) {
                        int[] location = new int[2];
                        ((View) v.getParent()).getLocationOnScreen(location);
                        parentX = location[0];   // location of original picture parent
                        parentY = location[1];
                    }
                    switch (flingType) {
                        case FLING_TO_RECTANGLE:     // fling to defined rectangle position & size
                            if (mOnFlingDoneStartListener != null) mOnFlingDoneStartListener.run();
                            flingOutBackground();
                            v.animate()    // move and resize to original picture
                                .setDuration(flingPhotoDoneDuration)
                                .x(iX - parentX - curX - (curSizeX - iSizeX) / 2)    // move to relative coord in orig. pict. parent
                                .y(iY - parentY - curY - (curSizeY - iSizeY) / 2)
                                .scaleX(iSizeX / curSizeX)                      // scale to orig. picture size
                                .scaleY(iSizeY / curSizeY)
                                .setInterpolator(new EasingInterpolator(Ease.QUAD_OUT))
                                .withEndAction(() -> {
                                    if (mOnFlingDoneFinishListener != null) mOnFlingDoneFinishListener.run();
                                });
                            break;
                        case FLING_TO_TOP:      // fling to screen top
                            if (mOnFlingDoneStartListener != null) mOnFlingDoneStartListener.run();
                            flingOutBackground();
                            v.animate()    // fling out and fade out the picture
                                .alpha(0.0f)
                                .setDuration(flingPhotoDoneDuration)
                                .y(-curY - curSizeY + (curSizeY - curSizeY*endScale) / 2 - parentY)
                                .scaleX(endScale)
                                .scaleY(endScale)
                                .setInterpolator(new EasingInterpolator(Ease.QUAD_OUT))
                                .withEndAction(() -> {
                                    if (mOnFlingDoneFinishListener != null) mOnFlingDoneFinishListener.run();
                                });
                            break;
                        case FLING_TO_BOTTOM:      // fling to screen bottom
                            if (mOnFlingDoneStartListener != null) mOnFlingDoneStartListener.run();
                            flingOutBackground();
                            Display d = ((WindowManager) ctx.getSystemService(ctx.WINDOW_SERVICE)).getDefaultDisplay();
                            Point size = new Point();
                            d.getSize(size);
                            v.animate()    // fling out and fade out the picture
                                .alpha(0.0f)
                                .setDuration(flingPhotoDoneDuration)
                                .y(size.y - parentY - curY - (curSizeY - curSizeY*endScale) / 2)
                                .scaleX(endScale)
                                .scaleY(endScale)
                                .setInterpolator(new EasingInterpolator(Ease.QUAD_OUT))
                                .withEndAction(() -> {
                                    if (mOnFlingDoneFinishListener != null) mOnFlingDoneFinishListener.run();
                                });
                            break;
                    }
                }
            }
            if (isTouched && event.getAction() == MotionEvent.ACTION_MOVE) {    // drag picture
                deltaY = savedY - event.getRawY();
                if (deltaY < 0) deltaY = 0;         // don't move the picture below initial position
                if (deltaY > flingStartDeltaY_Threshold) {
                    isFlinging = true;
                    if (mOnFlingDragStartListener != null) mOnFlingDragStartListener.run();
                }
                if (isFlinging) {
                    float proportion = (savedY - deltaY) / savedY;
                    v.setAlpha(proportion);
                    if (isNeedBackground && viewBackground != null) {
                        viewBackground.setAlpha(proportion);
                    }
                    v.setY(-deltaY);
                    if (mOnFlingDragListener != null) mOnFlingDragListener.drag(deltaY, proportion);
                }
            }
        }
        return isFlinging;
    }
}
