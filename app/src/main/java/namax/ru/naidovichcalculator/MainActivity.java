package namax.ru.naidovichcalculator;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    String LOG_TAG = "SAVE_CALC";

    HorizontalScrollView DispHsv, ResultHsv;
    String buttonsValues = "";
    static int DELETE_BUTTON = 0;
    static int SMALL_LENGTH = 15;
    static int MIDDLE_LENGTH = 22;
    boolean effects = true;
    Vibrator vibrator;

    SharedPreferences sPref;

    TextView resultValue, tip, display;
    Button one, two, three, four, five, six, seven, eight, nine, zero, add, sub, mul, div, dot, clear, save, leftBrace,
            rightBrace, equal, root, square, percentage;
    RelativeLayout backspace;
    int numOfDig = 5;//the number of digits after point

    private int leftBraceCount = 0;
    int id = 0;
    LinearLayout scrollLay;

    PopupMenu popupMenu, displayPopupMenu; //It is for decimal places

    float textSize;

    int buttonsHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE); //находим вибратор для кнопочек

        setContentView(R.layout.activity_main);

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
        root = (Button) findViewById(R.id.root);
        square = (Button) findViewById(R.id.square);
        percentage = (Button) findViewById(R.id.percentage);

        clear = (Button) findViewById(R.id.clear);
        backspace = (RelativeLayout) findViewById(R.id.backspace);
        save = (Button) findViewById(R.id.save);
        equal = (Button) findViewById(R.id.equal);

        display = (TextView) findViewById(R.id.display);
        resultValue = (TextView) findViewById(R.id.resultValue);
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
        backspace.setOnClickListener(this);
        leftBrace.setOnClickListener(this);
        rightBrace.setOnClickListener(this);
        save.setOnClickListener(this);
        clear.setOnClickListener(this);
        equal.setOnClickListener(this);
        root.setOnClickListener(this);
        square.setOnClickListener(this);
        percentage.setOnClickListener(this);

        scrollLay = (LinearLayout) findViewById(R.id.scrolling_layout);

        DispHsv = (HorizontalScrollView) findViewById(R.id.disp_hor_scr_v);
        ResultHsv = (HorizontalScrollView) findViewById(R.id.top_scr_hor_sc_v);

        popupMenu = new PopupMenu(this, save);
        popupMenu.inflate(R.menu.menu_round);

        displayPopupMenu = new PopupMenu(this, DispHsv);
        displayPopupMenu.inflate(R.menu.disp_pop_men);

        textSize = pixelsToSp(display.getTextSize());

        resultValue.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String result = resultValue.getText().toString().substring(1); // удаляем знаки = и пробел
                setClipboard(getApplicationContext(), result);
                Toast.makeText(getApplicationContext(), "copied to the clipboard", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        display.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                displayPopupMenu
                        .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {

                                    case R.id.copy_to_clipboard:
                                        setClipboard(getApplicationContext(), display.getText().toString());
                                        Toast.makeText(getApplicationContext(), "copied to the clipboard", Toast.LENGTH_SHORT).show();
                                        return false;

                                    case R.id.copy_from_clipboard:

                                        display.setText(getClipboard(getApplicationContext()));
                                        return false;
                                }
                                return false;
                            }
                        });
                displayPopupMenu.show();
                return false;
            }
        });

        CountDownTimer timer = new CountDownTimer(100, 100) { //таймер нужен для получения высоты кнопки, которая не успевает появиться без него
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {

                    buttonsHeight = save.getHeight();
                if(buttonsHeight != 0)
                {
                    loadState(); //загружаем предыдущее состояние после того как получаем высоту кнопки
                }
                else start();

            }
        };

        timer.start();
    }

    public float pixelsToSp(float px) {
        float scaledDensity = this.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    } // взял со StackOverFlow, нужно для получения размера текста, чтобы потом его уменьшать

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    } // создаем главное меню

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_clearList:
                scrollLay.removeAllViews();
                id = 0;
                scrollLay.addView(tip);
                break;

            case R.id.action_round:
                setDecimalPlaces();
                break;

            case R.id.b_effects:
                if (effects)
                {
                    effects = false;
                    Toast.makeText(this, R.string.b_effects_off, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    effects = true;
                    Toast.makeText(this, R.string.b_effects_on, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.mail:
                sendEmailToMe();
                break;

            case R.id.about:

                startActivity(new Intent(this, About.class));

                break;

            case R.id.quit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    } //обработчик нажатий на пункты меню

    private void sendEmailToMe() {
        Intent i = new Intent((Intent.ACTION_SEND));
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"max.naidovich@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "SAVE Calculator");
        try
        {
            startActivity(Intent.createChooser(i, "send mail"));
        }
        catch (ActivityNotFoundException ex)
        {
            Toast.makeText(this, R.string.mailCl_not_found, Toast.LENGTH_SHORT).show();
        }
    } //отправляем мне пиьсмо, не вставляется тема
    @Override
    public void onClick(View v) {
        playButtonsEffects(v);
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
                checkAndAdd(leftBrace.getText());
                break;

            case R.id.right_brace:
                checkAndAdd(rightBrace.getText());
                break;

            case R.id.backspace:
                deleteLastSymbol();
                break;

            case R.id.clear:
                setTextToTextView(display, "");
                leftBraceCount = 0;
                break;

            case R.id.save:
                String resV = resultValue.getText().toString();
                if (resV.length() >= 2) resV = resV.substring(2); // убираем = и пробел
                if (resV.equals(""))
                {
                    Toast.makeText(getApplicationContext(),
                            "Make some calculation and try again",
                            Toast.LENGTH_LONG).show();
                    break;
                }
                createNewButton(resV);
                break;

            case R.id.equal:
               String s = calculate(display.getText().toString());
                setTextToTextView(display, s);
               leftBraceCount = 0;
               break;

            case R.id.root:
                findAndSetRoot();
                break;

            case R.id.square:
                findAndSetSquare();
                break;

            case R.id.percentage:
                findAndSetPercentage();
                break;
        }






        String resultText = "= " + calculate(display.getText().toString());
        setTextToTextView(resultValue, resultText);

        ResultHsv.post(new Runnable() {
            public void run() {
                ResultHsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });

        DispHsv.post(new Runnable() {
            public void run() {
                DispHsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });

    } //обработчик нажатий постоянных кнопок, в конце реализована автопрокрутка HorizontalScrollView

    private void playButtonsEffects(View v) {
        if (effects)
        {
            if (vibrator.hasVibrator())
            {
                vibrator.vibrate(30);
            }

        }
    }

    private void findAndSetSquare() {
        String dispValue = calculate(display.getText().toString());
        String resValue;
        try {
            BigDecimal n = new BigDecimal(dispValue);
            n = n.multiply(n);
            n = n.setScale(numOfDig, RoundingMode.HALF_UP);
            resValue = n.toString();
            setTextToTextView(display, resValue);

        }catch (Exception e)
        {

        }

    }

    private void findAndSetRoot() {
        String dispValue = calculate(display.getText().toString());
        String resValue;
        try {
            BigDecimal n = new BigDecimal(dispValue);
            n = new BigDecimal(Math.sqrt(n.doubleValue()));
            n = n.setScale(numOfDig, RoundingMode.HALF_UP);
            resValue = n.toString();
            setTextToTextView(display, resValue);

        }catch (Exception e)
        {

        }
    }

    private void findAndSetPercentage() {

        String expression = display.getText().toString();
        if (expression.length() == 0) return;
        String lastSymbol = expression.substring(expression.length() - 1);
        if (!lastSymbol.matches("[0-9]")) return; //если не число - ничего не делаем
        String lastNumber = "";
        String resultExpression = "";
        BigDecimal lastNumberBD;
        BigDecimal expressionValueAfterDel;

        char[] chars = expression.toCharArray();

        for (int i = chars.length-1; i >= 0; i--) {

            String c = String.valueOf(chars[i]);

            if (c.matches("[0-9]") || c.equals(".")) lastNumber += c;
            else break;
        } //теперь у нас должно быть последнее число

            lastNumber = new StringBuilder(lastNumber).reverse().toString();

            lastNumberBD = new BigDecimal(lastNumber);

        if (expression.length() == lastNumber.length()) // если длины равны, значит других чисел на экране нет
        {
            try {
                BigDecimal n = new BigDecimal(lastNumber);
                resultExpression = n.divide(new BigDecimal(100), numOfDig, RoundingMode.HALF_UP).toString();
            }
            catch (Exception e)
            {

            }

        }
        else
        {
            try {

                int index = expression.length() - lastNumber.length()-1; //

                expression = expression.substring(0, index); // обрезаем проценты
                char[] expChars = expression.toCharArray();

                for (int i= index; i >= 0; i--) {
                    String c = String.valueOf(expChars[i]);
                    if (!c.matches("[0-9]") || !c.equals(".")) expression = expression.substring(0, expression.length() - 1);
                    else break;
                } // здесь получили выражение без последнего числа и прочих символов в конце
            }
            catch (Exception e)
            {

            }
            if (expression.equals("")) return;
            else
            {
                String s = calculate(expression);
                if (s.equals("")) return; // процент не считается если выражение на удалось вычислить (возоможно из-за символса скобки в конце)
                int newBraceIndex = expression.length();
                expressionValueAfterDel = new BigDecimal(s);

                String disp = display.getText().toString();
                disp = disp.substring(0, disp.length() - lastNumber.length());

                String operator = disp.substring(disp.length() - 1);
                String valueInPercent;

                if  (operator.equals("*") || operator.equals("/")){
                    valueInPercent = lastNumberBD.divide(new BigDecimal(100), numOfDig, BigDecimal.ROUND_UP).toString();
                }
                else valueInPercent = expressionValueAfterDel.multiply(lastNumberBD).
                        divide(new BigDecimal(100), numOfDig, BigDecimal.ROUND_UP).toString(); //умножаем на значение в конце и делим на 100



                resultExpression = "(" + disp.substring(0, newBraceIndex) + ")" + disp.substring(newBraceIndex);
                resultExpression += valueInPercent;
            }

        }//нужно получить выражение без последнего числа и цифр на конце

        setTextToTextView(display , removeExtraZeros(resultExpression));
    }

    private void setTextToTextView(TextView v, String s) { // нужно чтобы текст уменьшался при достижении границ экрана

        v.setText(s);

        if (s.length() >= MIDDLE_LENGTH)
        {
            v.setTextSize(textSize / 1.5f);
            return;
        }

        if (s.length() < MIDDLE_LENGTH && s.length() >= SMALL_LENGTH)
        {
            v.setTextSize(textSize / 1.2f);
            return;
        }
        if (s.length() < SMALL_LENGTH)
        {
            v.setTextSize(textSize);
            return;
        }
    } //расчитывает на сколько нужно уменьшить текст и выводит его

    private String calculate(String expression) {

        Calculation calculation = new Calculation(numOfDig);


        if (leftBraceCount != 0) //чтобы вычисления проходили корректно объекту типа Calculation нужно одинаковое колличество левых и правых скобок
        {
            for (int i = 0; i < leftBraceCount; i++)
            {
                expression += ")";
            }
        }

        String s = "";
        try {
            s = (calculation.getResult(expression).toString()); // вычисляем и складываем в s
        }
        catch (Exception e)
        {
            //если не получилось - ничего не делаем
        }

        return removeExtraZeros(s);
    } //здесь происходит вычисление, на основе великого объекта Calculation by Tolya

    private String removeExtraZeros(String s) {

        if (!s.equals(""))
        {
            String lastSymbol = s.substring(s.length() - 1);
            if (s.contains("."))
            {
                if (lastSymbol.equals("0")) {

                    do {
                        s = s.substring(0, s.length() - 1);
                        lastSymbol = s.substring(s.length() - 1);
                    } while (lastSymbol.equals("0"));
                }
            }

            if (lastSymbol.equals(".")) s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    private void setDecimalPlaces() {

        showPopupMenu();

    } // выставляем колличество знаков после запятой.

    private void checkAndAdd(CharSequence a) {
        String s = display.getText().toString();
        char c = a.charAt(0);
        Checker checker = new Checker(leftBraceCount, s, c);
        checker.check();
        setTextToTextView(display, checker.getDisplay());
        leftBraceCount = checker.getLeftBraceCount();

    } //создает объект Checker и проверяем можем ли мы вставить тот или иной символ

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
            setTextToTextView(display, s);
        }
        if (s.equals("")) leftBraceCount = 0;
    } //для бекспейса

    private void createNewButton(String s) {

        Button button = new Button(this);

        button.setBackgroundResource(R.drawable.button);
        button.setText(s);
        button.setId(id);
//        int height = save.getHeight();

        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, buttonsHeight); //делаем высоту новой кнопки такой же как остальные
        layoutParams.setMargins(1,0,1,0);
        button.setTextColor(getResources().getColor(R.color.mainColor));
        scrollLay.addView(button, 0, layoutParams);
        makeNewButtonClickable(button);
        registerForContextMenu(button);

        if (scrollLay.findViewById(R.id.tip) != null) {

           scrollLay.removeView(tip);       //удаляем надпись в спике кнопок
            id++;
        }
        else {

            id++;
        }

    }  // создаем новую кнопку

    private void makeNewButtonClickable(final Button button) {



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = display.getText().toString();
                String buttonsText = button.getText().toString();

                if (s.length() == 0) {
                    s += buttonsText;
                    setTextToTextView(display, s);
                    return;
                }

                String lastSymbol = s.substring(s.length() - 1);

                if (lastSymbol.equals("."))
                {
                    deleteLastSymbol();
                    s += '+' + buttonsText;
                }
                else if (lastSymbol.equals(")"))
                {
                    s += "*" + buttonsText;
                }
                else if (lastSymbol.matches("[0-9]"))
                {
                    s += "+" + buttonsText;
                }
                else s += buttonsText;

                playButtonsEffects(v);

                setTextToTextView(display, s);
                setTextToTextView(resultValue, "= " + calculate(display.getText().toString()));
                ResultHsv.post(new Runnable() {
                    public void run() {
                        ResultHsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    }
                });

                DispHsv.post(new Runnable() {
                    public void run() {
                        DispHsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    }
                });

            }
        });

    } // здесь получаем значения с новой кнопочки

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        DELETE_BUTTON = v.getId();
        menu.add(Menu.NONE, DELETE_BUTTON, Menu.NONE, "Delete");
    } //создаем меню для удаления одной кнопки

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == DELETE_BUTTON)
        {
            scrollLay.removeView(scrollLay.findViewById(DELETE_BUTTON));
        }

        if(scrollLay.getChildCount() == 0)
        {
            scrollLay.addView(tip);
        }

        return super.onContextItemSelected(item);
    } //здесь удаляем элемент из списка кнопок

    private void showPopupMenu() {


        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.menu0decP:
                                item.setChecked(!item.isChecked());
                                numOfDig = 0;
                                Toast.makeText(getApplicationContext(),
                                        "You choose zero decimal places",
                                        Toast.LENGTH_SHORT).show();

                                return true;

                            case R.id.menu1decP:
                                item.setChecked(!item.isChecked());
                                numOfDig = 1;
                                Toast.makeText(getApplicationContext(),
                                        "You choose one decimal places",
                                        Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.menu2decP:
                                item.setChecked(!item.isChecked());
                                numOfDig = 2;
                                Toast.makeText(getApplicationContext(),
                                        "You choose two decimal places",
                                        Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.menu5decP:
                                item.setChecked(!item.isChecked());
                                numOfDig = 5;
                                Toast.makeText(getApplicationContext(),
                                        "You five two decimal places",
                                        Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.menuUnlimdecP:
                                item.setChecked(!item.isChecked());
                                numOfDig = 30;
                                Toast.makeText(getApplicationContext(),
                                        "You choose 30 decimal places",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                ;
                        }
                        return false;
                    }
                });
        popupMenu.show();
    }

    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    } //копируем в буфер

    private String getClipboard(Context context) {

            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            return clipboard.getText().toString();
    } // берем из буфера

    private void saveState(){

        Button v;
        buttonsValues = "";
        String s = "";
        if (id != 0){ //если id = 0 значит кнопок в списке небыло
            for (int i = 0; i <= id; i++) { // бывает что кнопки нет, а id есть
                try {
                    v = (Button) scrollLay.findViewById(i);
                    s = v.getText().toString();
                    buttonsValues += s + " ";
                }
                catch (Exception e){}
            }
        }
        String dispVal = display.getText().toString();
        String resVal = resultValue.getText().toString();

        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("buttons", buttonsValues);
        ed.putString("display", dispVal);
        ed.putString("result", resVal);
        ed.putInt("leftBraceCount", leftBraceCount);
        ed.apply();
    }

    private void loadState(){

        sPref = getPreferences(MODE_PRIVATE);
        buttonsValues = sPref.getString("buttons", "");
        if (!buttonsValues.equals("")){
            String [] bValues = buttonsValues.split(" ");
            for (String s : bValues)
            {
                createNewButton(s);
            }
        }
        String dispVal = sPref.getString("display", "");
        String resVal = sPref.getString("result", "= ");
        leftBraceCount = sPref.getInt("leftBraceCount", 0);
        display.setText(dispVal);
        resultValue.setText(resVal);
    }

    @Override
    protected void onDestroy() {
        saveState();
        super.onDestroy();
    }
}
             