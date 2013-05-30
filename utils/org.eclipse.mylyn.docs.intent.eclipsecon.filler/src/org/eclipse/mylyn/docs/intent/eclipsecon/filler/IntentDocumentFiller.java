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
import org.eclipse.mylyn.docs.intent.client.ui.editor.IntentEditor;
import org.eclipse.mylyn.docs.intent.client.ui.editor.IntentEditorDocument;
import org.eclipse.swt.widgets.Display;

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
	public void documentChanged(final DocumentEvent event) {
		final String newText = event.getText();
		if (isReadyToChange) {
			final IntentEditorDocument intentDocument = ((IntentEditorDocument)event.fDocument);
			isReadyToChange = false;
			final String replacement = getText(newText);
			if (replacement != null) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						intentDocument.set(intentDocument.get().replace(FILL_REQUEST_IDENTIFIER + newText,
								replacement));
						IntentEditor intentEditor = ((IntentEditor)intentDocument.getIntentEditor());
						intentEditor
								.getProjectionViewer()
								.getTextWidget()
								.setCaretOffset(
										intentDocument.get().indexOf(replacement) + replacement.length());
					}
				});
			}
		} else {
			if (FILL_REQUEST_IDENTIFIER.equals(newText)) {
				isReadyToChange = true;
			} else {
				isReadyToChange = false;
			}
		}
	}

	private String getText(String textID) {
		if (textIDToText == null) {
			textIDToText = new LinkedHashMap<String, String>();
			textIDToText.put("1", "Scenario S-01: filter offers according to a Category");
			textIDToText
					.put("2",
							"*As a* end-user\n\t\t*I want to* be able to _filter_ offers according to their associated @Category@\n\t\t*Given* A set of offers\n\t\t*When* I select the category entitled \"Books\" in the _Category page_\n\t\t*Then* I should only see the offers associated to this category");
			textIDToText.put("3", "!file:///D:\\Intent_logo.png!");
			textIDToText
					.put("4",
							"* some item\n\t\t* some other item\n\n\t\t# numeric lists are handled too\n\t\t# yes indeed.");
			textIDToText
					.put("5",
							"All scenarios described here have a corresponding Acceptance test referenced by the Offer Acceptance TestSuite.\n\t\t@M\n\t\t\t@ref \"com.example.offer.test.acceptance/src/com/example/offer/test/acceptance/suite/OfferAcceptanceTestSuite.java#/\"\n\t\t\t \n\t\tM@");

		}
		return textIDToText.get(textID);
	}
}
