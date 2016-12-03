package TuxPowa.Control;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import TuxPowa.View.InformationLabel;
import TuxPowa.View.InterfaceUtilisateur;

public class makeFileActionMd implements ActionListener {

	InterfaceUtilisateur interfaceUtilisateur;

	public makeFileActionMd(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!interfaceUtilisateur.videoProcessor.isOpened()) {
			JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.pleaseOpenFile),
					InformationLabel.error, JOptionPane.ERROR_MESSAGE);
		} else if (!interfaceUtilisateur.videoProcessor.isTreated()) {
			JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.notTreated),
					InformationLabel.error, JOptionPane.ERROR_MESSAGE);
		} else {
			FileDialog saveFileWindow = new FileDialog(interfaceUtilisateur.mainWindow, InformationLabel.saveFile,
					FileDialog.SAVE);
			saveFileWindow.setVisible(true);
			if ((saveFileWindow.getDirectory() != null) && (saveFileWindow.getFile() != null)
					&& (interfaceUtilisateur.videoProcessor.generateMatchFileMd(saveFileWindow.getDirectory(),
							saveFileWindow.getFile()))) {
				JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow,
						new JLabel(InformationLabel.makeFileSuccess), InformationLabel.info,
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow,
						new JLabel(InformationLabel.makeFileFail), InformationLabel.error, JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}