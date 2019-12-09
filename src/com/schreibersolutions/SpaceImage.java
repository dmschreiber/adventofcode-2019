package com.schreibersolutions;

import sun.jvm.hotspot.memory.Space;

/**
 * Created by dschreiber on 12/9/19.
 */
public class SpaceImage {
    private String myData;
    private Layer[] layers;
    private int myWidth, myHeight;
    public int layerCount = 0;

    public class Layer {
        public int[][] values;
        private String myData;
        public int myWidth, myHeight;

        Layer(String data, int width, int height) {
            myWidth = width;
            myHeight = height;

            values = new int[width][height];
            myData = data;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    values[x][y] = Integer.parseInt(data.substring(width*y+x,width*y+x+1));
                    System.out.print(values[x][y]);

                }
                System.out.println();
            }
        }
    }

    SpaceImage(String data, int width, int height) {
        myData = data;
        myWidth = width;
        myHeight = height;

        layerCount = data.length() / (width*height);

        layers = new Layer[layerCount];
        for (int index = 0; index < layerCount; index++) {
            System.out.println("Layer " + String.valueOf(index+1));
            layers[index] = new Layer(myData.substring(index*width*height,(index+1)*width*height),width, height);

        }
    }

    public int countSpecificDigit(int whichLayer, int digit) {
        Layer l = layers[whichLayer];

        int count = 0;

        for (int x = 0; x < l.myWidth; x++) {
            for (int y = 0; y < l.myHeight; y++) {
                if (l.values[x][y] == digit) {
                    ++count;
                }
            }
        }
        return count;
    }

    public int whichLayerHasFewestZeros() {
        int minZeros = 999999999;
        int retval = -1;

        for (int whichLayer = 0; whichLayer < layers.length; whichLayer++) {
            if (countSpecificDigit(whichLayer,0) < minZeros) {
                minZeros = countSpecificDigit(whichLayer,0);
                retval = whichLayer;
            }
        }
        return retval;
    }

    public int quotientCount(int whichLayer, int digit1, int digit2) {
        return countSpecificDigit(whichLayer,digit1) * countSpecificDigit(whichLayer,digit2);
    }

    public int getColor(int x, int y) {

        int color = 2;
        int whichLayer;
        for (whichLayer = 0; (whichLayer < layers.length) && (color >= 2); whichLayer++) {
            color = layers[whichLayer].values[x][y];
        }
        whichLayer = whichLayer;
        return color;
    }

    public void displayImage() {
        for (int y = 0; y < myHeight; y++) {
            for (int x = 0; x < myWidth; x++) {
                int color = getColor(x,y);
                if (color == 0) {
                    System.out.print(" ");
                } else {
                    System.out.print("\u2588");
                }
            }
            System.out.println();
        }
    }
}
