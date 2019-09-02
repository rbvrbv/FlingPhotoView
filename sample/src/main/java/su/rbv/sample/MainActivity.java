package su.rbv.sample;

import androidx.appcompat.app.AppCompatActivity;
import su.rbv.flingPhotoView.FlingPhotoView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private View backgroundLayout;
    private FlingPhotoView flingPic1;
    private FlingPhotoView flingPic2;
    private FlingPhotoView flingPic3;

    private TextView tDelta;
    private TextView tProportion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundLayout = findViewById(R.id.background_photo_layout);
        flingPic1 = findViewById(R.id.fling_photo_view1);
        flingPic2 = findViewById(R.id.fling_photo_view2);
        flingPic3 = findViewById(R.id.fling_photo_view3);
        flingPic1.setImageResource(R.drawable.pic1);
        flingPic2.setImageResource(R.drawable.pic2);
        flingPic3.setImageResource(R.drawable.pic3);

        tDelta = findViewById(R.id.t_delta);
        tProportion = findViewById(R.id.t_proportion);
    }

    public void ClickPic1(View v) {
        flingPic1.setOnFlingDragStartListener(() -> {
            tDelta.setVisibility(View.VISIBLE);
            tProportion.setVisibility(View.VISIBLE);
        });
        flingPic1.setOnFlingDragListener((delta, proportion) -> {
            tDelta.setText(Float.toString(delta));
            tProportion.setText(Float.toString(proportion));
        });
        flingPic1.setOnFlingDoneStartListener(() -> {
            tDelta.setVisibility(View.INVISIBLE);
            tProportion.setVisibility(View.INVISIBLE);
        });
        flingPic1.setOnFlingReturnStartListener(() -> {
            tDelta.setVisibility(View.INVISIBLE);
            tProportion.setVisibility(View.INVISIBLE);
        });
        ClickPhotoView(flingPic1);
    }

    public void ClickPic2(View v) {
        flingPic2.setOnFlingDoneStartListener(() -> {
            Toast.makeText(this, "'Fling started' event listener example", Toast.LENGTH_SHORT).show();
        });
        ClickPhotoView(flingPic2);
    }

    public void ClickPic3(View v) {
        ImageView srcPic = findViewById(R.id.img3);
        int[] location = new int[2];
        srcPic.getLocationOnScreen(location);
        int srcPositionX = location[0];
        int srcPositionY = location[1];
        int srcSizeX = srcPic.getWidth();
        int srcSizeY = srcPic.getHeight();
        flingPic3.setFlingResultRect(srcPositionX, srcPositionY, srcSizeX, srcSizeY);
        flingPic3.setOnFlingReturnStartListener(() -> {
            Toast.makeText(this, "'Fling return started' event listener example", Toast.LENGTH_SHORT).show();
        });
        flingPic3.setOnFlingReturnFinishListener(() -> {
            Toast.makeText(this, "'Fling return done' event listener example", Toast.LENGTH_SHORT).show();
        });
        ClickPhotoView(flingPic3);
    }

    private void ClickPhotoView(final FlingPhotoView photo) {
        backgroundLayout.setVisibility(View.VISIBLE);
        photo.setVisibility(View.VISIBLE);
        photo.setOnFlingDoneFinishListener(() -> {
            photo.restoreView();
            backgroundLayout.setVisibility(View.INVISIBLE);
            photo.setVisibility(View.INVISIBLE);
        });
    }
}
