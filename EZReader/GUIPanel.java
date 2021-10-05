// This class displays and initializes the GUI panel.

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class GUIPanel extends JFrame implements GUIMonitor {

    public GUIPanel() {
    }

    // Text fields for entering model's free-parameter values:
    private JTextField alpha1Value, alpha2Value, alpha3Value, deltaValue;
    private JTextField IValue, ITargetValue, pFValue, pFTargetValue;
    private JTextField AValue, epsilonValue, lambdaValue, sigmaGammaValue, VValue;
    private JTextField eta1Value, eta2Value, omega1Value, omega2Value, psiValue;
    private JTextField M1Value, M2Value, SValue, xiValue;

    // Text fields for corpus file & output file names:
    private JTextField corpusFileName, outputFileName, targetFileName;

    // Buttons for including/excluding trials containing inter-word regressions:
    private JRadioButton noButton, yesButton;

    // Text fields for entering # of statistical subjects:
    private JTextField numberSubjects;

    // Buttons for selecting simulation output:
    private JRadioButton recordDistributions, recordStates, recordTrace, recordWordDVs, recordWordIVs;

    // Button for running simulation:
    private JButton runButton;

    // Re-set RUN button:
    public void notifyFinished() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                runButton.setEnabled(true);
            }
        });
    }

    // Start new thread to run simulation:
    public void runButtonPushed() {
        // Construct new instance of model:
        Model.monitor = this;
        Thread workhorse;
        workhorse = new Thread(new Runnable() {
            public void run() {
                try {
                    Model.run();
                }
                catch (FileNotFoundException fileNotFound) {
                    JOptionPane.showMessageDialog(GUIPanel.this, "The file doesn't exist", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        workhorse.start();
    }

    //**************************************************************************
    // GENERATE & DISPLAY GUI
    //**************************************************************************

    public void displayGUI() {

        // Set up event listeners:
        ButtonListener runSimulationListener = new ButtonListener();

        // Set up overall panel for GUI:
        this.setTitle("E-Z READER 10.2");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel GUIPanel = new JPanel();
        GUIPanel.setLayout(new GridBagLayout());

        // Set up query for output type:
        Box simulationModeBox = Box.createHorizontalBox();
        recordWordIVs = new JRadioButton("Word IVs  ", false);
        recordStates = new JRadioButton("Model States  ", false);
        recordTrace = new JRadioButton("Trace File  ", false);
        recordWordDVs = new JRadioButton("Word DVs  ", true);
        recordDistributions = new JRadioButton("Distributions  ", false);
        ButtonGroup simulationModeButtons = new ButtonGroup();
        simulationModeButtons.add(recordWordIVs);
        simulationModeButtons.add(recordStates);
        simulationModeButtons.add(recordTrace);
        simulationModeButtons.add(recordWordDVs);
        simulationModeButtons.add(recordDistributions);
        simulationModeBox.add(recordWordIVs);
        simulationModeBox.add(recordStates);
        simulationModeBox.add(recordTrace);
        simulationModeBox.add(recordWordDVs);
        simulationModeBox.add(recordDistributions);
        simulationModeBox.setBorder(BorderFactory.createTitledBorder("Simulation Output"));
        addItem(GUIPanel, simulationModeBox, 0, 0, 4, 1, GridBagConstraints.CENTER);

        // Set up query for including/excluding regression trials:
        Box includeRegressionsBox = Box.createHorizontalBox();
        yesButton = new JRadioButton("Yes       ", true);
        noButton = new JRadioButton("No       ", false);
        ButtonGroup includeRegressionsButtons = new ButtonGroup();
        includeRegressionsButtons.add(yesButton);
        includeRegressionsButtons.add(noButton);
        includeRegressionsBox.add(yesButton);
        includeRegressionsBox.add(noButton);
        includeRegressionsBox.setBorder(BorderFactory.createTitledBorder("Include Regressions?"));
        addItem(GUIPanel, includeRegressionsBox, 0, 1, 2, 1, GridBagConstraints.EAST);

        // Set up text-field panel for # subjects & "run" button:
        Box executeSimulationPanel = Box.createVerticalBox();
        GridLayout executeSimulationLayout = new GridLayout(1, 2);

        // Set up query for # subjects:
        JPanel numberSubjectsPanel = new JPanel();
        GridLayout numberSubjectsLayout = new GridLayout(1, 2);
        JLabel enterNumberSubjects = new JLabel("# Subjects:", JLabel.RIGHT);
        numberSubjects = new JTextField(3);
        numberSubjectsPanel.add(enterNumberSubjects);
        numberSubjectsPanel.add(numberSubjects);

        // Set up button to run simulation:
        JPanel runButtonPanel = new JPanel();
        GridLayout runButtonLayout = new GridLayout(1, 2);
        runButton = new JButton("RUN");
        runButton.addActionListener(runSimulationListener);
        runButtonPanel.add(runButton);

        // Add panels to "run simulation" panel:
        executeSimulationPanel.add(numberSubjectsPanel);
        executeSimulationPanel.add(runButtonPanel);
        executeSimulationPanel.setBorder(BorderFactory.createTitledBorder("Run Simulation"));
        addItem(GUIPanel, executeSimulationPanel, 3, 2, 1, 3, GridBagConstraints.CENTER);

        // Set up query for corpus file name:
        JPanel corpusFilePanel = new JPanel();
        FlowLayout corpusFileLayout = new FlowLayout();
        JLabel enterCorpusFileName = new JLabel(" Corpus File Name:", JLabel.RIGHT);
        corpusFileName = new JTextField(12);
        corpusFilePanel.add(enterCorpusFileName);
        corpusFilePanel.add(corpusFileName);
        addItem(GUIPanel, corpusFilePanel, 0, 2, 3, 1, GridBagConstraints.EAST);

        // Set up query for target-word file name:
        JPanel targetWordFilePanel = new JPanel();
        FlowLayout targetWordFileLayout = new FlowLayout();
        JLabel enterTargetWordFileName = new JLabel(" Target Word File Name:", JLabel.RIGHT);
        targetFileName = new JTextField(12);
        targetWordFilePanel.add(enterTargetWordFileName);
        targetWordFilePanel.add(targetFileName);
        addItem(GUIPanel, targetWordFilePanel, 0, 3, 3, 1, GridBagConstraints.EAST);

        // Set up query for output file name:
        JPanel outputFilePanel = new JPanel();
        FlowLayout outputFileLayout = new FlowLayout();
        JLabel enterOutputFileName = new JLabel(" Output File Name:", JLabel.RIGHT);
        outputFileName = new JTextField("SimulationResults.txt", 12);
        outputFilePanel.add(enterOutputFileName);
        outputFilePanel.add(outputFileName);
        addItem(GUIPanel, outputFilePanel, 0, 4, 3, 1, GridBagConstraints.EAST);

        // Set up query for free parameters:
        JPanel freeParameterPanel = new JPanel();
        GridLayout freeParameterLayout = new GridLayout(1, 5); // 1,4

        // Lexical-procesing parameter names:
        JPanel lexicalParametersPanel = new JPanel();
        GridLayout lexicalParametersLayout = new GridLayout(5, 2);
        Box lexicalParameters = Box.createVerticalBox();
        String str = "\u03B1";
        JLabel alpha1Label = new JLabel("    " + str + "1", JLabel.RIGHT);
        JLabel alpha2Label = new JLabel("    " + str + "2", JLabel.RIGHT);
        JLabel alpha3Label = new JLabel("    " + str + "3", JLabel.RIGHT);
        str = "\u0394";
        JLabel deltaLabel = new JLabel("    " + str, JLabel.RIGHT);
        lexicalParameters.add(alpha1Label);
        lexicalParameters.add(Box.createVerticalStrut(11));
        lexicalParameters.add(alpha2Label);
        lexicalParameters.add(Box.createVerticalStrut(11));
        lexicalParameters.add(alpha3Label);
        lexicalParameters.add(Box.createVerticalStrut(11));
        lexicalParameters.add(deltaLabel);
        lexicalParametersPanel.add(lexicalParameters);

        // Lexical-procesing parameter values:
        Box lexicalParameterValues = Box.createVerticalBox();
        alpha1Value = new JTextField("115", 3);
        alpha2Value = new JTextField("2.2", 3);
        alpha3Value = new JTextField("13", 3);
        deltaValue = new JTextField("0.22", 3);
        lexicalParameterValues.add(alpha1Value);
        lexicalParameterValues.add(alpha2Value);
        lexicalParameterValues.add(alpha3Value);
        lexicalParameterValues.add(deltaValue);
        lexicalParametersPanel.add(lexicalParameterValues);
        lexicalParametersPanel.setBorder(BorderFactory.createTitledBorder("Lexical"));

        // Post-lexical-procesing parameter names:
        JPanel postLexicalParametersPanel = new JPanel();
        GridLayout postLexicalParametersLayout = new GridLayout(5, 2);
        Box postLexicalParameters = Box.createVerticalBox();
        JLabel ILabel = new JLabel("    I", JLabel.RIGHT);
        JLabel pFLabel = new JLabel("    pF", JLabel.RIGHT);
        JLabel ITargetLabel = new JLabel("    I(n)", JLabel.RIGHT);
        JLabel pFTargetLabel = new JLabel("    pF(n)", JLabel.RIGHT);
        postLexicalParameters.add(ILabel);
        postLexicalParameters.add(Box.createVerticalStrut(11));
        postLexicalParameters.add(pFLabel);
        postLexicalParameters.add(Box.createVerticalStrut(11));
        postLexicalParameters.add(ITargetLabel);
        postLexicalParameters.add(Box.createVerticalStrut(11));
        postLexicalParameters.add(pFTargetLabel);
        postLexicalParameters.add(Box.createVerticalStrut(11));
        postLexicalParametersPanel.add(postLexicalParameters);

        // Post-lexical-procesing parameter values:
        Box postLexicalParameterValues = Box.createVerticalBox();
        IValue = new JTextField("25.0", 3);
        pFValue = new JTextField("0.01", 3);
        ITargetValue = new JTextField("25.0", 3);
        pFTargetValue = new JTextField("0.01", 3);
        postLexicalParameterValues.add(IValue);
        postLexicalParameterValues.add(pFValue);
        postLexicalParameterValues.add(ITargetValue);
        postLexicalParameterValues.add(pFTargetValue);
        postLexicalParametersPanel.add(postLexicalParameterValues);
        postLexicalParametersPanel.setBorder(BorderFactory.createTitledBorder("Post-Lexical"));

        // Saccadic-latency parameter names:
        JPanel latencyParametersPanel = new JPanel();
        GridLayout latencyParametersLayout = new GridLayout(5, 2);
        Box latencyParameters = Box.createVerticalBox();
        JLabel M1Label = new JLabel("    M1", JLabel.RIGHT);
        JLabel M2Label = new JLabel("    M2", JLabel.RIGHT);
        JLabel SLabel = new JLabel("    S", JLabel.RIGHT);
        str = "\u03Be";
        JLabel xiLabel = new JLabel("    " + str, JLabel.RIGHT);
        latencyParameters.add(M1Label);
        latencyParameters.add(Box.createVerticalStrut(11));
        latencyParameters.add(M2Label);
        latencyParameters.add(Box.createVerticalStrut(11));
        latencyParameters.add(SLabel);
        latencyParameters.add(Box.createVerticalStrut(11));
        latencyParameters.add(xiLabel);
        latencyParametersPanel.add(latencyParameters);

        // Saccadic-latency parameter values:
        Box latencyParameterValues = Box.createVerticalBox();
        M1Value = new JTextField("125", 3);
        M2Value = new JTextField("25", 3);
        SValue = new JTextField("25", 3);
        xiValue = new JTextField("0.5", 3);
        latencyParameterValues.add(M1Value);
        latencyParameterValues.add(M2Value);
        latencyParameterValues.add(SValue);
        latencyParameterValues.add(xiValue);
        latencyParametersPanel.add(latencyParameterValues);
        latencyParametersPanel.setBorder(BorderFactory.createTitledBorder("Sac. Time"));

        // Saccadic-error parameter names:
        JPanel errorParametersPanel = new JPanel();
        GridLayout errorParametersLayout = new GridLayout(5, 2);
        Box errorParameters = Box.createVerticalBox();
        str = "\u03A8";
        JLabel psiLabel = new JLabel("    " + str, JLabel.RIGHT);
        str = "\u03A9";
        JLabel omega1Label = new JLabel("    " + str + "1", JLabel.RIGHT);
        JLabel omega2Label = new JLabel("    " + str + "2", JLabel.RIGHT);
        str = "\u03B7";
        JLabel eta1Label = new JLabel("    " + str + "1", JLabel.RIGHT);
        JLabel eta2Label = new JLabel("    " + str + "2", JLabel.RIGHT);
        errorParameters.add(psiLabel);
        errorParameters.add(Box.createVerticalStrut(11));
        errorParameters.add(omega1Label);
        errorParameters.add(Box.createVerticalStrut(11));
        errorParameters.add(omega2Label);
        errorParameters.add(Box.createVerticalStrut(11));
        errorParameters.add(eta1Label);
        errorParameters.add(Box.createVerticalStrut(11));
        errorParameters.add(eta2Label);
        errorParametersPanel.add(errorParameters);

        // Saccadic-error parameter values:
        Box errorParameterValues = Box.createVerticalBox();
        psiValue = new JTextField("7.0", 3);
        omega1Value = new JTextField("6.0", 3);
        omega2Value = new JTextField("3.0", 3);
        eta1Value = new JTextField("0.5", 3);
        eta2Value = new JTextField("0.1", 3);
        errorParameterValues.add(psiValue);
        errorParameterValues.add(omega1Value);
        errorParameterValues.add(omega2Value);
        errorParameterValues.add(eta1Value);
        errorParameterValues.add(eta2Value);
        errorParametersPanel.add(errorParameterValues);
        errorParametersPanel.setBorder(BorderFactory.createTitledBorder("Sac. Acc."));

        // Vision & misc. parameter names:
        JPanel miscParametersPanel = new JPanel();
        GridLayout miscParametersLayout = new GridLayout(5, 2);
        Box miscParameters = Box.createVerticalBox();
        JLabel VLabel = new JLabel("    V", JLabel.RIGHT);
        str = "\u03B5";
        JLabel epsilonLabel = new JLabel("    " + str, JLabel.RIGHT);
        JLabel ALabel = new JLabel("    A", JLabel.RIGHT);
        str = "\u03BB";
        JLabel lambdaLabel = new JLabel("    " + str, JLabel.RIGHT);
        str = "\u03C3" + "\u03B3";
        JLabel sigmaGammaLabel = new JLabel("    " + str, JLabel.RIGHT);
        miscParameters.add(VLabel);
        miscParameters.add(Box.createVerticalStrut(11));
        miscParameters.add(epsilonLabel);
        miscParameters.add(Box.createVerticalStrut(11));
        miscParameters.add(ALabel);
        miscParameters.add(Box.createVerticalStrut(11));
        miscParameters.add(lambdaLabel);
        miscParameters.add(Box.createVerticalStrut(11));
        miscParameters.add(sigmaGammaLabel);
        miscParametersPanel.add(miscParameters);

        // Vision & misc. parameter values:
        Box miscParameterValues = Box.createVerticalBox();
        VValue = new JTextField("50", 3);
        epsilonValue = new JTextField("1.15", 3);
        AValue = new JTextField("25.0", 3);
        lambdaValue = new JTextField("0.25", 3);
        sigmaGammaValue = new JTextField("20", 3);
        miscParameterValues.add(VValue);
        miscParameterValues.add(epsilonValue);
        miscParameterValues.add(AValue);
        miscParameterValues.add(lambdaValue);
        miscParameterValues.add(sigmaGammaValue);
        miscParametersPanel.add(miscParameterValues);
        miscParametersPanel.setBorder(BorderFactory.createTitledBorder("Vision & Misc."));

        // Add individual parameter panels to "free parameter" panel:
        freeParameterPanel.add(lexicalParametersPanel);
        freeParameterPanel.add(postLexicalParametersPanel);
        freeParameterPanel.add(latencyParametersPanel);
        freeParameterPanel.add(errorParametersPanel);
        freeParameterPanel.add(miscParametersPanel);
        freeParameterPanel.setBorder(BorderFactory.createTitledBorder("Free Parameters"));
        addItem(GUIPanel, freeParameterPanel, 0, 5, 4, 4, GridBagConstraints.CENTER);

        // Finish setting up GUI:
        this.add(GUIPanel);
        this.pack();
        this.setVisible(true);
    }

    // Add smaller panel to GridBag panel:
    private void addItem(JPanel p, JComponent c, int x, int y, int width, int height, int align) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = x;
        gc.gridy = y;
        gc.gridwidth = width;
        gc.gridheight = height;
        gc.weightx = 100.0;
        gc.weighty = 100.0;
        gc.insets = new Insets(5, 5, 5, 5);
        gc.anchor = align;
        gc.fill = GridBagConstraints.NONE;
        p.add(c, gc);
    }

    // Event listener for running simulations:
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == runButton) { // i.e., "RUN" button pressed

                // Determine type of simulation output:
                if (recordWordIVs.isSelected()) EZReader10.displayCorpus = true;
                else EZReader10.displayCorpus = false;
                if (recordStates.isSelected()) EZReader10.displayStates = true;
                else EZReader10.displayStates = false;
                if (recordTrace.isSelected()) EZReader10.displayTrace = true;
                else EZReader10.displayTrace = false;
                if (recordWordDVs.isSelected()) EZReader10.displayMeans = true;
                else EZReader10.displayMeans = false;
                if (recordDistributions.isSelected()) EZReader10.displayDists = true;
                else EZReader10.displayDists = false;

                // Determine # of subjects:
                String n = numberSubjects.getText();
                EZReader10.NSubjects = Integer.parseInt(n);

                // Determine name of corpus file:
                n = corpusFileName.getText();
                EZReader10.corpusFile = n;

                // Determine name of target-word file:
                n = targetFileName.getText();
                EZReader10.targetFile = n;

                // Determine name of output file:
                n = outputFileName.getText();
                EZReader10.outputFile = n;

                // Determine if inter-word-regression trails included:
                if (yesButton.isSelected()) EZReader10.regressions = true;
                else EZReader10.regressions = false;

                // Determine values of all free parameters:
                n = alpha1Value.getText();
                EZReader10.Alpha1 = Double.parseDouble(n);
                n = alpha2Value.getText();
                EZReader10.Alpha2 = Double.parseDouble(n);
                n = alpha3Value.getText();
                EZReader10.Alpha3 = Double.parseDouble(n);
                n = deltaValue.getText();
                EZReader10.Delta = Double.parseDouble(n);
                n = IValue.getText();
                EZReader10.I = Double.parseDouble(n);
                n = pFValue.getText();
                EZReader10.pF = Double.parseDouble(n);
                n = ITargetValue.getText();
                EZReader10.ITarget = Double.parseDouble(n);
                n = pFTargetValue.getText();
                EZReader10.pFTarget = Double.parseDouble(n);
                n = M1Value.getText();
                EZReader10.M1 = Double.parseDouble(n);
                n = M2Value.getText();
                EZReader10.M2 = Double.parseDouble(n);
                n = SValue.getText();
                EZReader10.S = Double.parseDouble(n);
                n = xiValue.getText();
                EZReader10.Xi = Double.parseDouble(n);
                n = psiValue.getText();
                EZReader10.Psi = Double.parseDouble(n);
                n = omega1Value.getText();
                EZReader10.Omega1 = Double.parseDouble(n);
                n = omega2Value.getText();
                EZReader10.Omega2 = Double.parseDouble(n);
                n = eta1Value.getText();
                EZReader10.Eta1 = Double.parseDouble(n);
                n = eta2Value.getText();
                EZReader10.Eta2 = Double.parseDouble(n);
                n = VValue.getText();
                EZReader10.V = Double.parseDouble(n);
                n = epsilonValue.getText();
                EZReader10.Epsilon = Double.parseDouble(n);
                n = AValue.getText();
                EZReader10.A = Double.parseDouble(n);
                n = lambdaValue.getText();
                EZReader10.Lambda = Double.parseDouble(n);
                n = sigmaGammaValue.getText();
                EZReader10.SigmaGamma = Double.parseDouble(n);

                // Initiate simulation using above parameters, etc.:
                runButtonPushed();
            }
        }
    }
}

