package com.hynial.cucumber.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnmappableCharacterException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CommonUtil {

    public static boolean isEmpty(String s) {
        return s == null || "".equals(s);
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static boolean isEmptyWithTrim(String s) {
        return s == null || "".equals(s.trim());
    }

    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void writeFileWithBom(String outPath, String content) {
        try {
            // add BOM head to avoid excel open encode error.
            byte[] BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            Files.write(Paths.get(outPath), BOM, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.writeString(Paths.get(outPath), content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (UnmappableCharacterException e){
            System.out.println("UnmappableCharacter:" + content);
        } catch (IOException e) {
            System.out.println("ExceptionCharacter:" + content);
            e.printStackTrace();
        }
    }

    public static void writeFile(String outPath, String content) {
        try {
            Files.writeString(Paths.get(outPath), content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (UnmappableCharacterException e){
            System.out.println("UnmappableCharacter:" + content);
        } catch (IOException e) {
            System.out.println("ExceptionCharacter:" + content);
            e.printStackTrace();
        }
    }

    public static void appendFile(String outPath, String content){
        try {
            Files.writeString(Paths.get(outPath), content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (UnmappableCharacterException e){
            System.out.println("UnmappableCharacter:" + content);
        } catch (IOException e) {
            System.out.println("ExceptionCharacter:" + content);
            e.printStackTrace();
        }
    }
}
