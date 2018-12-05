import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private int currentChar;
    private int currentPosition;
    private InputStream inputStream;

    private StringBuilder builder;

    public Lexer(InputStream inputStream) throws LexerException {
        this.inputStream = inputStream;
        this.currentPosition = 0;
        this.builder = new StringBuilder();
        readNextCharacter();
    }

    private void readNextCharacter() throws LexerException {
        currentPosition += 1;
        try {
            currentChar = inputStream.read();
        } catch (IOException e) {
            throw new LexerException(e.getMessage(), currentPosition);
        }
    }

    private void flushBuffer(List<Token> result) {
        if (builder.length() > 0) {
            result.add(new Token(TokenType.NUMBER, builder.toString()));
            builder = new StringBuilder();
        }
    }

    public List<Token> splitIntoTokens() throws LexerException {
        List<Token> result = new ArrayList<Token>();

        while (true) {
            while (isBlank(currentChar)) {
                readNextCharacter();
            }

            if (currentChar == -1) {
                flushBuffer(result);
                result.add(new Token(TokenType.END, ""));
                break;
            }

            switch ((char)currentChar) {
                case '+':
                    readNextCharacter();
                    flushBuffer(result);
                    result.add(new Token(TokenType.PLUS, "+"));
                    break;
                case '-':
                    readNextCharacter();
                    flushBuffer(result);
                    result.add(new Token(TokenType.MINUS, "-"));
                    break;
                case '*':
                    readNextCharacter();
                    flushBuffer(result);
                    result.add(new Token(TokenType.MULT, "*"));
                    break;
                case '(':
                    readNextCharacter();
                    flushBuffer(result);
                    result.add(new Token(TokenType.LEFT_PAREN, "("));
                    break;
                case ')':
                    readNextCharacter();
                    flushBuffer(result);
                    result.add(new Token(TokenType.RIGHT_PAREN, ")"));
                    break;
                default:
                    if (currentChar >= '0' && currentChar <= '9') {
                        builder.append((char)currentChar);
                    } else {
                        throw new LexerException("Invalid character: \"" + ((char) (currentChar)) + "\"", currentPosition);
                    }
                    readNextCharacter();
            }
        }

        return result;
    }

    private boolean isBlank(int c) {
        return Character.isWhitespace(c) || c == '\t' || c == '\n' || c == '\r';
    }

}
