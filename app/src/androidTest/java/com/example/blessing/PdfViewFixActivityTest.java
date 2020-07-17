package com.example.blessing;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class PdfViewFixActivityTest {
@Rule
public ActivityTestRule<PdfViewFixActivity> activityActivityTestRule = new ActivityTestRule<>(PdfViewFixActivity.class);
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testativity()throws Exception{
    Thread.sleep(3000);
    }
}