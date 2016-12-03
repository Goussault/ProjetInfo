package TuxPowa.Model;

import java.io.Serializable;

class MatchEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * <p>
	 * - not initialized: 0
	 * </p>
	 * <p>
	 * - left: 1
	 * </p>
	 * <p>
	 * - right: 2
	 * </p>
	 */
	String teamName;
	EventsPossible event;

	int frame;

	public MatchEvent(String teamName0, String teamName1, EventsPossible event, int frame) {
		super();
		this.event = event;
		this.frame = frame;
		switch (event) {

		case left_but:
		case left_gamelle:
		case left_sortie_de_terrain: {
			this.teamName = teamName0;
			break;
		}

		case right_but:
		case right_gamelle:
		case right_sortie_de_terrain: {
			this.teamName = teamName1;
			break;
		}
		default: {
			this.teamName = null;
			break;
		}
		}
	}

	@Override
	public String toString() {
		switch (event) {
		case not_initialized:
			return "error:evenement pas initialise";
		case left_but:
		case right_but:
			return new StringBuilder().append("frame: " + String.valueOf(frame)).append(" ").append(teamName)
					.append("  a marque un but!").toString();
		case left_gamelle:
		case right_gamelle:
			return new StringBuilder().append("frame: " + String.valueOf(frame)).append(" ").append(teamName)
					.append("  a marque un gamelle!").toString();
		case left_sortie_de_terrain:
		case right_sortie_de_terrain:
			return new StringBuilder().append("frame: " + String.valueOf(frame)).append(" ").append(teamName)
					.append(teamName).append("  a causer une sortie de terrain!").toString();
		default:
			break;
		}

		return null;
	}

	public String getTeamName() {
		return teamName;
	}

	public EventsPossible getEvent() {
		return event;
	}

}