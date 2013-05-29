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

import org.eclipse.mylyn.docs.intent.client.ui.editor.annotation.IntentAnnotation;
import org.eclipse.mylyn.docs.intent.client.ui.editor.quickfix.AbstractIntentFix;
import org.eclipse.mylyn.docs.intent.client.ui.editor.quickfix.provider.IntentQuickFixProvider;

public class IntentQuickFixProvider1 implements IntentQuickFixProvider {

	public IntentQuickFixProvider1() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canCreateQuickFix(IntentAnnotation annotation) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AbstractIntentFix createQuickFix(IntentAnnotation annotation) {
		// TODO Auto-generated method stub
		return null;
	}

}
