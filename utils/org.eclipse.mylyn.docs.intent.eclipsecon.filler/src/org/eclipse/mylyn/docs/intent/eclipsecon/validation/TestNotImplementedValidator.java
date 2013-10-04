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
import org.eclipse.mylyn.docs.intent.bridge.java.Method;

public class TestNotImplementedValidator implements EValidator {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.EValidator#validate(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 */
	@Override
	public boolean validate(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate(eObject.eClass(), eObject, diagnostics, context);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.EValidator#validate(org.eclipse.emf.ecore.EClass,
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 */
	@Override
	public boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		boolean validated = true;
		if (eObject instanceof Method) {
			validated = validateMethod((Method)eObject, diagnostics);
		}
		return validated;
	}

	private boolean validateMethod(Method method, DiagnosticChain diagnostics) {
		if (method.getContent().split("\n").length < 2) {
			diagnostics
					.add(new BasicDiagnostic(Diagnostic.ERROR,
							"org.eclipse.mylyn.docs.intent.eclipsecon.filler", 0,
							"Test method not implemented", null));
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.EValidator#validate(org.eclipse.emf.ecore.EDataType, java.lang.Object,
	 *      org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 */
	@Override
	public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

}
