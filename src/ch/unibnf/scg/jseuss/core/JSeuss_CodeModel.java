package ch.unibnf.scg.jseuss.core;

import java.io.File;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCase;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JSwitch;
import com.sun.codemodel.JVar;

public class JSeuss_CodeModel {

	public boolean generateJavaFactory(Class<?> baseInterface, String factoryPackage,
			String factoryName, Class<?>[] selectorEnums) {
		boolean generated = false;
		JCodeModel codemodel = new JCodeModel();
		JPackage rootPackage = codemodel.rootPackage();
		JPackage fPackage = rootPackage.subPackage(factoryPackage);
		try {
			JDefinedClass factoryClass = fPackage._class(JMod.PUBLIC,
					factoryName);
			JDefinedClass factoryEnum = factoryClass._enum(JMod.PUBLIC, "FactoryType");
			JMethod factoryMethod = factoryClass.method(JMod.PUBLIC,
					baseInterface, "createInstance");
			JVar methodParameter = factoryMethod.param(factoryEnum, "concreteType");
			JBlock methodBody = factoryMethod.body();
			JSwitch bodySwitch = methodBody._switch(methodParameter);
			
			// create an enum which will make it easy to use our factory
			for(int i = 0; i < selectorEnums.length; i++){
				Class<?> c = selectorEnums[i];
				JCase aCase = bodySwitch._case(factoryEnum.enumConstant(c.getSimpleName()));
				JBlock caseBody = aCase.body();
				caseBody._return(JExpr._new(codemodel.ref(selectorEnums[i])));
			}
			codemodel.build(new File("./src/"));
			generated = true;
		} catch (JClassAlreadyExistsException e) {
			e.printStackTrace();
			generated = false;
		} catch (IOException e) {
			e.printStackTrace();
			generated = false;
		}
		return generated;
	}

	public boolean generateJavaInterface(Class<?> concreteClass,
			String interfacePackage, String interfaceName) {
		boolean generated = false;
		JCodeModel codemodel = new JCodeModel();
		JPackage rootPackage = codemodel.rootPackage();
		JPackage iPackage = rootPackage.subPackage(interfacePackage);
		// JPackage iPackage =
		// rootPackage.subPackage(getPackageName(concreteClass));
		try {
			JDefinedClass definedInterface = iPackage._interface(interfaceName);
			for (Method method : concreteClass.getDeclaredMethods()) {
				if (Modifier.isPublic(method.getModifiers())
						&& !Modifier.isStatic(method.getModifiers())) {
					JMethod iMethod = definedInterface.method(JMod.PUBLIC,
							method.getReturnType(), method.getName());
					for (Class ex : method.getExceptionTypes()) {
						// add exceptions
						iMethod._throws(ex);
					}
					for (int i = 0; i < method.getParameterTypes().length; i++) {
						// add params
						Class<?> param = method.getParameterTypes()[i];
						iMethod.param(param, "arg" + i);
					}
				}
			}
			codemodel.build(new File("./src/"));
			generated = true;
		} catch (JClassAlreadyExistsException e) {
			e.printStackTrace();
			generated = false;
		} catch (IOException e) {
			e.printStackTrace();
			generated = false;
		}
		return generated;
	}
}
