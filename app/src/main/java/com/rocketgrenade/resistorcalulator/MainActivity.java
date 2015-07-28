package com.rocketgrenade.resistorcalulator;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import java.lang.reflect.Field;


/*
         Temperature Band
               |
      Tolerance band
             | |
   ------------
--- | | | |  | | ---
   ------------
    | | | |
First band
      | | |
  Second band
        | |
    Third band or multiplier band depending on type of resistor
          |
      multiplier band, always last one before gap
RESISTOR VALUES:

For digit bands (the first 2-3 bands before a gap depending on the type of resistor:
 0: Black
 1: Brown
 2-7: ROYGBV no indigo
 8: Grey
 9: White

For mutiplier band (the final band before the gap)
 same values for the colours Black-Violet above but used in the equation 10^x, Grey and White are
 not used
 -1: Gold
 -2: Silver

For Tolerance band (first band after gap) values are in +- plus or minus
 Gold: 5%
 Silver: 10%
 Brown: 1%
 Red: 2%
 Green: 0.5%
 Blue: 0.25%
 Violet: 0.1%

For Temperature band (the last band if there is more than one band after the gap)
 Brown: 100ppm
 Red: 50ppm
 Orange: 15ppm
 Yellow: 25ppm
 */

public class MainActivity extends ActionBarActivity {

    NumberPicker firstBandPicker, secondBandPicker, thirdBandPicker, multBandPicker, tolerBandPicker, tempBandPicker;
    RadioButton num4, num5, num6;
    TextView resultText;
    private int numBands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstBandPicker = (NumberPicker) findViewById(R.id.firstBandPicker);
        firstBandPicker.setMaxValue(9);
        firstBandPicker.setOnValueChangedListener(colorChangeListener);
        secondBandPicker = (NumberPicker) findViewById(R.id.secondBandPicker);
        secondBandPicker.setMaxValue(9);
        secondBandPicker.setOnValueChangedListener(colorChangeListener);
        thirdBandPicker = (NumberPicker) findViewById(R.id.thirdBandPicker);
        thirdBandPicker.setMaxValue(9);
        thirdBandPicker.setOnValueChangedListener(colorChangeListener);
        multBandPicker = (NumberPicker) findViewById(R.id.multBandPicker);
        multBandPicker.setMaxValue(9);
        multBandPicker.setOnValueChangedListener(multChangeListener);
        tolerBandPicker = (NumberPicker) findViewById(R.id.tolerBandPicker);
        tolerBandPicker.setMaxValue(7);
        tolerBandPicker.setOnValueChangedListener(toleranceChangeListener);
        tempBandPicker = (NumberPicker) findViewById(R.id.tempBandPicker);
        tempBandPicker.setMaxValue(3);
        tempBandPicker.setOnValueChangedListener(temperatureChangeListener);

        thirdBandPicker.setEnabled(false);
        tempBandPicker.setEnabled(false);
        thirdBandPicker.setBackgroundColor(Color.rgb(220, 220, 220));
        tempBandPicker.setBackgroundColor(Color.rgb(220, 220, 220));

        num4 = (RadioButton) findViewById(R.id.numBand4);
        num5 = (RadioButton) findViewById(R.id.numBand5);
        num6 = (RadioButton) findViewById(R.id.numBand6);

        resultText = (TextView) findViewById(R.id.resultText);

        numBands = 4;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    NumberPicker.OnValueChangeListener colorChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldval, int newval) {
            int color = Color.BLACK;
            setColor(picker, color);
            switch (newval) {
                case 0:
                    color = Color.BLACK;
                    setColor(picker, Color.WHITE);
                    break;
                case 1:
                    color = Color.rgb(210, 105, 30);
                    break;
                case 2:
                    color = Color.RED;
                    break;
                case 3:
                    color = Color.rgb(255, 165, 0);
                    break;
                case 4:
                    color = Color.YELLOW;
                    break;
                case 5:
                    color = Color.GREEN;
                    setColor(picker, Color.WHITE);
                    break;
                case 6:
                    color = Color.BLUE;
                    setColor(picker, Color.WHITE);
                    break;
                case 7:
                    color = Color.rgb(255, 100, 255);
                    break;
                case 8:
                    color = Color.GRAY;
                    break;
                case 9:
                    color = Color.WHITE;
                    break;
            }
            picker.setBackgroundColor(color);
            doCalc();
        }
    };

    NumberPicker.OnValueChangeListener multChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldval, int newval) {
            int color = Color.BLACK;
            switch (newval) {
                case 0:
                    color = Color.rgb(192, 192, 192);
                    break;
                case 1:
                    color = Color.rgb(218,165,32);
                    break;
                case 2:
                    color = Color.BLACK;
                    break;
                case 3:
                    color = Color.rgb(210, 105, 30);
                    break;
                case 4:
                    color = Color.RED;
                    break;
                case 5:
                    color = Color.rgb(255, 165, 0);
                    break;
                case 6:
                    color = Color.YELLOW;
                    break;
                case 7:
                    color = Color.GREEN;
                    break;
                case 8:
                    color = Color.BLUE;
                    break;
                case 9:
                    color = Color.rgb(255, 100, 255);
                    break;
            }
            picker.setBackgroundColor(color);
            setColor(picker, color);
            doCalc();
        }
    };

    NumberPicker.OnValueChangeListener toleranceChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldval, int newval) {
            int color = Color.BLACK;
            switch (newval) {
                case 0:
                    color = Color.rgb(218,165,32);
                    break;
                case 1:
                    color = Color.rgb(192, 192, 192);
                    break;
                case 2:
                    color = Color.rgb(210, 105, 30);
                    break;
                case 3:
                    color = Color.RED;
                    break;
                case 4:
                    color = Color.GREEN;
                    break;
                case 5:
                    color = Color.BLUE;
                    break;
                case 6:
                    color = Color.rgb(255, 100, 255);
                    break;
                case 7:
                    color = Color.GRAY;
            }
            picker.setBackgroundColor(color);
            setColor(picker, color);
            doCalc();
        }
    };

    NumberPicker.OnValueChangeListener temperatureChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldval, int newval) {
            int color = Color.BLACK;
            /* Brown: 100ppm
             Red: 50ppm
             Orange: 15ppm
             Yellow: 25ppm*/
            switch (newval) {
                case 0:
                    color = Color.rgb(210, 105, 30);
                    break;
                case 1:
                    color = Color.RED;
                    break;
                case 2:
                    color = Color.rgb(255, 165, 0);
                    break;
                case 3:
                    color = Color.YELLOW;
                    break;
            }
            picker.setBackgroundColor(color);
            setColor(picker, color);
            doCalc();
        }
    };

    public void numBandHandler(View view){
        int disabledColor = Color.rgb(220,220,220);
        switch(view.getId()){
            case R.id.numBand4:
                num5.setChecked(false);
                num6.setChecked(false);
                numBands = 4;
                thirdBandPicker.setEnabled(false);
                tempBandPicker.setEnabled(false);
                thirdBandPicker.setValue(0);
                thirdBandPicker.setBackgroundColor(disabledColor);
                tempBandPicker.setValue(0);
                tempBandPicker.setBackgroundColor(disabledColor);
                break;
            case R.id.numBand5:
                num4.setChecked(false);
                num6.setChecked(false);
                numBands = 5;
                thirdBandPicker.setEnabled(true);
                tempBandPicker.setEnabled(false);
                tempBandPicker.setValue(0);
                tempBandPicker.setBackgroundColor(disabledColor);
                break;
            case R.id.numBand6:
                num4.setChecked(false);
                num5.setChecked(false);
                numBands = 6;
                thirdBandPicker.setEnabled(true);
                tempBandPicker.setEnabled(true);
                break;
        }
        doCalc();
    }

    private String getTolerance(){
        String result = "";
        switch(tolerBandPicker.getValue()){
            case 0:
                result = " 5%";
                break;
            case 1:
                result = " 10%";
                break;
            case 2:
                result = " 1%";
                break;
            case 3:
                result = " 2%";
                break;
            case 4:
                result = " 0.5%";
                break;
            case 5:
                result = " 0.25%";
                break;
            case 6:
                result = " 0.10%";
                break;
            case 7:
                result = " 0.05%";
                break;
        }
        return result;
    }

    private String getTemp(){
        String result = "";
        switch(tempBandPicker.getValue()){
            case 0:
                result = " 100ppm";
                break;
            case 1:
                result = " 50ppm";
                break;
            case 2:
                result = " 15ppm";
                break;
            case 3:
                result = " 25ppm";
                break;
        }
        return result;
    }

    private void doCalc(){
        int band1, band2, band3, multBand, band5, band6;
        double total = 0;
        String result = "";
        band1 = firstBandPicker.getValue();
        band2 = secondBandPicker.getValue();
        band3 = thirdBandPicker.getValue();
        multBand = multBandPicker.getValue();
        band5 = tolerBandPicker.getValue();
        band6 = tempBandPicker.getValue();
        switch(numBands){
            case 4:
                total = ((band1*10)+(band2))*(Math.pow(10, multBand-2));
                break;
            case 5:
                total = ((band1*100)+(band2*10)+band3)*(Math.pow(10, multBand-2));
                result = getTolerance();
                break;
            case 6:
                total = ((band1*100)+(band2*10)+band3)*(Math.pow(10, multBand-2));
                result = getTolerance()+getTemp();
                break;
        }
        resultText.setText(String.format("%.3g",total)+" ohms"+result);
    }

    protected void setColor(NumberPicker picker, int color){
        /*
         Why in all h*ll would you make something like the Paint a private rather than
         protected member or not set a method for changing the paint, if it was protected
         that'd make it easy to just extend the class, but whatever now we have to do some really
         nasty hackery to be able to dynamically change the colour of the text; but I guess in
         the long scheme of things this is kind of an edge case as normally you wouldn't do
         something like this.

         This link has a fuller version that handles more exceptions.
         http://stackoverflow.com/questions/22962075/change-the-text-color-of-numberpicker
        */
        for(int child = 0; child < picker.getChildCount(); child++){
            View view = picker.getChildAt(child);
            if(view instanceof EditText){
                try{
                    Field selectorWheelPaintField = picker.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(picker)).setColor(color);
                }
                catch(NoSuchFieldException e){
                    Log.w("setColor", e);
                }
                catch(IllegalAccessException e){
                    Log.w("setColor", e);
                }
            }
        }
    }
}
