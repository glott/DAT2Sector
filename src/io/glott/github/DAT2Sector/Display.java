package io.glott.github.DAT2Sector;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	private File[] DATFiles;
	private File keyFile;
	private DataHandler handler;

	private HashMap<String, String[]> keyValues = new HashMap<>();

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
					if ((DATFiles != null && DATFiles.length >= 1))
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
				if (keyFile != null && (!keyFile.getName().toLowerCase().equals("maps.key") || !parseKey()))
				{
					keyFile = null;
					convertXMLButton.setEnabled(false);
				}
				if (keyFile != null && keyFile.exists())
					if ((DATFiles != null && DATFiles.length >= 1))
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
				convertXMLButton.setEnabled(false);
			}
		});

		convertXMLButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//
				DATFiles = null;
				convertSCTButton.setEnabled(false);
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

	private boolean parseKey()
	{
		if (keyFile == null || !keyFile.exists() || !keyFile.getName().equals("maps.key"))
			return false;
		String[] s = DataHandler.readFile(keyFile).split("\n");
		for (String z : s)
		{
			String[] q = z.split("\\|");
			if (q.length == 3) keyValues.put(q[0], new String[]{q[1], q[2]});
		}
		if (keyValues == null || keyValues.size() == 0)
			return false;
		return true;
	}

}
