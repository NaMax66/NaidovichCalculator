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

    public void run()
    {
        if (display.matches("(?i).*[a-z].*")) // I find it RE in http://www.cyberforum.ru/java-j2se/thread1003818.html
        {
            display = "";
            leftBraceCount = 0;
        }
        String symbolString = "";
              symbolString += symbol;
        if (display.length() == 0 && symbolString.matches("[0-9]"))
        {
            display += symbol;
            return;
        }
        if (symbol == '(' )
        {
            checkAndAddLeftBrace(symbol);
        }
        else if (symbol == ')' )
        {
            checkAndAddRightBrace(symbol);
        }
        else if (symbolString.matches("[0-9]"))
            display += symbol;
        else if (symbolString.equals("."))
        {
            pointMethod();
        }
        else {
            checkIfContainBrace();
            checkAndAddSymbol(symbol);
        }
    }

    private void pointMethod() {

        if (display.length() == 0)
        {
            display += "0.";
            return;
        }

        String lastSymbol = display.substring(display.length() - 1);
        if (lastSymbol.contains(".")) return;
        else if (lastSymbol.matches("[0-9]"))
        {
            if (display.contains("*") || display.contains("/") || display.contains("-") || display.contains("+"))
            {
                int lastMultiplyIndex = 0;
                int lastDivIndex = 0;
                int lastSubIndex = 0;
                int lastAddIndex = 0;

                if (display.contains("*"))
                {
                    lastMultiplyIndex = display.lastIndexOf("*");
                }
                if (display.contains("/"))
                {
                    lastDivIndex = display.lastIndexOf("/");
                }
                if (display.contains("-"))
                {
                    lastSubIndex = display.lastIndexOf("-");
                }
                if (display.contains("+"))
                {
                    lastAddIndex = display.lastIndexOf("+");
                }

                ArrayList<Integer> list = new ArrayList<>();
                list.add(lastMultiplyIndex);
                list.add(lastDivIndex);
                list.add(lastSubIndex);
                list.add(lastAddIndex);

                int lastOperatorIndex = 0;

                for (Integer i : list)
                {
                    if (i >= lastOperatorIndex) lastOperatorIndex = i;
                }

                String lastNumber = display.substring(lastOperatorIndex);
                if (lastNumber.contains(".")) return;
                else display += ".";
            }
            else if (display.contains(".")) return;
            else display += ".";

        }
        else return;

    }


    private void checkAndAddLeftBrace(char c) {

        if (display.length() == 0)
        {
            display += c;
            leftBraceCount++;
        }

        else
        {
            String lastSymbol = display.substring(display.length() - 1);
            if (lastSymbol.equals(")") || lastSymbol.matches("[0-9]"))
            {
                display += "*";
                display += c;
                leftBraceCount++;
            }
            else if (lastSymbol.equals("."))
            {
                display += "0*";
                display += c;
                leftBraceCount++;
            }
            else
            {
                display += c;
                leftBraceCount++;
            }

        }

    }

    private void checkAndAddRightBrace(char c) {         // )

        if (leftBraceCount == 0) return;
        String lastSymbol = display.substring(display.length() - 1);
        if (lastSymbol.equals("(")) return;
        if (lastSymbol.equals("."))
        {
            deleteLastSymbol();
            display += c;
            leftBraceCount--;
        }
        else
        {
            display += c;
            leftBraceCount--;
        }

    }

    private void checkIfContainBrace() {

        if (display.length() <= 1) return;
        String lastSymbols = display.substring(display.length() - 2);
        if (lastSymbols.equals("*("))
        {
            deleteLastSymbol();
            deleteLastSymbol();
        }

    }

    private void checkAndAddSymbol(char c){

        if (display.length() == 0) return;

        if(
                (display.charAt(display.length() - 1) == '+') ||
                        (display.charAt(display.length() - 1) == '*') ||
                        (display.charAt(display.length() - 1) == '/') ||
                        (display.charAt(display.length() - 1) == '-') ||
                        (display.charAt(display.length() - 1) == '.') ||
                        (display.charAt(display.length() - 1) == '(')
                )
        {
            deleteLastSymbol();
            display = display + c;
        }

        else display = display + c;

    }

    private void deleteLastSymbol() {

        if(!display.equals(""))
            display = display.substring(0, display.length() - 1);

    }


}
