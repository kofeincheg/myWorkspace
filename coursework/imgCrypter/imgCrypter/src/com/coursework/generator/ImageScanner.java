package com.coursework.generator;

import org.apache.commons.io.FilenameUtils;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ImageScanner {
    private Color myColor;
    private Generator generator;
    private File savedFile;
    private BufferedImage img;
    public double indicator;

    public File getSavedImage() {
        return savedFile;
    }

    public void scanImage(File file, double x, double y, double z, double alpha, double beta, boolean enFlag, boolean deFlag) {
        indicator = 0.0;
        try {
            img = ImageIO.read(file);
            generator = new Generator();
            generator.generateRossler(img.getHeight() * img.getWidth(), x, y, z, alpha, beta);
            generator.systemRessler(img.getHeight() * img.getWidth(), generator.setX, generator.setY, generator.setZ, generator.omega);

            if (enFlag && !deFlag){
                int[][] matrix = new int[img.getWidth()][img.getHeight()];
                for (int i = 0; i < img.getWidth(); i++) {
                    for (int j = 0; j < img.getHeight(); j++) {
                        myColor = new Color(img.getRGB(i, j));
                        int r = myColor.getRed() ^ generator.getArrayRedXor()[i + j];
                        int g = myColor.getGreen() ^ generator.getArrayGreenXor()[i + j];
                        int b = myColor.getBlue() ^ generator.getArrayBlueXor()[i + j];
                        matrix[i][j] = getIntFromColor(r, g, b);
                    }
                }
                encryptMatrix(matrix, img.getWidth()*img.getHeight(), (generator.setX^generator.setY^generator.setZ^generator.omega));
                for (int i = 0; i < matrix.length; i++) {
                    for (int j = 0; j < matrix[i].length; j++) {
                        img.setRGB(i, j, matrix[i][j]);
                    }
                }
            }
            if (deFlag && !enFlag){
                int[][] matrix = new int[img.getWidth()][img.getHeight()];
                for (int i = 0; i < img.getWidth(); i++) {
                    for (int j = 0; j < img.getHeight(); j++) {
                        matrix[i][j] = img.getRGB(i, j);
                    }
                }
                decryptMatrix(matrix, img.getWidth()*img.getHeight(), (generator.setX^generator.setY^generator.setZ^generator.omega));
                for (int i = 0; i < img.getWidth(); i++) {
                    for (int j = 0; j < img.getHeight(); j++) {
                        myColor = new Color(matrix[i][j]);
                        int r = myColor.getRed() ^ generator.getArrayRedXor()[i + j];
                        int g = myColor.getGreen() ^ generator.getArrayGreenXor()[i + j];
                        int b = myColor.getBlue() ^ generator.getArrayBlueXor()[i + j];
                        img.setRGB(i, j, getIntFromColor(r, g, b));
                    }
                }
            }

            String fileExtension = FilenameUtils.getExtension(file.getName());
            if (fileExtension.contains("jpeg") || fileExtension.contains("jpg")) {
                fileExtension = "png";
            }
            ImageIO.write(img, fileExtension, new File(FilenameUtils.getBaseName(file.getName()) + "." + fileExtension));
            savedFile = new File("im.tmp");
            ImageIO.write(img, "png", savedFile);
        } catch (Exception e) {
            System.out.println("Incorrect File " + e.getMessage());
        }
        System.out.println("Finished");
    }

    private int getIntFromColor(int Red, int Green, int Blue) {
        Red = (Red << 16) & 0x00FF0000;
        Green = (Green << 8) & 0x0000FF00;
        Blue = Blue & 0x000000FF;
        return 0xFF000000 | Red | Green | Blue;
    }


    private void shuffle(int[] array, Random random) {
        int n = array.length;
        for (int i = 0; i < array.length; i++) {
            // Get a random index of the array past i.
            int rand = random.nextInt(n);
            // Swap the random element with the present element.
            int randomElement = array[rand];
            array[rand] = array[i];
            array[i] = randomElement;
        }
    }

    private void encryptMatrix(int[][] matrix, int size, int key) {
        int k = 0;
        int[] array1 = new int[size];

        Random random = new Random(key);
        Map<Integer, Integer> hashmap = new HashMap<Integer, Integer>();

        for (int i = 0; i < array1.length; i++) {
            array1[i] = i;
        }

        shuffle(array1, random);
        //System.out.println(Arrays.toString(array1));

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                hashmap.put(k, matrix[i][j]);
                k++;
            }
        }
        k = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = hashmap.get(indexOfArray(array1, k));
                k++;
            }
        }
    }

    private void decryptMatrix(int[][] matrix, int size, int key) {
        Map<Integer, Integer> hashmap = new HashMap<Integer, Integer>();
        Random random = new Random(key);
        int m = 0;
        int[] array1 = new int[size];

        for (int i = 0; i < array1.length; i++) {
            array1[i] = i;
        }

        int[] array2 = Arrays.copyOf(array1, array1.length);

        shuffle(array2, random);

        System.out.println(Arrays.toString(array2));

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                hashmap.put(m, matrix[i][j]);
                //System.out.println(m + ":" + matrix[i][j]);
                m++;
            }
        }
        m = 0;
        for (int i = 0; i <= matrix.length - 1; i++) {
            for (int j = 0; j <= matrix[i].length - 1; j++) {
                matrix[i][j] = hashmap.get(searchIndex(array2, m));
                m++;
            }
        }
    }

    private int indexOfArray(int[] array1, int index) {
        for (int i = 0; i < array1.length; i++) {
            if (i == index) {
                return array1[i];
            }

        }
        return -1;
    }

    private int searchIndex(int[] array1, int index) {
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] == index) {
                return i;
            }

        }
        return -1;
    }

}
