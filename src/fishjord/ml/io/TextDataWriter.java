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
public class TextDataWriter implements DataWriter {

    private final PrintWriter dataOut;
    private final PrintWriter labelOut;
    private final char delim;

    public TextDataWriter(File dataFile, char delim) throws IOException {
        this(dataFile, delim, null);
    }

    public TextDataWriter(File dataFile, char delim, File labelFile) throws IOException {
        if (labelFile != null) {
            labelOut = new PrintWriter(labelFile);
        } else {
            labelOut = null;
        }
        dataOut = new PrintWriter(dataFile);
        this.delim = delim;
    }

    @Override
    public void writePattern(DataLine write) throws IOException {
        if (labelOut != null) {
            if (write.label == null) {
                labelOut.println(-2);
            } else {
                labelOut.println(write.label);
            }
        }

        for (int index = 0; index < write.pattern.length; index++) {
            dataOut.print(write.pattern[index]);
            if (index + 1 != write.pattern.length) {
                dataOut.print(delim);
            }
        }

        dataOut.println();
    }

    @Override
    public void close() throws IOException {
        dataOut.close();
        if (labelOut != null) {
            labelOut.close();
        }
    }
}
