// This class initializes the sentence corpus that is used to run simulations.

import java.util.*;

public class Corpus {

    //**************************************************************************

    public void initialize(Scanner diskScanner, ArrayList<Sentence> text) {

        double charPos = 0; // cumulative character position witin sentence
        int sentenceN = 0; // sentence #
        int wordN = 0; // word #

        EZReader10.NSentences = 0;

        // Initialize classes:
        Sentence s = new Sentence();

        while (diskScanner.hasNext()) {

            // Read in word attributes:
            Word w = new Word();
            w.iv.N = wordN;



            w.iv.freq = diskScanner.nextDouble();
            w.iv.length = diskScanner.nextInt();
            w.iv.cloze = diskScanner.nextDouble();
            w.iv.sp = " " + diskScanner.next();

            //System.out.println(w.iv.sp);



            // Calculate frequency variables:
            w.iv.lnFreq = Math.log(w.iv.freq);
            w.iv.fc = (int)Math.log10(w.iv.freq - 0.5);

            // Calculate letter positions & OVP:
            w.iv.pos0 = charPos;
            w.iv.pos1 = charPos + 1.0;
            w.iv.posN = w.iv.pos1 + w.iv.length;
            w.iv.OVP = w.iv.pos1 + (w.iv.length * 0.5);
            charPos += (w.iv.length + 1.0);

            // Add word to sentence:
            s.add(w);
            wordN++;

            // Identify end of sentence:
            if (w.iv.sp.charAt(w.iv.sp.length() - 1) == '@') {
                EZReader10.NSentences++;
                w.iv.sp = w.iv.sp.substring(0, w.iv.sp.length() - 1);
                s.N = sentenceN;
                s.numberWords = wordN;
                text.add(s);
                s = new Sentence();
                sentenceN++;
                wordN = 0;
                charPos = 0;
            }
        }
    }

    //**************************************************************************

    // Re-initialize values of word DVs:
    public void zeroDVs(ArrayList<Sentence> text) {

        for (int i = 0; i < EZReader10.NSentences; i++) {
            for (int j = 0; j < text.get(i).numberWords; j++) {
                text.get(i).get(j).dv.FFD = 0;
                text.get(i).get(j).dv.GD = 0;
                text.get(i).get(j).dv.NFix = 0;
                text.get(i).get(j).dv.Pr1 = 0;
                text.get(i).get(j).dv.Pr2 = 0;
                text.get(i).get(j).dv.PrF = 0;
                text.get(i).get(j).dv.PrS = 0;
                text.get(i).get(j).dv.SFD = 0;
                text.get(i).get(j).dv.TT = 0;
            }
        }
    }
}

