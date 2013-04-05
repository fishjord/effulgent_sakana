/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fishjord.ml.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class SVMSparseWriter implements DataWriter {
    private final PrintWriter dataOut;

    public SVMSparseWriter(File dataFile) throws IOException {
        dataOut = new PrintWriter(dataFile);
    }

    @Override
    public void writePattern(DataLine write) throws IOException {
        if(write.label == null) {
            dataOut.print(-2);
        } else {
            dataOut.print(write.label);
        }

        for(int index = 0;index < write.pattern.length;index++) {
            if(write.pattern[index] != 0) {
                dataOut.print(" ");
                dataOut.print(index);
                dataOut.print(":");
                dataOut.print(write.pattern[index]);

            }
        }
        dataOut.println();
    }

    @Override
    public void close() throws IOException {
        dataOut.close();
    }

}
