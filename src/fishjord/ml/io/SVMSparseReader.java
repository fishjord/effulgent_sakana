/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fishjord.ml.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class SVMSparseReader implements DataReader {

    private final BufferedReader dataReader;
    private final Pattern splitRegex;
    private final int numFeatures;
    private String buf;
    private int lineno;

    public SVMSparseReader(File dataFile, int numFeatures) throws IOException {
        dataReader = new BufferedReader(new FileReader(dataFile));
        splitRegex = Pattern.compile("\\s+");
        lineno = 0;
        this.numFeatures = numFeatures;
    }

    @Override
    public boolean readNextPattern(DataLine read) throws IOException {
        buf = dataReader.readLine();

        if (buf == null) {
            return false;
        }

        lineno++;

        String[] lexemes = splitRegex.split(buf);
        if (read.pattern == null) {
            read.pattern = new float[numFeatures];
        } else if (read.pattern.length != lexemes.length - 1) {
            throw new IOException("Not the right number of lexemes on line " + lineno);
        }

        read.label = Integer.valueOf(lexemes[0]);
        Arrays.fill(read.pattern, 0);
        String[] tmp;

        for (int index = 1; index < lexemes.length; index++) {
            try {
                tmp = lexemes[index].split(":");
                if(tmp.length != 2) {
                    throw new IOException("Bad feature " + index + " value on line " + lineno);
                }

                read.pattern[Integer.valueOf(tmp[0])] = Float.valueOf(tmp[1]);
            } catch (NumberFormatException e) {
                throw new IOException("Bad feature " + index + " value on line " + lineno);
            }
        }

        return true;
    }

    @Override
    public void close() throws IOException {
        dataReader.close();
    }
}
