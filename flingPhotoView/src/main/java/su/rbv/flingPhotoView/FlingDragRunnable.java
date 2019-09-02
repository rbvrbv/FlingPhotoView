package su.rbv.flingPhotoView;

@FunctionalInterface
public interface FlingDragRunnable {

        void drag(float deltaY, float proportion);

}
