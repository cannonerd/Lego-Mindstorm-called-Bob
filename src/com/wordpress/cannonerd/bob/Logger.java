package com.wordpress.cannonerd.bob;

import java.io.*;

public class Logger {
	private FileOutputStream out = null;
	private File data = null;
	
	public void end() {
		try {
			out.close();
		}
		catch (IOException e) {
			System.out.println("Don't we all love Java. Fuck yeah!");
		}
	}
	
	public void write(String message)
	{
		data = new File("bob.dat");
		
		try {
			out = new FileOutputStream(data);
		}
		catch (IOException e) {
			System.out.println("Iz fail");
		}
		
		DataOutputStream dataOut = new DataOutputStream(out);
		try {
			dataOut.writeChars(message);
		}
		catch (IOException e) {
			System.out.println("Fuxors");
		}
	}
}
