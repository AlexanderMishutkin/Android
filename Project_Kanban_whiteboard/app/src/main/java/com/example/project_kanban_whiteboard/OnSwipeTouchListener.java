package com.example.project_kanban_whiteboard;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import java.util.List;

public class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector;
    private final List<String> columns;
    private final Button btn;
    private final MainActivity context;

    public OnSwipeTouchListener(Context ctx, List<String> columns, Button btn, MainActivity context){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        this.columns = columns;
        this.btn = btn;
        this.context = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 70;
        private static final int SWIPE_VELOCITY_THRESHOLD = 70;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

    public void onSwipeRight() {
        MainActivity.Note note = (MainActivity.Note) btn.getTag();
        if (note.column < columns.size() - 1) {
            context.removeSelection();
            note.column++;
            context.fireDataChanged();
            context.styleButtons();

        }
    }
    public void onSwipeLeft() {
        MainActivity.Note note = (MainActivity.Note) btn.getTag();
        if (note.column > 0) {
            context.removeSelection();
            note.column--;
            context.fireDataChanged();
            context.styleButtons();
        }
    }
}