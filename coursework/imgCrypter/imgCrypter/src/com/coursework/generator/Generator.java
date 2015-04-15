package com.coursework.generator;

import com.coursework.utils.AppConst;

import java.util.Arrays;
import java.util.Random;

public class Generator {
    private int[] arrayRedXor;
    private int[] arrayGreenXor;
    private int[] arrayBlueXor;

    private double[] arrayRosslerX;
    private double[] arrayRosslerY;
    private double[] arrayRosslerZ;
    private double[] arrayXcube;
    int setX;
    int setY;
    int setZ;
    int omega;

    public int[] getArrayRedXor() {
        return arrayRedXor;
    }

    public int[] getArrayGreenXor() {
        return arrayGreenXor;
    }

    public int[] getArrayBlueXor() {
        return arrayBlueXor;
    }

    public void systemRessler(int quantityPixels, int x, int y, int z, int omega) {
        arrayRedXor = new int[quantityPixels];
        arrayGreenXor = new int[quantityPixels];
        arrayBlueXor = new int[quantityPixels];
        Random randomRed = new Random(x);
        Random randomGreen = new Random(y);
        Random randomBlue = new Random(z);
        for (int i = 0; i < arrayRedXor.length; i++) {
            arrayRedXor[i] = randomRed.nextInt(255);
            arrayGreenXor[i] = randomGreen.nextInt(255);
            arrayBlueXor[i] = randomBlue.nextInt(255);
        }
    }

    public void generateRossler(int quantityPixels, double x, double y, double z, double alpha, double beta) {
        double tempX = 0; double tempY = 0; double tempZ = 0;
        double var = 0;
        //int setX = 0; int setY = 0; int setZ = 0;
        arrayRosslerX = new double[quantityPixels];
        arrayRosslerY = new double[quantityPixels];
        arrayRosslerZ = new double[quantityPixels];


        arrayRosslerX[0] = x;
        arrayRosslerY[0] = y;
        arrayRosslerZ[0] = z;

        for (int i = 1; i <quantityPixels; i++) {
            arrayRosslerX[i] = (arrayRosslerX[i-1] + (-arrayRosslerY[i-1] - arrayRosslerZ[i-1])) * AppConst.DT;
            arrayRosslerY[i] = (arrayRosslerY[i-1] + (arrayRosslerX[i-1] + AppConst.P * arrayRosslerY[i-1])) * AppConst.DT;
            arrayRosslerZ[i] = (arrayRosslerZ[i-1] + (AppConst.Q + arrayRosslerZ[i-1] * (arrayRosslerX[i-1] - AppConst.R))) * AppConst.DT;
        }

        System.out.println(Arrays.toString(arrayRosslerX));
        System.out.println(Arrays.toString(arrayRosslerY));
        System.out.println(Arrays.toString(arrayRosslerZ));

        var = arrayRosslerX[100];

        for (int i = 0; i <quantityPixels; i++) {
            tempX += arrayRosslerX[i];
            tempY += arrayRosslerY[i];
            tempZ += arrayRosslerZ[i];
        }
        tempX = 10000 * tempX - 10000 * Math.round(tempX);
        tempY = 10000 * tempY - 10000 * Math.round(tempY);
        tempZ = 10000 * tempZ - 10000 * Math.round(tempZ);

        setX = (int) tempX * 100;
        setY = (int) tempY * 100;
        setZ = (int) tempZ * 100;

        System.out.println(setX);
        System.out.println(setY);
        System.out.println(setZ);

        generateXCube(quantityPixels, alpha, beta, var);
        System.out.println(Arrays.toString(arrayXcube));

        setX = setX ^ omega;
        setY = setY ^ omega;
        setZ = setZ ^ omega;
        System.out.println(setX);
        System.out.println(setY);
        System.out.println(setZ);
    }

    public void generateXCube(int quantityPixels, double alpha, double beta,  double var) {
        double temp = 0;
        arrayXcube    = new double[quantityPixels];
        arrayXcube[0] = var;
        for (int i = 1; i <quantityPixels; i++) {
            arrayXcube[i] = alpha - beta * arrayXcube[i-1] + Math.pow(arrayXcube[i-1], 3);
        }

        for (int i = 0; i <quantityPixels ; i++) {
            temp += arrayXcube[i];
        }
        omega = (int) temp;
    }

}
