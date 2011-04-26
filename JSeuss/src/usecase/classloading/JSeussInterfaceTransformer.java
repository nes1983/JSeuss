package usecase.classloading;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import ch.unibnf.scg.jseuss.core.javaassist.generic.JSeussJavaassist;
import ch.unibnf.scg.jseuss.core.javaassist.guice.JavaInterfaceGenerator;
import ch.unibnf.scg.jseuss.utils.JSeussConfig;

public class JSeussInterfaceTransformer {
	
	private ClassPool classPool;
	private final Collection<File> allGeneratedFiles;
	private final Collection<String> allGeneratedFileNames;
	
	public JSeussInterfaceTransformer() {
		classPool = ClassPool.getDefault();
		allGeneratedFiles = new ArrayList<File>();
		allGeneratedFileNames = new ArrayList<String>();
		addFilesRecursively(new File(JSeussConfig.OUTPUT_DIR + JSeussConfig.GENERATED_PACKAGE_PREFIX.replace('.', '/')), allGeneratedFiles);
		System.out.println("Found: " + allGeneratedFiles);
		for(File f : allGeneratedFiles) {
			String name = f.getPath().replace('\\', '/').replaceFirst(JSeussConfig.OUTPUT_DIR, "").replace('/', '.');
			name = name.substring(0, name.length()-".class".length());
			allGeneratedFileNames.add(name);
			System.out.println("+ " + name);
		}
	}

	public void transform() throws NotFoundException, CannotCompileException {
	    String[] classNames = new String[allGeneratedFileNames.size()];
	    int index=0;
	    for(String fileName : allGeneratedFileNames)
	    	classNames[index++] = fileName;
	    CtClass[] classes = classPool.get(classNames);
		for(CtClass originalInterfaceClass : classes) {
			originalInterfaceClass.defrost();
			CtClass interfaceClass = classPool.makeInterface(originalInterfaceClass.getName());
			interfaceClass = JavaInterfaceGenerator.generateMethodsWithInterfaceTransformation(originalInterfaceClass, interfaceClass);
			
			try {
				JSeussJavaassist.writeCtClass(interfaceClass);
				System.out.println("created interface and wrote to disk: " + interfaceClass.getName());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private static void addFilesRecursively(File file, Collection<File> all) {
	    final File[] children = file.listFiles();
	    if (children != null) {
	        for (File child : children) {
	        	if(child.isFile())
	        		all.add(child);
	            addFilesRecursively(child, all);
	        }
	    }
	}
}
