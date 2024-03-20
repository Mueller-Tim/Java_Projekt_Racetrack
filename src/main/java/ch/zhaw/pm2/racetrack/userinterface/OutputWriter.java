package ch.zhaw.pm2.racetrack.userinterface;

import org.beryx.textio.TextTerminal;

/**
 * The OutputWriter class is responsible for one way communication with the user
 *
 */
public class OutputWriter {

	/**
	 * Prints the provided message to the specified terminal
	 *
	 * @param terminal
	 * @param message
	 */
	public void print(TextTerminal<?> terminal, String message) {
		terminal.println(message);
	}

	/**
	 * Inserts the provided parameters at the predefined points in the message and
	 * prints the message to the specified terminal
	 *
	 * @param terminal
	 * @param message
	 * @param params
	 */
	public void print(TextTerminal<?> terminal, String message, String[] params) {
		terminal.println(String.format(message, params));
	}

}
