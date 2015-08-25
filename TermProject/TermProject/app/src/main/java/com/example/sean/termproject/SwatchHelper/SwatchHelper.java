package com.example.sean.termproject.SwatchHelper;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import java.util.ArrayList;

/**
 * Created by Sean Thomas on 4/22/2015.
 */
public class SwatchHelper {
    private static int interpPoints = 20;
    private float LHue;
    private float RHue;
    private float Saturation;
    private float Value;

    public SwatchHelper( float LHue,
                         float RHue,
                         float Saturation,
                         float Value ) {
        this.LHue = LHue;
        this.RHue = RHue;
        this.Saturation = Saturation;
        this.Value = Value;
    }

    public float getLHue() {
        return LHue;
    }
    public float getRHue() {
        return RHue;
    }
    public float getSaturation() {
        return Saturation;
    }
    public float getValue() {
        return Value;
    }

    public int getColorLeft() {
        return HSVtoColor( LHue, Saturation, Value );
    }
    public int geColorRight() {
        return HSVtoColor( RHue, Saturation, Value );
    }

    private int[] getInterpolation(){
        double range = ( RHue > LHue ) ? ( RHue - LHue ) : ( RHue + ( 360 - LHue ) );

        int[] colorArray = new int[interpPoints];
        double temp = getLHue(); // Use doubles to minimize rounding errors
        double delta = range / interpPoints;

        for( int i = 0; i < interpPoints; i++ ) {
            colorArray[i] = HSVtoColor( (float)temp, Saturation, Value );
            temp += delta;
            temp %= 360.0;
        }

        return colorArray;
    }

    public SwatchHelper diffSat( float sat ) {
        return new SwatchHelper( LHue,
                                 RHue,
                                 sat,
                                 Value );
    }
    public SwatchHelper diffVal( float val ) {
        return new SwatchHelper( LHue,
                                 RHue,
                                 Saturation,
                                 val );
    }

    public GradientDrawable getGradient() {
        return new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                getInterpolation() );
    }

    public static int HSVtoColor( float hue, float saturation, float value ) {
        float[] hsv = { hue,        // float: [0.0f, 360.0f]
                        saturation, // float: [0.0f, 1.0f]
                        value       // float: [0.0f, 1.0f]
                      };

        return Color.HSVToColor( hsv );
    }

    public static ArrayList<SwatchHelper> createHueSwatches( int count,
                                                             float centerHue ){
        ArrayList<SwatchHelper> Swatches = new ArrayList<>();
        double range = 360.0 / (double)count;
        float tempSat = 1.0f;
        float tempVal = 1.0f;

        double tempHue = ( range > centerHue ) ?
                            ( 360.0 - ( range/2.0 - centerHue )) :
                            ( centerHue - range/2.0 );
        double tempHueB;
        for( int i = 0; i < count; i++ ) {
            tempHueB = ( tempHue + range ) % 360;
            Swatches.add(
                    new SwatchHelper( (float) tempHue,
                                      (float) tempHueB,
                                        tempSat,
                                        tempVal)
                        );
            tempHue = tempHueB;
        }
        return Swatches;
    }

    public static ArrayList<SwatchHelper> createSatSwatches( int count,
                                                             SwatchHelper baseSwatch ) {
        ArrayList<SwatchHelper> Swatches = new ArrayList<>();
        double range = 1.0 / (double)count;
        double tempSat = 1.0;

        for (int i = 0; i < count ; i++) {
            Swatches.add( baseSwatch.diffSat( (float)tempSat ) );
            tempSat -= range;
        }

        return Swatches;
    }

    public static ArrayList<SwatchHelper> createValSwatches( int count,
                                                             SwatchHelper baseSwatch ) {
        ArrayList<SwatchHelper> Swatches = new ArrayList<>();
        double range = 1.0 / (double)count;
        double tempVal = 1.0;

        for (int i = 0; i < count ; i++) {
            Swatches.add( baseSwatch.diffVal( (float)tempVal ) );
            tempVal -= range;
        }

        return Swatches;
    }
}
