package com.example.offer.catalog.offertypes;

import com.example.offer.catalog.api.ICustomerType;
import com.example.offer.catalog.api.IOfferType;

public class SpecialPromotionOfferType implements IOfferType {
	private static final String OFFER_TYPE_DESCRIPTION = "Offer subject to applicable law";

	@Override
	public String getOfferDescription() {
		return OFFER_TYPE_DESCRIPTION;
	}

	@Override
	public ICustomerType getTargetCustomerTypes() {
		// TODO Auto-generated method stub
		return null;
	}

}
