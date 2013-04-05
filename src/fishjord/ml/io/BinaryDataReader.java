/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fishjord.ml.io;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Format:
 * field        | type                 | desc
 * -----------------------------------------------------
 * magic        |         int4         | magic number
 * version      |         int4         | version number (expected 1)
 * scale_method |         int1         | patterns scaling method (0 = no scaling, 1 = [0-1], 2=[-1,1])
 * labels       |         int1         | file contains label
 * num_patterns |         int4         | number of patterns in the file
 * num_features |         int2         | number of features per pattern
 * column_min   | float * num features | minimum value in every column
 * column_max   | float * num features | max value in every column
 * column_avg   | float * num features | avg value for every column
 *
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class BinaryDataReader implements DataReader {
    private final DataInputStream dataStream;
    private final int numPatterns;
    private final int numFeatures;
    private int currPattern = 0;
    private final boolean labelsPresent;

    public BinaryDataReader(File dataFile) throws IOException {
        dataStream = new DataInputStream(new FileInputStream(dataFile));

        int magic = dataStream.readInt();
        if(magic != BinaryDataWriter.MAGIC || dataStream.readByte() != 1) {
            throw new IOException(dataFile + " doesn't look like a file I know how to parse");
        }
        labelsPresent = dataStream.readByte() == 1;
        numPatterns = dataStream.readInt();
        numFeatures = dataStream.readShort();
    }

    public int getNumPatterns() {
        return numPatterns;
    }

    public int getNumFeatures() {
        return numFeatures;
    }

    public boolean isLabelsPresent() {
        return labelsPresent;
    }

    @Override
    public boolean readNextPattern(DataLine read) throws IOException {
        if(read.pattern == null) {
            read.pattern = new float[numFeatures];
        }
        if(currPattern >= numPatterns) {
            return false;
        }

        if(read.pattern.length != numFeatures) {
            throw new IOException("Input object contains different number of features");
        }

        try {
            if(labelsPresent) {
                read.label = (int)dataStream.readShort();
            } else {
                read.label = null;
            }

            for(int index = 0;index < numFeatures;index++) {
                read.pattern[index] = dataStream.readFloat();
            }

        } catch(EOFException e) {
            throw new IOException("Unexpected end of file");
        }

        currPattern++;
        return true;
    }

    @Override
    public void close() throws IOException {
        dataStream.close();
    }

}
