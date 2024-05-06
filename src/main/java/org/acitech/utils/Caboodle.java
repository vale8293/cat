package org.acitech.utils;

/** A caboodle of useful functions */
public class Caboodle {

    public static double wrap(double value, double lower, double upper) {
        double range = upper - lower;
        return ((value - lower) % range + range) % range + lower;
    }

    public static double clamp(double value, double lower, double upper) {
        return Math.max(Math.min(value, upper), lower);
    }

}
