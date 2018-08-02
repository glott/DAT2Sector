package io.glott.github.DAT2Sector;

import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataHandler
{

	private JProgressBar progressBar;

	private static String SCT_STA = "[INFO]\n" +
			"ZXX\n" +
			"ZXX_CTR\n" +
			"ZXX\n" +
			"N025.47.55.401\n" +
			"W080.17.36.692\n" +
			"60.000\n" +
			"54.000\n" +
			"5.480\n" +
			"1.000\n" +
			"\n" +
			"[VOR]\n" +
			"\n" +
			"[NDB]\n" +
			"\n" +
			"[AIRPORT]\n" +
			"KMIA 000.000 N025.47.43.300 W080.17.24.400\n" +
			"\n" +
			"[RUNWAY]\n" +
			"\n" +
			"[FIXES]\n" +
			"\n" +
			"[ARTCC]\n" +
			"\n" +
			"[ARTCC HIGH]\n" +
			"\n" +
			"[ARTCC LOW]\n" +
			"\n" +
			"[SID]\n" +
			"\n" +
			"[STAR]";

	private static String SCT_END = "\n" +
			"[LOW AIRWAY]\n" +
			"\n" +
			"[HIGH AIRWAY]\n" +
			"\n" +
			"[GEO]\n" +
			"\n" +
			"[REGIONS]\n" +
			"\n" +
			"[LABELS]\n";

	private static String readFile(File f)
	{
		try
		{
			byte[] encoded = Files.readAllBytes(Paths.get(f.getPath()));
			return new String(encoded, StandardCharsets.UTF_8);
		} catch (Exception ignored)
		{
		}
		return "";

	}

	public void dat_sct(File[] files)
	{
		try
		{
			File e = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "Convert.sct2");
			if (!e.exists()) e.createNewFile();
			PrintWriter pw = new PrintWriter(e);
			pw.println(SCT_STA);

			int i = 0;
			for (File f : files)
			{
				String s = readFile(f);
				String line = "";
				pw.print(String.format("%1$-" + 24 + "s", f.getName().replace(".dat", "")).substring(0, 24) + "  N099.00.00.000 E099.00.00.000 N099.00.00.000 E099.00.00.000\n");
				boolean start = false;
				for (String z : s.split("\n"))
				{
					if (z.contains("LINE !"))
					{
						start = true;
						pw.print(convertLine(line));
						line = "";
					} else if (start)
						line += "\n" + z;
				}

				i++;
				int k = (int) (100F * i / files.length);
				progressBar.setStringPainted(true);
				progressBar.setValue(k);
				progressBar.setString(i + " / " + files.length);
				progressBar.update(progressBar.getGraphics());
			}
			pw.println(SCT_END);
			pw.close();
			progressBar.setString("Converted " + files.length + " maps to sct2!");
		} catch (Exception ignored)
		{
		}

	}

	private String convertLine(String line)
	{
		String[] split = line.split("\n");
		if (split.length <= 1) return "";
		String out = "                          " + convertIndividualLine(split[1]);
		for (int i = 1; i < split.length - 1; i++)
			out += " " + convertIndividualLine(split[i]) + "\n                          " + convertIndividualLine(split[i]);
		out += " " + convertIndividualLine(split[split.length - 1]);
		return out + "\n";
	}

	private String convertIndividualLine(String s)
	{
		String[] z = s.split(" ");
		return "N0" + z[1] + "." + z[2] + "." + z[3].substring(0, 6) + " W" + z[5] + "." + z[6] + "." + z[7].substring(0, 6);
	}

	public void setProgressBar(JProgressBar progressBar)
	{
		this.progressBar = progressBar;
	}
}
