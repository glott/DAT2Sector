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
	private JButton DATButton;
	private JButton SCTButton;
	private JButton XMLButton;
	private JProgressBar progressBar;

	private File[] files;
	private DataHandler handler;

	public Display(DataHandler handler)
	{
		this.handler = handler;
		handler.setProgressBar(progressBar);

		DATButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				files = selectFiles();
			}
		});

		SCTButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handler.dat_sct(files);
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

	private File[] selectFiles()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Downloads"));
		chooser.setFileFilter(new FileNameExtensionFilter("DAT File (*.dat)", "dat"));
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setMultiSelectionEnabled(true);
		chooser.showOpenDialog(frame);
		return chooser.getSelectedFiles();
	}

}
