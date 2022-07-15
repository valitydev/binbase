package dev.vality.binbase.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BinBaseConstant {

    public static final long MIN_LOWER_ENDPOINT = 0L;
    public static final long MAX_UPPER_ENDPOINT = (long) Math.pow(10, 18);
    public static final long DEFAULT_MIN_LOWER_ENDPOINT = (long) Math.pow(10, 17);

    public static final int DEFAULT_SIZE = 18;
    public static final int RIGHT_PAD_SIZE = 18;

    public static final String FILE_EXTENSION_XML = "xml";
    public static final String FILE_EXTENSION_CSV = "csv";

}
