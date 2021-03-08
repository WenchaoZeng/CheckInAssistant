package com.zwc.clockinassistant;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test() {
        Global.setCheckoutTimeFromString("23:19");
        String a = Global.getCheckoutTimeAsString();
        long b = Global.diffToCheckoutTime(new Date());
        int temp = 1;
    }
}