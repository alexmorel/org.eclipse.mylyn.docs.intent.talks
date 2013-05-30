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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.mylyn.docs.intent.bridge.java.Classifier;
import org.eclipse.mylyn.docs.intent.client.ui.editor.IntentEditorDocument;
import org.eclipse.mylyn.docs.intent.client.ui.editor.annotation.IntentAnnotation;
import org.eclipse.mylyn.docs.intent.client.ui.editor.quickfix.AbstractIntentFix;
import org.eclipse.mylyn.docs.intent.client.ui.editor.quickfix.provider.IntentQuickFixProvider;
import org.eclipse.mylyn.docs.intent.client.ui.logger.IntentUiLogger;
import org.eclipse.mylyn.docs.intent.collab.handlers.adapters.IntentCommand;
import org.eclipse.mylyn.docs.intent.collab.handlers.adapters.RepositoryAdapter;
import org.eclipse.mylyn.docs.intent.core.modelingunit.ExternalContentReference;

public class OfferQuickFixProvider implements IntentQuickFixProvider {

	public OfferQuickFixProvider() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.docs.intent.client.ui.editor.quickfix.provider.IntentQuickFixProvider#canCreateQuickFix(org.eclipse.mylyn.docs.intent.client.ui.editor.annotation.IntentAnnotation)
	 */
	@Override
	public boolean canCreateQuickFix(IntentAnnotation annotation) {
		return (annotation.getCompilationStatus().getTarget() instanceof ExternalContentReference)
				&& (((ExternalContentReference)annotation.getCompilationStatus().getTarget())
						.getExternalContent() instanceof Classifier);
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
		final Classifier classifier = (Classifier)externalContentRef.getExternalContent();
		final String newName = "testScenario1";
		return new AbstractIntentFix(annotation) {

			@Override
			public String getDisplayString() {
				return "Rename test method to 'testScenario1()'";
			}

			@Override
			protected void applyFix(RepositoryAdapter repositoryAdapter, IntentEditorDocument document) {
				IFile file = ResourcesPlugin.getWorkspace().getRoot()
						.getFile(new Path(externalContentRef.getUri().trimFragment().toString()));
				if (file != null && file.exists()) {
					IJavaElement javaElement = JavaCore.create(file);
					if (javaElement instanceof ICompilationUnit) {
						try {
							IMethod method = ((ICompilationUnit)javaElement).getAllTypes()[0].getMethods()[0];
							method.rename("testScenario01", true, new NullProgressMonitor());
						} catch (JavaModelException e) {
							IntentUiLogger.logError(e);
						}
					}
				}
				repositoryAdapter.execute(new IntentCommand() {

					@Override
					public void execute() {
						externalContentRef.setMarkedAsMerged(true);
					}

				});
				document.set(document.get() + " ");

			}
		};
	}
}
