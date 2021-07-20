import numpy as np
import pandas as pd

import json

with open('datasets/participants.txt', 'r') as file:
    participants = json.loads(file.read())

class Word:
    #class for word data
    def __init__(self, file, line_part, text):
        
        self._file = file
        
        tokenized_line_part = line_part.split()
        self._line = int(tokenized_line_part[1])
        self._part = int(tokenized_line_part[3])
        self._text = text
        
        self._participants = []
        self._durations = []
        self._start_times = []
        self._length = -1
        self._freq = -1
        self._predict = -1
        
        
    def add(self, participant, duration, start_time, length, freq, predict, level):
        
        self._participants.append(participant)
        self._durations.append(int(duration))
        self._start_times.append(int(start_time))
        self._length = int(float(length))
        self._freq = int(float(freq))
        self._predict = float(predict)
        self._level = level
        
    
    def get_basic_info(self):
        
        return self._file + "  " +\
        str(self._line) + "  " +\
        str(self._part) + "  " +\
        self._text + "  " +\
        str(self._participants) + "  " +\
        str(self._durations) + "  " +\
        str(self._start_times) + "  " +\
        str(self._length) + "  " +\
        str(self._freq) + "  " +\
        str(self._predict)
        
    def get_text(self):
        return self._text
    
    def get_freq(self):
        return self._freq
    
    def get_pred(self):
        return round(self._predict, 2)
    
    def get_leng(self):
        return self._length
    
    def get_subject(self):
        return self._participants[0]
    
    def get_level(self):
        return self._level
        
        
    def single_fix_duration(self):
        #duration of first_fixation with cases where only one fixation is made.
        result = []

        for index, person in enumerate(self._participants):
            if self._is_unique(person, self._participants):
                result.append(self._durations[index])

        return round(np.mean(result), 2)


    def first_fix_duration(self):
        #duration of first fixation on word.
        fixations_durations = []
        
        for person in self._unique_participants():
            fixations_durations.append(self._durations[self._find_first_fix(self._get_index_of_person_fixations(person))])
        
        return round(np.mean(fixations_durations), 2)
        

    def gaze_duration(self):
        #sum of all fixations on word n before moving to n+1.
        gaze_durations = []
        
        for person in self._unique_participants():
            gaze_durations.append(self._person_gaze_duation(self._get_index_of_person_fixations(person)))
        
        return round(np.mean(gaze_durations), 2)


    def total_time(self):
        #sum of all fixations and regressions.
        return round(np.sum(self._durations) / len(self._unique_participants()), 2)


    def fix_probability(self):
        #count of participants who made at least one fixation / count of participants in experiment
        return round(len(self._unique_participants()) / len(participants[self._file]), 2)


    def prob_of_one_fix(self):
        # count of participants who made only one fixation / count of participants in experiment
        count = 0

        for person in self._unique_participants():
            if self._is_unique(person, self._participants):
                count += 1

        return round(count / len(participants[self._file]), 2)


    def prob_of_2_or_more_fix(self):
        # count of participants who made 2 or more fixations / count of participants in experiment
        count = 0

        for person in self._unique_participants():
            if not self._is_unique(person, self._participants):
                count += 1

        return round(count / len(participants[self._file]), 2)


    def prob_of_skip(self):
        # count of participants who didn't make a fixation / count of participants in experiment

        skipper = 0

        for person in participants[self._file]:
            if person not in self._unique_participants():
                skipper += 1

        return round(skipper / len(participants[self._file]), 2)


    def _unique_participants(self):
        #returns the list of participants without repetition
        result = []

        for person in self._participants:
            if person not in result:
                result.append(person)

        return result


    def _is_unique(self, item, some_list):
        #returns true if there is only one of item in some_list
        count = 0

        for val in some_list:
            if val == item:
                count += 1

        if count == 1:
            return True
        return False
    
    
    def _get_index_of_person_fixations(self, someone):
        #returns the indexes of all fixations for one person
        
        result = []
        for index, person in enumerate(self._participants):
            if person == someone:
                result.append(index)
                
        return result
    
    def _find_first_fix(self, list_of_fix_indexes):
        #returns the index of the first fixation (temporaly)
        
        result = list_of_fix_indexes[0]
        first_fix = self._start_times[list_of_fix_indexes[0]]
        
        for index in list_of_fix_indexes:
            if self._start_times[index] < first_fix:
                first_fix = self._start_times[index]
                result = index
                
        return result
        
        
    def _person_gaze_duation(self, list_of_fix_indexes):
        #sum of all fixations on word n before moving to n+1.
        fix_durations = []
        
        time = self._start_times[list_of_fix_indexes[0]]
        
        for fix_index in list_of_fix_indexes:
            if self._start_times[fix_index] - time < 100:
                fix_durations.append(self._durations[fix_index])
                time = self._start_times[fix_index]
            
        return np.sum(fix_durations)
        
        