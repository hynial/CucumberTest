package com.hynial.cucumber.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileResourceUtil {
    public static void main(String[] args) throws IOException {

        FileResourceUtil app = new FileResourceUtil();

        // read all files from a resources folder
        try {

            // files from src/main/resources/
//            List<File> result = app.getAllFilesFromResource("./context-wechat");
            List<File> result = app.getFilesFromResourceBySuffix("./context-wechat", ".properties");
            for (File file : result) {
                System.out.println("file : " + file);
//                printFile(file);
            }

//            System.getProperty("java.class.path");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }

    private ClassLoader getClassLoader(){
        return getClass().getClassLoader();
    }

    public List<File> getFilesFromResourceBySuffix(String folder, String suffix) throws URISyntaxException, IOException {
        URL resource = getClassLoader().getResource(folder);
        List<File> collect = Files.walk(Paths.get(resource.toURI()))
                .filter(path -> path.getFileName().toString().toLowerCase().endsWith(suffix.toLowerCase()))
                .map(x -> x.toFile())
                .collect(Collectors.toList());

        return collect;
    }

    public List<File> getAllFilesFromResource(String folder) throws URISyntaxException, IOException {
        URL resource = getClassLoader().getResource(folder);
        // dun walk the root path, we will walk all the classes
        List<File> collect = Files.walk(Paths.get(resource.toURI()))
//                .filter(Files::isRegularFile)
                .map(x -> x.toFile())
                .collect(Collectors.toList());

        return collect;
    }

    // print a file
    private static void printFile(File file) {
        List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
