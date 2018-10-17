package com.ua.notifier;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Created by boss on 18.06.2018.
 */
public class Utils {
    public static String getStackTrace(Exception e)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        ps.close();
        return baos.toString();
    }
}
