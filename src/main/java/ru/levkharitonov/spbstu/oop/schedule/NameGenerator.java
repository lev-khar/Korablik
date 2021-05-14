package ru.levkharitonov.spbstu.oop.schedule;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NameGenerator {
    final private List<String> first;
    final private List<String> second;
    final private java.util.Random rand = new java.util.Random();

    public NameGenerator() {
        first = read("src/main/resources/first.txt");
        second = read("src/main/resources/second.txt");
        Collections.shuffle(first);
        Collections.shuffle(second);
    }

    public String generateName(){
        return first.get(rand.nextInt(first.size())) + " " + second.get(rand.nextInt(second.size()));
    }

    private static List<String> read(String path) {
        List<String> list = new ArrayList<>(220);
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader fb = new BufferedReader(fileReader);
            String line = fb.readLine();
            while (line != null) {
                list.add(line);
                line = fb.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
