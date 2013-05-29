package com.example.offer.test.acceptance.suite;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * The TestSuite launching all acceptance tests defined for the Offer.com platform.
 */
public class OfferAcceptanceTestSuite extends TestSuite {
	/**
	 * Launches the collaborative test suite.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * Creates the {@link junit.framework.TestSuite TestSuite} for all Intent UI tests.
	 * 
	 * @return The test suite containing all intent ui tests
	 */
	public static Test suite() {
		final TestSuite suite = new TestSuite("Offers.com - Acceptance Tests");
		
		return suite;
	}

}
