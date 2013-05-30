/**
 * Copyright (c) 2013 THALES GLOBAL SERVICES
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Obeo - Initial API and implementation
 **/
package org.eclipse.mylyn.docs.intent.eclipsecon.validation;

import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.mylyn.docs.intent.bridge.java.Classifier;
import org.eclipse.mylyn.docs.intent.bridge.java.Method;

public class CustomOffersValidator implements EValidator {

	@Override
	public boolean validate(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate(eObject.eClass(), eObject, diagnostics, context);
	}

	@Override
	public boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		if (eObject instanceof Classifier) {
			Classifier classifier = (Classifier)eObject;
			if (classifier.getName().contains("Scenar")) {
				for (Method method : classifier.getMethods()) {
					if (!(method.getName().startsWith("testScenario"))) {
						diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR,
								"org.eclipse.mylyn.docs.intent.eclipsecon.filler", 0, "The Acceptance Test '"
										+ classifier.getName().substring(
												classifier.getName().lastIndexOf(".") + 1)
										+ "' contains an uncorrectly named method ('"
										+ method.getSimpleName()
										+ "') : should be of the form 'testScenario SCENARIO_ID'.", null));
						return false;
					}

				}
			}
		}
		return true;
	}

	@Override
	public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

}
