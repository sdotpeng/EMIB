// This class instantiates all of E-Z Reader's component processes (e.g., L1).

import java.util.*;

public class Process {

    double dur; // duration (ms)
    double durCopy; // Copy of duration is retained (i.e., not decremented)
    double length; // saccade length (# of character spaces)
    String name; // process label (e.g., "L1", "M2", etc.)
    int word; // word # associated with process (e.g., L1 for word #5)

    // Initialize classes:
    Display display = new Display();
    Gamma g = new Gamma();
    Random r = new Random();
    Saccade saccade = new Saccade();

    //**************************************************************************
    // PRE-ATTENTIVE VISUAL PROCESSING
    //**************************************************************************

    void initializeV(int N) {

        name = "V";
        word = N;

        // Calculate duration (ms):
        dur = EZReader10.V;
    }

    //**************************************************************************
    // FAMILIARITY CHECK
    //**************************************************************************

    void initializeL1(ArrayList<Sentence> text, int S, int N, boolean ongoingI)
        {

        name = "L1";
        word = N;

        // Determine if word can be predicted (i.e., has the previous word been integrated?):
        double clozeValue;
        if (ongoingI == true) clozeValue = 0;
        else clozeValue = 1.0;

        // Calculate duration (ms):
        double PrGuess = r.nextDouble();
        if (PrGuess < (text.get(S).get(N).iv.cloze)) dur = 0;
        else {
            double mu = EZReader10.Alpha1 - (EZReader10.Alpha2 * text.get(S).get(N).iv.lnFreq) - (EZReader10.Alpha3 * (text.get(S).get(N).iv.cloze * clozeValue));
            dur = g.nextDouble(mu, EZReader10.SigmaGamma);
        }
    }

    //**************************************************************************
    // LEXICAL ACCESS
    //**************************************************************************

    void initializeL2(ArrayList<Sentence> text, int S, int N, boolean ongoingI)
        {

        name = "L2";
        word = N;

        // Determine if word can be predicted (i.e., has the previous word been integrated?):
        double clozeValue;
        if (ongoingI == true) clozeValue = 0;
        else clozeValue = 1.0;

        // Calculate duration (ms):
        double mu = EZReader10.Alpha1 - (EZReader10.Alpha2 * text.get(S).get(N).iv.lnFreq) - (EZReader10.Alpha3 * (text.get(S).get(N).iv.cloze* clozeValue));
        mu *= EZReader10.Delta;
        dur = g.nextDouble(mu, EZReader10.SigmaGamma);
    }

    //**************************************************************************
    // POST-LEXICAL INTEGRATION
    //**************************************************************************

    void initializeI(ArrayList<Sentence> text, int S, int N) {

        name = "I";
        word = N;

        // Calculate duration of I for target and non-target words (ms):
        if (N == text.get(S).target) dur = g.nextDouble(EZReader10.ITarget, EZReader10.SigmaGamma);
        else dur = g.nextDouble(EZReader10.I, EZReader10.SigmaGamma);
    }

    //**************************************************************************
    // ATTENTION SHIFT
    //**************************************************************************

    void initializeA(int N) {

        name = "A";
        word = N;

        // Calculate duration (ms):
        dur = g.nextDouble(EZReader10.A, EZReader10.SigmaGamma);
    }

    //**************************************************************************
    // LABILE SACCADIC PROGRAMMING
    //**************************************************************************

    void initializeM1(ArrayList<Process> active, double currentPos, double
            targetPos, int N) {

        name = "M1";
        word = N;

        // Calculate intended saccade length:
        length = targetPos - currentPos;

        // Calculate duration (ms):
        dur = g.nextDouble(EZReader10.M1, EZReader10.SigmaGamma);
        durCopy = dur;

        // Cancel any pending M1 & adjust new M1 duration accordingly:
        for (int i = 0; i < active.size(); i++) if (active.get(i).name.equals("M1")) {

            double programmingTimeCompleted = active.get(i).durCopy - active.get(i).dur;
            double engageTime = dur * EZReader10.Xi;
            if (programmingTimeCompleted > engageTime) dur -= engageTime;
            else dur -= programmingTimeCompleted;

            active.remove(i);
        }
    }

    //**************************************************************************
    // NON-LABILE SACCADIC PROGRAMMING
    //**************************************************************************

    void initializeM2(double intendedSaccade, double launchSiteFixDur, int N) {

        name = "M2";
        word = N;

        // Calculate saccadic error (character spaces):
        double randomError = saccade.randomError(intendedSaccade);
        double systematicError = saccade.systematicError(intendedSaccade, launchSiteFixDur);

        // Calculate actual saccade length (character spaces):
        length = intendedSaccade + randomError + systematicError;

        // Calculate duration (ms):
        dur = g.nextDouble(EZReader10.M2, EZReader10.SigmaGamma);
    }

    //**************************************************************************
    // SACCADE EXECUTION
    //**************************************************************************

    void initializeS(double actualSaccade, int N) {

        name = "S";
        word = N;

        // Calculate length (character spaces):
        length = actualSaccade;

        // Calculate duration (ms):
        dur = EZReader10.S;
    }

    //**************************************************************************
    // ADJUST LEXICAL-PROCESSING RATE
    //**************************************************************************

    double calcRate(ArrayList<Sentence> text, int S, int N, double currentPos) {

        // Calculate mean absolute deviation between fixation position & letters of attended word:
        double meanAbsDev = 0;
        for (double i = text.get(S).get(N).iv.pos1; i <= text.get(S).get(N).iv.posN; i++) meanAbsDev += Math.abs(i - currentPos);
        meanAbsDev /= text.get(S).get(N).iv.length;

        // Return updated lexical-processing rate:
        return Math.pow(EZReader10.Epsilon, meanAbsDev);
    }
}
