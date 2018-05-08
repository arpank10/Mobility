package com.example.ark.ark.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ark.ark.R;
import com.example.ark.ark.Sensors.Rotation;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by durgesh on 3/9/17.
 */

public class RotationFragment extends Fragment {

    private TextView x,y,z;
    private float[] rotValues;
    private Rotation rot;
    private Timer timer;
    View rootView;
    public RotationFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootView = inflater.inflate(R.layout.fragment_rotation, container, false);
        x=rootView.findViewById(R.id.rx);
        y=rootView.findViewById(R.id.ry);
        z=rootView.findViewById(R.id.rz);
        rot =new Rotation(getActivity().getApplicationContext());
            rotValues = new float[3];
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() == null)
                        return;
                    rotValues = rot.getLastReading();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayData();
                            ;
                        }
                    });
                }

            }, 0, 50);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Rotation");
    }



    private void displayData()
    {
        x.setText(String.format("%.5f", rotValues[0]));
        y.setText(String.format("%.5f", rotValues[1]));
        z.setText(String.format("%.5f", rotValues[2]));

    }
    @Override
    public void onDestroyView() {
        rootView=null;
        rot.unregister();
        if(timer!=null)
            timer.cancel();
        super.onDestroyView();
    }
}
