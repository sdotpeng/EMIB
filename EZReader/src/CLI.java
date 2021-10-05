/**
 * Command Line Interface for E-Z Reader 10
 *
 * @author Siyuan Peng
 * @version 0.1
 */

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CLI {

    private static final String HELP_DOC = "Usage: java CLI [command] [argument]\n" +
            "\n" +
            "command:\n" +
            "  -h\tShow help\n" +
            "\n" +
            "  -ic\tInput corpus file\n" +
            "  -it\tInput targets file\n" +
            "  -o \tOutput file (xls or txt)\n" +
            "  -ns\tNumber of subjects\n" +
            "  -so\tSimulation Output:\n" +
            "  \t\twordivs, modelstates, tracefile, worddvs, distributions\n" +
            "  -r \tRegression\n" +
            "\n" +
            "  -a1\t\u03B11\n" +
            "  -a2\t\u03B12\n" +
            "  -a3\t\u03B13\n" +
            "  -d\t\u0394\n" +
            "  \n" +
            "  -i\tI\n" +
            "  -pf\tpF\n" +
            "  -itg\tI(n)\n" +
            "  -pftg\tpF(n)\n" +
            "  \n" +
            "  -m1\tM1\n" +
            "  -m2\tM2\n" +
            "  -s\tS\n" +
            "  -xi\t\u03Be\n" +
            "  \n" +
            "  -psi\t\u03A8\n" +
            "  -o1\t\u03A91\n" +
            "  -o2\t\u03A92\n" +
            "  -e1\t\u03B71\n" +
            "  -e2\t\u03B72\n" +
            "  \n" +
            "  -v\tV\n" +
            "  -ep\t\u03B5\n" +
            "  -a\tA\n" +
            "  -l\t\u03BB\n" +
            "  -sg\t\u03C3\u03B3\n" +
            "\n" +
            "argument:\n" +
            "  add arguments after the command";


    public static void main(String[] args) {

        // Set default values
        initialize();

        // Parse arguments from command line
        parse(args);

        // Run simulations
        run();

    }

    public static void parse(String[] arguments) {

        // Ready to parse arguments
        List<String> argsList = java.util.Arrays.asList(arguments);
        int parameterIndex;
        String parameter;

        // -h         Help document
        if (argsList.contains("-h")) {
            System.out.println(HELP_DOC);
            System.exit(1);
        }

        // -ic        Input corpus file
        if (argsList.contains("-ic")) {
            parameterIndex = argsList.indexOf("-ic") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.corpusFile = parameter;
        }

        // -it        Input targets file
        if (argsList.contains("-it")) {
            parameterIndex = argsList.indexOf("-it") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.targetFile = parameter;
        }

        // -o         Output file
        if (argsList.contains("-o")) {
            parameterIndex = argsList.indexOf("-o") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.outputFile = parameter;
        }

        // -ns        Number of subjects
        if (argsList.contains("-ns")) {
            parameterIndex = argsList.indexOf("-ns") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.NSubjects = Integer.parseInt(parameter);
        }

        // -so        Simulation Output
        if (argsList.contains("-so")) {
            EZReader10.displayMeans = false;
            parameterIndex = argsList.indexOf("-so") + 1;
            parameter = argsList.get(parameterIndex);
            switch (parameter) {
                case "wordivs" -> EZReader10.displayCorpus = true;
                case "modelstates" -> EZReader10.displayStates = true;
                case "tracefile" -> EZReader10.displayTrace = true;
                case "worddvs" -> EZReader10.displayMeans = true;
                case "distributions" -> EZReader10.displayDists = true;
            }
        }

        // -r         Regression
        if (argsList.contains("-r")) {
            parameterIndex = argsList.indexOf("-r") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.regressions = Boolean.parseBoolean(parameter);
        }

        // -a1        Alpha1
        if (argsList.contains("-a1")) {
            parameterIndex = argsList.indexOf("-a1") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.Alpha1 = Double.parseDouble(parameter);
        }

        // -a2        Alpha2
        if (argsList.contains("-a2")) {
            parameterIndex = argsList.indexOf("-a2") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.Alpha2 = Double.parseDouble(parameter);
        }

        // -a3        Alpha3
        if (argsList.contains("-a3")) {
            parameterIndex = argsList.indexOf("-a3") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.Alpha3 = Double.parseDouble(parameter);
        }

        // -d         Delta
        if (argsList.contains("-d")) {
            parameterIndex = argsList.indexOf("-d") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.Delta = Double.parseDouble(parameter);
        }

        // -i         I
        if (argsList.contains("-i")) {
            parameterIndex = argsList.indexOf("-i") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.I = Double.parseDouble(parameter);
        }

        // -pf        pF
        if (argsList.contains("-pf")) {
            parameterIndex = argsList.indexOf("-pf") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.pF = Double.parseDouble(parameter);
        }

        // -itg       ITarget
        if (argsList.contains("-itg")) {
            parameterIndex = argsList.indexOf("-itg") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.ITarget = Double.parseDouble(parameter);
        }

        // -pftg      pFTarget
        if (argsList.contains("-pftg")) {
            parameterIndex = argsList.indexOf("-pftg") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.pFTarget = Double.parseDouble(parameter);
        }

        // -m1        M1
        if (argsList.contains("-m1")) {
            parameterIndex = argsList.indexOf("-m1") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.M1 = Double.parseDouble(parameter);
        }

        // -m2        M2
        if (argsList.contains("-m2")) {
            parameterIndex = argsList.indexOf("-m2") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.M2 = Double.parseDouble(parameter);
        }

        // -s         S
        if (argsList.contains("-s")) {
            parameterIndex = argsList.indexOf("-s") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.S = Double.parseDouble(parameter);
        }

        // -xi        Xi
        if (argsList.contains("-xi")) {
            parameterIndex = argsList.indexOf("-xi") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.Xi = Double.parseDouble(parameter);
        }

        // -psi       Psi
        if (argsList.contains("-psi")) {
            parameterIndex = argsList.indexOf("-psi") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.Psi = Double.parseDouble(parameter);
        }

        // -o1        Omega1
        if (argsList.contains("-o1")) {
            parameterIndex = argsList.indexOf("-o1") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.Omega1 = Double.parseDouble(parameter);
        }

        // -o2        Omega2
        if (argsList.contains("-o2")) {
            parameterIndex = argsList.indexOf("-o2") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.Omega2 = Double.parseDouble(parameter);
        }

        // -e1        Eta1
        if (argsList.contains("-e1")) {
            parameterIndex = argsList.indexOf("-e1") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.Eta1 = Double.parseDouble(parameter);
        }

        // -e2        Eta2
        if (argsList.contains("-e2")) {
            parameterIndex = argsList.indexOf("-e2") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.Eta2 = Double.parseDouble(parameter);
        }

        // -v         V
        if (argsList.contains("-v")) {
            parameterIndex = argsList.indexOf("-v") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.V = Double.parseDouble(parameter);
        }

        // -ep        Epsilon
        if (argsList.contains("-ep")) {
            parameterIndex = argsList.indexOf("-ep") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.Epsilon = Double.parseDouble(parameter);
        }

        // -a         A
        if (argsList.contains("-a")) {
            parameterIndex = argsList.indexOf("-a") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.A = Double.parseDouble(parameter);
        }

        // -l         Lambda
        if (argsList.contains("-l")) {
            parameterIndex = argsList.indexOf("-l") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.Lambda = Double.parseDouble(parameter);
        }

        // -sg        SigmaGamma
        if (argsList.contains("-sg")) {
            parameterIndex = argsList.indexOf("-sg") + 1;
            parameter = argsList.get(parameterIndex);
            EZReader10.SigmaGamma = Double.parseDouble(parameter);
        }
    }

    /**
     * Initialize the parameters
     */
    public static void initialize() {

        // EZReader10.corpusFile = "DefaultCorpus.txt";
        // EZReader10.targetFile = "DefaultTargets.txt";
        EZReader10.corpusFile = "SRC98Corpus.txt";
        EZReader10.targetFile = "SRC98Targets.txt";
        EZReader10.outputFile = "SimulationOutput.txt";
        EZReader10.NSubjects = 100;

        EZReader10.displayCorpus = false;
        EZReader10.displayStates = false;
        EZReader10.displayTrace = false;
        EZReader10.displayMeans = true;
        EZReader10.displayDists = false;

        EZReader10.regressions = true;

        EZReader10.Alpha1 = 115;
        EZReader10.Alpha2 = 2.2;
        EZReader10.Alpha3 = 13;
        EZReader10.Delta = 0.22;

        EZReader10.I = 25.0;
        EZReader10.pF = 0.01;
        EZReader10.ITarget = 25.0;
        EZReader10.pFTarget = 0.01;

        EZReader10.M1 = 125;
        EZReader10.M2 = 25;
        EZReader10.S = 25;
        EZReader10.Xi = 0.5;

        EZReader10.Psi = 7.0;
        EZReader10.Omega1 = 6.0;
        EZReader10.Omega2 = 3.0;
        EZReader10.Eta1 = 0.5;
        EZReader10.Eta2 = 0.1;

        EZReader10.V = 50;
        EZReader10.Epsilon = 1.15;
        EZReader10.A = 25.0;
        EZReader10.Lambda = 0.25;
        EZReader10.SigmaGamma = 20;
    }

    /**
     * Make the Model run
     */
    public static void run() {
        Thread workhorse;
        workhorse = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Model.run();
                } catch (FileNotFoundException fileNotFound) {
                    System.out.println("Could not find the file: re-enter file name");
                }
            }
        });
        workhorse.start();
    }

}
