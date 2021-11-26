import numpy as np
import pandas as pd
from tqdm import tqdm
from EZReader import EZReader
import math
from itertools import product

ezreader = EZReader(corpus="datasets/1_1_corpus.txt",
                    target="datasets/1_1_target.txt",
                    simulation="worddvs",
                    num_subject=314,
                    output='outputs/result3.txt')

# a1 = np.arange(50, 500, 3)
# a2 = np.arange(1, 5, 0.5)
# a3 = np.arange(0, 20, 4)
# d = np.arange(0.1, 0.6, 0.1)

# i = np.arange(5, 50, 10)
# pf = np.arange(0.01, 0.15, 0.03)

a1 = np.arange(150, 450, 3)
a2 = np.arange(1, 5, 0.5)
a3 = np.arange(0, 20, 4)
d = np.arange(0.1,0.6,0.2)

i = np.arange(5, 50, 10)
pf = np.arange(0.01, 0.11, 0.05)

params = product(a1, a2, a3, d, i, pf)

params = list(params)[80000:]

index = 80000
results = pd.DataFrame(
    columns=['index', 'SFD', 'FFD', 'GD', 'TT', 'PrF', 'Pr1', 'Pr2', 'PrS', 'token'])

for param in tqdm(list(params)):
    a1, a2, a3, d, i, pf = param
    ezreader.clear_params()
    ezreader.set_params(a1=a1, a2=a2, a3=a3, d=d, i=i, pf=pf)
    result_per_param_set = ezreader.generate_prediction(5)
    result_per_param_set['index'] = index
    results = results.append(result_per_param_set, ignore_index=True)
    index += 1

results.to_csv('Result3.csv')

