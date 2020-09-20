package infraestrutura.util;


import infraestrutura.robots.AbstractRobot;
import infraestrutura.robots.AbstractThreadRobot;

import java.awt.event.KeyListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jogo.GameManager;



public class RobotClassLoader {

	public static List loadRobotPlayers(){
		java.util.List robotPlayers = new ArrayList();
		
		//Package pkg = Package.getPackage("players");
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("robots");
		File dir = null;
		try {
			dir = new File(url.toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(dir);
	
		File[] classFiles = dir.listFiles();
		for (File file2 : classFiles) {
			if(file2.getName().endsWith(".class")){
				String className = file2.getName().replace(".class", "");
								
				if(validRobot(className))
					robotPlayers.add(className);
			}			
		}	
		
		return robotPlayers;
	}
	
	private static boolean validRobot(String className){
		return className.endsWith("Robot");
	}
	/*private static boolean validRobot(String className){
		boolean valid = false;
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); 	
		
		try {
			Class clazz = classLoader.loadClass("robots."+className);
						
			Class [] params = new Class[]{};						
			
			Constructor  constr = clazz.getConstructor(params);	
			AbstractThreadRobot clazzInstance  = (AbstractThreadRobot) constr.newInstance(new Object[]{});
			
			System.out.println("clazzInstance : " + clazzInstance.getClass());
			
			if(clazzInstance instanceof AbstractThreadRobot)
				valid = true;
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return valid;	
		
	}*/
}
