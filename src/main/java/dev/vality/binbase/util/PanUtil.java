package dev.vality.binbase.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import static dev.vality.binbase.util.BinBaseConstant.DEFAULT_SIZE;
import static dev.vality.binbase.util.BinBaseConstant.RIGHT_PAD_SIZE;

@UtilityClass
public class PanUtil {

    public static void validatePan(String pan) throws IllegalArgumentException {
        if (!pan.matches("^\\d{6,19}$")) {
            throw new IllegalArgumentException("Invalid pan format");
        }
    }

    public static long toLongValue(String pan) throws IllegalArgumentException {
        validatePan(pan);
        return Long.parseLong(
                StringUtils.rightPad(pan.substring(0, Math.min(pan.length(), RIGHT_PAD_SIZE)), DEFAULT_SIZE, "0"));
    }

    public static String formatPan(String pan) {
        return StringUtils.rightPad(pan.substring(0, Math.min(pan.length(), 6)), pan.length(), '*');
    }

}
