//******************************************************************************
// E-Z Reader, version 10.2J
// (c) March 9, 2019
// Erik D. Reichle, Ph.D.
// erik.reichle@mq.edu.au
// Macquarie University, Northryde, NSW 2019, Australia
//******************************************************************************

import java.io.*;

public class EZReader10 {

    // I/O file names:
    public static String corpusFile;
    public static String outputFile;
    public static String targetFile;

    // Free parameters:
    public static double A;
    public static double Alpha1;
    public static double Alpha2;
    public static double Alpha3;
    public static double Delta;
    public static double Epsilon;
    public static double Eta1;
    public static double Eta2;
    public static double I;
    public static double ITarget;
    public static double Lambda;
    public static double M1;
    public static double M2;
    public static double Omega1;
    public static double Omega2;
    public static double pF;
    public static double pFTarget;
    public static double Psi;
    public static double S;
    public static double SigmaGamma;
    public static double V;
    public static double Xi;

    // Fixed (simulation) parameters:
    public static int maxLength = 50; // = # letters + space left of word
    public static int maxSentenceLength = 70; // maximum # words / sentence
    public static int NSentences;
    public static int NSubjects;

    // Siimulation control soggles:
    public static boolean displayCorpus; // word IVs
    public static boolean displayDists; // IOVP distributions, etc.
    public static boolean displayStates; // E-Z Reader's internal states
    public static boolean displayTrace; // trace of individual fixations
    public static boolean displayMeans; // mean word DVs
    public static boolean regressions; // include regressions?

    public static void main(String[] args) throws FileNotFoundException {

        // Start graphic user interface (GUI):
        GUIPanel gui = new GUIPanel();
        gui.displayGUI();
    }
}
