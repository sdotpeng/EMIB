// This class contains various methods that are used to display (in the output file) various corpus and eye-movement data.

import java.io.PrintStream;
import java.util.*;

public class Display {

    //**************************************************************************

    // Display text corpus:
    public void corpus(PrintStream diskWriter, ArrayList<Sentence> text) {

        for (int i = 0; i < EZReader10.NSentences; i++) {
            diskWriter.printf(" Sentence %2d\n", i);
            for (int j = 0; j < text.get(i).numberWords; j++) {
                diskWriter.printf(" %5.0f %4.2f %2.0f %3.0f %4.1f %3.0f" + text.get(i).get(j).iv.sp, text.get(i).get(j).iv.freq, text.get(i).get(j).iv.cloze, text.get(i).get(j).iv.length, 
                        text.get(i).get(j).iv.pos0, text.get(i).get(j).iv.OVP, text.get(i).get(j).iv.posN);
                if (j == text.get(i).target) diskWriter.printf(" *"); // target
                diskWriter.println();
            }
        }
    }

    //**************************************************************************

    // Display trace of fixation durations & locations:
    public void fixations(PrintStream diskWriter, ArrayList<Sentence> text, int S, ArrayList<Fixation> trace) {

        for (int i = 0; i < trace.size(); i++) {
            diskWriter.printf(" dur: %3.0f pos: %4.1f word: %2d ", trace.get(i).dur, trace.get(i).pos, trace.get(i).word);
            diskWriter.printf(text.get(S).get(trace.get(i).word).iv.sp);
            diskWriter.println();
        }
    }

    //**************************************************************************

    // Display fixation landing-site distributions for individual words:
    public void fixDists(PrintStream diskWriter, ArrayList<Sentence> text) {

        diskWriter.println();
        diskWriter.printf(" First-fixation landing-site distributions:\n");
        for (int S = 0; S < EZReader10.NSentences; S++) {
            for (int j = 1; j < text.get(S).numberWords - 1; j++) {
                for (int k = 0; k <= text.get(S).get(j).iv.length; k++) diskWriter.printf(" %3.2f", text.get(S).get(j).dv.FixDist[k]);
                diskWriter.printf(" " + text.get(S).get(j).iv.sp);
                if (j == text.get(S).target) diskWriter.printf(" *"); // target
                diskWriter.println();
            }
        }
    }

    //**************************************************************************

    // Display IOVP distributions for individual words:
    public void IOVPs(PrintStream diskWriter, ArrayList<Sentence> text) {

        diskWriter.println();
        diskWriter.printf(" IOVP distributions:\n");
        for (int S = 0; S < EZReader10.NSentences; S++) {
            for (int j = 1; j < text.get(S).numberWords - 1; j++) {
                for (int k = 0; k <= text.get(S).get(j).iv.length; k++) diskWriter.printf(" %4.0f", text.get(S).get(j).dv.IOVP[k]);
                diskWriter.printf(" " + text.get(S).get(j).iv.sp);
                if (j == text.get(S).target) diskWriter.printf(" *"); // target
                diskWriter.println();
            }
        }
    }

     //**************************************************************************

    // Display refixation-probability distributions for individual words:
    public void refixProbs(PrintStream diskWriter, ArrayList<Sentence> text) {

        diskWriter.println();
        diskWriter.printf(" Refixation probability distributions:\n");
        for (int S = 0; S < EZReader10.NSentences; S++) {
            for (int j = 1; j < text.get(S).numberWords - 1; j++) {
                for (int k = 0; k <= text.get(S).get(j).iv.length; k++) diskWriter.printf(" %3.2f", text.get(S).get(j).dv.RefixProb[k]);
                diskWriter.printf(" " + text.get(S).get(j).iv.sp);
                if (j == text.get(S).target) diskWriter.printf(" *"); // target
                diskWriter.println();
            }
        }
    }

    //**************************************************************************

    // Display mean first-fixation landing-site distributions for words of each given length:
    public void meanFixDists(PrintStream diskWriter, ArrayList<Word> fixDists) {

        diskWriter.println();
        diskWriter.printf(" First-fixation landing-site distributions:\n");
        for (int i = 1; i < EZReader10.maxLength; i++) {
            diskWriter.printf(" %2d-letter: ", i);
            for (int j = 0; j <= i; j++) diskWriter.printf(" %4.2f", fixDists.get(i).dv.FixDist[j]);
            diskWriter.println();
        }
        diskWriter.println();
    }

    //**************************************************************************

    // Display mean IOVP distributions for words of each given length:
    public void meanIOVPs(PrintStream diskWriter, ArrayList<Word> IOVPs) {

        diskWriter.println();
        diskWriter.printf(" IOVP distributions:\n");
        for (int i = 1; i < EZReader10.maxLength; i++) {
            diskWriter.printf(" %2d-letter: ", i);
            for (int j = 0; j <= i; j++) diskWriter.printf(" %4.0f", IOVPs.get(i).dv.IOVP[j]);
            diskWriter.println();
        }
        diskWriter.println();
    }

    //**************************************************************************

    // Display mean refixation landing-site distributions for words of each given length:
    public void meanRefixProbs(PrintStream diskWriter, ArrayList<Word> fixDists) {

        diskWriter.println();
        diskWriter.printf(" Refixation probability distributions:\n");
        for (int i = 1; i < EZReader10.maxLength; i++) {
            diskWriter.printf(" %2d-letter: ", i);
            for (int j = 0; j <= i; j++) diskWriter.printf(" %4.2f", fixDists.get(i).dv.RefixProb[j]);
            diskWriter.println();
        }
        diskWriter.println();
    }


    //**************************************************************************

    // Display interal states of model:
    public void state(PrintStream diskWriter, int sentence, int N, Fixation f, double processingRate, ArrayList<Process> active, boolean[] IF, ArrayList<Sentence> text, String state) {

        diskWriter.printf(" S %2d", sentence); // sentence #
        diskWriter.printf(" N %2d", N); // word # being attended
        diskWriter.printf(" fix# %2d", f.number); // fixation #
        diskWriter.printf(" word %2d", f.word); // word # being fixated
        diskWriter.printf(" pos %4.1f", f.pos); // fixation position
        diskWriter.printf(" dur %4.0f", f.dur); // fixation duration
        diskWriter.printf(" pr %5.2f", processingRate); // processing rate
        diskWriter.printf(" - " + state); // model state

        // Identify & display all active processes:
        for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("V")) diskWriter.printf(" V %1.0f [%d]", active.get(i).dur, active.get(i).word);
        for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("L1")) diskWriter.printf(" L1 %1.0f [%d]", active.get(i).dur, active.get(i).word);
        for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("L2")) diskWriter.printf(" L2 %1.0f [%d]", active.get(i).dur, active.get(i).word);
        for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("I")) diskWriter.printf(" I %1.0f [%d]", active.get(i).dur, active.get(i).word);
        for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("A")) diskWriter.printf(" A %1.0f [%d]", active.get(i).dur, active.get(i).word);
        for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("M1")) diskWriter.printf(" M1 %1.0f [%1d %2.1f]", active.get(i).dur, active.get(i).word, active.get(i).length);
        for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("M2")) diskWriter.printf(" M2 %1.0f [%2.1f]", active.get(i).dur, active.get(i).length);
        for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("S")) diskWriter.printf(" S %1.0f", active.get(i).dur);
        diskWriter.printf(" IF:");
        for (int i = 0; i < text.get(sentence).numberWords; i++) if (IF[i] == true) diskWriter.printf(" %d", i);
        diskWriter.println();
    }

    //**************************************************************************

    // Display word-based means:
    public void wordBasedMeans(PrintStream diskWriter, ArrayList<Sentence> text) {

        diskWriter.println();
        diskWriter.printf(" Word-based means:\n");
        for (int i = 0; i < EZReader10.NSentences; i++) {
            for (int j = 1; j < text.get(i).numberWords - 1; j++) {
                diskWriter.printf(" SFD %3.0f ", text.get(i).get(j).dv.SFD);
                diskWriter.printf(" FFD %3.0f ", text.get(i).get(j).dv.FFD);
                diskWriter.printf(" GD %3.0f ", text.get(i).get(j).dv.GD);
                diskWriter.printf(" TT %3.0f ", text.get(i).get(j).dv.TT);
                diskWriter.printf(" PrF %3.2f", text.get(i).get(j).dv.PrF);
                diskWriter.printf(" Pr1 %3.2f", text.get(i).get(j).dv.Pr1);
                diskWriter.printf(" Pr2 %3.2f", text.get(i).get(j).dv.Pr2);
                diskWriter.printf(" PrS %3.2f" + text.get(i).get(j).iv.sp, text.get(i).get(j).dv.PrS);
                if (j == text.get(i).target) diskWriter.printf(" *"); // target
                diskWriter.println();
            }
        }

        // Calculate means word-based measures for target words:
        double targetFFD = 0;
        double targetGD = 0;
        double targetPrF = 0;
        double targetPrS = 0;
        double targetSFD = 0;
        double targetTT = 0;

        for (int i = 0; i < EZReader10.NSentences; i++) {
            for (int j = 0; j < text.get(i).numberWords; j++) {
                if (j == text.get(i).target) {
                    targetFFD += text.get(i).get(j).dv.FFD;
                    targetGD += text.get(i).get(j).dv.GD;
                    targetPrF += text.get(i).get(j).dv.PrF;
                    targetPrS += text.get(i).get(j).dv.PrS;
                    targetSFD += text.get(i).get(j).dv.SFD;
                    targetTT += text.get(i).get(j).dv.TT;
                }
            }
        }

        targetFFD /= EZReader10.NSentences;
        targetGD /= EZReader10.NSentences;
        targetPrF /= EZReader10.NSentences;
        targetPrS /= EZReader10.NSentences;
        targetSFD /= EZReader10.NSentences;
        targetTT /= EZReader10.NSentences;

        // System.out.printf("\n FFD %3.0f; SFD %3.0f; GD %3.0f; TT %3.0f; PrF %4.2f; PrS %4.2f\n\n", targetFFD, targetSFD, targetGD, targetTT, targetPrF, targetPrS);
    }
}

