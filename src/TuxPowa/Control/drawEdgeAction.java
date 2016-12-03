package TuxPowa.Control;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import TuxPowa.View.InformationLabel;
import TuxPowa.View.InterfaceUtilisateur;

public class drawEdgeAction implements ActionListener {

	InterfaceUtilisateur interfaceUtilisateur;

	public drawEdgeAction(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!interfaceUtilisateur.videoProcessor.isOpened()) {
			JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.pleaseOpenFile),
					InformationLabel.error, JOptionPane.ERROR_MESSAGE);
		} else if (interfaceUtilisateur.videoProcessor.isDrawed()) {
			if (JOptionPane.showConfirmDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.replaceDraw),
					InformationLabel.info, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				interfaceUtilisateur.videoProcessor.cancelDraw();
				interfaceUtilisateur.locateFrame();
				drawEdge();
			}
		} else {
			drawEdge();
		}
	}

	private void drawEdge() {
		interfaceUtilisateur.currentFrame = interfaceUtilisateur.startFrame;
		interfaceUtilisateur.locateFrame();
		if (interfaceUtilisateur.videoProcessor.hasNextImage()) {
			interfaceUtilisateur.drawBordersImage = InterfaceUtilisateur.resizeBufferedImage(
					interfaceUtilisateur.videoProcessor.nextImage(),
					interfaceUtilisateur.videoProcessor.getVideoWidth() / 2,
					interfaceUtilisateur.videoProcessor.getVideoHeight() / 2);
		}
		interfaceUtilisateur.drawBordersSketchpad.setIcon(new ImageIcon(interfaceUtilisateur.drawBordersImage));
		interfaceUtilisateur.drawBordersSketchpad.repaint();

		interfaceUtilisateur.drawBordersSketchpad.addMouseListener(interfaceUtilisateur.drawBordersMouseEvent);

		interfaceUtilisateur.drawBordersPanel.setLayout(new BorderLayout(20, 20));
		interfaceUtilisateur.drawBordersPanel.add(interfaceUtilisateur.drawBordersLabel, BorderLayout.NORTH);
		interfaceUtilisateur.drawBordersPanel.add(interfaceUtilisateur.drawBordersSketchpad, BorderLayout.CENTER);
		interfaceUtilisateur.drawBordersPanel.setVisible(true);
		// save the coordinates
		int reply = JOptionPane.showConfirmDialog(interfaceUtilisateur.mainWindow,
				interfaceUtilisateur.drawBordersPanel, "tracer les contours", JOptionPane.OK_CANCEL_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			interfaceUtilisateur.videoProcessor.draw(interfaceUtilisateur.drawBordersMouseEvent.coordinatesXOfEdge,
					interfaceUtilisateur.drawBordersMouseEvent.coordinatesYOfEdge,
					interfaceUtilisateur.drawBordersMouseEvent.coordinatesXOfGoal,
					interfaceUtilisateur.drawBordersMouseEvent.coordinatesYOfGoal);
			interfaceUtilisateur.locateFrame();
		}
	}

}