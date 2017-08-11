package com.hyd.hydrogenpac.utils;

import org.junit.Test;

/**
 * (description)
 * created at 2017/8/11
 *
 * @author yidin
 */
public class RandomStringGeneratorTest {

    @Test
    public void generate() throws Exception {
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomStringGenerator.generate(40, true, true, true));
        }
    }

}