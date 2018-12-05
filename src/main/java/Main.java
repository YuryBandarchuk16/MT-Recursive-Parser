import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.model.Factory.mutGraph;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: <expr>");
            System.exit(0);
        }
        try {
            String expression = args[0];
            tokenizeAndBuild(expression);
            System.out.println("Tokenized, parsed and drawn graph successfully!");
        } catch (LexerException e) {
            System.out.println("Lexing error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error: " + e.getLocalizedMessage());
        } catch (ParserException e) {
            System.out.println("Parsing error: " + e.getMessage());
        }
    }

    private static void tokenizeAndBuild(String expression) throws IOException, LexerException, ParserException {
        Parser parser = new Parser(IOUtils.toInputStream(expression, "UTF-8"));

        MutableGraph graph = mutGraph(expression).setDirected(true);
        graph = parser.parse().getSources(graph);
        Graphviz.fromGraph(graph).height(2000 + expression.length() * 10).render(Format.PNG).toFile(new File("example/ex1.png"));
    }
}
