package TuxPowa.Control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import TuxPowa.View.InterfaceUtilisateur;

public class updateImageAction implements ActionListener {

	InterfaceUtilisateur interfaceUtilisateur;

	public updateImageAction(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (interfaceUtilisateur.videoProcessor.hasNextImage()) {
			interfaceUtilisateur.videoProgressBar.setValue(interfaceUtilisateur.currentFrame);
			interfaceUtilisateur.setFrameIndicatorLabel();
			interfaceUtilisateur.currentImage = new ImageIcon(
					InterfaceUtilisateur.resizeBufferedImage(interfaceUtilisateur.videoProcessor.nextImage(),
							interfaceUtilisateur.videoPanel.getWidth(), interfaceUtilisateur.videoPanel.getHeight()));
			// currentImage = new ImageIcon(videoProcessor.nextImage());
			interfaceUtilisateur.videoPanel.setIcon(interfaceUtilisateur.currentImage);
			interfaceUtilisateur.videoPanel.repaint();
			interfaceUtilisateur.currentFrame++;
		} else {
			interfaceUtilisateur.changeToPausedState();
			interfaceUtilisateur.currentFrame = interfaceUtilisateur.startFrame;
			interfaceUtilisateur.locateFrame();
		}
	}

}