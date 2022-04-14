package com.example.protoolsactivitipoc.util;

import java.util.Random;

public class Utils {

    public static String pickRandomString() {
        String[] texts = {"Je suis un panda", "Le panda mange du bamboo", "Et boit du café", "Je veux aller boire une bière",
                "Le panda roule", "C'est quand le weekend?"};
        return texts[new Random().nextInt(texts.length)];
    }
}
