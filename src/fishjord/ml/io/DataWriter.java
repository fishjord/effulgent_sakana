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
public interface DataWriter {
    public void writePattern(DataLine write) throws IOException;
    public void close() throws IOException;
}
