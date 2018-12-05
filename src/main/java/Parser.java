import java.io.InputStream;
import java.util.List;

public class Parser {

    private List<Token> tokens;

    private Integer currentTokenIndex;

    public Parser(InputStream inputStream) throws LexerException {
        Lexer lexer = new Lexer(inputStream);
        tokens = lexer.splitIntoTokens();
        currentTokenIndex = 0;
    }

    public Tree parse() throws ParserException {
        Tree result = A();

        if (currentToken().getType() != TokenType.END) {
            throw new ParserException("Invalid sequence of tokens.\n" +
                "Number of successfully parsed tokens: " + currentTokenIndex + ".\n" +
                "Tokens list: " + tokens
            );
        }

        return result;
    }

    private Token currentToken() {
        return tokens.get(currentTokenIndex);
    }

    private void moveToNextToken() {
        currentTokenIndex++;
    }

    private Tree parseInvalidToken() throws ParserException {
        return parseInvalidToken(
            "Token ID: " + currentTokenIndex + "\nTokens list: " + tokens
        );
    }

    private Tree parseInvalidToken(String additionalMessage) throws ParserException {
        throw new ParserException("Invalid token: " + currentToken() + ". " + additionalMessage);
    }

    private Tree A() throws ParserException {
        switch (currentToken().getType()) {
            case LEFT_PAREN:
            case MINUS:
            case NUMBER:
                Tree left = M();
                Token token = currentToken();
                Tree right = APrime();
                if (token.getType() == TokenType.PLUS ||
                    token.getType() == TokenType.MINUS) {
                    return new Tree("A", left, new Tree(token.getValue()), right);
                } else {
                    return new Tree("A", left, right);
                }
            default:
                return parseInvalidToken();
        }
    }

    private Tree APrime() throws ParserException {
        switch (currentToken().getType()) {
            case PLUS:
            case MINUS:
                moveToNextToken();
                Tree left = M();
                Token token = currentToken();
                Tree right = APrime();
                if (token.getType() == TokenType.PLUS ||
                token.getType() == TokenType.MINUS) {
                    return new Tree("A'", left, new Tree(token.getValue()), right);
                } else {
                    return new Tree("A'", left, right);
                }
            case END:
            case RIGHT_PAREN:
                // eps
                return new Tree("A'");
            default:
                return parseInvalidToken();
        }
    }

    private Tree E() throws ParserException {
        switch (currentToken().getType()) {
            case LEFT_PAREN:
                moveToNextToken();
                Tree innerExpression = A();
                if (currentToken().getType() != TokenType.RIGHT_PAREN) {
                    parseInvalidToken("Expected )");
                }
                moveToNextToken();
                return new Tree(
                    "E",
                    new Tree("("), innerExpression, new Tree(")")
                );
            case NUMBER:
                Tree result = new Tree("E", new Tree(currentToken().getValue()));
                moveToNextToken();
                return result;
            default:
                return parseInvalidToken();
        }
    }

    private Tree M() throws ParserException {
        switch (currentToken().getType()) {
            case LEFT_PAREN:
            case MINUS:
            case NUMBER:
                Tree left = Q();
                Token token = currentToken();
                Tree right = MPrime();
                if (token.getType() == TokenType.MULT) {
                    return new Tree("M", left, new Tree(token.getValue()), right);
                } else {
                    return new Tree("M", left, right);
                }
            default:
                return parseInvalidToken();
        }
    }

    private Tree MPrime() throws ParserException {
        switch (currentToken().getType()) {
            case MULT:
                moveToNextToken();
                Tree left = Q();
                Token token = currentToken();
                Tree right = MPrime();
                if (token.getType() == TokenType.MULT) {
                    return new Tree("M'", left, new Tree(token.getValue()), right);
                } else {
                    return new Tree("M'", left, right);
                }
            case END:
            case RIGHT_PAREN:
            case PLUS:
            case MINUS:
                // eps
                return new Tree("M'");
            default:
                return parseInvalidToken();
        }
    }

    private Tree Q() throws ParserException {
        switch (currentToken().getType()) {
            case MINUS:
                moveToNextToken();
                return new Tree("Q", new Tree("-"), E());
            case LEFT_PAREN:
            case NUMBER:
                return new Tree("Q", E());
            default:
                return parseInvalidToken();
        }
    }
}
