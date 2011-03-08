package ludo;

/**
 * Factory that creates the players of a board game.
 * @author Niko Schwarz, 2010 
 *
 */
public interface IPlayerFactory {
	/**
	 * Creates a player on a board game.
	 * @param startField the start field of this player.
	 * @return the created player.
	 */
	public IPlayer create(Square startField);
}
