import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
// import java.util.Iterator;
import java.util.List;

import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetComparator;
import it.uniroma1.lcl.babelnet.BabelSynsetIDRelation;
// import it.uniroma1.lcl.babelnet.iterators.BabelIterator;
import it.uniroma1.lcl.babelnet.iterators.BabelSynsetIterator;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import it.uniroma1.lcl.jlt.util.Language;

public class BabelNetExtraction {
    static public void main(String[] args) {
        try {
            String word = "parade";
            // getFrequencies(word);
            getRelationsWeight(word);
            Path outputFile = getOutputPath();
            // writeAllSynsetsFrequency(outputFile);
            // getAllSynsets();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Prototyping method to view single synset relationships' frequency */
    public static void getRelationsWeight(String word) throws IOException {
        BabelNet bn = BabelNet.getInstance();
        System.out.println("SYNSETS WITH English word: \"" + word + "\"\n");
        List<BabelSynset> synsets = bn.getSynsets(word, Language.EN, BabelPOS.NOUN);
        Collections.sort(synsets, new BabelSynsetComparator(word));
        for (BabelSynset synset : synsets) {
            for (BabelSynsetIDRelation relation : synset.getEdges())
                System.out.println(synset.getId()
                                   + "\t" + relation.getNormalizedWeight()
                                   + "\t" + relation.getWeight()
                                   + "\t" + relation.getTarget()
                                   + "\t" + relation.getBabelSynsetIDTarget()
                                   + "\t" + relation.getLanguage()
                                   + "\t" + relation.toString());
            System.out.println("-----");
        }
    }

    /** Prototyping method to view single word synsets' frequency */
    public static void getFrequencies(String word) throws IOException {
        BabelNet bn = BabelNet.getInstance();
        System.out.println("SYNSETS WITH English word: \"" + word + "\"\n");
        List<BabelSynset> synsets = bn.getSynsets(word, Language.EN, BabelPOS.NOUN);
        Collections.sort(synsets, new BabelSynsetComparator(word));
        for (BabelSynset synset : synsets) {
            for (BabelSense sense : synset.getSenses(Language.EN))
                System.out.println(synset.getId()
                                   + "\t" + sense.getFrequency().toString()
                                   + "\t" + sense.getWordNetOffset()
                                   + "\t" + sense.toString());
            System.out.println("-----");
        }
    }

    /** Method of interest to write all BN synsets' frequency */
    public static void writeAllSynsetsFrequency(Path outputPath)
            throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter writer = new PrintWriter(outputPath.toString(), "UTF-8")) {
            BabelNet bn = BabelNet.getInstance();
            BabelSynsetIterator bnIter = bn.getSynsetIterator();
            while (bnIter.hasNext()) {
                BabelSynset synset = bnIter.next();
                for (BabelSense sense : synset.getSenses(Language.EN)) {
                    writer.println(synset.getId()
                                   + "\t" + sense.getFrequency().toString()
                                   + "\t" + sense.getWordNetOffset()
                                   + "\t" + sense.toString());
                }
            }
        }
    }

    /** Test method */
    public static void getAllSynsets() {
        BabelNet bn = BabelNet.getInstance();
        BabelSynsetIterator bnIter = bn.getSynsetIterator();
        // BabelIterator<BabelSynset> bnIter = bn.getSynsetIterator();
        // Iterator<BabelSynset> bnIter = bn.getSynsetIterator();
        while (bnIter.hasNext()) {
            System.out.println(bnIter.next().getId());
        }
        // for (BabelSynset synset : bnIter) {
        //     System.out.println(synset.getId());
        // }
    }

    public static Path getOutputPath() {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path outputPath = currentPath.resolve("output");
        Path file = outputPath.resolve("bn_extr.txt");
        /* Create outputPath if does not exist */
        if (Files.notExists(outputPath)) {
            try {
                Files.createDirectories(outputPath);
                System.out.println("Path did not exist, created: " + outputPath.toString());
            } catch (java.io.IOException e) {
                System.out.println("createDirectory failed:" + e);
            }
        }
        return file;
    }
}
