package com.example.doz.sunfordummies;

import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        LocalTime time = LocalTime.now();
        time.toString();
    }
}