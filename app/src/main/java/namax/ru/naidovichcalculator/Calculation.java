package namax.ru.naidovichcalculator;

import java.math.BigDecimal;

/**
 * Created by User on 016 16.08.16.
 */
public class Calculation {

    private BigDecimal iX ,iY ,iZ;
    private char cop1, cop2;
    private short iOperationCount = 0;
    private static int iC = 0;
    private int round;

    Calculation(int round) {
        this.round = round;
    }

    public BigDecimal calc(String s) throws Exception
    {

        char cThis;
        boolean isEnded = false;

        if (s.charAt(0)== '-')
        {
            iX = new BigDecimal(0);
            cop1 = '-';
            iOperationCount++;
        }
        for (int i = 0; i<s.length(); i++)              //цикл в котором творится магия от 0 до длинны файла-1
        {
            cThis = s.charAt(i);                        //считываем текущий символ
            if (Character.isDigit(cThis))               //если выхватили число
            {
                String sTemp = ReadNum(s,i);
                PlaceNum(new BigDecimal(sTemp));    //захавали число куда надо
                i = i + sTemp.length()-1;
            }
            else
            if (cThis == '+' || cThis == '-' || cThis == '*' || cThis == '/') PlaceOp(cThis);
            if (cThis == '(')
            {
                Calculation calcObj = new Calculation(round);
                BigDecimal solution = calcObj.calc(s.substring(i + 1));
                PlaceNum(solution);
                i=i+iC+1;
            }
            if (cThis == ')')
            {
                isEnded = true;
                iC = i;
            }
            if (iOperationCount == 3) SolveXYZ(iX,cop1,iY,cop2,iZ);
            if (isEnded) break;
        }
        if (iOperationCount==3) SolveXYZ(iX,cop1,iY,cop2,iZ);
        SolveXY(iX,cop1,iY);

        return iX;
    }

    private String ReadNum(String sString, int iIndex) //считывает число из строки sString начиная с индекса iIndex
    {
        char cSym;
        String sResult = "";
        cSym = sString.charAt(iIndex);
        for (int i = iIndex; i<sString.length() && (sString.charAt(i)=='.' || Character.isDigit(sString.charAt(i))); i++)
        {
            cSym = sString.charAt(i);
            sResult = sResult + cSym;
        }
        return sResult;
    }

    private void SolveXYZ(BigDecimal X, char op1, BigDecimal Y, char op2, BigDecimal Z)
    {
        short op1Priority, op2Priority;
        if (op1 == '*' || op1 == '/') op1Priority = 3;
        else op1Priority = 1;
        if (op2 == '*' || op2 == '/') op2Priority = 2;
        else op2Priority = 0;
        if (op1Priority > op2Priority)
        {

            switch (op1)
            {
                case '*': iX = X.multiply(Y); break;
                case '/': iX = X.divide(Y, round, BigDecimal.ROUND_HALF_UP); break;
                case '+': iX = X.add(Y); break;
                case '-': iX = X.subtract(Y); break;
            }
            cop1 = op2;
            iY = Z;
        }
        else
        {
            switch (op2)
            {
                case '*': iY = X.multiply(Y); break;
                case '/': iY = X.divide(Y, round, BigDecimal.ROUND_HALF_UP); break;
                case '+': iY = X.add(Y); break;
                case '-': iY = X.subtract(Y); break;
            }
        }
        iOperationCount = 2;
    }

    private void SolveXY(BigDecimal X, char op1, BigDecimal Y) throws Exception
    {
        switch (op1)
        {
            case '*': iX = X.multiply(Y); break;
            case '/':
                //проверка деления на 0
                iX = X.divide(Y, round, BigDecimal.ROUND_HALF_UP); break;
            case '+': iX = X.add(Y); break;
            case '-': iX = X.subtract(Y); break;
        }
        iOperationCount = 1;
    }

    private void PlaceNum(BigDecimal X)
    {
        if (iOperationCount == 0)
        {
            iX = X;
            iOperationCount++;
        }
        else
        {
            if (iOperationCount == 1)
            {
                iY = X;
                iOperationCount++;
            }
            else
            {
                iZ = X;
                iOperationCount++;
            }
        }
    }

    private void PlaceOp(char op)
    {
        if (iOperationCount == 1) cop1 = op;
        else cop2 = op;
    }

}
