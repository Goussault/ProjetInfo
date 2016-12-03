package TuxPowa.Control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import TuxPowa.View.InformationLabel;
import TuxPowa.View.InterfaceUtilisateur;

public class playPauseAction implements ActionListener {

	InterfaceUtilisateur interfaceUtilisateur;

	public playPauseAction(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (interfaceUtilisateur.playingVideo) {
			interfaceUtilisateur.changeToPausedState();
		} else {
			if (interfaceUtilisateur.videoProcessor.isOpened()) {
				interfaceUtilisateur.changeToPlayState();
			} else {
				JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow,
						new JLabel(InformationLabel.pleaseOpenFile), InformationLabel.error, JOptionPane.ERROR_MESSAGE);
			}
		}

	}
}