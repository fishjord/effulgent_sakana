/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fishjord.ml.utils;

import fishjord.ml.io.BinaryDataReader;
import fishjord.ml.io.BinaryDataWriter;
import fishjord.ml.io.DataFormat;
import fishjord.ml.io.DataReader;
import fishjord.ml.io.DataWriter;
import fishjord.ml.io.SVMSparseWriter;
import fishjord.ml.io.TextDataReader;
import fishjord.ml.io.TextDataWriter;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class IOUtils {
    public static int countLines(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        int ret = 0;
        while(reader.readLine() != null) {
            ret += 1;
        }

        return ret;
    }

    public static DataReader getReader(String filename) throws IOException{
        File labelFile = null;
        File dataFile;
        if (filename.contains(",")) {
            labelFile = new File(filename.split(",")[1]);
            dataFile = new File(filename.split(",")[0]);
        } else {
            dataFile = new File(filename);
        }

        return getReader(dataFile, labelFile);
    }

    public static DataReader getReader(File dataFile) throws IOException{
        return getReader(dataFile, null);
    }

    public static DataReader getReader(File dataFile, File labelFile) throws IOException {
        DataFormat format = guessFormat(dataFile);

        if (format == DataFormat.csv) {
            return new TextDataReader(dataFile, ",", labelFile);
        } else if(format == DataFormat.whitespace) {
            return new TextDataReader(dataFile, "\\s", labelFile);
        } else if(labelFile != null) {
            throw new IOException("Label file not expected with format '" + format + "'");
        } else if(format == DataFormat.svm) {
            throw new IOException("I don't support reading svm files at this time");
        } else if(format == DataFormat.binary) {
            return new BinaryDataReader(dataFile);
        } else {
            throw new IOException("Unknown file format '" + format + "'");
        }
    }

    public static DataFormat guessFormat(File dataFile) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(dataFile));
        byte[] buf = new byte[1024];
        int read = is.read(buf);
        String s = new String(buf, 0, read);

        if(s.startsWith("jfml")) {
            return DataFormat.binary;
        } else if(s.contains(",")) {
            return DataFormat.csv;
        } else {
            return DataFormat.whitespace;
        }
    }

    public static DataWriter getWriter(String filename, DataFormat format) throws IOException {
        File labelFile = null;
        File dataFile;
        if (filename.contains(",")) {
            labelFile = new File(filename.split(",")[1]);
            dataFile = new File(filename.split(",")[0]);
        } else {
            dataFile = new File(filename);
        }

        if (format == DataFormat.csv) {
            return new TextDataWriter(dataFile, ',', labelFile);
        } else if(format == DataFormat.whitespace) {
            return new TextDataWriter(dataFile, '\t', labelFile);
        } else if(labelFile != null) {
            throw new IOException("Label file not expected with format '" + format + "'");
        } else if(format == DataFormat.svm) {
            return new SVMSparseWriter(dataFile);
        } else if(format == DataFormat.binary) {
            return new BinaryDataWriter(dataFile);
        } else {
            throw new IOException("Unknown file format '" + format + "'");
        }
    }
}
