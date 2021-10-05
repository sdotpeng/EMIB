// This class contains methods to calculate various statistics for the eye-movement measures.

import java.util.*;

public class Stats {

    //**************************************************************************

    // Calculate means for 5 frequency classes of words:
    public void calcClassMeans(ArrayList<Sentence> text, ArrayList<Word> classMeans) {

        // Counter to tally # words per frequency class:
        int[] N = new int[] { 0, 0, 0, 0, 0 };

        // Sum DVs for each class:
        for (int i = 0; i < EZReader10.NSentences; i++) {
            for (int j = 1; j < text.get(i).numberWords - 1; j++) {


                // System.out.println("i: " + i + "   j: " + j);

                int freqClass = text.get(i).get(j).iv.fc;
                classMeans.get(freqClass).dv.SFD += text.get(i).get(j).dv.SFD;
                classMeans.get(freqClass).dv.FFD += text.get(i).get(j).dv.FFD;
                classMeans.get(freqClass).dv.GD += text.get(i).get(j).dv.GD;
                classMeans.get(freqClass).dv.Pr1 += text.get(i).get(j).dv.Pr1;
                classMeans.get(freqClass).dv.Pr2 += text.get(i).get(j).dv.Pr2;
                classMeans.get(freqClass).dv.PrS += text.get(i).get(j).dv.PrS;
                N[freqClass]++;
            }
        }

        // Calculate class means:
        for (int i = 0; i < 5; i++) {
            classMeans.get(i).dv.SFD /= N[i];
            classMeans.get(i).dv.FFD /= N[i];
            classMeans.get(i).dv.GD /= N[i];
            classMeans.get(i).dv.Pr1 /= N[i];
            classMeans.get(i).dv.Pr2 /= N[i];
            classMeans.get(i).dv.PrS /= N[i];
        }
    }

    //**************************************************************************

    // Calculate means first-fixation landing-site distributions:
    public void calcFixDists(ArrayList<Sentence> text, ArrayList<Word> fixDists) {

        // Counter to tally # of words of each length:
        int N[] = new int[EZReader10.maxLength];
        for (int i = 0; i < EZReader10.maxLength; i++) N[i] = 0;

        // Calculate distributions:
        for (int length = 0; length < EZReader10.maxLength; length++) {

            for (int i = 0; i < EZReader10.NSentences; i++) {
                for (int j = 1; j < text.get(i).numberWords - 1; j++) {
                    if (text.get(i).get(j).iv.length == length) {
                        if (text.get(i).get(j).dv.Pr1 != 0) {
                            for (int k = 0; k < EZReader10.maxLength; k++) fixDists.get(length).dv.FixDist[k] += text.get(i).get(j).dv.FixDist[k];
                            N[length]++;
                        }
                    }
                }
            }
        }

        for (int length = 0; length < EZReader10.maxLength; length++) {
            for (int i = 0; i < EZReader10.maxLength; i++) if (N[length] > 0) fixDists.get(length).dv.FixDist[i] /= N[length];
        }
    }

    //**************************************************************************

    // Calculate mean IOVP distributions:
    public void calcIOVPs(ArrayList<Sentence> text, ArrayList<Word> IOVPs) {

        // Calculate distributions:
        for (int length = 0; length < EZReader10.maxLength; length++) {

            for (int i = 0; i < EZReader10.NSentences; i++) {
                for (int j = 1; j < text.get(i).numberWords - 1; j++) {
                    if (text.get(i).get(j).iv.length == length) {
                        for (int k = 0; k < EZReader10.maxLength; k++) {
                            if (text.get(i).get(j).dv.IOVP[k] > 0) {
                                IOVPs.get(length).dv.IOVP[k] += text.get(i).get(j).dv.IOVP[k];
                                IOVPs.get(length).dv.N[k]++;
                            }
                        }
                    }
                }
            }
        }

        for (int length = 0; length < EZReader10.maxLength; length++) {
            for (int i = 0; i < EZReader10.maxLength; i++) if (IOVPs.get(length).dv.N[i] > 0) IOVPs.get(length).dv.IOVP[i] /= IOVPs.get(length).dv.N[i];
        }
    }

    //**************************************************************************

    // Calculate word-based means for each word in sentence:
    public void calcMeans(ArrayList<Sentence> text, int[] regressionN) {

        // Fixation-duration measures:
        for (int i = 0; i < EZReader10.NSentences; i++) {
            for (int j = 1; j < text.get(i).numberWords - 1; j++) {

                if (text.get(i).get(j).dv.Pr1 > 0) text.get(i).get(j).dv.SFD /= text.get(i).get(j).dv.Pr1;

                if (text.get(i).get(j).dv.PrF > 0) text.get(i).get(j).dv.TT /= text.get(i).get(j).dv.PrF;

                if ((text.get(i).get(j).dv.Pr1 + text.get(i).get(j).dv.Pr2) > 0) {
                    text.get(i).get(j).dv.FFD /= (text.get(i).get(j).dv.Pr1 + text.get(i).get(j).dv.Pr2);
                    text.get(i).get(j).dv.GD /= (text.get(i).get(j).dv.Pr1 + text.get(i).get(j).dv.Pr2);

                    // Distributions:
                    for (int k = 0; k < EZReader10.maxLength; k++) {
                        if (text.get(i).get(j).dv.FixDist[k] > 0) text.get(i).get(j).dv.RefixProb[k] /= text.get(i).get(j).dv.FixDist[k];
                        text.get(i).get(j).dv.FixDist[k] /= (text.get(i).get(j).dv.Pr1 + text.get(i).get(j).dv.Pr2);
                        if (text.get(i).get(j).dv.N[k] > 0) text.get(i).get(j).dv.IOVP[k] /= text.get(i).get(j).dv.N[k];
                    }
                }

                // Fixation-prob. measures:
                text.get(i).get(j).dv.Pr1 /= (EZReader10.NSubjects - regressionN[i]);
                text.get(i).get(j).dv.Pr2 /= (EZReader10.NSubjects - regressionN[i]);
                text.get(i).get(j).dv.PrS /= (EZReader10.NSubjects - regressionN[i]);
                text.get(i).get(j).dv.PrF /= (EZReader10.NSubjects - regressionN[i]);
            }
        }
    }

    //**************************************************************************

    // Calculate means refixation-probability distributions:
    public void calcRefixProbs(ArrayList<Sentence> text, ArrayList<Word> refixProbs) {

        // Counter to tally # of words of each length:
        int N[] = new int[EZReader10.maxLength];
        for (int i = 0; i < EZReader10.maxLength; i++) N[i] = 0;

        // Caclulate distributions:
        for (int length = 0; length < EZReader10.maxLength; length++) {
            for (int i = 0; i < EZReader10.NSentences; i++) {
                for (int j = 1; j < text.get(i).numberWords - 1; j++) {
                    if (text.get(i).get(j).iv.length == length) {
                        for (int k = 0; k < EZReader10.maxLength; k++) refixProbs.get(length).dv.RefixProb[k] += text.get(i).get(j).dv.RefixProb[k];
                        N[length]++;
                    }
                }
            }
        }

        for (int length = 0; length < EZReader10.maxLength; length++) {
            for (int i = 0; i < EZReader10.maxLength; i++) if (N[length] > 0) refixProbs.get(length).dv.RefixProb[i] /= N[length];
        }
    }

    //**************************************************************************

    // Calcuate RMSD for Schilling et al. (1998) word-frequency classes:
    double calcRMSD(ArrayList<Word> classMeans) {

        double result;

        // Initialize observed means and standard deviations (based on Schilling et al., 1998):
        double[] obsMeanFFD = new double[] { 248, 234, 228, 223, 208 };
        double[] obsSDFFD = new double[] { 26.34, 31.65, 31.31, 42.21, 64.75 };
        double[] obsMeanGD = new double[] { 293, 272, 256, 234, 214 };
        double[] obsSDGD = new double[] { 38.76, 44.81, 40.29, 56.61, 71.11 };
        double[] obsMeanPr1 = new double[] { 0.68, 0.70, 0.68, 0.44, 0.32 };
        double[] obsSDPr1 = new double[] { 0.4665, 0.4583, 0.4665, 0.4964, 0.4665 };
        double[] obsMeanPrS = new double[] { 0.10, 0.13, 0.22, 0.55, 0.67 };
        double[] obsSDPrS = new double[] { 0.3000, 0.3363, 0.4142, 0.4975, 0.4702 };

        // Calculate RMSD:
        double RMSD = 0;
        for (int i = 0; i < 5; i++) {
            RMSD += Math.pow(((obsMeanFFD[i] - classMeans.get(i).dv.FFD) / obsSDFFD[i]), 2);
            RMSD += Math.pow(((obsMeanGD[i] - classMeans.get(i).dv.GD) / obsSDGD[i]), 2);
            RMSD += Math.pow(((obsMeanPr1[i] - classMeans.get(i).dv.Pr1) / obsSDPr1[i]), 2);
            RMSD += Math.pow(((obsMeanPrS[i] - classMeans.get(i).dv.PrS) / obsSDPrS[i]), 2);
        }
        RMSD /= 20.0; // Note: = 20 pairs of means
        result = Math.sqrt(RMSD);
        return result;
    }

    //**************************************************************************

    // Update word-based measures for all words in a given sentence:
    public void calcWordDVs(ArrayList<Sentence> text, int S, ArrayList<Fixation> trace) {

        for (int i = 1; i < text.get(S).numberWords - 1; i++) {

            // Initialize word class to hold DVs:
            Word w = new Word();

            // Convert fixation trace into word DVs:
            for (int j = 0; j < trace.size(); j++) {
                if (trace.get(j).word == i) {
                    // Identify possible interword regression:
                    boolean regression = false;
                    for (int k = 0; k <= j; k++) if (trace.get(k).word > i) regression = true;
                    // If saccade was regression, increment NFix and TT:
                    if (regression == true) {
                        w.dv.NFix++;
                        w.dv.TT += trace.get(j).dur;
                    }
                    // If saccade was progressive:
                    else {
                        w.dv.NFix++; // increment # fixations

                        // If fixation is first:
                        if (w.dv.FFD == 0) {
                            w.dv.FFD = trace.get(j).dur; // increment FFD
                            w.dv.Pos1 = (int)(trace.get(j).pos - text.get(S).get(trace.get(j).word).iv.pos0); // calculate within-word fixation location
                            w.dv.FixDist[w.dv.Pos1]++; // record fixation location for fixation landing-site distribution
                            if (j < (trace.size() - 1) && i == trace.get(j + 1).word) w.dv.RefixProb[w.dv.Pos1]++; // record fixation location for refixation-probability distribution
                            w.dv.GD = trace.get(j).dur; // increment GD
                        }
                        // If fixation is not the first:
                        else if (j == 0 || (j > 0 && trace.get(j - 1).word == i)) w.dv.GD += trace.get(j).dur; // increment GD
                        w.dv.TT += trace.get(j).dur; // increment TT
                    }
                }
            }

            // Calculate DVs derived indirectly from trace:
            if (w.dv.FFD == 0) w.dv.PrS++; // increment # skips
            else {
                //w.dv.PrF++; // increment # fixations
                if (w.dv.FFD == w.dv.GD) { // i.e., word was fixated only once during first pass
                    w.dv.SFD = w.dv.FFD; // SFD = FFD
                    w.dv.Pr1++; // increment # single-fixation durations
                    // Increment arrays for IOVPs:
                    w.dv.IOVP[w.dv.Pos1] = w.dv.SFD; // durations
                    w.dv.N[w.dv.Pos1]++; // increment IOVP fixation-location count
                }
                else w.dv.Pr2++; // increment # multiple fixations
            }
            if (w.dv.TT > 0) w.dv.PrF++;

            // Add values to running totals used for means across subjects:
            text.get(S).get(i).dv.SFD += w.dv.SFD;
            text.get(S).get(i).dv.FFD += w.dv.FFD;
            text.get(S).get(i).dv.GD += w.dv.GD;
            text.get(S).get(i).dv.TT += w.dv.TT;
            text.get(S).get(i).dv.Pr1 += w.dv.Pr1;
            text.get(S).get(i).dv.Pr2 += w.dv.Pr2;
            text.get(S).get(i).dv.PrS += w.dv.PrS;
            text.get(S).get(i).dv.PrF += w.dv.PrF;
            text.get(S).get(i).dv.NFix += w.dv.NFix;
            for (int j = 0; j < EZReader10.maxLength; j++) {
                text.get(S).get(i).dv.FixDist[j] += w.dv.FixDist[j];
                text.get(S).get(i).dv.RefixProb[j] += w.dv.RefixProb[j];
                text.get(S).get(i).dv.IOVP[j] += w.dv.IOVP[j];
                text.get(S).get(i).dv.N[j] += w.dv.N[j];
            }
        }
    }
}

