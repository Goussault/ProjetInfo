package TuxPowa.Control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.JOptionPane;

import TuxPowa.View.InformationLabel;
import TuxPowa.View.InterfaceUtilisateur;

public class exitApplicationAction extends WindowAdapter implements ActionListener {

	InterfaceUtilisateur interfaceUtilisateur;

	public exitApplicationAction(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String message = InformationLabel.quitCheck;
		String title = InformationLabel.quit;
		int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

}