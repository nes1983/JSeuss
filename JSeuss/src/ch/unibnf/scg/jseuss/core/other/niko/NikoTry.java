package ch.unibnf.scg.jseuss.core.other.niko;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.AnnotationsWriter;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class NikoTry {
	public static  void main(String[] args) throws Throwable {
		
		 Translator t = new Translator() {
			@Override
			public void onLoad(ClassPool poolArg, String loadedClassName)
					throws NotFoundException, CannotCompileException {
				final ClassPool pool = poolArg;
				final String classNameSrsly = loadedClassName;
				CtMethod bodies[] = pool.get(loadedClassName).getDeclaredMethods();
				if(bodies.length == 0 || loadedClassName.equals("ch.unibnf.scg.jseuss.core.NikoGo")) {
					return;
				}
				final CtClass clazz = pool.get(loadedClassName);
				if(clazz.isFrozen()){
					clazz.defrost();
				}
				CtMethod body = clazz.getDeclaredMethods()[0];
				body.instrument(new ExprEditor() {
				     public void edit(NewExpr e) throws CannotCompileException {
				    	 try {
				    		 String replacement =   "$_ = (" + e.getClassName() + ") " + "FrenchSpellChecker" + "Factory" + ".get();"; // XXX helper method factor out

					    	 System.out.println(replacement);
//					    	 System.out.println(classNameSrsly);

					         CtField newField = new CtField(pool.get("com.google.inject.Provider"), e.getClassName() + "Factory", clazz); // XXX helper method factor out
					         
					         ConstPool constPool = clazz.getClassFile().getConstPool();
					         ByteArrayOutputStream output = new ByteArrayOutputStream();
					         AnnotationsWriter writer = new AnnotationsWriter(output, constPool);
	
					         writer.numAnnotations(2);
					         writer.annotation("Inject", 1);
					         writer.memberValuePair("name");
					         writer.constValueIndex("chiba");
	
					         writer.close();
					         byte[] attribute_info = output.toByteArray();
//					         AnnotationsAttribute anno
//					             = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag,
//					                                        attribute_info);
					         
					         //newField.setAttribute("Inject", attribute_info);
					         clazz.defrost();
					         clazz.addField(newField);

					         e.replace(replacement);
					         
					         clazz.writeFile(".");

				    	 } catch(NotFoundException nfe) {
				    		 throw new RuntimeException(nfe);
				    	 } catch(IOException ioe) {
				    		 throw new RuntimeException(ioe);
				    	 }
				     }
				 });
				
			}

			@Override
			public void start(ClassPool arg0) throws NotFoundException,
					CannotCompileException {
				// TODO Auto-generated method stub
			}
		 };
		 
		 ClassPool pool = ClassPool.getDefault();
	     Loader classLoader = new Loader();
	     classLoader.addTranslator(pool, t);
	     classLoader.run("ch.unibnf.scg.jseuss.core.NikoGo", new String[]{} );
		

	}
}
