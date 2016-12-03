package TuxPowa.Control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import TuxPowa.View.InformationLabel;
import TuxPowa.View.InterfaceUtilisateur;

public class nameTeamAction implements ActionListener {

	InterfaceUtilisateur interfaceUtilisateur;

	public nameTeamAction(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!interfaceUtilisateur.videoProcessor.isOpened()) {
			JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.pleaseOpenFile),
					InformationLabel.error, JOptionPane.ERROR_MESSAGE);
		} else if (interfaceUtilisateur.videoProcessor.isNamed()) {
			if (JOptionPane.showConfirmDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.replaceName),
					InformationLabel.info, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				interfaceUtilisateur.videoProcessor.cancelName();
				nameTeam();
				interfaceUtilisateur.setFrameIndicatorLabel();
			}
		} else {
			nameTeam();
		}
	}

	public void nameTeam() {
		String teamName0 = JOptionPane.showInputDialog(interfaceUtilisateur.mainWindow, InformationLabel.nameTeam0);
		String teamName1 = JOptionPane.showInputDialog(interfaceUtilisateur.mainWindow, InformationLabel.nameTeam1);
		interfaceUtilisateur.videoProcessor.name(teamName0, teamName1);
	}

}