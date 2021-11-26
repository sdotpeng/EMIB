import subprocess, math
import numpy as np
import time


class EZReader:

    VALID_PARAMS = [
        'a1', 'a2', 'a3',
        'd', 'i', 'pf', 'itg', 'pftg',
        'm1', 'm2', 's', 'xi',
        'psi', 'o1', 'o2', 'e1', 'e2', 'v',
        'ep', 'a', 'l', 'sg'
    ]

    SIMULATIONS = ['wordivs', 'modelstates', 'tracefile', 'worddvs', 'distributions']

    def __init__(self, corpus="datasets/1_1_corpus.txt", target="datasets/1_1_target.txt", simulation="worddvs", num_subject=10, output="outputs/result.txt"):
        """Wrapper for EZReader in Python

        Parameters
        ----------
        corpus : str
            name of the corpus file
        target : str
            name of the target file
        """
        self.corpus = corpus
        self.target = target
        self.simulation = simulation
        self.num_subject = num_subject

        self.params = {}
        self.output = output

    def set_params(self, **kwargs):
        """Set the parameters for the EZReader for twisting

        Raises
        ------
        RuntimeError
            when an input parameters is not valid
        """

        for parameter in kwargs:
            if parameter not in self.VALID_PARAMS:
                raise RuntimeError("invalid parameters '{}'".format(parameter))

        for parameter, value in kwargs.items():
            self.params[parameter] = value

    def get_params(self):
        """Return existing parameters for the EZReader object

        Returns
        -------
        str
            parameters string
        """
        output = ", ".join([str(parameter) + "=" + str(value)
                            for parameter, value in self.params.items()])

        return output

    def clear_params(self):
        """Clear all parameters in the EZReader
        """

        self.params = {}

    def set_simultion(self, simulation):
        """Set simulation type

        Parameters
        ----------
        simulation : str
            type of simulation

        Raises
        ------
        RuntimeError
            when an input simulation is not valid
        """

        if simulation not in self.SIMULATIONS:
            raise RuntimeError("invalid simulation type {}".format(simulation))

        self.simulation = simulation

    def set_output(self, output):
        """Set output file name

        Parameters
        ----------
        output : str
            name of the output file
        """

        self.output = "outputs/" + output if "outputs/" not in output else output

    def set_num_subject(self, num_subject):
        """Set number of object for the simulation

        Parameters
        ----------
        num_subject : int
            number of subjects
        """
        self.num_subject = num_subject

    def run(self):
        """Run simulation with all given parameters
        """
        command = 'cd EZReader; java CLI -so {si} -o {o} -ic {ic} -it {it} '.format(
            si=self.simulation,
            o=self.output,
            ic=self.corpus,
            it=self.target
        )

        if len(self.params) > 0:
            command += " ".join(["-{parameter} {value}".format(parameter=parameter, value=value) for parameter, value in self.params.items()])

        process = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)

        line = ''
        while line != b'Done':
            line = process.stdout.readlines()[0]

        return


    def fetch(self):
        """Fetch the result file

        Returns
        -------
        str
            output file
        """
        with open("EZReader/" + self.output, "r") as result:
            return result.read()

    def get_prediction(self, metric):

        with open('EZReader/datasets/1_1_corpus.txt', 'r') as file:
            freq_lookup = {row.strip().split()[-1] : int(row.strip().split()[0]) for row in file.readlines()}

        self.run()

        raw_prediction = self.fetch()
        raw_prediction = raw_prediction.split('\n')
        start = raw_prediction.index(' Word-based means:') + 1
        end = raw_prediction.index(' First-fixation landing-site distributions:') - 1
        raw_prediction = raw_prediction[start : end]

        results = [[], [], [], [], []]

        for line in raw_prediction:
            row = line.split()
            freq = freq_lookup[row[-1]]
            freq_class = math.floor(math.log(freq, 10)) if freq != 0 else 0
            print(row)
            if metric == 'GD':
                GD = row[5]
                if GD != "Infinity":
                    results[freq_class].append(int(GD))
            elif metric == 'TT':
                TT = row[7]
                print(TT)
                if TT != "Infinity":
                    results[freq_class].append(int(TT))

        predicted = []
        for clss in results:
            predicted.append(np.array(clss).mean())

        return predicted


# ezreader = EZReader()
# ezreader.set_params(a1=125, a2=20, psi=3)
# print(ezreader.get_params())
# for _ in range(5):
#     ezreader.run()
#     print(ezreader.get_prediction())