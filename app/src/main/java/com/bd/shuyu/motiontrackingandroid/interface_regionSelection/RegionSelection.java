package com.bd.shuyu.motiontrackingandroid.interface_regionSelection;

import android.support.v7.app.AppCompatActivity;

public class RegionSelection extends AppCompatActivity {
region_button.setOnClickListener(new OnClickListener() {

@Override
public void onClick(View v) {
        objects = category;
        adap.notifyDataSetChanged();
        proximity_button.setPressed(false);
        region_button.setPressed(true);

        }
        })
//add button for selecting which object to track

mycodes_Button.setOnTouchListener(new OnTouchListener() {
@Override
public boolean RegionSelection(View v, MotionEvent event) {
        mycodes_Button.setPressed(true);
        return OnTouchListener;
        }
        })
        }