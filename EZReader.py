import subprocess


class EZReader:

    VALID_PARAMS = [
        'a1', 'a2', 'a3',
        'd', 'i', 'pf', 'itg', 'pftg',
        'm1', 'm2', 's', 'xi',
        'psi', 'o1', 'o2', 'e1', 'e2', 'v',
        'ep', 'a', 'l', 'sg'
    ]

    SIMULATIONS = ['wordivs', 'modelstates', 'tracefile', 'worddvs', 'distributions']

    def __init__(self, corpus="datasets/1_1_corpus.txt", target="datasets/1_1_target.txt", simulation="worddvs", num_subject=10):
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
        self.output = "outputs/result.txt"

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

        # for line in process.stdout.readlines():
        #     print(line)

    def fetch(self):
        """Fetch the result file

        Returns
        -------
        str
            output file
        """
        with open("EZReader/" + self.output, "r") as result:
            return result.read()


# ezreader = EZReader()
# ezreader.set_params(a1=125, a2=20, psi=3)
# print(ezreader.get_params())
# ezreader.run()
# print(ezreader.fetch())