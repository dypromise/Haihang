package com.greenorbs.tagassist.flightproxy.dcsadapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.log4j.Logger;

public class DCSBlockReader {
	
	protected static Logger _logger = Logger.getLogger(DCSBlockReader.class);
	
	private static HashSet<String> _blockTokens = new HashSet<String>();
	
	static {
		_blockTokens.add(DCSBlock.TOKEN_BSM);
		_blockTokens.add(DCSBlock.TOKEN_HOLD);
	}
	
	public static ArrayList<DCSBlock> readBlocks(File file) {
		_logger.info("Reading blocks from file: " + file.getName());
		
		ArrayList<DCSBlock> blocks = new ArrayList<DCSBlock>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			DCSBlock block = null;
			do {
				block = readNextBlock(in);
				if (block != null) {
					blocks.add(block);
				}
			} while (block != null);
			in.close();
		} catch (Exception e) {
			blocks = null;
			_logger.error(e);
		}
		
		return blocks;
	}
	
	private static DCSBlock readNextBlock(BufferedReader in) {
		DCSBlock block = null;
		
		try {
			if (in.ready()) {
				String line = null;
				
				while (in.ready()) {
					line = in.readLine().trim();
					if (_blockTokens.contains(line)) {
						block = new DCSBlock(line);
						break;
					}
				}
				
				if (block != null) {
					String endToken = block.getEndToken();
					while (in.ready() && !(line = in.readLine().trim()).startsWith(endToken)) {
						block.appendLine(line);
					}
				}
			}
		} catch (Exception e) {
			block = null;
			_logger.error(e);
		}
		
		return block;
	}
	
}
