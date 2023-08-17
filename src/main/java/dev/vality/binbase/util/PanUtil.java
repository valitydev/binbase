package dev.vality.binbase.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import static dev.vality.binbase.util.BinBaseConstant.DEFAULT_SIZE;
import static dev.vality.binbase.util.BinBaseConstant.RIGHT_PAD_SIZE;

@UtilityClass
public class PanUtil {

    public static void validatePan(String pan) throws IllegalArgumentException {
        if (!pan.matches("^\\d{4,19}$")) {
            throw new IllegalArgumentException("Invalid pan format");
        }
    }

    public static long toLongValue(String pan) throws IllegalArgumentException {
        validatePan(pan);
        // add left 0 to get conventionally formatted pan (as it was before) and then produce correct-length range
        String lpaddedPan = pan.length() < 6 ? StringUtils.leftPad(pan, 6, "0") : pan;
        return Long.parseLong(
                StringUtils.rightPad(lpaddedPan.substring(0, Math.min(lpaddedPan.length(), RIGHT_PAD_SIZE)), DEFAULT_SIZE, "0"));
    }

    public static String formatPan(String pan) {
        return StringUtils.rightPad(pan.substring(0, Math.min(pan.length(), 6)), pan.length(), '*');
    }

}
