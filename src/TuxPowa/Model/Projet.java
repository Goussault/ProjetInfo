package TuxPowa.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Projet implements Serializable {

	private static final long serialVersionUID = 1L;
	public String fileName;
	public String teamName0 = null;
	public String teamName1 = null;
	public List<Integer> coordinatesXOfTheBall = new ArrayList<>();
	public List<Integer> coordinatesYOfTheBall = new ArrayList<>();
	public List<MatchEvent> matchEvents = new ArrayList<>();
	public Integer[] score = new Integer[] { 0, 0 };
	public Integer[] coordinatesXOfEdge = new Integer[4];
	public Integer[] coordinatesYOfEdge = new Integer[4];
	public Integer[] coordinatesXOfGoal = new Integer[4];
	public Integer[] coordinatesYOfGoal = new Integer[4];
	double[] threshold = new double[] { 0, 0, 0 };

	@Override
	public String toString() {
		return "Projet [fileName=" + fileName + ", teamName0=" + teamName0 + ", teamName1=" + teamName1
				+ ", coordinatesXOfTheBall=" + coordinatesXOfTheBall + ", coordinatesYOfTheBall="
				+ coordinatesYOfTheBall + ", matchEvents=" + matchEvents + ", score=" + Arrays.toString(score)
				+ ", coordinatesXOfEdge=" + Arrays.toString(coordinatesXOfEdge) + ", coordinatesYOfEdge="
				+ Arrays.toString(coordinatesYOfEdge) + ", coordinatesXOfGoal=" + Arrays.toString(coordinatesXOfGoal)
				+ ", coordinatesYOfGoal=" + Arrays.toString(coordinatesYOfGoal) + ", threshold="
				+ Arrays.toString(threshold) + "]";
	}

}
