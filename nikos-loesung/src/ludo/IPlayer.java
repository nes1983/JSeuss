package ludo;

/**
 * Player in a board game. Knows his stones and can move his stones a number of steps. 
 * @author Niko Schwarz
 *
 */

public interface IPlayer {

	/**
	 * Answers whether this player has won.
	 * @return true if this player has won; false otherwise.
	 */
	public abstract boolean isWinner();

	/**
	 * Moves one of the player's stones <code>steps</steps> fields forward, according to the game rules.
	 * Typically, <code>steps</steps> is the result of a die throw.
	 * @param steps the number of steps to move the stones.
	 */
	public abstract void move(int steps);

	/**
	 * Answers the stones of this player.
	 * @return the stones of this player.
	 */
	public abstract Iterable<Stone> stones();

	/**
	 * Answers a rendering of this player.
	 * @return a rendering of this player.
	 */
	public abstract String toString();
	
	/**
	 * Returns any one of this player's stones.
	 * @return any one of this player's stones.
	 */
	public abstract Stone anyStone();

}