// This is the core E-Z Reader class.  Its internal states instantiate the model's components and generates eye movements.

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class Model {

    public static GUIMonitor monitor = null;

    public static void run() throws FileNotFoundException {

        // Initialize classes:
        Display display = new Display();
        Random r = new Random();
        Stats stats = new Stats();

        // Initialize output file:
        PrintStream diskWriter = new PrintStream(EZReader10.outputFile);
        // Initialize sentence corpus:
        Scanner diskScanner1 = new Scanner(new File(EZReader10.corpusFile));
        ArrayList<Sentence> text = new ArrayList<>();
        Corpus corpus = new Corpus();
        corpus.initialize(diskScanner1, text);

        // Initialize target words:
        Scanner diskScanner2 = new Scanner(new File(EZReader10.targetFile));
        int k = 0;
        while (diskScanner2.hasNext()) {
            text.get(k).target = diskScanner2.nextInt();
            k++;
        }

        // *** Display properties of words in sentence corpus:
        if (EZReader10.displayCorpus == true) display.corpus(diskWriter, text);

        // # of regressions/sentence:
        int regressionN[] = new int[EZReader10.NSentences];

        // flags indicating integration failure of particular words:
        boolean[] IF = new boolean[EZReader10.maxSentenceLength];

        String state;

        // Beginning of subject loop:
        for (int subject = 0; subject < EZReader10.NSubjects; subject++) {

            // Beginning of sentence loop:
            for (int S = 0; S < EZReader10.NSentences; S++) {
            //for (int S = 0; S < 1; S++) { // Note: De-comment to manual check trace files.

                // Initialize fixation:
                int fixationN = 0;
                ArrayList<Fixation> trace = new ArrayList<>();
                Fixation f = new Fixation();
                f.number = fixationN;
                f.dur = 0;
                f.pos = text.get(S).get(0).iv.OVP;
                f.word = 0;

                // Start L1:
                ArrayList<Process> active = new ArrayList<>();
                Process p = new Process();
                int N = 0;
                p.initializeL1(text, S, N, true);
                double rate = p.calcRate(text, S, N, f.pos);
                p.dur *= rate;
                active.add(p);

                // Initialize integration-failure flags for all words:
                for (int i = 0; i < EZReader10.maxSentenceLength; i++) IF[i] = false;

                state = "[START]";

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                // MODEL STARTS READING SINGLE SENTENCE
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                boolean sentenceDone = false;
                while (!sentenceDone) {

                    // *** Display model state:
                    if (EZReader10.displayStates == true) display.state(diskWriter, S, N, f, rate, active, IF, text, state);

                    // Identify processing w/ shortest duration:
                    double minProcessDuration = active.get(0).dur;
                    int minProcessID = 0;
                    for (int i = active.size() - 1; i >= 0; i--) if (active.get(i).dur < minProcessDuration) {
                        minProcessDuration = active.get(i).dur;
                        minProcessID = i;
                    }

                    // Store attributes of shortest process:
                    Process sp = new Process();
                    sp.dur = active.get(minProcessID).dur;
                    sp.durCopy = active.get(minProcessID).durCopy;
                    sp.name = active.get(minProcessID).name;
                    sp.length = active.get(minProcessID).length;
                    sp.word = active.get(minProcessID).word;

                    // Remove shortest process from list of active processes:
                    active.remove(minProcessID);

                    // Decrement all remaining process durations:
                    for (int i = 0; i < active.size(); i++) active.get(i).dur -= sp.dur;

                    // Increase fixation duration (except if saccade ongoing):
                    if (!sp.name.equals("S")) f.dur += sp.dur;

                    //**********************************************************
                    // DETERMINE & EXECUTE NEXT MODEL STATE
                    //**********************************************************

                    // PRE-ATTENTIVE VISUAL PROCESSING (V):
                    if (sp.name.equals("V")) {
                        state = "[V]";

                        // Adjust L1 rate:
                        for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("L1")) {

                            active.get(i).dur /= rate;
                            rate = p.calcRate(text, S, N, f.pos);
                            active.get(i).dur *= rate;
                        }
                    }

                    // FAMILIARITY CHECK (L1):
                    else if (sp.name.equals("L1")) {
                        state = "[L1]";

                        // Deterine if integration is on-going for previous word:
                        boolean ongoingI = false;
                        for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("I") && active.get(i).word == (N - 1)) ongoingI = true;

                        // Start L2:
                        p = new Process();
                        p.initializeL2(text, S, N, ongoingI);
                        active.add(p);

                        // Cancel any pending M1 & start M1:
                        if (N < text.get(S).numberWords - 1) {
                            p = new Process();
                            p.initializeM1(active, f.pos, text.get(S).get(N + 1).iv.OVP, N + 1);
                            active.add(p);
                        }
                    }

                    // LEXICAL ACCESS (L2):
                    else if (sp.name.equals("L2")) {

                        // Determine if integation of the previous word failed:
                        boolean IFailure = false;
                        int IFWord = 0; // integration failure word
                        for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("I")) {
                            IFWord = active.get(i).word;
                            active.remove(i);
                        }

                        // Prohibit integration failure from potentially happening twice on a word.
                        if (IFWord > 0 && IF[IFWord] == false) {
                            IF[IFWord] = true;
                            IFailure = true;
                            N = IFWord;
                        }

                        // Slow integration failure:
                        if (IFailure == true) {
                            state = "[L2/SLOW I]";

                            // Note: A and/or L1 do NOT have to be removed from active processes because L2 just finished (i.e., A and L1 cannot be ongoing).

                            // Start A:
                            p = new Process();
                            p.initializeA(N);
                            active.add(p);

                            // Cancel any pending M1 & start M1:
                            p = new Process();
                            p.initializeM1(active, f.pos, text.get(S).get(N).iv.OVP, N);
                            active.add(p);

                        }

                        // Integration was successful:
                        else {
                            state = "[L2]";

                            // Start I:
                            p = new Process();
                            p.initializeI(text, S, N); // Integration of current word begins
                            active.add(p);

                            // Start A:
                            if (N < text.get(S).numberWords - 1) {
                                p = new Process();
                                p.initializeA(sp.word + 1); // Attention is directed towards next word
                                active.add(p);
                            }
                        }
                    }

                    // POST-LEXICAL INTEGRATION (I):
                    else if (sp.name.equals("I")) {

                        // Rapid integration failure:
                        double PrIFailure = r.nextDouble();
                        //System.out.println(text.get(sp.word).target);
                        //if (IF[sp.word] == false && N > 0 && ((PrIFailure < EZReader10.pF && sp.word != text.get(sp.word).target) || (PrIFailure < EZReader10.pFTarget && sp.word == text.get(sp.word).target))) {
                        if (IF[sp.word] == false && N > 0 && ((PrIFailure < EZReader10.pF && sp.word != 0 || (PrIFailure < EZReader10.pFTarget && sp.word == 0)))) {
                            state = "[RAPID I]";

                            // Flag word for integration failure:
                            IF[sp.word] = true;

                            // Stop A, L1, and/or L2:
                            for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("A")) active.remove(i);
                            for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("L1")) active.remove(i);
                            for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("L2")) active.remove(i);

                            // Start A:
                            N = sp.word; // i.e., shift attention to source of integration difficulty
                            p = new Process();
                            p.initializeA(N);
                            active.add(p);

                            // Cancel any pending M1 & start M1:
                            p = new Process();
                            p.initializeM1(active, f.pos, text.get(S).get(N).iv.OVP, N);
                            active.add(p);
                        }
                        else {

                            state = "[I]";

                            if (sp.word == text.get(S).numberWords - 1) sentenceDone = true;
                        }
                    }

                    // ATTENTION SHIFT (A):
                    else if (sp.name.equals("A")) {
                        state = "[A]";

                        // Shift attention to word:
                        N = sp.word;

                        // Deterine if integration is on-going for previous word:
                        boolean ongoingI = false;
                        for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("I") && active.get(i).word == (N - 1)) ongoingI = true;

                        // Start L1:
                        p = new Process();
                        p.initializeL1(text, S, N, ongoingI);
                        rate = p.calcRate(text, S, N, f.pos);
                        p.dur *= rate;
                        active.add(p);
                    }

                    // LABILE SACCADIC PROGRAMMING (M1):
                    else if (sp.name.equals("M1")) {
                        state = "[M1]";

                        // Start M2:
                        p = new Process();
                        p.initializeM2(sp.length, f.dur, sp.word);
                        active.add(p);
                    }

                    // NON-LABILE SACCADIC PROGRAMMING (M2):
                    else if (sp.name.equals("M2")) {
                        state = "[M2]";

                        // Start S:
                        p = new Process();
                        p.initializeS(sp.length, sp.word);
                        active.add(p);
                    }

                    // SACCADE EXECUTION (S):
                    else if (sp.name.equals("S")) {
                        state = "[S]";

                        // Terminate previous fixation:
                        trace.add(f);

                        // Start new fixation:
                        fixationN++;
                        f = new Fixation();
                        f.number = fixationN;
                        f.dur = 0;
                        f.pos = trace.get(fixationN - 1).pos + sp.length;
                        if (f.pos < 0) f.pos = 0;
                        double lastChar = text.get(S).get(text.get(S).numberWords - 1).iv.posN;
                        if (f.pos > lastChar) f.pos = lastChar;
                        for (int i = 0; i < text.get(S).numberWords; i++) if (f.pos >= text.get(S).get(i).iv.pos0 && f.pos < text.get(S).get(i).iv.posN) f.word = i;

                        // Start V:
                        p = new Process();
                        p.initializeV(N);
                        active.add(p);

                        // Start M1 (automatic refixation):
                        double PrRefixate = r.nextDouble();
                        double saccadeError = Math.abs(text.get(S).get(sp.word).iv.OVP - f.pos);
                        if (PrRefixate < (EZReader10.Lambda * saccadeError)) {
                            p = new Process();
                            p.initializeM1(active, f.pos, text.get(S).get(sp.word).iv.OVP, sp.word);
                            active.add(p);
                        }
                    }

                    //**********************************************************
                    // MODEL STATE EXECUTED
                    //**********************************************************

                }

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                // MODEL HAS FINISHED READING SENTENCE
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                active.clear(); // remove any remaining active processes

                // *** Display trace of individual fixations:
                if (EZReader10.displayTrace == true) display.fixations(diskWriter, text, S, trace);

                // If the model did not made an interword regression, then calcuate word DVs for each word in the sentence:
                if (EZReader10.regressions == false) {
                    boolean regression = false;
                    for (int i = 1; i < trace.size(); i++) if (trace.get(i - 1).word > trace.get(i).word) regression = true;
                    if (regression == false) stats.calcWordDVs(text, S, trace);
                    else regressionN[S]++;
                }
                // Calculate word DVs including regressions:
                else stats.calcWordDVs(text, S, trace);

                trace.clear(); // remove trace of fixations

            } // End of sentences loop

        } // End of subject loop

        // Calculate mean word-based measures (across subjects) for every word in sentence corpus:
        stats.calcMeans(text, regressionN);


        //----------------------------------------------------------------------
        // Note: The code in this section is not available in the GUI version of the model.
        //       It's used to calculate the goodness-of-fit (as per Reichle, Pollatsek, & Rayner, 2012)
        //       using the RMSD between observed and simulated frequency-class means.

        // Initialize classes for frequency-class means:
        ArrayList<Word> classMeans = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Word classMean = new Word();
            classMeans.add(classMean);
        }

        // Calculate frequency-class means:
        // System.out.println(text.get(0).get(0).dv.SFD);
        stats.calcClassMeans(text, classMeans);

        // Display RMSD:
        double RMSD = stats.calcRMSD(classMeans);
        // System.out.printf(" RMSD = %5.4f\n", RMSD);
        //----------------------------------------------------------------------


        // *** Display mean word-based measures and distributions for all words:
        if (EZReader10.displayMeans == true) {
            display.wordBasedMeans(diskWriter, text);
            display.fixDists(diskWriter, text);
            display.refixProbs(diskWriter, text);
            display.IOVPs(diskWriter, text);
        }

        // *** Display mean distributions for words of each given length:
        if (EZReader10.displayDists == true) {

            // Initialize classes for first-fixation landing-site distributions:
            ArrayList<Word> fixDists = new ArrayList<>();
            for (int i = 0; i < EZReader10.maxLength; i++) {
                Word fixDist = new Word();
                fixDists.add(fixDist);
            }

            // Calculate & display first-fixation landing-site distributions:
            stats.calcFixDists(text, fixDists);
            display.meanFixDists(diskWriter, fixDists);
            fixDists.clear();

            // Initialize classes for refixation-probability distributions:
            ArrayList<Word> refixProbs = new ArrayList<>();
            for (int i = 0; i < EZReader10.maxLength; i++) {
                Word refixProb = new Word();
                refixProbs.add(refixProb);
            }

            // Calculate & display refixation-probability distributions:
            stats.calcRefixProbs(text, refixProbs);
            display.meanRefixProbs(diskWriter, refixProbs);

            // Initialize classes for IOVPs:
            ArrayList<Word> IOVPs = new ArrayList<>();
            for (int i = 0; i < EZReader10.maxLength; i++) {
                Word IOVP = new Word();
                IOVPs.add(IOVP);
            }

            // Calculate & display IOVPs:
            stats.calcIOVPs(text, IOVPs);
            display.meanIOVPs(diskWriter, IOVPs);
        }

        // Re-initialize DVs:
        corpus.zeroDVs(text);
    }
}

