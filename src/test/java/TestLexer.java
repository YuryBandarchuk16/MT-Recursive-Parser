import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestLexer {

    @Test
    public void testSimple() throws IOException, LexerException {
        String s = "    1 ";
        InputStream inputStream = IOUtils.toInputStream(s, "UTF-8");
        Lexer lexer = new Lexer(inputStream);
        List<Token> tokens = lexer.splitIntoTokens();
        Assert.assertEquals(tokens.size(), 2);
        Assert.assertEquals(tokens.get(0).getType(), TokenType.NUMBER);
        Assert.assertEquals(tokens.get(0).getValue(), "1");
        Assert.assertEquals(tokens.get(1).getType(), TokenType.END);
    }

    @Test
    public void testExpressionWithParens() throws IOException, LexerException {
        String s = "3 *    (11 + 2) - 4 * 6";
        InputStream inputStream = IOUtils.toInputStream(s, "UTF-8");
        Lexer lexer = new Lexer(inputStream);
        List<Token> tokens = lexer.splitIntoTokens();
        Assert.assertEquals(tokens.get(3).getType(), TokenType.NUMBER);
        Assert.assertEquals(tokens.get(3).getValue(), "11");
    }
}
