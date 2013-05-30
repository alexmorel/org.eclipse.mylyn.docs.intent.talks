package org.eclipse.mylyn.docs.intent.eclipsecon.filler;

import org.eclipse.emf.ecore.EValidator;
import org.eclipse.mylyn.docs.intent.bridge.java.JavaPackage;
import org.eclipse.mylyn.docs.intent.eclipsecon.validation.CustomOffersValidator;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// Registering the filler part listener
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.addPartListener(new FillerPartListener());

		// Plug a custom validation rule
		EValidator.Registry.INSTANCE.put(JavaPackage.eINSTANCE, new CustomOffersValidator());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
