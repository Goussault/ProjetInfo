package TuxPowa.Control;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import TuxPowa.View.InformationLabel;
import TuxPowa.View.InterfaceUtilisateur;

public class openFileAction implements ActionListener {

	InterfaceUtilisateur interfaceUtilisateur;

	public openFileAction(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		FileDialog openFileDialog = new FileDialog(interfaceUtilisateur.mainWindow, InformationLabel.openFile,
				FileDialog.LOAD);
		openFileDialog.setVisible(true);
		if (this.fileChosenAcceptable(openFileDialog.getFile())) {
			interfaceUtilisateur.videoProcessor.close();
			interfaceUtilisateur.currentVideoName = openFileDialog.getDirectory() + openFileDialog.getFile();
			interfaceUtilisateur.videoProcessor.open(interfaceUtilisateur.currentVideoName);
			interfaceUtilisateur.currentFrame = 0;
			interfaceUtilisateur.locateFrame();
			interfaceUtilisateur.endFrame = interfaceUtilisateur.videoProcessor.getVideoLengthFrame();
			interfaceUtilisateur.updateRateMillisecond = 1000 / interfaceUtilisateur.videoProcessor.getVideoFrameRate();
			interfaceUtilisateur.playingControlTimer.setDelay(interfaceUtilisateur.updateRateMillisecond);
			interfaceUtilisateur.videoProgressBar.setMaximum(interfaceUtilisateur.endFrame);
			interfaceUtilisateur.videoProgressBar.setMajorTickSpacing(interfaceUtilisateur.endFrame / 10);
			interfaceUtilisateur.videoProgressBar.setMinorTickSpacing(interfaceUtilisateur.endFrame / 20);
			if (interfaceUtilisateur.videoProcessor.isTreated()) {
				String[] events = interfaceUtilisateur.videoProcessor.getEvents();
				for (String event : events) {
					interfaceUtilisateur.eventsListModel.addElement(event);
				}
				Integer[] score = interfaceUtilisateur.videoProcessor.getScore();
				interfaceUtilisateur.scoreShower.setText(score[0] + ":" + score[1]);
				interfaceUtilisateur.setFrameIndicatorLabel();
			} else {
				String teamName0 = JOptionPane.showInputDialog(interfaceUtilisateur.mainWindow,
						InformationLabel.nameTeam0);
				String teamName1 = JOptionPane.showInputDialog(interfaceUtilisateur.mainWindow,
						InformationLabel.nameTeam1);
				interfaceUtilisateur.videoProcessor.name(teamName0, teamName1);
				interfaceUtilisateur.setFrameIndicatorLabel();
			}
		} else {
			JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.pleaseOpenFile),
					InformationLabel.error, JOptionPane.ERROR_MESSAGE);
		}

	}

	boolean fileChosenAcceptable(String fileName) {
		if (fileName == null) {
			return false;
		}
		String[] extensions = { ".MP4", ".AVI", ".MKV", ".WMV", ".mp4", ".avi", ".mkv", ".wmv" };
		for (String extension : extensions) {
			if (fileName.endsWith(extension)) {
				return true;
			}
		}
		return false;
	}

}