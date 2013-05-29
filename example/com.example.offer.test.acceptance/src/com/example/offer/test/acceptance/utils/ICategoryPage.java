package com.example.offer.test.acceptance.utils;

import java.util.ArrayList;
import java.util.Collection;

import com.example.offer.catalog.api.IOffer;
import com.example.offer.catalog.categories.BookCategory;

public class ICategoryPage {

	
	public boolean selectCategory(String categoryName) {
		return new BookCategory().categoryName.equals(categoryName);
	}
	
	public Collection<IOffer> getAvailableOffers() {
		Collection<IOffer> availableOffers =  new ArrayList<IOffer>();
		availableOffers.add(new IOffer());
		return availableOffers;
	}
}
