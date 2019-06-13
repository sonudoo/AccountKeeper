package com.sonudoo.AccountKeeper;


public class Cipher {
    private static Cipher single_instance = null;

    static {
        System.loadLibrary("Cipher");
    }

    private Cipher() {
    }

    public static Cipher getInstance() {
        if (single_instance == null)
            single_instance = new Cipher();
        return single_instance;
    }

    private int[] stringtoArray(String s) {
        int[] stringArray = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            stringArray[i] = (int) s.charAt(i);
        }
        return stringArray;
    }

    private String arraytoString(int[] resultArray) {
        String resultString = "";
        for (int i = 0; i < resultArray.length; i++) {
            resultString += (char) resultArray[i];
        }
        return resultString;
    }

    public String encryptString(String s, int passcode) {
        int[] stringArray = stringtoArray(s);
        int[] resultArray = encrypt(passcode, stringArray);
        String resultString = arraytoString(resultArray);
        return resultString;
    }

    public String decryptString(String s, int passcode) {
        int[] stringArray = stringtoArray(s);
        int[] resultArray = decrypt(passcode, stringArray);
        String resultString = arraytoString(resultArray);
        return resultString;
    }

    private native int[] encrypt(int key, int[] array);

    private native int[] decrypt(int key, int[] array);
}
