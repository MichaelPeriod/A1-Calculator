package com.example.a1_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import android.util.Log;
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log
import kotlin.math.sin
import kotlin.math.tan

/*
* Made by Michael Utz
* Finished 8/31
* Updated 9/7
* Worked Alone
* */

class MainActivity : AppCompatActivity(), View.OnClickListener {
    //Variable declaration
    var firstNum = "";
    var secondNum = "";
    var action = "";
    var numCurr = 1;
    var lastSolve = "0";
    val logTag = "INFO";

    override fun onCreate(savedInstanceState: Bundle?) {
        //initialize window
        super.onCreate(savedInstanceState)

        //Get and set orientation
        if(resources.configuration.orientation == 1){ //Vertical
            setContentView(R.layout.activity_main);
        } else { //Horizontal
            setContentView(R.layout.activity_main_h);

            //Initialize extra buttons
            findViewById<Button>(R.id.button_sin).setOnClickListener(this);
            findViewById<Button>(R.id.button_cos).setOnClickListener(this);
            findViewById<Button>(R.id.button_tan).setOnClickListener(this);
            findViewById<Button>(R.id.button_log).setOnClickListener(this);
            findViewById<Button>(R.id.button_ln).setOnClickListener(this);
        }

        //Initialize all buttons to call onClick
        findViewById<Button>(R.id.button_clear).setOnClickListener(this);
        findViewById<Button>(R.id.button_0).setOnClickListener(this);
        findViewById<Button>(R.id.button_1).setOnClickListener(this);
        findViewById<Button>(R.id.button_2).setOnClickListener(this);
        findViewById<Button>(R.id.button_3).setOnClickListener(this);
        findViewById<Button>(R.id.button_4).setOnClickListener(this);
        findViewById<Button>(R.id.button_5).setOnClickListener(this);
        findViewById<Button>(R.id.button_6).setOnClickListener(this);
        findViewById<Button>(R.id.button_7).setOnClickListener(this);
        findViewById<Button>(R.id.button_8).setOnClickListener(this);
        findViewById<Button>(R.id.button_9).setOnClickListener(this);
        findViewById<Button>(R.id.button_plus).setOnClickListener(this);
        findViewById<Button>(R.id.button_minus).setOnClickListener(this);
        findViewById<Button>(R.id.button_mul).setOnClickListener(this);
        findViewById<Button>(R.id.button_div).setOnClickListener(this);
        findViewById<Button>(R.id.button_per).setOnClickListener(this);
        findViewById<Button>(R.id.button_equals).setOnClickListener(this);
        findViewById<Button>(R.id.button_dot).setOnClickListener(this);
        findViewById<Button>(R.id.button_sign).setOnClickListener(this);

        //Load data before the pause if exists
        if(savedInstanceState != null){
            firstNum = savedInstanceState.getString("firstNum").toString();
            secondNum = savedInstanceState.getString("secondNum").toString();
            action = savedInstanceState.getString("action").toString();
            numCurr = savedInstanceState.getInt("numCurr");
            lastSolve = savedInstanceState.getString("lastSolve").toString();

            //Log for debug
            Log.i(logTag, "Restored $firstNum $secondNum $action $numCurr $lastSolve.");

            //Return display to last displayed
            if(firstNum.isEmpty() && lastSolve.isNotEmpty())
                setText(lastSolve);
            else
                updateText();
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //Save the state as is
        super.onSaveInstanceState(outState);

        outState.putString("firstNum", firstNum);
        outState.putString("secondNum", secondNum);
        outState.putString("action", action);
        outState.putInt("numCurr", numCurr);
        outState.putString("lastSolve", lastSolve);

        Log.v(logTag, "Saved");
    }

    override fun onClick(button: View?) {
        //Call appropriate function for each button
        when (button?.id) {
            R.id.button_clear -> {
                miscButtons("C");
            }
            R.id.button_sign -> {
                miscButtons("+/-");
            }
            R.id.button_per -> {
                miscButtons("%");
            }
            R.id.button_equals -> {
                miscButtons("=");
            }
            R.id.button_0 -> {
                numPressed("0");
            }
            R.id.button_1 -> {
                numPressed("1");
            }
            R.id.button_2 -> {
                numPressed("2");
            }
            R.id.button_3 -> {
                numPressed("3");
            }
            R.id.button_4 -> {
                numPressed("4");
            }
            R.id.button_5 -> {
                numPressed("5");
            }
            R.id.button_6 -> {
                numPressed("6");
            }
            R.id.button_7 -> {
                numPressed("7");
            }
            R.id.button_8 -> {
                numPressed("8");
            }
            R.id.button_9 -> {
                numPressed("9");
            }
            R.id.button_dot -> {
                numPressed(".");
            }
            R.id.button_div -> {
                symbolPressed("/");
            }
            R.id.button_mul -> {
                symbolPressed("*");
            }
            R.id.button_minus -> {
                symbolPressed("-");
            }
            R.id.button_plus -> {
                symbolPressed("+");
            }
            R.id.button_sin -> {
                trigButtons("sin");
            }
            R.id.button_cos -> {
                trigButtons("cos");
            }
            R.id.button_tan -> {
                trigButtons("tan");
            }
            R.id.button_log -> {
                trigButtons("log");
            }
            R.id.button_ln -> {
                trigButtons("ln");
            }
            else -> {
                setText("NAN");
                //Log button error
                Log.v(logTag, "Button Pressed:" + "Button isn't registered");
            }
        }
    }

    /*Display functions*/
    //Set the text to current position
    fun updateText(){
        updateText(numCurr);
    }

    //Set text to specific position
    fun updateText(pos: Int){
        if(pos == 1){
            setText(firstNum);
        } else {
            setText(secondNum);
        }
    }

    //Find textbox and set to string
    fun setText(s: String){
        val txtBox = findViewById<TextView>(R.id.txt_res);
        txtBox.text = s;
    }

    /*Utility functions*/
    //Reset all values (Not including last solve)
    fun clearValues(){
        firstNum = "";
        secondNum = "";
        action = "";
        numCurr = 1;
    }

    //Check if a number is valid
    fun isValidNum(s: String) : Boolean {
        //Don't allow empty strings
        if (s.isEmpty()) return false;

        //Ignore negative and period symbols
        var testString = s;
        testString = testString.replace("-", "")
        testString = testString.replace(".", "");

        return testString.isDigitsOnly();
    }

    //Calculate the solution to the problem
    fun solveProblem(): String {
        //Ensure number is valid
        if(!isValidNum(firstNum) || !isValidNum(secondNum)) return "NAN";

        //Switch for the symbol
        var ans = 0f;
        when(action){
            "+" -> { //Addition
                ans = firstNum.toFloat() + secondNum.toFloat();
            }
            "-" -> { //Subtraction
                ans = firstNum.toFloat() - secondNum.toFloat();
            }
            "/" -> { //Division
                ans = firstNum.toFloat() / secondNum.toFloat();
            }
            "*" -> { //Multiplication
                ans = firstNum.toFloat() * secondNum.toFloat();
            }
            else -> { //Invalid symbol
                return "NAN";
            }
        }

        //Return answer
        return ans.toString();
    }

    /*Input action management*/
    //Handle numbers being pressed
    fun numPressed(num: String) {
        //Log button pressed
        Log.v(logTag, "Button Pressed:$num");

        //Add to appropriate number and update the text
        if(numCurr == 1)
            firstNum += num;
        else
            secondNum += num;

        updateText();
    }

    //Handle symbols being pressed
    fun symbolPressed(symbol: String){
        //Log button pressed
        Log.v(logTag, "Button Pressed:$symbol");

        //If first number is empty and last solve isn't use the last number
        if(firstNum.isEmpty()){
            if(lastSolve.isNotEmpty()){
                firstNum = lastSolve;
            }
            else { //If no numbers have been pressed don't allow symbols
                return;
            }
        }

        //Solve previous problem if pressed follow up symbols
        if(numCurr >= 2){
            lastSolve = solveProblem();
            clearValues();
            firstNum = lastSolve;
            setText(lastSolve);
        }

        //Move pointer to new number
        action = symbol;
        numCurr++;
    }

    //Handle all trig buttons in part 2
    fun trigButtons(function : String) {
        //Log button pressed
        Log.v(logTag, "Button Pressed: $function");

        //If first number is empty and last solve isn't use the last number
        if(firstNum.isEmpty()){
            if(lastSolve.isNotEmpty()){
                firstNum = lastSolve;
            }
            else { //If no numbers have been pressed don't allow function
                return;
            }
        }

        //NOTE: All trig functions work in radians
        //Select and apply appropriate function
        when (function) {
            "sin" -> {
                if(numCurr == 1){
                    firstNum = sin(firstNum.toDouble()).toString();
                    Log.v(logTag, firstNum);
                } else {
                    secondNum = sin(secondNum.toDouble()).toString();
                }
            }
            "cos" -> {
                if(numCurr == 1){
                    firstNum = cos(firstNum.toDouble()).toString();
                } else {
                    secondNum = cos(secondNum.toDouble()).toString();
                }
            }
            "tan" -> {
                if(numCurr == 1){
                    firstNum = tan(firstNum.toDouble()).toString();
                } else {
                    secondNum = tan(secondNum.toDouble()).toString();
                }
            }
            "log" -> {
                if(numCurr == 1){
                    firstNum = log(firstNum.toDouble(), 10.0).toString();
                } else {
                    secondNum = log(secondNum.toDouble(), 10.0).toString();
                }
            }
            "ln" -> {
                if(numCurr == 1){
                    firstNum = ln(firstNum.toDouble()).toString();
                } else {
                    secondNum = ln(secondNum.toDouble()).toString();
                }
            }
        }

        //Show text once complete
        updateText();
    }

    //All other buttons
    fun miscButtons(action: String){
        //Log button pressed
        Log.v(logTag, "Button Pressed:$action");

        when(action){
            "%" -> { //Divide appropriate number by 100
                if(numCurr == 1){
                    firstNum = (firstNum.toFloat() / 100f).toString();
                } else {
                    secondNum = (secondNum.toFloat() / 100f).toString();
                }
                updateText();
            }
            "+/-" -> { //Add or remove negative sign
                if(numCurr == 1){
                    firstNum = if(firstNum.isEmpty()){
                        "-";
                    } else if(firstNum[0].equals("-")){
                        firstNum.substring(1);
                    } else {
                        "-$firstNum";
                    }
                } else {
                    secondNum = if(secondNum.isEmpty()){
                        "-";
                    } else if(secondNum[0].equals("-")){
                        secondNum.substring(1);
                    } else {
                        "-$secondNum";
                    }
                }
                updateText();
            }
            "=" -> { //Calculate equation
                lastSolve = solveProblem();
                setText(lastSolve);
                clearValues();
            }
            "C" -> { //Clear calculator
                clearValues();
                lastSolve = "";
                updateText();
            }
        }
    }
}