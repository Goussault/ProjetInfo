package TuxPowa.Control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import TuxPowa.View.InterfaceUtilisateur;

public class closeFileAction implements ActionListener {

	InterfaceUtilisateur interfaceUtilisateur;

	public closeFileAction(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (interfaceUtilisateur.videoProcessor.isOpened()) {
			interfaceUtilisateur.initializationVideoPanel();
			interfaceUtilisateur.setFrameIndicatorLabel();
		}
	}
}