package de.noneatme.xmlgenerator;

/*
 ** June 2014
 ** Author: Noneatme (Jonas Ba)
 ** Version: 1.0.0
 ** Name: cMain.java
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

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class cMain implements Runnable
{
	@Parameter(names = {"-log"}, description = "Operationen Loggen")
	public boolean log			= true;
	
	@Parameter(names = {"-clientfolder"}, description = "Ordner mit den Client-Dateien, z.B. 'client/'")
	public String clientFolder	= "client";
	
	@Parameter(names = {"-serverfolder"}, description = "Ordner mit den Server-Dateien, z.B. 'server/'")
	public String serverFolder	= "server";
	
	@Parameter(names = {"-filevalue"}, description = "Downloaddateien Argument, z.B. 'customfile'")
	public String filevalue	= "file";
	
	@Parameter(names = {"-cachescripts"}, description = "Script-Dateien Cachen")
	public boolean cacheScripts = false;
	
	@Parameter(names = {"-nomainfolder"}, description = "Dateien im Hauptverzeichnis nicht hinzufuegen")
	public boolean dontUseMainFolder = false;
	
	@Parameter(names = {"-author"}, description = "Der Author der Ressource")
	public String author = "Unknow";
	
	@Parameter(names = {"-name"}, description = "Der Name der Ressource")
	public String resName = "Some resource";
	
	@Parameter(names = {"-version"}, description = "Die Version der Ressource")
	public String version = "1.0.0";
	
	@Parameter(names = {"-description"}, description = "Die Beschreibung des Gamemodes")
	public String description = "A nice little gamemode";
	
	@Parameter(names = {"-type"}, description = "Der Typ der Ressource")
	public String type = "script";
	
	@Parameter(names = {"-minserverversion"}, description = "Die minimale Serverversion")
	public String minServerVersion = "1.3.5";
	
	@Parameter(names = {"-minclientversion"}, description = "Die minimale Clientversion")
	public String minClientVersion = "1.3.5";
	
	@Parameter(names = {"-defaultdownload"}, description = "Standartwert fuer die Download-Dateien")
	public boolean downloadFiles	= true;
	
	@Parameter(names = {"-oop"}, description = "OOP Aktivieren?")
	public boolean oopActivated		= true;
	
	@Parameter(names = {"-syncmap"}, description = "Sychronisation der Elementdatas fuer Map-Objekte aktivieren")
	public boolean syncMapObjectDataActivated		= false;
	
	@Parameter(description = "<Eingabeordner>")
    public List<String> folder = new ArrayList<String>();
	
	public static void main(String[] args) 
	{
		try
		{
			JCommander cmd 	= new JCommander();
			cMain main		= new cMain();
			
			cmd.setProgramName("generator.jar");
			cmd.addObject(main);
			cmd.parse(args);
			
			if(main.showUsage())
			{
				cmd.usage();
			}
			else
			{
				main.run();
			}
		}

		catch(Throwable ex)
		{
			ex.printStackTrace();
		}
	}
	public boolean showUsage() 
	{
        return !(folder.size() == 1);
    }
	
	// Run //
	public void run()
	{
		String folderName = folder.get(0);
		new cXMLGenerator(this, folderName);
	}
}
