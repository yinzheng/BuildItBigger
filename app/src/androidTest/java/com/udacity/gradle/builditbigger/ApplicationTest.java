package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.v4.util.Pair;
import android.test.InstrumentationTestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Iris on 31/01/2017.
 * */
public class ApplicationTest extends InstrumentationTestCase
        implements EndpointsAsyncTask.AsyncResponse {
    public void testAsyncTask () throws Throwable {
        // create  a signal to let us know when our task is done.
        final CountDownLatch signal = new CountDownLatch(1);

        final EndpointsAsyncTask myTask
                = new EndpointsAsyncTask(this) {
            @Override
            protected void onPostExecute(String result) {
                delegate.processFinish(result);
                signal.countDown();
            }
        };

        // Execute the async task on the UI thread! THIS IS KEY!
        runTestOnUiThread(new Runnable() {

            @Override
            public void run() {
                myTask.execute();
            }
        });

        /* The testing thread will wait here until the UI thread releases it
     * above with the countDown() or 30 seconds passes and it times out.
     */
        signal.await(30, TimeUnit.SECONDS);
    }

    @Override
    public void processFinish(String result) {
        assertFalse(result, equals(""));
    }
}
