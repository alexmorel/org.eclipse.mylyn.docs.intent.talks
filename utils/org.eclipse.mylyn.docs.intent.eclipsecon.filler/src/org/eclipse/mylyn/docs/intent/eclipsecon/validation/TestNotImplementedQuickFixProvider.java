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
package org.eclipse.mylyn.docs.intent.eclipsecon.validation;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.mylyn.docs.intent.bridge.java.Classifier;
import org.eclipse.mylyn.docs.intent.bridge.java.Field;
import org.eclipse.mylyn.docs.intent.bridge.java.Method;
import org.eclipse.mylyn.docs.intent.bridge.java.resource.factory.JavaResourceFactory;
import org.eclipse.mylyn.docs.intent.client.ui.editor.IntentEditorDocument;
import org.eclipse.mylyn.docs.intent.client.ui.editor.annotation.IntentAnnotation;
import org.eclipse.mylyn.docs.intent.client.ui.editor.quickfix.AbstractIntentFix;
import org.eclipse.mylyn.docs.intent.client.ui.editor.quickfix.provider.IntentQuickFixProvider;
import org.eclipse.mylyn.docs.intent.client.ui.logger.IntentUiLogger;
import org.eclipse.mylyn.docs.intent.collab.handlers.adapters.RepositoryAdapter;
import org.eclipse.mylyn.docs.intent.core.modelingunit.ExternalContentReference;
import org.eclipse.mylyn.docs.intent.eclipsecon.filler.Activator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class TestNotImplementedQuickFixProvider implements IntentQuickFixProvider {

	public TestNotImplementedQuickFixProvider() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.docs.intent.client.ui.editor.quickfix.provider.IntentQuickFixProvider#canCreateQuickFix(org.eclipse.mylyn.docs.intent.client.ui.editor.annotation.IntentAnnotation)
	 */
	@Override
	public boolean canCreateQuickFix(IntentAnnotation annotation) {
		if (annotation.getCompilationStatus().getTarget() instanceof ExternalContentReference) {
			EObject externalContent = ((ExternalContentReference)annotation.getCompilationStatus()
					.getTarget()).getExternalContent();
			return externalContent instanceof Classifier || externalContent instanceof Method;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.docs.intent.client.ui.editor.quickfix.provider.IntentQuickFixProvider#createQuickFix(org.eclipse.mylyn.docs.intent.client.ui.editor.annotation.IntentAnnotation)
	 */
	@Override
	public AbstractIntentFix createQuickFix(IntentAnnotation annotation) {
		final ExternalContentReference externalContentRef = (ExternalContentReference)annotation
				.getCompilationStatus().getTarget();
		Method method = null;
		if (externalContentRef.getExternalContent() instanceof Classifier) {
			Iterator<Method> methodsIterator = ((Classifier)externalContentRef.getExternalContent())
					.getMethods().iterator();
			while (method == null && methodsIterator.hasNext()) {
				Method next = methodsIterator.next();
				if (next.getContent().split("\n").length < 2) {
					method = next;
				}
			}
		} else if (externalContentRef.getExternalContent() instanceof Method) {
			method = (Method)externalContentRef.getExternalContent();
		}
		final String methodName = method.getName();
		return new AbstractIntentFix(annotation) {

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.mylyn.docs.intent.client.ui.editor.quickfix.AbstractIntentFix#getImage()
			 */
			@Override
			public Image getImage() {
				return Activator.getImage("icon/quickfix.gif");
			}

			@Override
			public String getDisplayString() {
				return "Implement test method " + methodName;
			}

			@Override
			protected void applyFix(RepositoryAdapter repositoryAdapter, IntentEditorDocument document) {
				URI javaElementURI = URI.createURI(externalContentRef.getUri().toString().trim());
				IFile javaFile = ResourcesPlugin.getWorkspace().getRoot()
						.getFile(new Path(javaElementURI.trimFragment().toString()));
				FileEditorInput editorInput = new FileEditorInput(javaFile);
				IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry()
						.getDefaultEditor(javaElementURI.trimFragment().lastSegment());
				IEditorPart openedEditor = null;
				if (desc != null) {
					try {
						openedEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
								.openEditor(editorInput, desc.getId());
					} catch (PartInitException e) {
						IntentUiLogger.logError(e);
					}
				}

				// Step 2: select element described by URI (if any)
				if (openedEditor != null && javaElementURI.hasFragment()) {
					updateOpenedEditorSelection(openedEditor, javaFile, javaElementURI);
				}

			}
		};
	}

	/**
	 * Selects the java element (e.g. method, field...) described by the given {@link URI}.
	 * 
	 * @param openedEditor
	 *            the editor to update
	 * @param javaFile
	 *            the java file
	 * @param javaElementURI
	 *            the {@link URI} of the element to select
	 */
	private void updateOpenedEditorSelection(IEditorPart openedEditor, IFile javaFile, URI javaElementURI) {
		EObject eJavaElement = new JavaResourceFactory().createResource(javaElementURI.trimFragment())
				.getEObject(javaElementURI.fragment());
		try {
			IType javaType = ((ICompilationUnit)JavaCore.create(javaFile)).getTypes()[0];

			ISourceReference matchingElement = null;
			if (eJavaElement instanceof Classifier) {
				matchingElement = javaType;
			} else if (eJavaElement instanceof Method) {
				for (IMethod method : javaType.getMethods()) {
					if (method.getElementName().equals(((Method)eJavaElement).getSimpleName())) {
						// Todo consider parameters
						matchingElement = method;
					}
				}
			} else if (eJavaElement instanceof Field) {
				matchingElement = javaType.getField(((Field)eJavaElement).getName());
			}
			if (matchingElement != null) {
				ITextSelection textSelection = new TextSelection(
						matchingElement.getSourceRange().getOffset(), matchingElement.getSourceRange()
								.getLength());
				openedEditor.getEditorSite().getSelectionProvider().setSelection(textSelection);
			}
		} catch (JavaModelException e) {
			// Silent catch, element will not be selected
		}
	}
}
