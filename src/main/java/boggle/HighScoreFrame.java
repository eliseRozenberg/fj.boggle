package boggle;

import javax.swing.JFrame;

import com.google.inject.Inject;
import com.google.inject.Singleton;
@Singleton
public class HighScoreFrame extends JFrame {

	private StartFrame startFrame;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	public HighScoreFrame(StartFrame frame) {
		startFrame = frame;
	}
}
