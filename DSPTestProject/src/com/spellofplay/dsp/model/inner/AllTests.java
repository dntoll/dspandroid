package com.spellofplay.dsp.model.inner;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTest(new GameTests());

		//$JUnit-END$
		return suite;
	}

}
