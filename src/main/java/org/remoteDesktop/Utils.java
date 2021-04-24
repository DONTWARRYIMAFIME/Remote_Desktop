package org.remoteDesktop;

public class Utils {

    public static Integer tryParseStrToInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            System.out.format("Cannot convert %s to int\n", text);
            return null;
        }
    }

    public static String generatePassword(int length) {
        StringBuilder stringBuilder = new StringBuilder();



        return "1111";
    }

}
