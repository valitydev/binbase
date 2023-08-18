package dev.vality.binbase.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PanUtilTest {

    @Test
    void testValidatePan() {
        PanUtil.validatePan("220020");
        PanUtil.validatePan("2200200249999999999");
        assertThrows(IllegalArgumentException.class, () -> PanUtil.validatePan("22003/"));
        assertDoesNotThrow(() -> PanUtil.validatePan("22003"));
        assertThrows(IllegalArgumentException.class, () -> PanUtil.validatePan("22002002499999999999"));
    }

    @Test
    void testToLongValue() {
        assertEquals(220020000000000000L, PanUtil.toLongValue("220020"));
        assertEquals(220012323200000000L, PanUtil.toLongValue("22001232320"));
        assertEquals(234234234234234234L, PanUtil.toLongValue("234234234234234234"));
        assertEquals(324234234234234432L, PanUtil.toLongValue("3242342342342344324"));
        assertEquals(999999999999999999L, PanUtil.toLongValue("9999999999999999999"));
        assertEquals(100001000000000000L, PanUtil.toLongValue("100001"));
        assertEquals(1244000000000000L, PanUtil.toLongValue("001244"));
        assertEquals(1244000000000000L, PanUtil.toLongValue("1244"));
    }

    @Test
    void testFormatPan() {
        assertEquals("123", PanUtil.formatPan("123"));
        assertEquals("123322*", PanUtil.formatPan("123322/"));
        assertEquals("123322", PanUtil.formatPan("123322"));
        assertEquals("123322*", PanUtil.formatPan("1233222"));
        assertEquals("123322****", PanUtil.formatPan("1233222321"));
        assertEquals("233222*************", PanUtil.formatPan("2332222312312312333"));
    }

}
