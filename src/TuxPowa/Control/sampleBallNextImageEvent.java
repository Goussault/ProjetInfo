package TuxPowa.Control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import TuxPowa.View.InterfaceUtilisateur;

public class sampleBallNextImageEvent implements ActionListener{
	
	InterfaceUtilisateur interfaceUtilisateur;

	public sampleBallNextImageEvent(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(interfaceUtilisateur.currentFrame < interfaceUtilisateur.endFrame){
			interfaceUtilisateur.currentFrame += 10;
			interfaceUtilisateur.locateFrame();
			if (interfaceUtilisateur.videoProcessor.hasNextImage()) {
				interfaceUtilisateur.sampleBallImage = InterfaceUtilisateur.resizeBufferedImage(
						interfaceUtilisateur.videoProcessor.nextImage(),
						interfaceUtilisateur.videoProcessor.getVideoWidth() / 2,
						interfaceUtilisateur.videoProcessor.getVideoHeight() / 2);
			}
			interfaceUtilisateur.sampleBallSketchpad.setIcon(new ImageIcon(interfaceUtilisateur.sampleBallImage));
			interfaceUtilisateur.sampleBallSketchpad.repaint();
			
		}
		
	}

}
