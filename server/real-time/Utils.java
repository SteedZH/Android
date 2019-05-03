package com.steed.server;

import java.io.Closeable;
import java.io.IOException;

/**
 * 释放资源
 */
public class Utils {
    public static void close(Closeable... targets) {//接口closeable, ...可变参数
        for (Closeable target : targets) {
            try {
                if (null != target) {
                    target.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
