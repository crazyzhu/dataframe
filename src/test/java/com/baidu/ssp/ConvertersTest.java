package com.baidu.ssp;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

import static com.baidu.ssp.type.Converters.convert;

/**
 * Created by yijiezhu on 15-2-8.
 */
public class ConvertersTest {
    @Test
    public void testConverters() throws Exception {
        Assert.assertEquals(convert(1, String.class), "1");
        Assert.assertEquals(convert(1, Boolean.class), true);
        Assert.assertEquals(convert("1.1", Double.class), (Double) 1.1);
        Assert.assertEquals(convert("1", Double.class), (Double) 1d);
        Assert.assertEquals(convert("122131432143332141243124314", BigInteger.class),
                new BigInteger("122131432143332141243124314"));
    }
}
