package com.example.comp6239.utility;

import java.io.Closeable;
import java.io.IOException;

public class Utils {
    public static void close(Closeable... targets) {
        for (Closeable target : targets) {
            if (target != null) {
                try {
                    target.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
