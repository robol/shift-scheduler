/* GPLAboutDialog.java
 *
 * A generic handler for About events for GPL'd Java programs
 *
 * Written by Erskin L. Cherry (erskin@eldritch.org)
 * Copyright (c) 1999 Eldritch Enterprises
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Optionally, you may find a copy of the GNU General Public License
 * from http://www.fsf.org/copyleft/gpl.txt
 */

package it.robol.shiftscheduler.desktop;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/** 
 * <A HREF="http://www.eldritch.org/erskin/GPLAboutDialog/GPLAboutDialog.html">GPLAboutDialog</A> is an extension of AbstractAction which will
 * display an "About" dialog and/or the GPL when actionPerformed is invoked.
 *
 * GPLAboutDialog assumes the GPL is kept in the current folder and
 * called license.txt
 * <P>
 * If the file cannot be read, the user will be asked if the program
 * should try and download a new copy of the GPL from the internet.
 * <P>
 * The icon for the dialog that displays the GPL defaults to the
 * local file <A HREF="http://www.eldritch.org/erskin/GPLAboutDialog/gnu-logo.jpeg">gnu-logo.jpeg</A>
 * <P>
 * The source and class files can be found at
 * <A HREF="http://www.eldritch.org/erskin/GPLAboutDialog/GPLAboutDialog.java">http://www.eldritch.org/erskin/GPLAboutDialog/GPLAboutDialog.java</A>
 * and <A HREF="http://www.eldritch.org/erskin/GPLAboutDialog/GPLAboutDialog.class">http://www.eldritch.org/erskin/GPLAboutDialog/GPLAboutDialog.class</A>
 * <P>
 * Naturally, this program is licensed under the GPL as well.
 * Which is availble at <A HREF="http://www.fsf.org/copyleft/gpl.txt">http://www.fsf.org/copyleft/gpl.txt</A>
 * or <A HREF="http://www.eldritch.org/erskin/GPLAboutDialog/license.txt">from the GPLAboutDialog site</A>.
 * <P>
 * The java source, the class file, the default gnu-logo, and the GNU license
 * can be downloaded in one file from
 * <A HREF="http://www.eldritch.org/erskin/GPLAboutDialog/GPLAboutDialog.tar.bz2">http://www.eldritch.org/erskin/GPLAboutDialog/GPLAboutDialog.tar.bz2</A> (~13k)
 * 
 * @author <A HREF="mailto:erskin@eldritch.org">Erskin L. Cherry</A>
 * @version 1.3
 */
public class GPLAboutDialog extends AbstractAction {

	// Default parent component
	private Component parent = null;

	// Set when we have a dialog open, so we don't open more than one at
	// a time
	private boolean alreadyOpen = false;

	/**
	 * Returns the current parent Component the dialog will use when
	 * displayed.
	 *
	 * @return the parent {@link Component} the dialog will be attached to
	 */
	public Component getParent() { return parent; }

	/**
	 * Assigns the parent Component the dialog will use when displayed.
	 *
	 * @param parent	the new parent objects
	 */
	public void setParent(Component parent) { this.parent = parent; }

	// Default name string, in case we don't get passed one
	// from the constructor
	private String name = "";

	/**
	 * Returns the current program name the about dialog will display.
	 *
	 * @return the program name that will be displayed
	 */
	public String getName() { return name; }

	/**
	 * Assigns the program name the about dialog will display.
	 *
	 * @param name	the new program name that will be displayed
	 */
	public void setName(String name) { this.name = name; }

	// Default version string, in case we don't get passed one
	// from the constructor
	private String version = "";

	/**
	 * Returns the current version number the about dialog will display.
	 *
	 * @return the version number that will be displayed
	 */
	public String getVersion() { return version; }

	/**
	 * Assigns the version number the about dialog will display.
	 *
	 * @param version	the new version number that will be displayed
	 */
	public void setVersion(String version)
		{ this.version = version; };

	// Default blurb string, in case we don't get passed one
	// from the constructor
	private String blurb = "";

	/**
	 * Returns the current program purpose blurb the about dialog 
	 * will display.
	 *
	 * @return the program blurb that will be displayed
	 */
	public String getBlurb() { return blurb; }

	/**
	 * Assigns the program purpose blurb the about dialog will display.
	 *
	 * @param blurb	the new that will be displayed
	 */
	public void setBlurb(String blurb) { this.blurb = blurb; }

	// Default author/copyright string, in case we don't get passed one
	// from the constructor
	private String copyright = "";

	/**
	 * Returns the current author/copyright string the about dialog 
	 * will display.
	 *
	 * @return the copyright string that will be displayed
	 */
	public String getCopyright() { return copyright; }

	/**
	 * Assigns the author/copyright string the about dialog will display.
	 * Generally of the form "by John Doe\nCopyright 1999 John Doe"
	 *
	 * @param copyright the new author/copyright that will be displayed
	 */
	public void setCopyright(String copyright)
		{ this.copyright = copyright; }

	// Default internal frames flag, we assume that we shouldn't
	// just to be safe
	private boolean internalFrames = false;


	/**
	 * Returns <code>true</code> if the about dialog will open internal
	 * frames.
	 *
	 * @return <code>true</code> if internal frames will be displayed
	 */
	public boolean isInternalFrames() { return internalFrames; }


	/**
	 * Determines if the about diaog should be displayed using 
	 * internal frames.
	 *
	 * @param newInternalFrames	<code>true</code> if internal frames
	 *							should be used
	 */
	public void setInternalFrames(boolean newInternalFrames)
		{ internalFrames = newInternalFrames; }

	// The default icon for the GPL display dialog
	private Icon gnuLogo = new ImageIcon("gnu-logo.jpeg",
		"GNU's Not Unix Logo");

	/**
	 * Returns the current Icon that will be displayed on the GPL dialog
	 * if requested.
	 *
	 * The internal default is the local file "gnu-logo.jpeg"
	 *
	 * @return the {@link Icon} that will be displayed on the GPL dialog
	 */
	public Icon getGnuLogo() { return gnuLogo; }

	/**
	 * Assigns the Icon that will be displayed on the GPL dialog if
	 * requested.
	 *
	 * @param gnuLogo	the new Icon for the GPL dialog
	 */
	public void setGnuLogo(Icon gnuLogo) { this.gnuLogo = gnuLogo; }

	// Default program Icon for the about window
	// If the constructor doesn't override it, JOptionPane will use
	// an 'i' in a blue circle
	private Icon programLogo = null;

	/**
	 * Returns the current Icon that will be displayed on the about dialog
	 * for the program.
	 *
	 * The default is <code>null</code> which should result in 
	 * {@link JOptionPane} using it's internal information icon
	 *
	 * @return the {@link Icon} that will be displayed on the GPL dialog
	 */
	public Icon getProgramLogo() { return programLogo; }

	/**
	 * Assigns the Icon that will be displayed on the about dialog for
	 * the program.
	 *
	 * @param programLogo	the new program Icon for the about dialog
	 */
	public void setProgramLogo(Icon programLogo)
		{ this.programLogo = programLogo; }

	/**
	 * Constructs a new GPLAboutDialog with absolutely all options.
	 *
	 * @param parent		the parent Component for the dialog
	 * @param name			the program name
	 * @param version		the program version number
	 * @param blurb			the program purpose text
	 * @param copyright		the author/copyright text
	 * @param programLogo		the Icon for the about dialog
	 * @param gnuLogo		the Icon for the GPL dialog
	 * @param internalFrames	<code>true</code> if internal frames
	 *							should be used
	 */
	public GPLAboutDialog(Component parent, String name,
				String version, String blurb, String copyright,
				Icon programLogo, Icon gnuLogo, 
				boolean internalFrames) {
		// The overly complete constructor including GNU logo option
		this.parent = parent;
		this.name = name;
		this.version = version;
		this.blurb = blurb;
		this.copyright = copyright;
		this.programLogo = programLogo;
		this.gnuLogo = gnuLogo;
		this.internalFrames = internalFrames;
	}

	/**
	 * Constructs a new GPLAboutDialog with default gnu_logo.jpeg.
	 *
	 * @param parent		the parent Component for the dialog
	 * @param name			the program name
	 * @param version		the program version number
	 * @param blurb			the program purpose text
	 * @param copyright		the author/copyright text
	 * @param programLogo		the Icon for the about dialog
	 * @param internalFrames	<code>true</code> if internal frames
	 *							should be used
	 */
	public GPLAboutDialog(Component parent, String name,
				String version, String blurb, String copyright,
				Icon programLogo, boolean internalFrames) {
		// The general constructor
		this.parent = parent;
		this.name = name;
		this.version = version;
		this.blurb = blurb;
		this.copyright = copyright;
		this.programLogo = programLogo;
		this.internalFrames = internalFrames;
	}
	
	/**
	 * Constructs a new GPLAboutDialog with default gnu_logo
	 * and assumes we are not using internal frames.
	 *
	 * @param parent		the parent Component for the dialog
	 * @param name			the program name
	 * @param version		the program version number
	 * @param blurb			the program purpose text
	 * @param copyright		the author/copyright text
	 * @param programLogo		the Icon for the about dialog
	 */
	public GPLAboutDialog(Component parent, String name,
				String version, String blurb, String copyright,
				Icon programLogo) {
		// The same constructor without the last boolean option
		this.parent = parent;
		this.name = name;
		this.version = version;
		this.blurb = blurb;
		this.copyright = copyright;
		this.programLogo = programLogo;
	}

	/**
	 * Constructs a new GPLAboutDialog with the default gnu_logo
	 * and the default program icon.
	 *
	 * @param parent		the parent Component for the dialog
	 * @param name			the program name
	 * @param version		the program version number
	 * @param blurb			the program purpose text
	 * @param copyright		the author/copyright text
	 * @param internalFrames	<code>true</code> if internal frames
	 *							should be used
	 */
	public GPLAboutDialog(Component parent, String name,
				String version, String blurb, String copyright,
				boolean internalFrames) {
		// The same constructor without the program icon option
		this.parent = parent;
		this.name = name;
		this.version = version;
		this.blurb = blurb;
		this.copyright = copyright;
		this.internalFrames = internalFrames;
	}

	/**
	 * Constructs a new GPLAboutDialog with the default gnu_logo,
	 * default program icon, and the assumption that we are not
	 * using internal frames.
	 *
	 * @param parent		the parent Component for the dialog
	 * @param name			the program name
	 * @param version		the program version number
	 * @param blurb			the program purpose text
	 * @param copyright		the author/copyright text
	 */
	public GPLAboutDialog(Component parent, String name,
			String version, String blurb, String copyright) {
		// The same constructor without the last boolean
		// or the program icon
		this.parent = parent;
		this.name = name;
		this.version = version;
		this.blurb = blurb;
		this.copyright = copyright;
	}

	/**
	 * Displays and about dialog box, regardless of the event, and offers
	 * to display the GNU GPL, offering to download it if it is not found
	 * locally.
	 *
	 * @param e	ignored action event
	 */
	public void actionPerformed(ActionEvent e) {
		// We only want to open one About Dialog at a time
		// So if we already have, stop right here
		if(alreadyOpen) { return; }
	
		// Set aleadyOpen to true so we won't open another dialog
		// if this method is called again while we're still displaying
		// something
		alreadyOpen = true;

		// Open a standard about dialog with the option to
		// view the GPL or just say OK

		String message = 
			name +  " v" + version + "\n" + blurb + "\n\n" + copyright +
			"\n\nThis program is Open Source software, or more" +
			"\nspecifically, free software. You can redistribute" +
			"\nit and/or modify it under the terms of the GNU" +
			"\nGeneral Public License (GPL) as published by the " + 
			"\nFree Software Foundation; either version 2 of the" +
			"\nLicense, or (at your option) any later version.\n";

		int viewGPL;

		Object[] optionButtons = { "View GPL", "OK" }; 
		if(internalFrames) {
			viewGPL = JOptionPane.showInternalOptionDialog(parent,
				message, "About " + name, 0,
				JOptionPane.INFORMATION_MESSAGE, programLogo, 
				optionButtons, optionButtons[1]);
		} else {
			viewGPL = JOptionPane.showOptionDialog(parent,
				message, "About " + name, 0,
				JOptionPane.INFORMATION_MESSAGE, programLogo, 
				optionButtons, optionButtons[1]);
		}
	
		// If they wanted to view the GPL, try and read the file
		// from the current directory
		if(viewGPL == JOptionPane.YES_OPTION) {
			// Set up the scrollpane to hold the GPL once we read it
			JTextArea textArea = new JTextArea(15, 81);
			textArea.setEditable(false);
			textArea.setLineWrap(true);
			textArea.setFont(new Font("Courier", Font.PLAIN, 10));
			
			JScrollPane scrollPane = new JScrollPane(textArea);
			
			// The URL for the Free Software foundation
			// in case we need to download a new copy of the GPL
			URL gplURL = null;	
			try { gplURL = new URL("http://www.fsf.org/copyleft/gpl.txt"); }
			catch(MalformedURLException urlException) {}
	
			boolean loadedGPL = false;
			BufferedReader inGPL = null;
	
			// This while loop allows me to reuse the code to
			// load from a local file if I have to download the
			// GPL from the net
			while((viewGPL == JOptionPane.YES_OPTION) && 
				(loadedGPL == false)) {	
					
				// Then try and read it locally
				try {	
					inGPL = new BufferedReader(
						new FileReader(getClass().getResource("COPYING").getFile()));
					textArea.read(inGPL, "GNU General Public License");
	
					// I don't close this in the finally statement
					// because I may need it again before I get there
					inGPL.close();
					loadedGPL = true;
				}
				catch(IOException ioException) {
					// I don't put this in the finally statement
					// because I may need it again before I get there
					if(inGPL != null) { 
						try { inGPL.close(); }
						catch(IOException closeException) {} 
					}
				
	
					// If we have a problem, bring up a dialog
					// and ask if we should grab it from the net
							
					if(internalFrames) {
						viewGPL = JOptionPane.showInternalConfirmDialog( 
							parent, "Error reading file license.txt\n\n" + 
							ioException.getLocalizedMessage() +
							"\n\nDownload a new copy of the GPL from " + 
							gplURL.getProtocol() + "://" +
							gplURL.getHost() + "?", "Error reading file", 
							JOptionPane.YES_NO_OPTION,
							JOptionPane.ERROR_MESSAGE);
					} else {
						viewGPL = JOptionPane.showConfirmDialog( 
							parent, "Error reading file license.txt\n\n" + 
							ioException.getLocalizedMessage() +
							"\n\nDownload a new copy of the GPL from " + 
							gplURL.getProtocol() + "://" +
							gplURL.getHost() + "?", "Error reading file", 
							JOptionPane.YES_NO_OPTION,
							JOptionPane.ERROR_MESSAGE);
					}
	
					if(viewGPL == JOptionPane.YES_OPTION) {
						// Try and grab the GPL from the net
						BufferedWriter outGPL = null;
						try {
							inGPL = new BufferedReader(new 
								InputStreamReader(gplURL.openStream()));
							
							outGPL = new BufferedWriter(new 
								FileWriter("license.txt"));
									
							String textLine = null;
									
							while((textLine = inGPL.readLine()) != null) {
								outGPL.write(textLine);
								outGPL.newLine();
							}
						}
						catch(Exception netException) {
							// Tell them if we failed
							viewGPL = JOptionPane.NO_OPTION;
							if(internalFrames) {
								JOptionPane.showInternalMessageDialog(parent,
									"Error downloading file " +
									gplURL.getProtocol() + "://" +
									gplURL.getHost() + gplURL.getRef() +
									"\n\n" + netException.getMessage(), 
									"Error downloading file",
									JOptionPane.ERROR_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(parent,
									"Error downloading file " +
									gplURL.getProtocol() + "://" +
									gplURL.getHost() + gplURL.getRef() +
									"\n\n" + netException.getMessage(), 
									"Error downloading file",
									JOptionPane.ERROR_MESSAGE);
							}
						}
						finally {	
							try {
								if(outGPL != null) { outGPL.close(); }
								if(inGPL != null) { inGPL.close(); }
							}
							catch(IOException closeException) {}
						}
					}
				}
			}
			// If we actually got a hold of the file,
			// display it
			if(loadedGPL == true) {
				scrollPane.setPreferredSize(
					textArea.getPreferredScrollableViewportSize());
				if(internalFrames) {
					JOptionPane.showInternalMessageDialog(parent,
						scrollPane, "GNU General Public License",
						JOptionPane.INFORMATION_MESSAGE, gnuLogo);
				} else {
					JOptionPane.showMessageDialog(parent,
						scrollPane, "GNU General Public License",
						JOptionPane.INFORMATION_MESSAGE, gnuLogo);
				}
			}
		}
		// We're done, so if they want to open the window up again,
		// it's okay
		alreadyOpen = false;
	}
}

