public class LexerException extends Exception {

    public LexerException(String message, Integer position) {
        super("Error at position  " + position + ": " + message);
    }
}
