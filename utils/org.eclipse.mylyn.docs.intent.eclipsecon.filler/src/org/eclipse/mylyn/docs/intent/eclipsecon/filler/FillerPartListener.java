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

import org.eclipse.jface.text.IDocument;
import org.eclipse.mylyn.docs.intent.client.ui.editor.IntentEditor;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

public class FillerPartListener implements IPartListener {

	private IntentDocumentFiller intentDocumentFiller;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void partOpened(IWorkbenchPart part) {
		if (part instanceof IntentEditor) {
			IDocument intentDocument = ((IntentEditor)part).getDocumentProvider().getDocument(
					((IntentEditor)part).getEditorInput());
			if (intentDocumentFiller == null) {
				intentDocumentFiller = new IntentDocumentFiller();
			}
			intentDocument.addDocumentListener(intentDocumentFiller);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void partClosed(IWorkbenchPart part) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void partBroughtToTop(IWorkbenchPart part) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void partDeactivated(IWorkbenchPart part) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void partActivated(IWorkbenchPart part) {

	}

}
