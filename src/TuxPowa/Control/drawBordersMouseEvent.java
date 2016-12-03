package TuxPowa.Control;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import TuxPowa.View.InformationLabel;
import TuxPowa.View.InterfaceUtilisateur;

public class drawBordersMouseEvent extends MouseAdapter {

	InterfaceUtilisateur interfaceUtilisateur;

	public drawBordersMouseEvent(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
		init();
	}

	public Integer[] coordinatesXOfEdge;
	public Integer[] coordinatesYOfEdge;
	public Integer[] coordinatesXOfGoal;
	public Integer[] coordinatesYOfGoal;
	Point lastPoint;
	int count;
	long startTime;
	long endTime;

	public void init() {
		coordinatesXOfEdge = new Integer[4];
		coordinatesYOfEdge = new Integer[4];
		coordinatesXOfGoal = new Integer[4];
		coordinatesYOfGoal = new Integer[4];
		for (int i = 0; i < 4; i++) {
			coordinatesXOfEdge[i] = -1;
			coordinatesYOfEdge[i] = -1;
			coordinatesXOfGoal[i] = -1;
			coordinatesYOfGoal[i] = -1;
		}
		lastPoint = null;
		count = 0;
		startTime = 0;
		endTime = 0;
	}

	public void mouseClicked(MouseEvent e) {
		endTime = System.currentTimeMillis();
		if ((endTime - startTime) > 50) {
			startTime = endTime;
			if (count <= 3) {
				coordinatesXOfGoal[count] = 2 * e.getX();
				coordinatesYOfGoal[count] = 2 * e.getY();
				interfaceUtilisateur.drawBordersImageEditable = interfaceUtilisateur.drawBordersImage.createGraphics();
				interfaceUtilisateur.drawBordersImageEditable.setColor(Color.GREEN);
				interfaceUtilisateur.drawBordersImageEditable.drawOval(e.getX(), e.getY(), 3, 3);
				if (lastPoint != null) {
					interfaceUtilisateur.drawBordersImageEditable.setColor(Color.GREEN);
					interfaceUtilisateur.drawBordersImageEditable.setStroke(new BasicStroke(5.0f));
					interfaceUtilisateur.drawBordersImageEditable.drawLine(e.getX(), e.getY(), lastPoint.x,
							lastPoint.y);
					lastPoint = null;
				} else {
					lastPoint = e.getPoint();
				}
				interfaceUtilisateur.drawBordersImageEditable.dispose();
				interfaceUtilisateur.drawBordersSketchpad.setIcon(new ImageIcon(interfaceUtilisateur.drawBordersImage));
				interfaceUtilisateur.drawBordersSketchpad.repaint();
			} else if (count <= 7) {
				coordinatesXOfEdge[count - 4] = 2 * e.getX();
				coordinatesYOfEdge[count - 4] = 2 * e.getY();
				interfaceUtilisateur.drawBordersImageEditable = interfaceUtilisateur.drawBordersImage.createGraphics();
				interfaceUtilisateur.drawBordersImageEditable.setColor(Color.BLUE);
				interfaceUtilisateur.drawBordersImageEditable.drawOval(e.getX(), e.getY(), 3, 3);
				if (lastPoint != null) {
					interfaceUtilisateur.drawBordersImageEditable.setColor(Color.BLUE);
					interfaceUtilisateur.drawBordersImageEditable.setStroke(new BasicStroke(5.0f));
					interfaceUtilisateur.drawBordersImageEditable.drawLine(e.getX(), e.getY(), lastPoint.x,
							lastPoint.y);
					lastPoint = null;
				} else {
					lastPoint = e.getPoint();
				}
				interfaceUtilisateur.drawBordersImageEditable.dispose();
				interfaceUtilisateur.drawBordersSketchpad.setIcon(new ImageIcon(interfaceUtilisateur.drawBordersImage));
				interfaceUtilisateur.drawBordersSketchpad.repaint();
			} else {
				JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow,
						new JLabel(InformationLabel.alreadyEnouge), InformationLabel.info,
						JOptionPane.INFORMATION_MESSAGE);
			}
			if (count == 3) {
				interfaceUtilisateur.drawBordersLabel.setText(InformationLabel.draw2);
			}
			if (count == 7) {
				interfaceUtilisateur.drawBordersLabel.setText(InformationLabel.alreadyEnouge);
			}
			count++;
		}
	}
}