package TuxPowa.Model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import TuxPowa.Model.VideoPlayer;

public class VideoProcessor extends VideoPlayer {

	static {
		   System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		   System.loadLibrary("opencv_ffmpeg2413_64");
	}
	static int lastXball = 0;
	static int lastYball = 0;
	
	private String teamName0 = null;
	private String teamName1 = null;
	private Integer[] coordinatesXOfEdge = new Integer[4];
	private Integer[] coordinatesYOfEdge = new Integer[4];
	private Integer[] coordinatesXOfGoal = new Integer[4];
	private Integer[] coordinatesYOfGoal = new Integer[4];
	private List<Integer> coordinatesXOfTheBall = new ArrayList<>();
	private List<Integer> coordinatesYOfTheBall = new ArrayList<>();
	/**
	 * <p>
	 * ballCourse:
	 * </p>
	 * <p>
	 * - 0: can't detect the ball
	 * </p>
	 * <p>
	 * - 1: in the court
	 * </p>
	 * <p>
	 * - 2: at the left edge
	 * </p>
	 * <p>
	 * - 3: at the right edge
	 * </p>
	 * <p>
	 * - 4: at the left goal
	 * </p>
	 * <p>
	 * - 5: at the right goal
	 * </p>
	 * <p>
	 * - 6: out of the court
	 * </p>
	 */
	private List<Integer> ballCourse = new ArrayList<>();

	private List<MatchEvent> matchEvents = new ArrayList<>();

	public String[] getEvents() {
		String[] answer = new String[matchEvents.size()];
		int i = 0;
		for (MatchEvent m : matchEvents) {
			answer[i++] = m.toString();
		}
		return answer;
	}

	private Integer[] score = new Integer[] { 0, 0 };

	public Integer[] getScore() {
		return score;
	}

	private List<String> history = new ArrayList<>();

	public List<String> getHistory() {
		return history;
	}

	public VideoProcessor() {
		super();
		if (!new File("project" + File.separator + "history").exists()) {
			try {
				new File("project" + File.separator + "history").createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader("project" + File.separator + "history"));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				history.add(line);
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void open(String fileName) {
		treated = false;
		drawed = false;
		named = false;
		super.open(fileName);
		tryDeserialize();
	}

	public void close() {
		treated = false;
		drawed = false;
		named = false;
		super.close();
	}

	/**
	 * To demand the next image of the video.
	 * 
	 * @author ZHANG Heng, YAN Yutong
	 * 
	 * @return if it has opened, return true; else return false.
	 * 
	 * @version 1.0
	 * 
	 * @since 1.0
	 */
	public BufferedImage nextImage() {
		BufferedImage nextImage = super.nextImage();
		Graphics2D nextImageEditable = nextImage.createGraphics();
		nextImageEditable.setStroke(new BasicStroke(5.0f));
		if (drawed) {
			// draw the position of goal
			nextImageEditable.setColor(Color.GREEN);
			nextImageEditable.drawLine(coordinatesXOfGoal[0], coordinatesYOfGoal[0], coordinatesXOfGoal[1],
					coordinatesYOfGoal[1]);
			nextImageEditable.drawLine(coordinatesXOfGoal[2], coordinatesYOfGoal[2], coordinatesXOfGoal[3],
					coordinatesYOfGoal[3]);
			// draw the position of edge
			nextImageEditable.setColor(Color.BLUE);
			nextImageEditable.drawLine(coordinatesXOfEdge[0], coordinatesYOfEdge[0], coordinatesXOfEdge[1],
					coordinatesYOfEdge[1]);
			nextImageEditable.drawLine(coordinatesXOfEdge[2], coordinatesYOfEdge[2], coordinatesXOfEdge[3],
					coordinatesYOfEdge[3]);
		}
		if (treated) {
			// draw the position of ball
			nextImageEditable.setColor(Color.RED);
			int widthBall = 10;
			nextImageEditable.drawOval(coordinatesXOfTheBall.get(super.getCurrentPositionFrame()) - widthBall,
					coordinatesYOfTheBall.get(super.getCurrentPositionFrame()) - widthBall, 2 * widthBall,
					2 * widthBall);
		}
		if (sampled) {
			// write the threshold
			nextImageEditable.setColor(Color.WHITE);
			String str = new StringBuilder().append("seuillage: " + threshold[0] + " ; " + threshold[1] + " ; " + threshold[2])
					.toString();
			int x = 50;
			int y = 50;
			nextImageEditable.drawString(str, x, y);
		}
		nextImageEditable.dispose();
		return nextImage;
	}

	boolean named = false;

	public void name(String teamName0, String teamName1) {
		this.teamName0 = teamName0;
		this.teamName1 = teamName1;
		named = true;
	}

	public void cancelName() {
		named = false;
	}

	public boolean isNamed() {
		return named;
	}

	public String[] getNames() {
		return new String[] { teamName0, teamName1 };
	}

	boolean drawed = false;

	public void draw(Integer[] coordinatesXOfEdge, Integer[] coordinatesYOfEdge, Integer[] coordinatesXOfGoal,
			Integer[] coordinatesYOfGoal) {
		this.coordinatesXOfEdge = coordinatesXOfEdge;
		this.coordinatesYOfEdge = coordinatesYOfEdge;
		this.coordinatesXOfGoal = coordinatesXOfGoal;
		this.coordinatesYOfGoal = coordinatesYOfGoal;
		drawed = true;
	}

	public void cancelDraw() {
		drawed = false;
	}

	public boolean isDrawed() {
		return drawed;
	}

	double[] threshold = new double[] { 0, 0, 0 };

	boolean sampled = false;

	public void sample(List<Integer> x, List<Integer> y) {
		for (int i = 0; i < x.size(); i++) {
			double[] temp = super.currentImage.get((int) y.get(i), (int) x.get(i));
			for (int j = 0; j < threshold.length; j++) {
				threshold[j] += temp[j];
			}
		}
		for (int i = 0; i < threshold.length; i++) {
			threshold[i] = threshold[i] / x.size();
		}
		sampled = true;
	}

	public void cancelSample() {
		sampled = false;
	}

	public boolean isSampled() {
		return sampled;
	}

	boolean treated = false;

	public void treat() {

		coordinatesXOfTheBall.clear();
		coordinatesYOfTheBall.clear();
		ballCourse.clear();
		matchEvents.clear();

		super.setCurrentPositionFrame(0);
		do {
			Mat image = super.nextMat();
			int[] result = AnalyzeTheLocation(image);
			coordinatesXOfTheBall.add(result[0]*8/5);
			coordinatesYOfTheBall.add(result[1]*3/2);
			ballCourse.add(result[2]);
		} while (super.hasNextImage());
		updateMatchEvents();
		updateMatchScore();
		serializeProjet();
		addHistory();
		treated = true;
	}

	public void cancelTreat() {
		named = false;
		drawed = false;
		sampled = false;
		treated = false;
		deleteHistory();
		deserializeProjet();
	}

	public boolean isTreated() {
		return treated;
	}

	private void addHistory() {
		if (history.indexOf(super.fileName) == -1) {
			history.add(super.fileName);
			try {
				PrintWriter out = new PrintWriter(
						new BufferedWriter(new FileWriter("project" + File.separator + "history")));
				out.flush();
				for (String s : history) {
					out.println(s);
				}
				out.close();
			} catch (IOException e) {
				if (!new File("project" + File.separator + "history").exists()) {
					try {
						new File("project" + File.separator + "history").createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					e.printStackTrace();
				}
			}
		}
	}

	private void deleteHistory() {
		for (Iterator<String> iter = history.listIterator(); iter.hasNext();) {
			if (iter.next() == super.fileName) {
				iter.remove();
			}
		}
		try {
			PrintWriter out = new PrintWriter(
					new BufferedWriter(new FileWriter("project" + File.separator + "history")));
			out.flush();
			for (String s : history) {
				out.println(s);
			}
			out.close();
		} catch (IOException e) {
			if (!new File("project" + File.separator + "history").exists()) {
				try {
					new File("project" + File.separator + "history").createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				e.printStackTrace();
			}
		}
	}

	static private final HashMap<String, EventsPossible> matchTemplates = new HashMap<>();
	static {
		matchTemplates.put("150", EventsPossible.left_but);
		matchTemplates.put("151", EventsPossible.left_gamelle);
		matchTemplates.put("136", EventsPossible.left_sortie_de_terrain);
		matchTemplates.put("140", EventsPossible.right_but);
		matchTemplates.put("141", EventsPossible.right_gamelle);
		matchTemplates.put("126", EventsPossible.right_sortie_de_terrain);
	}

	private void tryDeserialize() {
		if (new File("project" + File.separator + super.fileName.hashCode()).exists()) {
			try {
				FileInputStream fis = new FileInputStream("project" + File.separator + super.fileName.hashCode());
				ObjectInputStream ois = new ObjectInputStream(fis);
				Projet p = (Projet) ois.readObject();
				// System.out.println(p);
				super.fileName = p.fileName;
				this.coordinatesXOfEdge = p.coordinatesXOfEdge;
				this.coordinatesYOfEdge = p.coordinatesYOfEdge;
				this.coordinatesXOfGoal = p.coordinatesXOfGoal;
				this.coordinatesYOfGoal = p.coordinatesYOfGoal;
				this.coordinatesXOfTheBall = p.coordinatesXOfTheBall;
				this.coordinatesYOfTheBall = p.coordinatesYOfTheBall;
				this.matchEvents = p.matchEvents;
				this.score = p.score;
				this.teamName0 = p.teamName0;
				this.teamName1 = p.teamName1;
				this.threshold = p.threshold;
				treated = true;
				named = true;
				sampled = true;
				drawed = true;
				fis.close();
				ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void serializeProjet() {

		Projet p = new Projet();
		p.fileName = super.fileName;
		p.coordinatesXOfEdge = this.coordinatesXOfEdge;
		p.coordinatesYOfEdge = this.coordinatesYOfEdge;
		p.coordinatesXOfGoal = this.coordinatesXOfGoal;
		p.coordinatesYOfGoal = this.coordinatesYOfGoal;
		p.coordinatesXOfTheBall = this.coordinatesXOfTheBall;
		p.coordinatesYOfTheBall = this.coordinatesYOfTheBall;
		p.matchEvents = this.matchEvents;
		p.score = this.score;
		p.teamName0 = this.teamName0;
		p.teamName1 = this.teamName1;
		p.threshold = this.threshold;

		try {
			File f = new File("project" + File.separator + p.fileName.hashCode());
			if (!f.getParentFile().exists())
				f.getParentFile().mkdirs();
			if (!f.exists())
				f.createNewFile();
			FileOutputStream fileOut = new FileOutputStream("project" + File.separator + p.fileName.hashCode());
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(p);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}

	}

	private void deserializeProjet() {

		File f = new File("project" + File.separator + super.fileName.hashCode());
		if (f.exists()) {
			f.delete();
		}

	}

	/**
	 * analyze the image
	 * 
	 * @param image
	 * @return 3 integer: first:x axis of the ball; second: y axis of the
	 *         ball;third: position of the ball
	 *
	 *         position: - 0: can't detect the ball - 1: in the court - 2: at
	 *         the edge - 3: at the goal - 4: out of the court
	 *
	 */
	private int[] AnalyzeTheLocation(Mat image) {
	  
		   Mat hierarchy = new Mat();
	       Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10));

		   // Conversion en HSV et separation des composantes
		   Mat out = new Mat(image.size(), CvType.CV_8UC1);
	       Imgproc.cvtColor(image, out, Imgproc.COLOR_BGR2HSV);
	       Vector<Mat> channels = new Vector<Mat>();
	       Core.split(out, channels);
	       Mat dst = new Mat(channels.elementAt(0).size(), CvType.CV_8UC1);
	       Mat dst2 = new Mat(channels.elementAt(0).size(), CvType.CV_8UC1);
	       Mat dst3 = new Mat(channels.elementAt(0).size(), CvType.CV_8UC1);
	       
	       // Seuillage suivant les composantes H et S
	       Imgproc.threshold(channels.elementAt(0), dst, 50, 255, Imgproc.THRESH_BINARY_INV);
	       Imgproc.threshold(channels.elementAt(1), dst2, 125, 255, Imgproc.THRESH_BINARY);
	       Core.bitwise_and(dst, dst2, dst3);
	       
	       // Redimension de l'image et filtrage
	       Imgproc.resize(dst3, out, new Size(800, 480));
	       Imgproc.GaussianBlur(out, out, new Size(9,9), 0, 0); 
	       Imgproc.erode(out, out, element);
	       Imgproc.dilate(out, out, element);
	       
	       // Detecte les bords en utilisant 
	       Mat out_canny = new Mat();
	       Imgproc.Canny( out, out_canny, 50.0, 50.0*2);
	      
	       // Trouves les contours
	       List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	       Imgproc.findContours(out_canny, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0,0));

	       // Obtient les moments
	       Vector<Moments> mu = new Vector<Moments>();
	       mu.setSize(contours.size());
	       for( int i = 0; i < contours.size(); i++ )
	          { 
	    	   		mu.set(i, Imgproc.moments( contours.get(i), false)); 
	          }
	       
	       //Si la balle est detectée 
	       if(contours.size() != 0)
	       {
		       Point mc = new Point(mu.get(0).get_m10()/mu.get(0).get_m00() , mu.get(0).get_m01()/mu.get(0).get_m00());
		       lastXball = (int) (mu.get(0).get_m10()/mu.get(0).get_m00());// Enregistre le dernier coup au cas où la prochaine image il n'y a 
	    	   lastYball = (int) (mu.get(0).get_m01()/mu.get(0).get_m00());// pas de de balle detectée
		       System.out.println(mc.toString()); 
		       return new int[] {(int)mc.x, (int)mc.y, 2};
	       }
	       else //Si elle n'est pas détécté
	       {
	    	   Point mc = new Point(lastXball, lastYball);
	    	   System.out.println(mc.toString()); 
	    	   return new int[] {(int)mc.x, (int)mc.y, 2};
	       }	      
	}
	
	/*private int zone_detected(int x, int y)
	{
		/*private Integer[] coordinatesXOfEdge = new Integer[4];
		  private Integer[] coordinatesYOfEdge = new Integer[4];
		  private Integer[] coordinatesXOfGoal = new Integer[4];
		  private Integer[] coordinatesYOfGoal = new Integer[4];*/
	/*	int zone = 0;
		if (x < )
		return zone;*/
	//}

	private void updateMatchEvents() {
		ballCourse.clear();
		int[] aaa = new int[] { 1, 5, 0, 1, 5, 1, 1, 3, 6, 1, 4, 0, 1, 4, 1, 1, 2, 6 };
		for (int aa : aaa) {
			ballCourse.add(aa);
		}
		List<Integer> temp = new ArrayList<>();
		Integer old = ballCourse.get(0);
		temp.add(old);
		for (Integer i : ballCourse) {
			if (i != old) {
				old = i;
				temp.add(old);
			}
		}
		// System.out.println(temp);
		int i = 0;
		do {
			String s = String.valueOf(temp.get(i)) + String.valueOf(temp.get(i + 1)) + String.valueOf(temp.get(i + 2));
			for (String template : matchTemplates.keySet()) {
				if (template.equals(s)) {
					matchEvents.add(new MatchEvent(teamName0, teamName1, matchTemplates.get(template),
							super.getCurrentPositionFrame()));
				}
			}
			i++;
		} while (i + 3 <= temp.size());
	}

	private void updateMatchScore() {
		for (MatchEvent event : matchEvents) {
			switch (event.getEvent()) {
			case left_but: {
				score[0]++;
				break;
			}
			case left_gamelle: {
				score[0] += 2;
				break;
			}
			case right_but: {
				score[1]++;
				break;
			}
			case right_gamelle: {
				score[1] += 2;
				break;
			}
			default:
				break;

			}
		}
	}

	public boolean generateMatchFileMd(String directory, String fileName) {
		if (treated) {
			PrintWriter out = null;
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(directory + fileName)));
				out.println("# Match results:");
				out.println("## Match events:");
				for (MatchEvent event : matchEvents) {
					out.println("1. " + event.toString());
				}
				out.println("## The final score:");
				out.println("| " + teamName0 + " | " + teamName1 + " |");
				out.println("|---|---|");
				out.println(String.valueOf("| " + score[0]) + " | " + String.valueOf(score[1]) + " |");
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				out.close();
			}
			return true;
		} else {
			return false;
		}

	}

	public boolean generateMatchFileJson(String directory, String fileName) {
		if (treated) {
			PrintWriter out = null;
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(directory + fileName)));
				out.println("{");
				out.println();
				out.println("\t\"teamNames\": ");
				out.println("\t{");
				out.println("\t\t\"teamName0\": \"" + teamName0 + "\",");
				out.println("\t\t\"teamName1\": \"" + teamName1 + "\"");
				out.println("\t},");
				out.println();
				out.println("\t\"matchEvents\": ");
				out.println("\t[");
				int i = 0;
				for (MatchEvent event : matchEvents) {
					i++;
					out.println("\t\t{");
					out.println("\t\t\t\"frame\": \"" + event.frame + "\",");
					out.println("\t\t\t\"teamName\": \"" + event.teamName + "\",");
					switch (event.event) {
					case left_but:
					case right_but:
						out.println("\t\t\t\"event\": \"but\",");
						break;
					case left_gamelle:
					case right_gamelle:
						out.println("\t\t\t\"event\": \"gamelle\",");
						break;
					case left_sortie_de_terrain:
					case right_sortie_de_terrain:
						out.println("\t\t\t\"event\": \"sortie_de_terrain\",");
						break;
					default:
						out.println("\t\t\t\"event\": \"System error.\",");
						break;
					}
					if (i == matchEvents.size()) {
						out.println("\t\t}");
					} else {
						out.println("\t\t},");
					}

				}
				out.println("\t],");
				out.println();
				out.println("\t\"matchScores\": ");
				out.println("\t{");
				out.println("\t\t\"" + teamName0 + "\": \"" + score[0] + "\",");
				out.println("\t\t\"" + teamName1 + "\": \"" + score[1] + "\"");
				out.println("\t}");
				out.println();
				out.println("}");

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				out.close();
			}
			return true;
		} else {
			return false;
		}
	}

}
