/*******************************************************************************
 * Copyright (c) 2010, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.docs.intent.eclipsecon.filler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;

public class IntentDocumentFiller implements IDocumentListener {

	private static final String FILL_REQUEST_IDENTIFIER = "x";

	boolean isReadyToChange = false;

	private Map<String, String> textIDToText;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.text.IDocumentListener#documentAboutToBeChanged(org.eclipse.jface.text.DocumentEvent)
	 */
	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.text.IDocumentListener#documentChanged(org.eclipse.jface.text.DocumentEvent)
	 */
	@Override
	public void documentChanged(DocumentEvent event) {
		if (isReadyToChange) {
			String textID = event.getText();
			isReadyToChange = false;
			String replacement = getText(textID);
			if (replacement == null) {
				replacement = FILL_REQUEST_IDENTIFIER + textID;
			}

			event.fDocument.set(event.fDocument.get().replace(FILL_REQUEST_IDENTIFIER + textID, replacement));
		} else {
			if (FILL_REQUEST_IDENTIFIER.equals(event.getText())) {
				isReadyToChange = true;
			} else {
				isReadyToChange = false;
			}
		}
	}

	private String getText(String textID) {
		if (textIDToText == null) {
			textIDToText = new LinkedHashMap<String, String>();
			textIDToText.put("1", "some *text* with @Styling@");
		}
		return textIDToText.get(textID);
	}
}
