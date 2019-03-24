package com.bd.shuyu.motiontrackingandroid;
import android.app.Activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bd.shuyu.motiontrackingandroid.R;
import com.bd.shuyu.motiontrackingandroid.interface_regionSelection.RegionSelection;


public class SELActivity_orig extends AppCompatActivity {

    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buttons);

        final RegionSelection view = (RegionSelection) findViewById(R.id.dragRect);

        if (null != view) {
            view.setOnUpCallback(new RegionSelection.OnUpCallback() {
                @Override
                public void onRectFinished(final Rect rect) {
                    Toast.makeText(getApplicationContext(), "Rect is (" + rect.left + ", " + rect.top + ", " + rect.right + ", " + rect.bottom + ")",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}