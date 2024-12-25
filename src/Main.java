import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Для выхода введите exit. Ввод: ");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while (!(input = bufferedReader.readLine()).equals("exit")) {
            try {
                System.out.println(calc(input));
            } catch (UnsupportedOperationException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public static String calc(String input) {
        int operatorIndex = findOperatorIndex(input);
        if (operatorIndex < 0) throw new UnsupportedOperationException("Строка не является математической операцией");


        String[] operands = input.split("[+\\-*/]");
        if (operands.length != 2) {
            throw new UnsupportedOperationException("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }
        String first = operands[0].trim();
        String second = operands[1].trim();
        boolean isRoman = isRoman(first) && isRoman(second);
        validateOperands(first, second, isRoman);
        int number1 = isRoman ? romanToArabic(first) : Integer.parseInt(first);
        int number2 = isRoman ? romanToArabic(second) : Integer.parseInt(second);
        int result = performOperation(number1, number2, input.charAt(operatorIndex));

        if (result < 1 && isRoman) {
            throw new UnsupportedOperationException("В римской системе нет отрицательных чисел");
        }

        return isRoman ? arabicToRoman(result) : String.valueOf(result);
    }
    private static int findOperatorIndex(String input) {
        for (char operator : new char[]{'+', '-', '*', '/'}) {
            int index = input.indexOf(operator);
            if (index > 0) return index;
        }
        return -1;
    }
    private static void validateOperands(String first, String second, boolean isRoman) {
        if ((isRoman && (!isRoman(first) || !isRoman(second))) ||
                (!isRoman && (isRoman(first) || isRoman(second)))) {
            throw new UnsupportedOperationException("Используются одновременно разные системы счисления");
        }

        if (!isRoman) {
            int num1 = Integer.parseInt(first);
            int num2 = Integer.parseInt(second);
            if (num1 > 10 || num2 > 10) {
                throw new UnsupportedOperationException("Числа не должны быть больше 10");
            }
        }
    }
    private static int performOperation(int number1, int number2, char operator) {
        return switch (operator) {
            case '+' -> number1 + number2;
            case '-' -> number1 - number2;
            case '*' -> number1 * number2;
            case '/' -> {
                if (number2 == 0) throw new UnsupportedOperationException("Деление на ноль");
                yield number1 / number2;
            }
            default -> throw new UnsupportedOperationException("Неизвестный оператор!");
        };
    }
    private static boolean isRoman(String s) {
        return s.matches("(X|IX|IV|V?I{0,3})");
    }
    private static int romanToArabic(String s) {
        return switch (s) {
            case "I" -> 1;
            case "II" -> 2;
            case "III" -> 3;
            case "IV" -> 4;
            case "V" -> 5;
            case "VI" -> 6;
            case "VII" -> 7;
            case "VIII" -> 8;
            case "IX" -> 9;
            case "X" -> 10;
            default -> 0;
        };
    }
    private static String arabicToRoman(int num) {
        if (num < 1) return "";
        StringBuilder ret = new StringBuilder();
        int[] vals = {100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] keys = {"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        for (int i = 0; i < vals.length; i++) {
            while (num >= vals[i]) {
                ret.append(keys[i]);
                num -= vals[i];
            }
        }
        return ret.toString();
    }
}
