import java.io.IOException;
import java.util.Collections;
import java.util.List;

import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetComparator;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import it.uniroma1.lcl.jlt.util.Language;


public class SynsetsFrequency
{
    public static void getFrequencies() throws IOException {
        BabelNet bn = BabelNet.getInstance();
        String word = "parade";
        System.out.println("SYNSETS WITH English word: \"" + word + "\"\n");
        List<BabelSynset> synsets = bn.getSynsets(word, Language.EN, BabelPOS.NOUN);
        Collections.sort(synsets, new BabelSynsetComparator(word));
        for (BabelSynset synset : synsets) {
            for (BabelSense sense : synset.getSenses(Language.EN))
                System.out.println(synset.getId() + "\t"
                                 + sense.toString() + "\t"
                                 + sense.getFrequency().toString());
            System.out.println("}\n  -----");
        }
    }

	static public void main(String[] args) {
		try {
			getFrequencies();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
