package boggle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

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
		

		StringBuilder builder = new StringBuilder();
		builder.append("https://wordsapiv1.p.mashape.com/words/");
		builder.append(word);
		builder.append("/definitions");

		HttpResponse<JsonNode> response = null;

		try {
			response = Unirest
					.get(builder.toString())
					.header("X-Mashape-Key",
							"tUX0EvhpmFmshGEJpal40dLinQHip1nvCqWjsnERTWgoGmbBcK")
					.header("Accept", "application/json").asJson();
		} catch (UnirestException e) {
			frame.setWordInvalid();
			caught = true;

		}
		

		if (!caught) {
			int size = word.length();
			int points = frame.addScore(size);
			frame.appendWord(word, points);
			frame.setWordValid();
		}
	}
}
