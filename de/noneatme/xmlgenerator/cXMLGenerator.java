package de.noneatme.xmlgenerator;

/*
 ** June 2014
 ** Author: Noneatme (Jonas Ba)
 ** Version: 1.0.0
 ** Name: cXMLGenerator.java
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

import java.io.File;
import java.util.ArrayList;

import de.noneatme.xmlgenerator.exceptions.CantParseFileException;
import de.noneatme.xmlgenerator.exceptions.FileIsInMainFolderException;
import de.noneatme.xmlgenerator.exceptions.UnknowFileExtensionException;

public class cXMLGenerator
{
	private String	folderName;
	public static cMain	main;
	private int 	recDepth 		= 1;
	private int		folderDepth		= 0;
	private cXMLWriter writer;
	
	private int 	folderAmmount 	= 0;
	private int 	fileAmmount		= 0;
	
	public static String version = "0.0.1";
	
	public cXMLGenerator(cMain main, String foldername)
	{
		cXMLGenerator.main		= main;
		this.folderName = foldername;
		this.folderDepth = this.folderName.split("\\\\",-1).length-1;

		this.writer		= new cXMLWriter(this.folderName + "\\meta.xml");
		
		System.out.println("Folder Depth: " + this.folderDepth);
		this.generateXML();
		
		this.writer.close();
	}
	
	// Parse File //
	
	private boolean parseFile(String currentPath, String extension, File file)
	{
		String filePath	= currentPath;
		System.out.println("Found File: " + filePath + ", Extension: " + extension);
		
		cCustomFileParser parser = new cCustomFileParser(filePath, extension, this.recDepth, this.folderDepth, this.writer);
		
		try
		{
			parser.parseExtension();
		
		}
		catch(FileIsInMainFolderException ex)
		{
			System.out.println("Skipping File - File is in main Folder");
		}
		catch(CantParseFileException ex)
		{
			System.out.println("Assuming Downloadable File.");
		}
		catch (NotAllowedFileException e)
		{
			System.out.println("Skipping File - Not Allowed.");
		}
		return true;
	}
	
	// Init Parse File //
	private boolean initParseFile(File file, int depth)
	{
		if(file.exists())
		{
			String extension = null;
			String fileName	= file.getName();
			String path		= file.getAbsolutePath();
			
			// Taken from Stackoverflow //
			int i = fileName.lastIndexOf('.');
			int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

			if (i > p) 
			{
			    extension = fileName.substring(i+1);
			}
			
			if(extension != null)
			{
				return this.parseFile(path, extension, file);
			}
			else
			{
				try
				{
					throw new UnknowFileExtensionException();
				}
				// Eclipse, wat are u doing //
				catch (UnknowFileExtensionException e)
				{

					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	// Rekursionsmethode //
	private boolean parseFolder(String folderName)
	{
		System.out.println("List Folder: " + folderName + ", Depth: " + this.recDepth);
		
		this.writer.setDepth(this.recDepth);
		this.writer.writeComment("Folder: " + folderName);
		
		cDirectoryInfo info = this.listFilesForFolder(new File(folderName));
		
		for(int folderIndex = 0; folderIndex < info.directorys.size(); folderIndex++)
		{
			// For each Folder in Folder //
			File folder = (File)info.directorys.get(folderIndex);
			String[] parts = folder.getAbsolutePath().split("\\\\", -1);
			
			boolean hidden = false;

			if(parts[0].contains("\\."))
			{
				hidden = true;
			}
			
			if(folder.exists() && !(folder.getAbsolutePath() == folderName) && (folder.isDirectory()) && (!hidden))
			{
				// Parse the folder //
				this.recDepth++;
				this.folderAmmount++;
				boolean result = this.parseFolder(folder.getAbsolutePath());
				this.recDepth--;
				
				if(!result)
				{
					System.out.println("Folder Error!");
				}
			}	
			else
			{
				return false;
			}
		}

		
		// Jetzt jede Datei durchgehen //
		for(int fileIndex = 0; fileIndex < info.files.size(); fileIndex++)
		{
			File file = (File)info.files.get(fileIndex);
			
			if(file.exists() && file.isFile())
			{
				this.fileAmmount++;
				this.initParseFile(file, this.recDepth);
			}
		}
		this.writer.writeSpace();
		
		return true;
	}
	
	private void parseInfoLine()
	{
		this.writer.setDepth(1);
		this.writer.writeComment("Info line");
		this.writer.writeLine("<info author='" + cXMLGenerator.main.author + "' version='" + cXMLGenerator.main.version + "' type='" + cXMLGenerator.main.type + "' name='" + cXMLGenerator.main.resName + "' description='" + cXMLGenerator.main.description + "'/>");
		this.writer.writeSpace();
		
		String oopActivated	= "false";
		String sync			= "false";
		if(cXMLGenerator.main.oopActivated)
		{
			oopActivated	= "true";
		}
		
		if(cXMLGenerator.main.syncMapObjectDataActivated)
		{
			sync			= "true";
		}
		this.writer.writeComment("Meta settings");
		this.writer.writeLine("<oop>" + oopActivated + "</oop>");
		this.writer.writeLine("<min_mta_version client='" + cXMLGenerator.main.minClientVersion + "' server='" + cXMLGenerator.main.minServerVersion + "'/>");
		this.writer.writeLine("<sync_map_element_data>" + sync + "</sync_map_element_data>");
		this.writer.writeSpace();
		
	}
	
	private boolean generateXML()
	{
		this.writer.writeToXML("<meta>");
		this.parseInfoLine();
		
		this.parseFolder(this.folderName);
		System.out.println();
		System.out.println("Found " + this.fileAmmount + " files in " + this.folderAmmount + " folders - meta.xml generated! (Maybe)");
		return true;
	}

	private cDirectoryInfo listFilesForFolder(final File folder)
	{
		ArrayList<Object> directorys 	= new ArrayList<Object>();
		ArrayList<Object> files 		= new ArrayList<Object>();
		
		for (final File fileEntry : folder.listFiles())
		{
			if (fileEntry.isDirectory())
			{
				directorys.add(fileEntry);
			}
			else
			{
				files.add(fileEntry);
			}
		}
		return new cDirectoryInfo(directorys, files);
	}
}
