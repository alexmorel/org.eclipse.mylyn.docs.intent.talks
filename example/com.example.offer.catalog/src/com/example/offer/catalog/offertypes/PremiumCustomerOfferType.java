package com.example.offer.catalog.offertypes;

import com.example.offer.catalog.api.ICustomerType;

import com.example.offer.catalog.api.IOfferType;

public class PremiumCustomerOfferType implements IOfferType {
	private static final String OFFER_TYPE_DESCRIPTION = "Offer limited to premium members";

	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public String getOfferDescription() {
		return OFFER_TYPE_DESCRIPTION;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICustomerType getTargetCustomerTypes() {
		// TODO Auto-generated method stub
		return null;
	}
}
