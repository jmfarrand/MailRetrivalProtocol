package server;

/**
 * The State enum class holds enum values to track the user state
 * in the smtp server
 * 
 * <br>States:<br>
 * NC: New Connection<br>
 * AU: Authentication<br>
 * CE: Connection established
 * 
 * @author 100385188
 * @version 1.0
 * @since 2017-12-07
 *
 */

public enum State {
	NC, AU, CE
}
