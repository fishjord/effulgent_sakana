/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fishjord.ml.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class YaDTDataWriter implements DataWriter {

    private final PrintWriter dataOut;
    private final PrintWriter namesOut;
    private final char delim;
    private boolean namesWritten = false;

    public YaDTDataWriter(File stem) throws IOException {
        namesOut = new PrintWriter(stem.getAbsolutePath() + ".names");
        dataOut = new PrintWriter(stem.getAbsolutePath() + ".data");
        delim = ',';
    }

    @Override
    public void writePattern(DataLine write) throws IOException {
        if (!namesWritten) {
            for(int index = 0;index < write.pattern.length;index++) {
                namesOut.println("feature" + (index + 1) + ",string,discrete");
            }
            namesOut.println("label,string,class");
        }

        for (int index = 0; index < write.pattern.length; index++) {
            dataOut.print(write.pattern[index]);
            dataOut.print(delim);
        }

        dataOut.println((write.label == null)? -2: write.label);
    }

    @Override
    public void close() throws IOException {
        dataOut.close();
        namesOut.close();
    }
}
