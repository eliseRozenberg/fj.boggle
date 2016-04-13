package boggle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;

public class BoggleThread extends Thread {

	private final String word;
	private final BoggleFrame frame;
	private boolean caught;

	public BoggleThread(String word, BoggleFrame frame) {

		this.frame = frame;
		this.word = word;
		this.caught = false;
	}

	@Override
	public void run() {

		try {
			URL dictionaryURL = new URL("https://en.wiktionary.org/w/api.php?action=query&format=json&titles=" + word);
			HttpURLConnection connection = (HttpURLConnection) dictionaryURL.openConnection();
			InputStream input = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			Gson gson = new Gson();
			WordExistsJson wordExists = gson.fromJson(reader, WordExistsJson.class);

			if (wordExists.getQuery().getPages().containsKey(-1)) {
				frame.setWordInvalid();
				caught = true;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!caught) {
			int size = word.length();
			int points = frame.addScore(size);
			frame.appendWord(word, points);
			frame.setWordValid();
		}
	}
}
