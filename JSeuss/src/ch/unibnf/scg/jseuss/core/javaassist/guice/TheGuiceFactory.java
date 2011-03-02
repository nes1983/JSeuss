package ch.unibnf.scg.jseuss.core.javaassist.guice;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class TheGuiceFactory {

	private URLClassLoader urlClassLoader;

	private ClassDetails containerClass;
	private ClassDetails guiceBinder;
	private ClassDetails guiceProvider;

	private boolean createJar = false;
	private String basePkg = "jseuss.generated.";
	private String providerFullName = null;
	private String binderFullName = null;

	public TheGuiceFactory(String basePakage, boolean bundleJar) {
		urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		createJar = bundleJar;
		if(basePakage != null && basePakage.length() > 0)
			basePkg = basePakage;
		if(!basePkg.endsWith("."))
			basePkg += ".";
		providerFullName = basePkg + "GuiceProviderImplementation";
		binderFullName = basePkg + "GuiceBinderImplementation";
	}

	public Class<?> makeJavaInterface(Class<?> concreteClass,
			String interfaceName) {
		String interfaceFullName = basePkg + interfaceName;
		Class<?> theInterface = JavaInterfaceGenerator.generate(concreteClass,
				interfaceFullName, createJar);
		System.out.println("generated: " + theInterface.getName());
		return theInterface;
	}

	public boolean toGuiceCode(Class<?> container, Class<?> toBeReplaced,
			Class<?> toReplace, Class<?> theInterface) {
		boolean done = false;
		init(container, toBeReplaced, toReplace, theInterface);

		guiceProvider = GuiceProviderGenerator.generate(
				toReplace, theInterface, providerFullName, false);
		addToClassPath(guiceProvider);

		guiceBinder = GuiceBinderGenerator.generate(
				guiceProvider.getTheClass(), theInterface, binderFullName,
				false);
		addToClassPath(guiceBinder);

		containerClass = 
				ContainerClassHandler.makeGuiceCompliant(container,
						toBeReplaced, theInterface, guiceBinder.getTheClass(),
						false);
		addToClassPath(containerClass);

		if (createJar) {
			// create Jar out of this generated code.
		}

		if (guiceProvider.isDone() && containerClass.isDone()
				&& containerClass.isDone()) {
			done = true;
		}
		return done;
	}

	private void init(Class<?> container, Class<?> toBeReplaced,
			Class<?> toReplace, Class<?> theInterface) {
		containerClass = new ClassDetails(container);

	}

	@SuppressWarnings("deprecation")
	private void addToClassPath(ClassDetails cd) {
		try {
			File f = cd.getTheClassFile();
			URL u = f.toURL();
			Class<?> urlClass = URLClassLoader.class;
			Method method = urlClass.getDeclaredMethod("addURL",
					new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(urlClassLoader, new Object[] { u });
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
