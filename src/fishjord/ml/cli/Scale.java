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
import fishjord.ml.utils.DataUtils;
import fishjord.ml.utils.DataUtils.DataAttributes;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class Scale {

    public static void main(String[] args) throws IOException {
        if(args.length != 2) {
            System.err.println("USAGE: Scale <input_data> <output_data>");
            System.exit(1);
        }

        File inFile = new File(args[0]);
        DataAttributes dataAttributes = DataUtils.computeDataStats(inFile);
        DataUtils.writeDataAttrs(dataAttributes, System.out);

        DataLine pattern = new DataLine();
        DataReader reader = IOUtils.getReader(inFile);
        DataWriter writer = IOUtils.getWriter(args[1], DataFormat.binary);

        while(reader.readNextPattern(pattern)) {
            DataUtils.scaleMean(pattern, dataAttributes);
            writer.writePattern(pattern);
        }

        reader.close();
        writer.close();
    }
}
