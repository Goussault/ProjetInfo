package TuxPowa.Control;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import TuxPowa.View.InterfaceUtilisateur;

public class sampleBallMouseEvent extends MouseAdapter {

	InterfaceUtilisateur interfaceUtilisateur;

	public sampleBallMouseEvent(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	public List<Integer> x = new ArrayList<>();
	public List<Integer> y = new ArrayList<Integer>();
	public double[] threshold = new double[3];
	long startTime;
	long endTime;

	public sampleBallMouseEvent() {
		super();
		init();
	}

	public void init() {
		x.clear();
		y.clear();
		startTime = 0;
		endTime = 0;
	}

	public void mouseClicked(MouseEvent e) {
		endTime = System.currentTimeMillis();
		if ((endTime - startTime) > 50) {
			startTime = endTime;
			x.add(2 * e.getX());
			y.add(2 * e.getY());
			interfaceUtilisateur.sampleBallImageEditable = interfaceUtilisateur.sampleBallImage.createGraphics();
			interfaceUtilisateur.sampleBallImageEditable.setColor(Color.RED);
			interfaceUtilisateur.sampleBallImageEditable.drawOval(e.getX() - 1, e.getY() - 1, 3, 3);
			interfaceUtilisateur.sampleBallImageEditable.dispose();
			interfaceUtilisateur.sampleBallSketchpad.setIcon(new ImageIcon(interfaceUtilisateur.sampleBallImage));
			interfaceUtilisateur.sampleBallSketchpad.repaint();
		}
	}
}