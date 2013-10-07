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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.mylyn.docs.intent.bridge.java.Classifier;
import org.eclipse.mylyn.docs.intent.bridge.java.Field;
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
		boolean noIssues = true;
		if (method.getClassifierPath().endsWith("Tests.java")) {
			// Get the context from import
			Classifier context = extractContextFromImports(method);
			if (context != null) {
				// We get each calls to a method of the context
				for (String contextCall : extractContextCalls(method.getContent())) {
					// This method should be implemented in context
					for (Method contextMethod : context.getMethods()) {
						if (contextCall.equals(contextMethod.getSimpleName())) {
							// i.e. not contains 'TO DO'
							if (contextMethod.getContent().contains("TODO")) {
								diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR,
										"org.eclipse.mylyn.docs.intent.eclipsecon.filler", 0,
										"Context method " + context.getClassifierPath() + "."
												+ contextMethod.getName()
												+ " is not implemented, but it is called in "
												+ method.getName(), null));
								noIssues = false;
							}
						}
					}
				}

			}
		}
		return noIssues;
	}

	/**
	 * Returns a collection containing all methods called on the context in the given method content.
	 * 
	 * @param content
	 *            the content of a method to analyse
	 * @return a collection containing all methods called on the context in the given method content
	 */
	private Collection<String> extractContextCalls(String content) {
		Collection<String> contextCalls = new ArrayList<String>();
		String[] methodLines = content.replaceAll(";", "").split("\n");
		for (int i = 0; i < methodLines.length; i++) {
			if (methodLines[i].startsWith("context.")) {
				contextCalls.add(methodLines[i].replace("context.", "").replace("()", ""));
			}
		}
		return contextCalls;

	}

	/**
	 * Returns the Context corresponding to the "context" field of the given classifer (if any).
	 * 
	 * @param method
	 *            the {@link Method} referencing a context
	 * @return the Context corresponding to the "context" field of the given classifer (if any)
	 */
	private Classifier extractContextFromImports(Method method) {
		Field contextField = null;
		Classifier context = null;

		// Step 1: extract classifier from method
		if (method.getClassifierPath() != null) {
			Classifier methodClassifier = (Classifier)method.eResource().getResourceSet()
					.getResource(URI.createURI(method.getClassifierPath()), true).getContents().iterator()
					.next();

			// Step 2: search for a "context" field
			Iterator<Field> fieldsIterator = methodClassifier.getFields().iterator();
			while (contextField == null && fieldsIterator.hasNext()) {
				Field next = fieldsIterator.next();
				if ("context".equals(next.getName())) {
					contextField = next;
				}
			}
		}
		// Step 2: get the corresponding classifier
		if (contextField != null) {
			// String contextPath = classifier.eResource().getURI().
			String contextPath = method.getClassifierPath().substring(0,
					method.getClassifierPath().lastIndexOf("src"))
					+ "src/" + contextField.getQualifiedType().replace(".", "/") + ".java";
			return (Classifier)method.eResource().getResourceSet()
					.getResource(URI.createURI(contextPath), true).getContents().iterator().next();
		}
		return context;
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
