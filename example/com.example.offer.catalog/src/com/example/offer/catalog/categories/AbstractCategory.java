package com.example.offer.catalog.categories;

import com.example.offer.catalog.api.IOfferCategory;

public class AbstractCategory implements IOfferCategory {

	public String categoryName;

	public AbstractCategory(String categoryName) {
		this.categoryName = categoryName;
	}
}
