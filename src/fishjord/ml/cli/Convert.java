/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fishjord.ml.cli;

import fishjord.ml.io.DataFormat;
import fishjord.ml.io.DataLine;
import fishjord.ml.io.DataReader;
import fishjord.ml.io.DataWriter;
import fishjord.ml.utils.IOUtils;
import java.io.IOException;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class Convert {

    private static void printUsageAndExit(String err) {
        System.err.println("USAGE: Convert <outformat> <infile> <outfile>");
        System.err.println("\tformats: csv, whitespace, svm, binary");
        System.err.println("Error: " + err);
        System.exit(1);
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            printUsageAndExit("Incorrect number of arguments");
        }

        DataReader reader;
        DataWriter writer;
        try {
            reader = IOUtils.getReader(args[1]);
            writer = IOUtils.getWriter(args[2], DataFormat.valueOf(args[0]));
        } catch (IOException e) {
            printUsageAndExit(e.getMessage());
            throw new RuntimeException("Silly java");
        }

        DataLine in = new DataLine();
        while(reader.readNextPattern(in)) {
            writer.writePattern(in);
        }

        reader.close();
        writer.close();
    }
}
