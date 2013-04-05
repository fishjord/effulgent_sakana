/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fishjord.ml.cli;

import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class MLMain {

    private static void printUsageAndExit() {
        System.err.println("USAGE: MLMain <subcommand> <subcommand_args>");
        System.err.println("\tsubcommands:");
        System.err.println("\tconvert - convert data file formats");
        System.err.println("\tsummary - print binary data summary data");
        System.err.println("\tscale   - rescale data");
        System.err.println("\tpca     - pca utils");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        if(args.length < 1) {
            printUsageAndExit();
        }

        String cmd = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        if(cmd.equals("convert")) {
            Convert.main(args);
        } else if(cmd.equals("summary")) {
            BinaryDataSummary.main(args);
        } else if(cmd.equals("scale")) {
            Scale.main(args);
        } else {
            printUsageAndExit();
        }
    }
}
