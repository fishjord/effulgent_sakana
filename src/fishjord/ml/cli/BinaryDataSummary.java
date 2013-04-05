/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fishjord.ml.cli;

import fishjord.ml.io.BinaryDataReader;
import fishjord.ml.io.DataFormat;
import fishjord.ml.utils.DataUtils;
import fishjord.ml.utils.DataUtils.DataAttributes;
import fishjord.ml.utils.IOUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class BinaryDataSummary {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("USAGE: BinaryDataSummary <binary_data_file>");
            System.exit(1);
        }

        File inputFile = new File(args[0]);
        System.out.println("Summary for " + inputFile);
        if (IOUtils.guessFormat(inputFile) == DataFormat.binary) {
            BinaryDataReader reader = new BinaryDataReader(inputFile);
            System.out.println("Labels included?: " + reader.isLabelsPresent());
            System.out.println("Num patterns: " + reader.getNumPatterns());
            System.out.println("Num Features: " + reader.getNumFeatures());
        } else {
            ObjectInputStream dis = new ObjectInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
            Object o = dis.readObject();
            dis.close();

            if(o instanceof DataAttributes) {
                DataUtils.writeDataAttrs((DataAttributes)o, System.out);
            } else {
                System.out.println(o);
            }
        }
    }
}
