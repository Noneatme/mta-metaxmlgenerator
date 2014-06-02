package de.noneatme.xmlgenerator;

/*
 ** June 2014
 ** Author: Noneatme (Jonas Ba)
 ** Version: 1.0.0
 ** Name: cXMLWriter.java
 ** Copyright (c) 2014 Jonas Ba (Noneatme)
 ** Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

// Class cXML Writer //
public class cXMLWriter
{
	private PrintWriter xmlFile;
	private int currentDepth = 0;
	
	// Constructor //
	public cXMLWriter(String path)
	{
		try
		{
			this.xmlFile	= new PrintWriter(path);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		
		this.setDepth(0);
		this.writeComment("XML Generator Version " + cXMLGenerator.version);
		this.writeComment("Generation Date: " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
	}

	// Current Level String //
	public String getCurrentLevelString()
	{
		return new String(new char[this.currentDepth]).replace("\0", "\t");
	}
	
	// Depth //
	public int setDepth(int depth)
	{
		return (this.currentDepth = depth);
	}
	
	// Comment 2 //
	public boolean writeComment(String comment)
	{
		String comment1 = this.getCurrentLevelString();
		String comment2 = comment1 + "<!-- " + comment + " -->";
		
		return this.writeToXML(comment2);
	}
	
	// Comment 1 //
	public boolean writeComment(String comment, boolean println)
	{
		String comment1 = this.getCurrentLevelString();
		String comment2 = comment1 + "<!-- " + comment + " -->";
		
		return this.writeToXML(comment2, println);
	}
	
	// Write Line //
	public boolean writeLine(String line)
	{
		return this.writeToXML(this.getCurrentLevelString() + line);
	}
	
	// Util //
	public boolean writeToXML(String line)
	{
		this.xmlFile.println(line);
		this.xmlFile.flush();
		return true;
	}
	// Util //
	public boolean writeToXML(String line, boolean println)
	{
		if(println)
			this.xmlFile.println(line);
		else
			this.xmlFile.print(line);
		
		this.xmlFile.flush();
		return true;
	}
	
	// Util //
	// Ueberladen //
	public boolean writeToXML()
	{
		this.xmlFile.println();
		this.xmlFile.flush();
		return true;
	}
	
	// Util //
	public boolean writeSpace()
	{
		return this.writeToXML(this.getCurrentLevelString());
	}
	
	// Close //
	public boolean close()
	{
		this.setDepth(1);
		this.writeToXML("</meta>");
		this.setDepth(0);
		this.writeSpace();
		this.writeComment("End of meta.xml file", false);
		
		this.xmlFile.close();
		return true;
	}
}
