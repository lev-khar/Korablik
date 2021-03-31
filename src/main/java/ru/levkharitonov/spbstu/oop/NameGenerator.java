package ru.levkharitonov.spbstu.oop;

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
        first = reader("src/main/resources/first.txt");
        second = reader("src/main/resources/second.txt");
        Collections.shuffle(first);
        Collections.shuffle(second);
    }

    public String generateName(){
        return first.get(rand.nextInt(first.size())) + " " + second.get(rand.nextInt(second.size()));
    }

    private static List<String> reader(String path) {
        List<String> list = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader fb = new BufferedReader(fileReader);
            String line = fb.readLine();
            while (line != null) {
                list.add(line);
                line = fb.readLine();
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return list;
    }
}
