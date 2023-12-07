package com.utils.anglefinder3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Display ;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
   // private Sensor mRotationSensor;
   // final float[] mValuesMagnet      = new float[3];
   // final float[] mValuesAccel       = new float[3];
 //   final float[] mValuesOrientation = new float[3];
 //   final float[] mRotationMatrix    = new float[9];

    // Gravity rotational data
    private float gravity[];
    // Magnetic rotational data
    private float magnetic[]; //for magnetic rotational data
    private float accels[] = new float[3];
    private float mags[] = new float[3];
    private float[] values = new float[3];

    // azimuth, pitch and roll
    private float azimuth;
    private float pitch;
    private float roll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btn_valider = (Button) findViewById(R.id.button);
        final TextView textx = (TextView) findViewById(R.id.xaxis);
        final TextView texty = (TextView) findViewById(R.id.yaxis);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //float vals[] = {0.435F , 0 , 0,0,0,0,0,0,0} ;
        //float fff = SensorManager.getInclination(vals) ;
        //Log.d("SensorData:" , "" + fff ) ;
        //mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        //if( mRotationSensor == null )
        //Log.d("SensorData:" , "NULL") ;
       // mSensorManager.registerListener(this, mRotationSensor, 16000);

        setListners(mSensorManager, this) ;

        btn_valider.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                /*
                SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
                SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);
                final CharSequence test;
               // test = "results: " + mValuesOrientation[0] +" "+mValuesOrientation[1]+ " "+ mValuesOrientation[2];
                  test = "results: " + RadiansToDegrees( mValuesOrientation[0] ) +" "+ RadiansToDegrees( mValuesOrientation[1] )+ " "+ RadiansToDegrees( mValuesOrientation[2] );

                Log.d("SensorData:" , test.toString()) ;
                // txt1.setText(test);

                */

                Log.d("SensorData:" , "[azimuth=" + azimuth +", pitch=" + pitch + ", roll =" + roll) ;
                textx.setText("" + Math.abs( (int)pitch )+"deg") ;
                texty.setText("" + Math.abs( (int)roll ) +"deg");
            }
        });


    }


    public static final float FLOAT_PI = (float) Math.PI;

    public static float RadiansToDegrees(float radians) {
        return (radians * 180f) / FLOAT_PI;
    }
/*
    public void onSensorChanged(@NonNull SensorEvent event) {


        // Handle the events for which we registered
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
                break;
        }
/*
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            // add your code

            // event.values[] contains the 3 accelerometer values
            Log.d("SensorData:" , " " + event.values.length) ;
            Log.d("SensorData:" , " " + event.values[0]) ;
            Log.d("SensorData:" , " " + event.values[1]) ;
            Log.d("SensorData:" , " " + event.values[2]) ;

        }


    }

***/
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                mags = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accels = event.values.clone();
                break;
        }

        if (mags != null && accels != null) {
            gravity = new float[9];
            magnetic = new float[9];
            SensorManager.getRotationMatrix(gravity, magnetic, accels, mags);
            float[] outGravity = new float[9];
            SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X,SensorManager.AXIS_Y, outGravity);
            SensorManager.getOrientation(outGravity, values);

            azimuth = values[0] * 57.2957795f;
            pitch =values[1] * 57.2957795f;
            roll = values[2] * 57.2957795f;
            mags = null;
            accels = null;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // add your code
        Log.d("SensorData:" , "In method onAccuracyChange!") ;
    }


    public void setListners(SensorManager sensorManager, SensorEventListener mEventListener)
    {
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }


}