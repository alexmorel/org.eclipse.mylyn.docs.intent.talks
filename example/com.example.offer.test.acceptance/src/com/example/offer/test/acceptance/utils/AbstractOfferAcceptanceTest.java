package com.example.offer.test.acceptance.utils;

import junit.framework.TestCase;;

public class AbstractOfferAcceptanceTest extends TestCase {

	
	protected boolean login(String userName, String password) {
		return true;
	}
	
	protected boolean logout() {
		return true;
	}
	
	protected ICategoryPage openCategoryPage() {
		return new ICategoryPage();
	}
}
