// This class instantiates words.

public class Word {

    DV dv = new DV(); // Initialize word DVs
    IV iv = new IV(); // Initialize word IVs

    // Dependent variables:
    public class DV {

        // Fixation-duration measures:
        double FFD = 0; // first-fixation duration (ms)
        double GD = 0; // gaze duration (ms)
        double SFD = 0; // single-fixation duration
        double TT = 0; // total time (ms)

        // Fixation-probability measures:
        double Pr1 = 0; // prob. of single fixation
        double Pr2 = 0; // prob. of 2+ fixations
        double PrF = 0; // prob. of fixating
        double PrS = 0; // prob. of skipping

        // Distributions:
        double FixDist[] = new double[EZReader10.maxLength]; // landings sites
        double IOVP[] = new double[EZReader10.maxLength]; // IOVPs
        int N[] = new int[EZReader10.maxLength]; // counter for IOVPs
        int NFix = 0; // # fixations
        int Pos1 = 0; // within-word first-fixation position
        double RefixProb[] = new double[EZReader10.maxLength]; // refix. probs.
    }

    // Independent variables:
    public class IV {
        double cloze; // cloze pred.
        int fc; // freq. class (1 = 1-10, 2 = 11-100, etc.)
        double freq; // freq. count (per million)
        double length; // # letters in word
        double lnFreq; // natural log word freq.
        int N; // within-sentence word #
        double OVP; // optimal-viewing position
        double pos0; // cumulative # of space preceding word
        double pos1; // cumulative # of first letter
        double posN; // cumulative # of last letter
        String sp; // word's spelling
    }
}

