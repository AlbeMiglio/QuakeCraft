package com.gmail.filoghost.quakecraft.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

import com.gmail.filoghost.quakecraft.QuakeCraft;

public class LogFile {
	
	public static void writeLine(String line) {
		
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(new File(QuakeCraft.plugin.getDataFolder(), "quake.log"), true));
			writer.newLine();
			writer.write(line);
			
		} catch (Exception ex) {
			
			QuakeCraft.logger.log(Level.WARNING, "Unable to write a log", ex);
			
		} finally {
			
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		
	}
}
