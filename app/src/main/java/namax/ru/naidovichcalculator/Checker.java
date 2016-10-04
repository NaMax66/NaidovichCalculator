package namax.ru.naidovichcalculator;

import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by User on 020 20.08.16.
 */
public class Checker {

    private int leftBraceCount = 0;
    private String display;
    char symbol;
    String lastSymbol;

    public Checker(int leftBraceCount, String display, char symbol) {
        this.leftBraceCount = leftBraceCount;
        this.display = display;
        this.symbol = symbol;
    }

    public int getLeftBraceCount() {
        return leftBraceCount;
    }

    public String getDisplay() {
        return display;
    }

    public void check() //всего 9 возможных ситуций, здесь описано - что произойдет, если перед текущим символом стоит тот или иной символ, включая  его самого
    {
        if (display.length() == 0) lastSymbol = "";
        else
        {
            lastSymbol = display.substring(display.length() - 1);  //если на дисплее ничего нет, пусть последним символом будет пустым, если есть - получаем последний символ
            isLastSymbolNumber();
        }

        switch (symbol)
        {
            case '+':
                checkAndAddPlus();
                break;

            case '-':
                checkAndAddMinus();
                break;

            case '*':
                checkAndAddMultiply();
                break;

            case '/':
                checkAndAddDivision();
                break;

            case '.':
                checkAndAddPoint();
                break;

            case '(':
                checkAndAddLeftBrace();
                break;

            case  ')':
                checkAndAddRightBrace();
                break;
        }

        String s = "" + symbol;
        if (s.matches("[0-9]")) //если символ число и стоит правая скобка - добавляем знак умножения
        {
            if (lastSymbol.equals(")")) display += "*" + symbol;
            else display += symbol;
        }

    }

    private void checkAndAddPoint() {

        switch (lastSymbol)
        {
            case "":
                display += "0.";
                break;

            case "number":

                String s = getLastNumber();
                if (!s.contains("."))
                    display += ".";
                break;
        }

    }

    private void checkAndAddDivision() {

        switch (lastSymbol)
        {
            case ".":
                deleteLastSymbol();
                display += '/';
                break;

            case "number":
                display += '/';
                break;

            case ")":
                display += '/';
                break;
        }

    }

    private void checkAndAddMultiply() {
        switch (lastSymbol)
        {
            case ".":
                deleteLastSymbol();
                display += '*';
                break;

            case "number":
                display += '*';
                break;

            case ")":
                display += '*';
                break;
        }
    }

    private void checkAndAddMinus() {

        switch (lastSymbol)
        {
            case "":
                display += '-';
                break;

            case "+":
                deleteLastSymbol();
                display += '-';
                break;

            case "-":
                deleteLastSymbol();
                display += '+';
                break;

            case "*":
                display += "(-";
                leftBraceCount++;
                break;

            case "/":
                display += "(-";
                leftBraceCount++;
                break;

            case ".":
                deleteLastSymbol();
                display += '-';
                break;

            case "number":
                display += '-';
                break;

            case "(":
                display += '-';
                break;

            case ")":
                display += '-';
                break;
        }
    }

    private void isLastSymbolNumber() {
        if (lastSymbol.matches("[0-9]"))

            lastSymbol = "number";
    }

    private void checkAndAddPlus() {

        switch (lastSymbol)
        {
            case "":
                break;

            case "+":
                break;

            case "-":
                deleteLastSymbol();
                display += '+';
                break;

            case "*":
                break;

            case "/":
                break;

            case ".":
                deleteLastSymbol();
                display += '+';
                break;

            case "number":
                display += '+';
                break;

            case "(":
                break;

            case ")":
                display += '+';
                break;

        }

    }

    private void checkAndAddLeftBrace() {

        switch (lastSymbol)
        {
            case "":
                display += '(';
                break;

            case "+":
                display += '(';
                break;

            case "-":
                display += '(';
                break;

            case "*":
                display += "(";
                break;

            case "/":
                display += "(";
                break;

            case ".":
                deleteLastSymbol();
                display += "*(";
                break;

            case "number":
                display += "*(";
                break;

            case "(":
                display += '(';
                break;

            case ")":
                display += "*(";
                break;

        }

        leftBraceCount++;

    }

    private void checkAndAddRightBrace() {         // )

        if (leftBraceCount == 0)
        {
            return;
        }

        switch (lastSymbol)
        {
            case ".":
                deleteLastSymbol();
                display += ')';
                leftBraceCount--;
                break;

            case "number":
                display += ')';
                leftBraceCount--;
                break;

            case ")":
                display += ')';
                leftBraceCount--;
                break;
        }


    }

    private void deleteLastSymbol() {

        if(!display.equals(""))
            display = display.substring(0, display.length() - 1);

    }

    private String getLastNumber()
    {
        String lastNumber = "";

            if (display.contains("*") || display.contains("/") || display.contains("-") || display.contains("+")) {
                int lastMultiplyIndex = 0;
                int lastDivIndex = 0;
                int lastSubIndex = 0;
                int lastAddIndex = 0;

                if (display.contains("*")) {
                    lastMultiplyIndex = display.lastIndexOf("*");
                }
                if (display.contains("/")) {
                    lastDivIndex = display.lastIndexOf("/");
                }
                if (display.contains("-")) {
                    lastSubIndex = display.lastIndexOf("-");
                }
                if (display.contains("+")) {
                    lastAddIndex = display.lastIndexOf("+");
                }

                ArrayList<Integer> list = new ArrayList<>();
                list.add(lastMultiplyIndex);
                list.add(lastDivIndex);
                list.add(lastSubIndex);
                list.add(lastAddIndex);

                int lastOperatorIndex = 0;

                for (Integer i : list) {
                    if (i >= lastOperatorIndex) lastOperatorIndex = i;
                }
                lastNumber = display.substring(lastOperatorIndex);
            }

      return lastNumber;
    }
}
