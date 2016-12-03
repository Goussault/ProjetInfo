package TuxPowa.Control;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import TuxPowa.View.InformationLabel;
import TuxPowa.View.InterfaceUtilisateur;

public class sampleBallAction implements ActionListener {

	InterfaceUtilisateur interfaceUtilisateur;

	public sampleBallAction(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (!interfaceUtilisateur.videoProcessor.isOpened()) {
			JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.pleaseOpenFile),
					InformationLabel.error, JOptionPane.ERROR_MESSAGE);
		} else if (interfaceUtilisateur.videoProcessor.isSampled()) {
			if (JOptionPane.showConfirmDialog(interfaceUtilisateur.mainWindow,
					new JLabel(InformationLabel.replaceSample), InformationLabel.info,
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				interfaceUtilisateur.videoProcessor.cancelSample();
				interfaceUtilisateur.locateFrame();
				sample();
			}
		} else {
			sample();
		}
	}

	public void sample() {
		interfaceUtilisateur.currentFrame = interfaceUtilisateur.startFrame;
		interfaceUtilisateur.locateFrame();
		if (interfaceUtilisateur.videoProcessor.hasNextImage()) {
			interfaceUtilisateur.sampleBallImage = InterfaceUtilisateur.resizeBufferedImage(
					interfaceUtilisateur.videoProcessor.nextImage(),
					interfaceUtilisateur.videoProcessor.getVideoWidth() / 2,
					interfaceUtilisateur.videoProcessor.getVideoHeight() / 2);
		}
		interfaceUtilisateur.sampleBallSketchpad.setIcon(new ImageIcon(interfaceUtilisateur.sampleBallImage));
		interfaceUtilisateur.sampleBallSketchpad.repaint();

		interfaceUtilisateur.sampleBallSketchpad.addMouseListener(interfaceUtilisateur.sampleBallMouseEvent);

		interfaceUtilisateur.sampleBallNextImageButton.addActionListener(interfaceUtilisateur.sampleBallNextImageEvent);

		interfaceUtilisateur.sampleBallPanel.setLayout(new BorderLayout(20, 20));
		interfaceUtilisateur.sampleBallPanel.add(interfaceUtilisateur.sampleBallLabel, BorderLayout.NORTH);
		interfaceUtilisateur.sampleBallPanel.add(interfaceUtilisateur.sampleBallSketchpad, BorderLayout.CENTER);
		interfaceUtilisateur.sampleBallPanel.add(interfaceUtilisateur.sampleBallNextImageButton, BorderLayout.SOUTH);
		interfaceUtilisateur.sampleBallPanel.setVisible(true);
		// save the coordinates
		if (JOptionPane.showConfirmDialog(interfaceUtilisateur.mainWindow, interfaceUtilisateur.sampleBallPanel,
				"échantillonnage", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
			interfaceUtilisateur.videoProcessor.sample(interfaceUtilisateur.sampleBallMouseEvent.x,
					interfaceUtilisateur.sampleBallMouseEvent.y);
			interfaceUtilisateur.locateFrame();
		}
	}

}