package io.glott.github.DAT2Sector;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Display
{
	private static JFrame frame;
	private JPanel panel;
	private JButton selectDATButton;
	private JButton convertSCTButton;
	private JButton convertXMLButton;
	private JProgressBar progressBar;
	private JButton selectKeyButton;
	private JButton selectSCTButton;

	private File[] DATFiles;
	private File SCTFile;
	private File keyFile;
	private DataHandler handler;

	public Display(DataHandler handler)
	{
		this.handler = handler;
		handler.setProgressBar(progressBar);

		selectDATButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
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
				if (keyFile != null && keyFile.exists())
					if ((DATFiles != null && DATFiles.length >= 1) || (SCTFile != null && SCTFile.exists()))
						convertXMLButton.setEnabled(true);
			}
		});

		selectSCTButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Downloads"));
				chooser.setFileFilter(new FileNameExtensionFilter("SCT2 File (*.sct2)", "sct2"));
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setMultiSelectionEnabled(false);
				chooser.showOpenDialog(frame);
				SCTFile = chooser.getSelectedFile();
				if (keyFile != null && keyFile.exists())
					if ((DATFiles != null && DATFiles.length >= 1) || (SCTFile != null && SCTFile.exists()))
						convertXMLButton.setEnabled(true);
			}
		});

		selectKeyButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Downloads"));
				chooser.setFileFilter(new FileNameExtensionFilter("Key File (*.key)", "key"));
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setMultiSelectionEnabled(false);
				chooser.showOpenDialog(frame);
				keyFile = chooser.getSelectedFile();
				if (keyFile != null && !keyFile.getName().toLowerCase().equals("maps.key")) keyFile = null;
				if (keyFile != null && keyFile.exists())
					if ((DATFiles != null && DATFiles.length >= 1) || (SCTFile != null && SCTFile.exists()))
						convertXMLButton.setEnabled(true);
			}
		});

		convertSCTButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handler.dat_sct(DATFiles);
				DATFiles = null;
				convertSCTButton.setEnabled(false);
				if(SCTFile == null || !SCTFile.exists())
					convertXMLButton.setEnabled(false);
			}
		});

	}

	public void run()
	{
		frame = new JFrame("DAT2Sector");
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

}
