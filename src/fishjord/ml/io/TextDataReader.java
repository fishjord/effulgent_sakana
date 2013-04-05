/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fishjord.ml.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class TextDataReader implements DataReader {

    private final BufferedReader labelReader;
    private final BufferedReader dataReader;
    private final Pattern splitRegex;
    private String buf;
    private int lineno;

    public TextDataReader(File dataFile, String delim) throws IOException {
        this(dataFile, delim, null);
    }

    public TextDataReader(File dataFile, String delim, File labelFile) throws IOException {
        if (labelFile != null) {
            labelReader = new BufferedReader(new FileReader(labelFile));
        } else {
            labelReader = null;
        }

        dataReader = new BufferedReader(new FileReader(dataFile));
        splitRegex = Pattern.compile(delim + "+");
        lineno = 0;
    }

    @Override
    public boolean readNextPattern(DataLine read) throws IOException {
        if (labelReader != null) {
            buf = labelReader.readLine();
            if (buf == null) {
                if (dataReader.readLine() == null) {
                    return false;
                } else {
                    throw new IOException("More patterns than labels!");
                }
            }
            read.label = Integer.valueOf(buf);
        } else {
            read.label = null;
        }

        buf = dataReader.readLine();

        if (buf == null) {
            if (labelReader != null) {
                throw new IOException("More labels than patterns!");
            }
            return false;
        }

        lineno++;

        String[] lexemes = splitRegex.split(buf);
        if (read.pattern == null) {
            read.pattern = new float[lexemes.length];
        } else if (read.pattern.length != lexemes.length) {
            throw new IOException("Not the right number of lexemes on line " + lineno);
        }
        for (int index = 0; index < lexemes.length; index++) {
            try {
                read.pattern[index] = Float.valueOf(lexemes[index]);
            } catch (NumberFormatException e) {
                throw new IOException("Bad feature " + index + " value on line " + lineno);
            }
        }

        return true;
    }

    public void close() throws IOException {
        if (labelReader != null) {
            labelReader.close();
        }
        dataReader.close();
    }
}
