package org.eclipse.mylyn.docs.intent.eclipsecon.filler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EValidator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.mylyn.docs.intent.bridge.java.JavaPackage;
import org.eclipse.mylyn.docs.intent.eclipsecon.validation.TestNotImplementedValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	/**
	 * The images and icons related to this plugin.
	 */
	private static Map<String, Image> imageMap = new HashMap<String, Image>();

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
		EValidator.Registry.INSTANCE.put(JavaPackage.eINSTANCE, new TestNotImplementedValidator());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	/**
	 * Returns the image at the given plug-in relative path ; if this image hasn't been loaded yet, load this
	 * image and add it to the imageMap.
	 * 
	 * @param path
	 *            path of the image to load (plug-in relative path)
	 * @return the image corresponding to the given path
	 */
	public static Image getImage(String path) {
		Image result = imageMap.get(path);
		if (result == null) {
			ImageDescriptor descriptor = getImageDescriptor(path);
			if (descriptor != null) {
				result = descriptor.createImage();
				imageMap.put(path, result);
			}
		}
		return result;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path.
	 * 
	 * @param imagePath
	 *            path of the image to load (plug-in relative path)
	 * @return the image descriptor of the image corresponding to the given path
	 */
	private static ImageDescriptor getImageDescriptor(String imagePath) {
		URL fullPathString = BundleUtility.find(getContext().getBundle(), imagePath);
		if (fullPathString == null) {
			try {
				fullPathString = new URL(imagePath);
			} catch (MalformedURLException e) {
				return null;
			}
		}
		return ImageDescriptor.createFromURL(fullPathString);
	}
}
