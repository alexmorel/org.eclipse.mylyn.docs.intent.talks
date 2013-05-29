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
			textIDToText.put("1", "some *text* with @Styling@");
		}
		return textIDToText.get(textID);
	}
}
