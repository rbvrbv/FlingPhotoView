package su.rbv.flingPhotoView;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import com.github.chrisbanes.photoview.*;

public class FlingPhotoView extends AppCompatImageView {

    private FlingPhotoViewAttacher attacher;
    private ScaleType pendingScaleType;

    public FlingPhotoView(Context context) {
        this(context, null);
    }

    public FlingPhotoView(Context context, AttributeSet attr) { this(context, attr, 0); }

    public FlingPhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        init(attr);
    }

    private void init(AttributeSet attr) {
        attacher = new FlingPhotoViewAttacher(this, getContext(), attr);
        //We always pose as a Matrix scale type, though we can change to another scale type
        //via the attacher
        super.setScaleType(ScaleType.MATRIX);
        //apply the previously applied scale type
        if (pendingScaleType != null) {
            setScaleType(pendingScaleType);
            pendingScaleType = null;
        }
    }

    /**
     * Get the current {@link PhotoViewAttacher} for this view. Be wary of holding on to references
     * to this attacher, as it has a reference to this view, which, if a reference is held in the
     * wrong place, can cause memory leaks.
     *
     * @return the attacher.
     */
    public FlingPhotoViewAttacher getAttacher() {
        return attacher;
    }

    @Override
    public ScaleType getScaleType() {
        return attacher.getScaleType();
    }

    @Override
    public Matrix getImageMatrix() {
        return attacher.getImageMatrix();
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        attacher.setOnLongClickListener(l);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        attacher.setOnClickListener(l);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (attacher == null) {
            pendingScaleType = scaleType;
        } else {
            attacher.setScaleType(scaleType);
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        // setImageBitmap calls through to this method
        if (attacher != null) {
            attacher.update();
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (attacher != null) {
            attacher.update();
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        if (attacher != null) {
            attacher.update();
        }
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        if (changed) {
            attacher.update();
        }
        return changed;
    }

    public void setRotationTo(float rotationDegree) {
        attacher.setRotationTo(rotationDegree);
    }

    public void setRotationBy(float rotationDegree) {
        attacher.setRotationBy(rotationDegree);
    }

    public boolean isZoomable() {
        return attacher.isZoomable();
    }

    public void setZoomable(boolean zoomable) {
        attacher.setZoomable(zoomable);
    }

    public RectF getDisplayRect() {
        return attacher.getDisplayRect();
    }

    public void getDisplayMatrix(Matrix matrix) {
        attacher.getDisplayMatrix(matrix);
    }

    @SuppressWarnings("UnusedReturnValue") public boolean setDisplayMatrix(Matrix finalRectangle) {
        return attacher.setDisplayMatrix(finalRectangle);
    }

    public void getSuppMatrix(Matrix matrix) {
        attacher.getSuppMatrix(matrix);
    }

    public boolean setSuppMatrix(Matrix matrix) {
        return attacher.setDisplayMatrix(matrix);
    }

    public float getMinimumScale() {
        return attacher.getMinimumScale();
    }

    public float getMediumScale() {
        return attacher.getMediumScale();
    }

    public float getMaximumScale() {
        return attacher.getMaximumScale();
    }

    public float getScale() {
        return attacher.getScale();
    }

    public void setAllowParentInterceptOnEdge(boolean allow) {
        attacher.setAllowParentInterceptOnEdge(allow);
    }

    public void setMinimumScale(float minimumScale) {
        attacher.setMinimumScale(minimumScale);
    }

    public void setMediumScale(float mediumScale) {
        attacher.setMediumScale(mediumScale);
    }

    public void setMaximumScale(float maximumScale) {
        attacher.setMaximumScale(maximumScale);
    }

    public void setScaleLevels(float minimumScale, float mediumScale, float maximumScale) {
        attacher.setScaleLevels(minimumScale, mediumScale, maximumScale);
    }

    public void setOnMatrixChangeListener(OnMatrixChangedListener listener) {
        attacher.setOnMatrixChangeListener(listener);
    }

    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        attacher.setOnPhotoTapListener(listener);
    }

    public void setOnOutsidePhotoTapListener(OnOutsidePhotoTapListener listener) {
        attacher.setOnOutsidePhotoTapListener(listener);
    }

    public void setOnViewTapListener(OnViewTapListener listener) {
        attacher.setOnViewTapListener(listener);
    }

    public void setOnViewDragListener(OnViewDragListener listener) {
        attacher.setOnViewDragListener(listener);
    }

    public void setScale(float scale) {
        attacher.setScale(scale);
    }

    public void setScale(float scale, boolean animate) {
        attacher.setScale(scale, animate);
    }

    public void setScale(float scale, float focalX, float focalY, boolean animate) {
        attacher.setScale(scale, focalX, focalY, animate);
    }

    public void setZoomTransitionDuration(int milliseconds) {
        attacher.setZoomTransitionDuration(milliseconds);
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
        attacher.setOnDoubleTapListener(onDoubleTapListener);
    }

    public void setOnScaleChangeListener(OnScaleChangedListener onScaleChangedListener) {
        attacher.setOnScaleChangeListener(onScaleChangedListener);
    }

    public void setOnSingleFlingListener(OnSingleFlingListener onSingleFlingListener) {
        attacher.setOnSingleFlingListener(onSingleFlingListener);
    }

    public void setOnFlingReturnStartListener(Runnable r) {
        attacher.setOnFlingReturnStartListener(r);
    }

    public void setOnFlingReturnFinishListener(Runnable r) {
        attacher.setOnFlingReturnFinishListener(r);
    }

    public void setOnFlingDoneStartListener(Runnable r) {
        attacher.setOnFlingDoneStartListener(r);
    }

    public void setOnFlingDoneFinishListener(Runnable r) {
        attacher.setOnFlingDoneFinishListener(r);
    }

    public void setOnFlingDragStartListener(Runnable r) {
        attacher.setOnFlingDragStartListener(r);
    }

    public void setOnFlingDragListener(FlingDragRunnable r) {
        attacher.setOnFlingDragListener(r);
    }

    public void setViewBackground(View bg) {
        attacher.setViewBackground(bg);
    }

    public View getViewBackground() {
        return attacher.getViewBackground();
    }

    public void setFlingResultRect(int iX, int iY, int iSizeX, int iSizeY) {
        attacher.setFlingResultRect(iX, iY, iSizeX, iSizeY);
    }

    public void setFlingResultRect(View v) {
        attacher.setFlingResultRect(v);
    }

    public void setFlingType(FlingType flingType) {
        attacher.setFlingType(flingType);
    }

    public FlingType getFlingType() {
        return attacher.getFlingType();
    }

    public void setNeedBackground(boolean needBackground) {
        attacher.setNeedBackground(needBackground);
    }

    public boolean getNeedBackground() {
        return attacher.getNeedBackground();
    }

    public int getFlingPhotoReturnDuration() {
        return attacher.getFlingPhotoReturnDuration();
    }

    public int getFlingPhotoDoneDuration() {
        return attacher.getFlingPhotoDoneDuration();
    }

    public float getEndScale() {
        return attacher.getEndScale();
    }

    public float getFlingDoneDeltaY_Threshold() {
        return attacher.getFlingDoneDeltaY_Threshold();
    }

    public float getFlingStartDeltaY_Threshold() {
        return attacher.getFlingStartDeltaY_Threshold();
    }

    public void setFlingPhotoReturnDuration(int flingPhotoReturnDuration) {
        attacher.setFlingPhotoReturnDuration(flingPhotoReturnDuration);
    }

    public void setFlingPhotoDoneDuration(int flingPhotoDoneDuration) {
        attacher.setFlingPhotoDoneDuration(flingPhotoDoneDuration);
    }

    public void setEndScale(float endScale) {
        attacher.setEndScale(endScale);
    }

    public void setFlingDoneDeltaY_Threshold(float flingDoneDeltaY_Threshold) {
        attacher.setFlingDoneDeltaY_Threshold(flingDoneDeltaY_Threshold);
    }

    public void setFlingStartDeltaY_Threshold(float flingStartDeltaY_Threshold) {
        attacher.setFlingStartDeltaY_Threshold(flingStartDeltaY_Threshold);
    }

    public void restoreView() {
        attacher.restoreView();
    }

}
