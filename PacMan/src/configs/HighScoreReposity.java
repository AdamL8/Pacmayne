package configs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Score;

public class HighScoreReposity {
	private static Logger logger = Logger.getAnonymousLogger();
	private FileWriter highScoreWriter = null;
	private FileReader highScoreFile = null;
	private BufferedReader bufferReader;
	private BufferedWriter bufferWriter;

	public List<Score> getHighScores(String path) {
		List<Score> scores = new ArrayList<>();
		try {
			highScoreFile = new FileReader(path);
			bufferReader = new BufferedReader(highScoreFile);
			while (true) {
				String line = bufferReader.readLine();
				if (line == null) {
					break;
				}
				scores.add(createLineScore(line));
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				bufferReader.close();
				highScoreFile.close();
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}

		return scores;
	}

	public Score createLineScore(String line) {
		String[] score;
		score = line.split(" ");
		int myScore = Integer.parseInt(score[0]);
		String myName = score[1];
		return new Score(myScore, myName);
	}

	public void writeHighScore(String path, List<Score> scores) {
		try {
			highScoreWriter = new FileWriter(path);
			bufferWriter = new BufferedWriter(highScoreWriter);

			for (Score score : scores) {
				String valueWrite = score.getScore() + " " + score.getName() + "\n";
				bufferWriter.write(valueWrite);
			}

		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				bufferWriter.flush();
				bufferWriter.close();
				highScoreWriter.close();
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
	}

	public void replaceHighScore(Score score) {
		List<Score> scores = getHighScores("ressource/text/highScoreRead.txt");
		for (Score existingScore : scores) {
			int index = scores.indexOf(existingScore);
			if (score.getScore() > existingScore.getScore()) {
				scores.set(index, score);
				score = existingScore;
			} else {
				scores.set(index, existingScore);
			}
		}
		writeHighScore("ressource/text/highScoreRead.txt", scores);
	}

}
