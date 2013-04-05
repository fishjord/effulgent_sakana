/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fishjord.ml.io;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class DataLine {
    Integer label;
    float[] pattern;

    public Integer getLabel() {
        return label;
    }

    public float[] getPattern() {
        return pattern;
    }
}
