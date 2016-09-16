package namax.ru.naidovichcalculator;

import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    static int DELETE_BUTTON = 0;

    TextView expression, tip;
    EditText display;
    String finalExpression;
    Button one, two, three, four, five, six, seven, eight, nine, zero, add, sub, mul, div, dot, clear, equal, clearList, backspace, save, leftBrace, rightBrace, round;
    int numOfDig = 5;       //the number of digits after point
    private int leftBraceCount = 0;
    int id = 0;
    TableLayout scrollLay;

    PopupMenu popupMenu; //It is for decimal places

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
        round = (Button) findViewById(R.id.round);


        clear = (Button) findViewById(R.id.clear);
        equal = (Button) findViewById(R.id.equal);
        clearList = (Button) findViewById(R.id.clearList);
        backspace = (Button) findViewById(R.id.backspace);
        save = (Button) findViewById(R.id.save);

        display = (EditText) findViewById(R.id.display);
//        expression = (TextView) findViewById(R.id.expression);
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
        save.setOnClickListener(this);
        clear.setOnClickListener(this);
        clearList.setOnClickListener(this);
        round.setOnClickListener(this);


        scrollLay = (TableLayout) findViewById(R.id.tableLayInScroll);

        popupMenu = new PopupMenu(this, round);
        popupMenu.inflate(R.menu.menu_round);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    } // создаем главное меню

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clearList) {
            Toast.makeText(this, "clear List is pressed", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.action_round)
        {
            Toast.makeText(this, "round is pressed", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    } //выводим сообщения о событиях

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
                checkAndAdd(leftBrace.getText());
                break;

            case R.id.right_brace:
                checkAndAdd(rightBrace.getText());
                break;

            case R.id.backspace:
                deleteLastSymbol();
                break;

            case R.id.equal:
                String s = calculate();

                display.setText(s);
                break;

            case R.id.clear:
                display.setText("");
                leftBraceCount = 0;
                break;

            case R.id.save:
                createNewButton();
                break;

            case R.id.clearList:
                scrollLay.removeAllViews();
                id = 0;
                break;

            case R.id.round:
                setDecimalPlaces();
                break;
        }
    }

    private String calculate() {

        Calculation calculation = new Calculation(numOfDig);
        String resultText = display.getText().toString();

        String s = "";
        try {
            s = (calculation.calc(resultText).toString()); // вычисляем и складываем в s
        }
        catch (Exception e)
        {
            s = "error"; //если не получилось - сразу выводим на экран
        }

        return s;
    } //здесь происходит вычисление, на основе великого объекта Calculation by Tolya

    private void setDecimalPlaces() {

        showPopupMenu();

    } // выставляем колличество знаков после запятой. Нужно переместить в выпадающее меню

    private void createNewButton() {

        if  (display.getText().toString().equals("")) return;

        Button button = new Button(this);
        String s = calculate();
        String lastSymbol = s.substring(s.length() - 1);
        if (!lastSymbol.matches("[0-9]")) return;
        button.setText(s);
        button.setId(id);
//            button.setBackgroundResource(R.drawable.button); //глючит

        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(1,0,1,0);
        button.setTextColor(getResources().getColor(R.color.textColor));
        scrollLay.addView(button, 0, layoutParams);
        makeNewButtonClickable(button);
        registerForContextMenu(button);

        if (id == 0) {

            tip.setVisibility(View.GONE);       //скрываем надпись в спике кнопок
            id++;
        }
        else {

            id++;
        }

    } //тут создаем новую кнопку, есть проблемы с добавлением текстуры

    private void checkAndAdd(CharSequence a) {
        String s = display.getText().toString();

        char c = a.charAt(0);

        Checker checker = new Checker(leftBraceCount, s, c);
        checker.run();
        display.setText(checker.getDisplay());
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
            display.setText(s);
        }
        if (s.equals("")) leftBraceCount = 0;
    } //для бекспейса

    private void makeNewButtonClickable(final Button button) {



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = display.getText().toString();
                if (s.substring(s.length() - 1).matches("[0-9]")) {
                    s += "+" + button.getText().toString();
                } else
                {
                    s += button.getText().toString();
                }
                display.setText(s);

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
        return super.onContextItemSelected(item);
    }

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
                                numOfDig = 100;
                                Toast.makeText(getApplicationContext(),
                                        "You choose 100 decimal places",
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
}
