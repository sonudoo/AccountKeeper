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
        /*
            This method converts a string to integer array
         */
        int[] stringArray = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            stringArray[i] = (int) s.charAt(i);
        }
        return stringArray;
    }

    private String arraytoString(int[] resultArray) {
        /*
            This method converts integer array to string.
         */
        String resultString = "";
        for (int i1 : resultArray) {
            resultString += (char) i1;
        }
        return resultString;
    }

    public String encryptString(String s, int passcode) {
        /*
            This method encrypts a string with the given passcode.
         */
        int[] stringArray = stringtoArray(s);
        int[] resultArray = encrypt(passcode, stringArray);
        return arraytoString(resultArray);
    }

    public String decryptString(String s, int passcode) {
        /*
            This method decrypts the string with the given passcode.
         */
        int[] stringArray = stringtoArray(s);
        int[] resultArray = decrypt(passcode, stringArray);
        return arraytoString(resultArray);
    }

    private native int[] encrypt(int key, int[] array);

    private native int[] decrypt(int key, int[] array);
}
