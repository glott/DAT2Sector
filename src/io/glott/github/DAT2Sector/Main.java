package io.glott.github.DAT2Sector;

public class Main
{
	public static void main(String[] args)
	{
		DataHandler handler = new DataHandler();
		Display display = new Display(handler);
		display.run();
	}
}
