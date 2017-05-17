package generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class LightsoutStartGenerator {
    private ArrayList<int[][]> solvableStarts = new ArrayList<>();
    private ArrayList<int[][]> unsolvableStarts = new ArrayList<>();

    private int[][] endConfigs = {
            {0, 0, 1, 1, 1},
            {0, 1, 0, 1, 0},
            {0, 1, 1, 0, 1},
            {1, 0, 0, 0, 1},
            {1, 0, 1, 1, 0},
            {1, 1 ,0, 1, 1},
            {1, 1, 1, 0, 0}
    };

    private Random random = new Random();
    private File data;
    private FileWriter writer;

    public LightsoutStartGenerator(){
        data = new File("data.txt");
        if(data.exists()){
            data.delete();
        }
        try {
            data.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer = new FileWriter(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[][] generateStart(){
        int[][] start = new int[5][5];
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                start[i][j] = random.nextInt(2);
            }
        }

        return start;
    }

    public boolean isSolvable(int[][] start){
        boolean solvable = false;
        int[][] end = new int[5][5];
        for(int i = 0; i < 5; i++){ //Don't modify input array
            for(int j = 0; j < 5; j++){
                end[i][j] = start[i][j];
            }
        }

        //Simulate first part of knight watchmen solution
        for(int i = 1; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(end[i - 1][j] == 1){
                    end[i - 1][j] = 0;
                    end[i][j] = toggle(end[i][j]);
                    if(i < 4) {
                        end[i + 1][j] = toggle(end[i + 1][j]);
                    }
                    if(j > 0){
                        end[i][j - 1] = toggle(end[i][j - 1]);
                    }
                    if(j < 4){
                        end[i][j + 1] = toggle(end[i][j + 1]);
                    }
                }
            }
        }

        //Check solvable end configurations
        for(int i = 0; i < 7; i++){
            if(Arrays.equals(end[4], endConfigs[i])){
                solvable = true;
                break;
            }
        }

        return solvable;
    }

    private int toggle(int num) {
        int toggledNum;
        if (num == 1) {
            toggledNum = 0;
        } else {
            toggledNum = 1;
        }

        return toggledNum;
    }

    public void createCSV(int iterations){
        int[][] gen;
        for(int i = 0; i < iterations; i++){
            gen = generateStart();
            if(isSolvable(gen)){
                solvableStarts.add(gen);
            } else{
                unsolvableStarts.add(gen);
            }
        }
        //write to file
        for(int i = 0; i < unsolvableStarts.size(); i++){
            for(int j = 0; j < 5; j++){
                for(int k = 0; k < 5; k++){
                    try {
                        writer.write(unsolvableStarts.get(i)[j][k] + ",");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                writer.write("0\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i < solvableStarts.size(); i++){
            for(int j = 0; j < 5; j++){
                for(int k = 0; k < 5; k++){
                    try {
                        writer.write(solvableStarts.get(i)[j][k] + ",");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                writer.write("1\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LightsoutStartGenerator generator = new LightsoutStartGenerator();
        generator.createCSV(10000);
    }
}
