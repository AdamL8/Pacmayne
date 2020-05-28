package image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.FileFinder;

public class FontRepository {

	private static Logger logger = Logger.getAnonymousLogger();
	private static String[] fontSprites = new String[26];
	private static String[] numberSprites = new String[10];
	private static FileFinder finder = new FileFinder("*.png");
	private static String resourceDirectoryPath = "ressource/sprites/";
	private char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'z'};
	private Integer[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	private Map<Character, String> mapPathLetter = new HashMap<>();
	private Map<Integer, String> mapPathNumber = new HashMap<>();
	
	public FontRepository() {
		letterToSprite();
		numberToSprite();
	}
	
	public String getFont(char letter) throws Exception {
		if(!mapPathLetter.containsKey(letter)) {
			throw new Exception("Try to access unknown letter " + letter);
		}
		return mapPathLetter.get(letter);
	}
	
	public String getFont(int number) throws Exception {
		if(!mapPathNumber.containsKey(number)) {
			throw new Exception("Try to access unknown number " + number);
		}
		return mapPathNumber.get(number);
	}
	
	private void letterToSprite() {
		for(int i=0; i<letters.length; ++i) {
			StringBuilder nameFile = new StringBuilder();
			nameFile.append("letter-");
			nameFile.append(letters[i]);
			nameFile.append(".png");
			finder.setPattern(nameFile.toString());
			try {
				Files.walkFileTree(Paths.get(resourceDirectoryPath), finder);
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
			// get the matched paths
	        Collection<Path> matchedFiles = finder.getMatchedPaths();
			
	        // print the matched paths
	        for (Path path : matchedFiles) {
	        	if(path != null) {
	        		mapPathLetter.put(letters[i], resourceDirectoryPath + path.getFileName().toString());
	        	}
	        }
	        finder.clearMatches();
		}
	}
	
	private void numberToSprite() {
		for(int i=0; i<numbers.length; ++i) {
			StringBuilder nameFile = new StringBuilder();
			nameFile.append("number-");
			nameFile.append(numbers[i].toString());
			nameFile.append("*.png");
			finder.setPattern(nameFile.toString());
			try {
				Files.walkFileTree(Paths.get(resourceDirectoryPath), finder);
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
			// get the matched paths
	        Collection<Path> matchedFiles = finder.getMatchedPaths();
			
	        // print the matched paths
	        for (Path path : matchedFiles) {
	        	mapPathNumber.put(numbers[i], resourceDirectoryPath + path.getFileName().toString());
	        }
	        finder.clearMatches();
		}
	}

}
