package org.json.utils.api;

import lombok.Getter;

import java.util.Random;

public class KeyGen {

    private static final char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final int licenseLength = 20;
    private static final int numCount = 5;
    private static Random rand = new Random();
    @Getter
    public static final String key = generateKey();

    @Getter
    public static final String prefix = generatePrefix();

    public static String generateKey() {
        StringBuilder rawPassword = new StringBuilder();

        for (int i = 0; i < licenseLength - numCount; i++) {
            if (letters != null) {
                rawPassword.append(get());
            }
        }

        for (int i = 0; i < numCount; i++) {
            rawPassword.append(getInt());
        }

        char[] shuffleLicense = rawPassword.toString().toCharArray();
        randomizeString(shuffleLicense);
        return new String(shuffleLicense);
    }

    private static String generatePrefix() {
        return key.toCharArray()[0] + "" + key.toCharArray()[key.toCharArray().length - 1];
    }

    private static void randomizeString(char[] license) {
        for (int i = license.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);

            char temp = license[index];
            license[index] = license[i];
            license[i] = temp;
        }
    }

    private static char get() {
        return KeyGen.letters[rand.nextInt(KeyGen.letters.length)];
    }

    private static int getInt() {
        return rand.nextInt(10);
    }


}
