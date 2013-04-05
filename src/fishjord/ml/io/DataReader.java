/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fishjord.ml.io;

import java.io.IOException;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public interface DataReader {
    public boolean readNextPattern(DataLine read) throws IOException;
    public void close() throws IOException;
}
