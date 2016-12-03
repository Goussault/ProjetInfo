package TuxPowa.Control;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import TuxPowa.View.InformationLabel;
import TuxPowa.View.InterfaceUtilisateur;

public class makeFileActionJson implements ActionListener {

	InterfaceUtilisateur interfaceUtilisateur;

	public makeFileActionJson(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (interfaceUtilisateur.videoProcessor.isOpened()) {
			if (interfaceUtilisateur.videoProcessor.isTreated()) {
				FileDialog saveFileWindow = new FileDialog(interfaceUtilisateur.mainWindow, InformationLabel.saveFile,
						FileDialog.SAVE);
				saveFileWindow.setVisible(true);
				if (interfaceUtilisateur.videoProcessor.generateMatchFileJson(saveFileWindow.getDirectory(),
						saveFileWindow.getFile())) {
					JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow,
							new JLabel(InformationLabel.makeFileSuccess), InformationLabel.info,
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow,
							new JLabel(InformationLabel.makeFileFail), InformationLabel.error,
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.notTreated),
						InformationLabel.error, JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.pleaseOpenFile),
					InformationLabel.error, JOptionPane.ERROR_MESSAGE);
		}
	}
}