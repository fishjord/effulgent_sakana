/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fishjord.ml.utils;

import fishjord.ml.io.DataLine;
import fishjord.ml.io.DataReader;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class DataUtils {

    public static class DataAttributes implements Serializable {

        private final int numPatterns;
        private final int numFeatures;
        private final float[] colMins;
        private final float[] colMaxes;
        private final float[] colAvgs;

        public DataAttributes(int numPatterns, int numFeatures, float[] colMins, float[] colMaxes, float[] colAvgs) {
            this.numPatterns = numPatterns;
            this.numFeatures = numFeatures;
            this.colMins = colMins;
            this.colMaxes = colMaxes;
            this.colAvgs = colAvgs;
        }

        public int getNumPatterns() {
            return numPatterns;
        }

        public int getNumFeatures() {
            return numFeatures;
        }

        public float[] getColMins() {
            return colMins;
        }

        public float[] getColMaxes() {
            return colMaxes;
        }

        public float[] getColAvgs() {
            return colAvgs;
        }
    }

    public static DataAttributes computeDataStats(File inputFile) throws IOException {
        File dataAttributeFile = new File(inputFile.getAbsolutePath() + ".attributes");
        if (dataAttributeFile.exists()) {
            try {
                DataAttributes dataAttrs;
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(dataAttributeFile)));
                dataAttrs = (DataAttributes) ois.readObject();
                ois.close();
                return dataAttrs;
            } catch (Exception e) {
            }
        }
        int numPatterns = 0;
        int numFeatures = 0;
        float[] colMins = null;
        float[] colMaxes = null;
        double[] colAvgsTmp = null;

        DataLine pattern = new DataLine();
        DataReader reader = IOUtils.getReader(inputFile);
        float[] vals;

        while (reader.readNextPattern(pattern)) {
            if (colMins == null) {
                numFeatures = pattern.getPattern().length;
                colMins = new float[numFeatures];
                colMaxes = new float[numFeatures];
                colAvgsTmp = new double[numFeatures];
                Arrays.fill(colMins, Float.MAX_VALUE);
                Arrays.fill(colMaxes, Float.MIN_VALUE);
            }

            vals = pattern.getPattern();
            for (int index = 0; index < vals.length; index++) {
                if (vals[index] < colMins[index]) {
                    colMins[index] = vals[index];
                }

                if (vals[index] > colMaxes[index]) {
                    colMaxes[index] = vals[index];
                }

                colAvgsTmp[index] += vals[index];
            }

            numPatterns++;
        }

        float[] colAvgs = new float[numFeatures];
        for (int index = 0; index < numFeatures; index++) {
            colAvgs[index] = (float) (colAvgsTmp[index] / numFeatures);
        }

        DataAttributes dataAttrs = new DataAttributes(numPatterns, numFeatures, colMins, colMaxes, colAvgs);
        if (dataAttributeFile.canWrite() || dataAttributeFile.createNewFile()) {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(dataAttributeFile)));
            oos.writeObject(dataAttrs);
            oos.close();
        }
        return dataAttrs;
    }

    public static void scaleMean(DataLine input, DataAttributes dataAttributes) {
        float[] pattern = input.getPattern();
        for (int index = 0; index < pattern.length; index++) {
            pattern[index] -= dataAttributes.getColAvgs()[index];
        }
    }

    public static void writeDataAttrs(DataAttributes dataAttrs, PrintStream out) {
        out.println("Number of features: " + dataAttrs.getNumFeatures());
        out.println("Number of patterns: " + dataAttrs.getNumPatterns());
        out.println("Column minimums:");
        for(int index = 0;index < dataAttrs.getNumFeatures();index++) {
            out.print(dataAttrs.getColMins()[index] + "\t");
        }
        out.println("\nColumn maximiums:");
        for(int index = 0;index < dataAttrs.getNumFeatures();index++) {
            out.print(dataAttrs.getColMaxes()[index] + "\t");
        }
        out.println("\nColumn averages:");
        for(int index = 0;index < dataAttrs.getNumFeatures();index++) {
            out.print(dataAttrs.getColAvgs()[index] + "\t");
        }
        out.println();
    }
}
