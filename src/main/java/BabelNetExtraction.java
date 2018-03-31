import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.babelscape.util.UniversalPOS;

import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetComparator;
import it.uniroma1.lcl.babelnet.BabelSynsetRelation;
import it.uniroma1.lcl.jlt.util.Language;


public class BabelNetExtraction {
    static public void main(String[] args) {
        try {
            String word = "parade";
            Path outputFile = getOutputPath();

            writeAllSynsetRelations(outputFile);
            // getAllSynsetRelations(word);

            // writeAllSynsetCorrespondences(outputFile);
            // getSynsetCorrespondence(word);

            // writeAllSynsetsFrequency(outputFile);
            // getFrequencies(word);

            // getAllSynsets();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Method of interest to write all relationships' frequency from a single synset */
    public static void writeAllSynsetRelations(Path outputPath)
            throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter writer = new PrintWriter(outputPath.toString(), "UTF-8")) {
            BabelNet bn = BabelNet.getInstance();
            Iterator<BabelSynset> bnIter = bn.iterator();
            while (bnIter.hasNext()) {
                BabelSynset synset = bnIter.next();
                for (BabelSynsetRelation relation : synset.getOutgoingEdges())
                    writer.println(synset.getID()
                                   + "\t" + synset.getLanguages()
                                   + "\t" + relation.toString()
                                   // + "\t" + relation.getWeight()
                                   // + "\t" + relation.getNormalizedWeight()
                                   + "\t" + relation.getPointer());
            }
        }
    }

    /** Prototyping method to view all relationships' frequency from a single synset */
    public static void getAllSynsetRelations(String word) throws IOException {
        BabelNet bn = BabelNet.getInstance();
        System.out.println("SYNSETS WITH English word: \"" + word + "\"\n");
        List<BabelSynset> synsets = bn.getSynsets(word, Language.EN, UniversalPOS.NOUN);
        Collections.sort(synsets, new BabelSynsetComparator(word));
        for (BabelSynset synset : synsets) {
            for (BabelSynsetRelation relation : synset.getOutgoingEdges())
                System.out.println(synset.getID().toString()
                                   + "\t" + synset.getLanguages()
                                   // + "\t" + relation.getTarget()
                                   // + "\t" + relation.getLanguage()
                                   // + "\t" + relation.getWeight()
                                   // + "\t" + relation.getNormalizedWeight()
                                   + "\t" + relation.toString()
                                   + "\t" + relation.getPointer());
            System.out.println("-----");
        }
    }

    /** Method of interest to write the correspondences between BN and WN synsets */
    public static void writeAllSynsetCorrespondences(Path outputPath)
            throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter writer = new PrintWriter(outputPath.toString(), "UTF-8")) {
            BabelNet bn = BabelNet.getInstance();
            Iterator<BabelSynset> bnIter = bn.iterator();
            while (bnIter.hasNext()) {
                BabelSynset synset = bnIter.next();
                if (!synset.getWordNetOffsets().isEmpty())
                    writer.println(synset.getID()
                                   + "\t" + synset.getLanguages()
                                   + "\t" + synset.getWordNetOffsets()
                                   + "\t" + synset.toString());
            }
        }
    }

    /** Prototyping method to view single word synsets' frequency */
    public static void getSynsetCorrespondence(String word) throws IOException {
        BabelNet bn = BabelNet.getInstance();
        System.out.println("SYNSETS WITH English word: \"" + word + "\"\n");
        List<BabelSynset> synsets = bn.getSynsets(word, Language.EN, UniversalPOS.NOUN);
        Collections.sort(synsets, new BabelSynsetComparator(word));
        for (BabelSynset synset : synsets)
            if (!synset.getWordNetOffsets().isEmpty())
                System.out.println(synset.getID()
                                   + "\t" + synset.getLanguages()
                                   + "\t" + synset.getWordNetOffsets()
                                   + "\t" + synset.toString());
    }

    /** Prototyping method to view single word synsets' frequency */
    public static void getFrequencies(String word) throws IOException {
        BabelNet bn = BabelNet.getInstance();
        System.out.println("SYNSETS WITH English word: \"" + word + "\"\n");
        List<BabelSynset> synsets = bn.getSynsets(word, Language.EN, UniversalPOS.NOUN);
        Collections.sort(synsets, new BabelSynsetComparator(word));
        for (BabelSynset synset : synsets) {
            for (BabelSense sense : synset.getSenses(Language.EN))
                System.out.println(synset.getID()
                                   + "\t" + sense.getFrequency().toString()
                                   + "\t" + sense.toString());
            System.out.println("-----");
        }
    }

    /** Method of interest to write all BN synsets' frequency */
    public static void writeAllSynsetsFrequency(Path outputPath)
            throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter writer = new PrintWriter(outputPath.toString(), "UTF-8")) {
            BabelNet bn = BabelNet.getInstance();
            Iterator<BabelSynset> bnIter = bn.iterator();
            while (bnIter.hasNext()) {
                BabelSynset synset = bnIter.next();
                for (BabelSense sense : synset.getSenses(Language.EN)) {
                    writer.println(synset.getID()
                                   + "\t" + sense.getFrequency().toString()
                                   + "\t" + sense.toString());
                }
            }
        }
    }

    /** Test method */
    public static void getAllSynsets() {
        BabelNet bn = BabelNet.getInstance();
        Iterator<BabelSynset> bnIter = bn.iterator();
        // BabelIterator<BabelSynset> bnIter = bn.getSynsetIterator();
        // Iterator<BabelSynset> bnIter = bn.getSynsetIterator();
        while (bnIter.hasNext()) {
            System.out.println(bnIter.next().getID());
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
