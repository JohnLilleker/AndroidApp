package com.liljo.story.util;

import java.io.InputStream;
import java.util.Scanner;

public class FileUtil {

    public static String inputStreamToString(final InputStream inputStream) {
        final Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        final String string = scanner.hasNext() ? scanner.next() : null;
        scanner.close();
        return string;
    }
}
