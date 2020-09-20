package infraestrutura.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class IOUtils {

	public static void main(String args[]){
		try {
			IOUtils  j = new IOUtils ();
			j.copyFile(new File(args[0]),new File(args[1]));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void copyFile(File in, File out) throws Exception {
		System.out.println("Copy");
		FileInputStream fis  = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);
		byte[] buf = new byte[1024];
		int i = 0;
		while((i=fis.read(buf))!=-1) {
			fos.write(buf, 0, i);
		}
		fis.close();
		fos.close();
	}

}
