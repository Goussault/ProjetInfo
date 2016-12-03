package TuxPowa.Control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import TuxPowa.View.InformationLabel;
import TuxPowa.View.InterfaceUtilisateur;

public class locateFrameAction implements ActionListener, ChangeListener {

	InterfaceUtilisateur interfaceUtilisateur;

	public locateFrameAction(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String pos = interfaceUtilisateur.FrameLocator.getText();
		if (this.frameLocationAcceptable(pos)) {
			interfaceUtilisateur.currentFrame = Integer.valueOf(pos);
			interfaceUtilisateur.locateFrame();
		} else {
			JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.inputError),
					InformationLabel.error, JOptionPane.ERROR_MESSAGE);
		}
	}

	public void stateChanged(ChangeEvent e) {
		if (interfaceUtilisateur.playingVideo == false) {
			interfaceUtilisateur.currentFrame = interfaceUtilisateur.videoProgressBar.getValue();
			interfaceUtilisateur.locateFrame();
		}
	}

	/**
	 * To test if the value of frame is acceptable.
	 * 
	 * @param pos
	 *            value of frame
	 * 
	 * @return true if it's acceptable
	 * 
	 * @author ZHANG Heng, YAN Yutong
	 * 
	 * @version 1.0
	 * 
	 * @since 1.0
	 */
	boolean frameLocationAcceptable(String pos) {
		try {
			int i = Integer.valueOf(interfaceUtilisateur.FrameLocator.getText());
			if ((i >= interfaceUtilisateur.startFrame) && (i <= interfaceUtilisateur.endFrame))
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}