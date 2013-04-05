/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fishjord.ml.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Format:
 * field        | type                 | desc
 * -----------------------------------------------------
 *4 magic        |         int4         | magic number
 *5 version      |         int1         | version number (expected 1)
 *6 labels       |         int1         | file contains label
 *7 num_patterns |         int4         | number of patterns in the file
 *11 num_features |         int2         | number of features per pattern
 *
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class BinaryDataWriter implements DataWriter {

    public static final int MAGIC = 1785097580;  //FOURCC = jfml
    public static final int HEADER_SIZE = 16;
    private RandomAccessFile dataOut;
    private int numFeatures = -1;
    private int numPatterns = 0;
    private boolean labelsPresent;

    public BinaryDataWriter(File dataFile) throws IOException {
        dataOut = new RandomAccessFile(dataFile, "rw");
        dataOut.writeInt(MAGIC);  //Magic number
        dataOut.writeByte(1);    //Version number
        dataOut.writeByte(0);    //Labels present?
        dataOut.writeInt(Integer.MIN_VALUE); //num patterns
        dataOut.writeShort(Short.MIN_VALUE); //num features
    }

    @Override
    public void writePattern(DataLine write) throws IOException {
        if(numFeatures == -1) {
            numFeatures = write.pattern.length;

        }
        numPatterns++;

        if(write.pattern.length != numFeatures) {
            throw new IOException("Line has an unexpected number of features");
        }

        if((write.label == null && labelsPresent) || (write.label != null && !labelsPresent)) {
            throw new IOException("Label error");
        }

        if(labelsPresent) {
            dataOut.writeShort(write.label);
        }

        for(int index = 0;index < write.pattern.length;index++) {
            dataOut.writeFloat(write.pattern[index]);
        }
    }

    @Override
    public void close() throws IOException {
        dataOut.seek(5);
        if(labelsPresent) {
            dataOut.writeByte(1);
        } else {
            dataOut.writeByte(0);
        }

        dataOut.writeInt(numPatterns);
        dataOut.writeShort(numFeatures);

        dataOut.close();
    }
}
