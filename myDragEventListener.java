package com.example.sevak.themaze;

import android.view.DragEvent;
import android.view.View;

public class myDragEventListener implements View.OnDragListener {

    // This is the method that the system calls when it dispatches a drag event to the
    // listener.
    public boolean onDrag(View v, DragEvent event) {

        // Defines a variable to store the action type for the incoming event
        final int action = event.getAction();

        // Handles each of the expected events
        switch (action) {
            case DragEvent.ACTION_DROP:
                StartPage.vX = (int) v.getX();
                StartPage.vY = (int) v.getY();
                return true;
        }
        return true;
    }
}