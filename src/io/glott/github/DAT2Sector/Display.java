package io.glott.github.DAT2Sector;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.HashMap;

public class Display
{
	private static JFrame frame;
	private JPanel panel;
	private JButton selectDATButton;
	private JButton convertSCTButton;
	private JButton convertXMLButton;
	private JProgressBar progressBar;
	private JButton selectKeyButton;
	private JButton selectSectorButton;

	private File[] DATFiles;
	private File keyFile;
	private File sctFile;

	private HashMap<String, String[]> keyValues = new HashMap<>();

	public Display(DataHandler handler)
	{
		handler.setProgressBar(progressBar);

		selectDATButton.addActionListener(e ->
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Downloads"));
			chooser.setFileFilter(new FileNameExtensionFilter("DAT File (*.dat)", "dat"));
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setMultiSelectionEnabled(true);
			chooser.showOpenDialog(frame);
			DATFiles = chooser.getSelectedFiles();
			if (DATFiles != null && DATFiles.length >= 1)
				convertSCTButton.setEnabled(true);
		});

		selectKeyButton.addActionListener(e ->
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Downloads"));
			chooser.setFileFilter(new FileNameExtensionFilter("Key File (*.key)", "key"));
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setMultiSelectionEnabled(false);
			chooser.showOpenDialog(frame);
			keyFile = chooser.getSelectedFile();
			if (keyFile == null || parseKey() || sctFile == null)
				convertXMLButton.setEnabled(false);
			else
				convertXMLButton.setEnabled(true);
		});

		selectSectorButton.addActionListener(e ->
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Downloads"));
			chooser.setFileFilter(new FileNameExtensionFilter("SCT2 File (*.sct2)", "sct2"));
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setMultiSelectionEnabled(false);
			chooser.showOpenDialog(frame);
			sctFile = chooser.getSelectedFile();
			if (keyFile == null || parseKey() || sctFile == null)
				convertXMLButton.setEnabled(false);
			else
				convertXMLButton.setEnabled(true);
		});

		convertSCTButton.addActionListener(e ->
		{
			sctFile = handler.dat_sct(DATFiles);
			DATFiles = null;
			convertSCTButton.setEnabled(false);

			if (keyFile == null || parseKey() || sctFile == null)
				convertXMLButton.setEnabled(false);
			else
				convertXMLButton.setEnabled(true);
		});

		convertXMLButton.addActionListener(e ->
		{
			handler.sct_xml(keyValues, sctFile);
			sctFile = null;
			convertXMLButton.setEnabled(false);
		});
	}

	void run()
	{
		frame = new JFrame("DAT2Sector");
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	private boolean parseKey()
	{
		if (keyFile == null || !keyFile.exists())
			return true;
		String[] s = DataHandler.readFile(keyFile).split("\n");
		for (String z : s)
		{
			String[] q = z.split("\\|");
			if (q.length == 3)
				keyValues.put(q[0], new String[]{q[1].toUpperCase().replace((char) 13 + "", ""), q[2].toUpperCase().replace((char) 13 + "", "")});
		}
		return keyValues == null || keyValues.size() == 0;
	}

}
