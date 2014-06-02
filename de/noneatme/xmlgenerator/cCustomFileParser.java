package de.noneatme.xmlgenerator;

/*
 ** June 2014
 ** Author: Noneatme (Jonas Ba)
 ** Version: 1.0.0
 ** Name: cCustomFileParser.java
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

import java.util.EnumSet;

import de.noneatme.xmlgenerator.exceptions.CantParseFileException;
import de.noneatme.xmlgenerator.exceptions.FileIsInMainFolderException;

public class cCustomFileParser
{
	private String		pfad;
	private String		extension;
	private int			depth;
	private int			folderDepth;
	private String		metaPath;
	private String[]	folderParts;
	private cXMLWriter	writer;

	// Script File Extensions //
	public static enum scriptFileExtensions
	{
		LUA, LUAC,
	};

	// Sound File Extensions //
	public static enum soundFileExtensions
	{
		AIFF, AAC, ATRAC, MP3, MP4, WAV, OGG, WMV, RIFF, PLS, WMA, MMF,
	}

	// Image File Extensions //
	public static enum imageFileExtensions
	{
		PNG, JPG, JPEG, BMP, TIFF, RAW, GIF, WEBP,
	}

	// Mod File Extensions //
	public static enum modFileExtensions
	{
		TXD, DFF,
	}

	// Not Allowed Files //
	public static enum notAllowedFilesExtensions
	{
		RAR, ZIP, PREFS,
	}

	public cCustomFileParser(String pfad, String extension, int depth, int folderDepth, cXMLWriter writer)
	{
		this.pfad = pfad; // Absoluter Pfad
		this.extension = extension;
		this.depth = depth;
		this.folderDepth = folderDepth;
		this.folderParts = this.pfad.split("\\\\");
		this.metaPath = "";
		this.writer = writer;

		for (int i = this.folderDepth + 1; i <= this.folderDepth + this.depth; i++)
		{
			this.metaPath = this.metaPath + this.folderParts[i];
			if (i < this.folderDepth + this.depth)
			{
				this.metaPath = this.metaPath + "\\";
			}

		}

	}

	private void writeScriptFile()
	{
		String type = "shared";
		String cache = "false";

		if (this.isFileInClientFolder(this.metaPath))
		{
			type = "client";
		}
		if (this.isFileInServerFolder(this.metaPath))
		{
			type = "server";
		}

		if (cXMLGenerator.main.cacheScripts)
		{
			cache = "true";
		}

		this.writer.setDepth(this.depth);
		this.writer.writeLine("<script src='" + this.metaPath + "' type='" + type + "' cache='" + cache + "'/>");
	}

	private void writeDownloadableFile()
	{
		String param1 = cXMLGenerator.main.filevalue;

		this.writer.setDepth(this.depth);
		this.writer.writeLine("<" + param1 + " src='" + this.metaPath + "'/>");
	}

	private boolean isFileInMainFolder(String pfad)
	{
		String ersterOrdner = pfad.split("\\\\")[this.depth - 1];

		if (ersterOrdner.equals(new String(this.pfad.split("\\\\")[0])))
		{
			return true;
		}
		return false;
	}

	private boolean isFileInServerFolder(String pfad)
	{
		String zweiterOrdner = pfad.split("\\\\")[0];
		if (zweiterOrdner.equals(new String(cXMLGenerator.main.serverFolder)))
		{
			return true;
		}
		return false;
	}

	private boolean isFileInClientFolder(String pfad)
	{
		String zweiterOrdner = pfad.split("\\\\")[0];
		if (zweiterOrdner.equals(new String(cXMLGenerator.main.clientFolder)))
		{
			return true;
		}
		return false;
	}

	public void parseExtension() throws FileIsInMainFolderException, CantParseFileException, NotAllowedFileException
	{
		String caseString = this.extension.toUpperCase().replace(".", "");

		// Main Folder nicht benutzen?
		if (!cXMLGenerator.main.dontUseMainFolder)
		{
			if (this.isFileInMainFolder(this.pfad))
			{
				throw new FileIsInMainFolderException();
			}
		}
		try
		{
			if(cCustomFileParser.enumContains(scriptFileExtensions.class, caseString))
			{
				this.writeScriptFile();
			}
			else if(cCustomFileParser.enumContains(soundFileExtensions.class, caseString))
			{
				this.writeDownloadableFile();
			}
			else if(cCustomFileParser.enumContains(imageFileExtensions.class, caseString))
			{
				this.writeDownloadableFile();
			}
			else if(cCustomFileParser.enumContains(notAllowedFilesExtensions.class, caseString))
			{
				throw new NotAllowedFileException();
			}
			else
			{
				this.writeDownloadableFile();
			}

		}
		catch (Throwable ex)
		{

			throw new CantParseFileException();
		}

	}

	public static <E extends Enum<E>> boolean enumContains(Class<E> _enumClass, String value)
	{
		try
		{
			return EnumSet.allOf(_enumClass).contains(Enum.valueOf(_enumClass, value));
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
