package namax.ru.naidovichcalculator;

import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView expression, tip;
    EditText display;
    String finalExpression;
    Button one, two, three, four, five, six, seven, eight, nine, zero, add, sub, mul, div, dot, clear, equal, clearList, backspace, save, leftBrace, rightBrace;
    int numOfDig = 5;       //the number of digits after point
    private int leftBraceCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        six = (Button) findViewById(R.id.six);
        seven = (Button) findViewById(R.id.seven);
        eight = (Button) findViewById(R.id.eight);
        nine = (Button) findViewById(R.id.nine);
        zero = (Button) findViewById(R.id.zero);
        dot = (Button) findViewById(R.id.dot);
        add = (Button) findViewById(R.id.add);
        sub = (Button) findViewById(R.id.sub);
        mul = (Button) findViewById(R.id.mul);
        div = (Button) findViewById(R.id.div);
        leftBrace = (Button) findViewById(R.id.left_brace);
        rightBrace = (Button) findViewById(R.id.right_brace);

        clear = (Button) findViewById(R.id.clear);
        equal = (Button) findViewById(R.id.equal);
        clearList = (Button) findViewById(R.id.clearList);
        backspace = (Button) findViewById(R.id.backspace);
        save = (Button) findViewById(R.id.save);

        display = (EditText) findViewById(R.id.display);
        expression = (TextView) findViewById(R.id.expression);
        tip = (TextView) findViewById(R.id.tip);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        dot.setOnClickListener(this);
        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        mul.setOnClickListener(this);
        div.setOnClickListener(this);
        equal.setOnClickListener(this);
        backspace.setOnClickListener(this);
        leftBrace.setOnClickListener(this);
        rightBrace.setOnClickListener(this);
//        save.setOnClickListener(this);
//        clear.setOnClickListener(this);
//        clearList.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.one:
                checkAndAdd(one.getText());

            break;

            case R.id.two:
                checkAndAdd(two.getText());
                break;

            case R.id.three:
                checkAndAdd(three.getText());
                break;

            case R.id.four:
                checkAndAdd(four.getText());
                break;

            case R.id.five:
                checkAndAdd(five.getText());
                break;

            case R.id.six:
                checkAndAdd(six.getText());
                break;

            case R.id.seven:
                checkAndAdd(seven.getText());
                break;

            case R.id.eight:
                checkAndAdd(eight.getText());
                break;

            case R.id.nine:
                checkAndAdd(nine.getText());
                break;

            case R.id.zero:
                checkAndAdd(zero.getText());
                break;

            case R.id.dot:
                checkAndAdd(dot.getText());
                break;

            case R.id.add:
                checkAndAdd(add.getText());
                break;

            case R.id.sub:
                checkAndAdd(sub.getText());
                break;

            case R.id.mul:
                checkAndAdd(mul.getText());
                break;

            case R.id.div:
                checkAndAdd(div.getText());
                break;

            case R.id.left_brace:
//                leftBraceCount++;
                checkAndAdd(leftBrace.getText());
                break;

            case R.id.right_brace:
//                leftBraceCount--;
                checkAndAdd(rightBrace.getText());
                break;

            case R.id.backspace:
                deleteLastSymbol();
                break;

            case R.id.equal:
                Calculation calculation = new Calculation(numOfDig);
                String resultText = display.getText().toString();
               try {
                   display.setText(calculation.calc(resultText).toString());
               }
               catch (Exception e)
               {
                   display.setText(R.string.something_wrong);
               }

                break;
        }
    }

    private void checkAndAdd(CharSequence a) {
        String s = display.getText().toString();

        char c = a.charAt(0);

        Checker checker = new Checker(leftBraceCount, s, c);
        checker.run();
        display.setText(checker.getDisplay());
        leftBraceCount = checker.getLeftBraceCount();
    }

    private void deleteLastSymbol() {

        String s = display.getText().toString();
        if (!s.equals("")) {
            String lastSymbol = s.substring(s.length() - 1);
            if (lastSymbol.equals("("))
            {
                leftBraceCount--;
                s = s.substring(0, s.length() - 1);
            }
            else if (lastSymbol.equals(")"))
            {
                leftBraceCount++;
                s = s.substring(0, s.length() - 1);
            }
            else
            {
                s = s.substring(0, s.length() - 1);
            }
            display.setText(s);
        }
        if (s.equals("")) leftBraceCount = 0;
    }
}
