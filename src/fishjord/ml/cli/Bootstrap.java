/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fishjord.ml.cli;

import fishjord.ml.io.BinaryDataReader;
import fishjord.ml.io.DataFormat;
import fishjord.ml.io.DataLine;
import fishjord.ml.io.DataWriter;
import fishjord.ml.utils.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.util.BitSet;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class Bootstrap {

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("r", "random-seed", true, "Set the random seed for bootstrapping");
        options.addOption("f", "out-format", true, "Output format (default=libsvm)");
        DataFormat outFormat = DataFormat.svm;
        long seed = System.currentTimeMillis();

        String inFile;
        String outFile;

        try {
            CommandLine line = new PosixParser().parse(options, args);

            if(line.hasOption("random-seed")) {
                seed = Long.valueOf(line.getOptionValue("random-seed"));
            }

            if(line.hasOption("out-format")) {
                outFormat = DataFormat.valueOf(line.getOptionValue("out-format"));
            }

            args = line.getArgs();
            if(args.length != 2) {
                throw new Exception("Unexpected number of command line arguments");
            }

            inFile = args[0];
            outFile = args[1];

        } catch(Exception e) {
            new HelpFormatter().printHelp("Bootstrap [options] <bin_pattern_file> <out_file>", options);
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
            return;
        }

        BinaryDataReader reader;
        if (inFile.endsWith(".gz")) {
            reader = new BinaryDataReader(new GZIPInputStream(new FileInputStream(inFile)));
        } else {
            reader = new BinaryDataReader(new File(inFile));
        }

        BitSet b = new BitSet(reader.getNumPatterns());
        Random r = new Random(seed);

        System.err.println("Starting Bootstrapping");
        System.err.println("Infile= " + inFile);
        System.err.println("Outfile= " + outFile + ", format= " + outFormat);
        System.err.println("Random seed= " + seed);

        long start = System.currentTimeMillis();
        int v;
        for(int index = 0;index < reader.getNumPatterns();index++) {
            v = r.nextInt(reader.getNumPatterns());
            b.set(v);
        }

        DataWriter out = IOUtils.getWriter(outFile, outFormat);
        DataLine pattern = new DataLine();

        for(int index = 0;index < reader.getNumPatterns();index++) {
            reader.readNextPattern(pattern);
            if(b.get(index)) {
                out.writePattern(pattern);
            }
        }

        System.out.println(b.cardinality() + " (" + b.cardinality() * 100 / reader.getNumFeatures() + "%) unique patterns written out in " + (System.currentTimeMillis() - start) / 1000.0f + "s");

        out.close();
        reader.close();
    }
}
