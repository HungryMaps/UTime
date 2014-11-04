package com.example.android.utime.app;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.TextView;

/**
 * Created by Joan Marchena on 03/11/14.
 * Se prueba el Activity
 */


public class EvaluacionTest extends ActivityUnitTestCase<Evaluacion> {
    private Evaluacion mTestActivity;
    private TextView mTestText;

    public EvaluacionTest() {
        super(Evaluacion.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Starts the MainActivity of the target application
        startActivity(new Intent(getInstrumentation().getTargetContext(), Evaluacion.class), null, null);

        // Getting a reference to the MainActivity of the target application
        mTestActivity = (Evaluacion) getActivity();

        // Getting a reference to the TextView of the MainActivity of the target application
        mTestText = (TextView) mTestActivity.findViewById(R.id.Nota);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
    *Prueba para un TextView del Activity de Evaluacion
     */
    public void testNota() {
        // The actual text displayed in the textview
        String actual = mTestText.getText().toString();

        // The expected text to be displayed in the textview
        String expected = "NOTA";

        // Check whether both are equal, otherwise test fails
        assertEquals(expected, actual.substring(0, 4));
    }


}

