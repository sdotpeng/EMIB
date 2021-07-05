import math
import pandas as pd
import conversion as conv
import statistics

import emip_toolkit as tk


def process_GazeBase(filename):
    df = pd.read_csv(filename)

    df['x_pixel'] = df['x'].apply(conv.dva2pixel_X)
    df['y_pixel'] = df['y'].apply(conv.dva2pixel_Y)

    df = df[df.val == 0]

    data = df[['n', 'x_pixel', 'y_pixel']].to_numpy()

    return data


def filter_fixations(data, minimum_duration=50, sample_duration=4, maximum_dispersion=25):
    # clears fixations in case called multiple times
    fixations = {}
    saccades = {}

    order = -1

    # create moving window based on minimum_duration
    window_size = int(math.ceil(minimum_duration / sample_duration))

    window_x = []
    window_y = []

    # go over all SMPs in trial data
    for sample in data:

        # get x and y for each sample (right eye only)
        # [23] R POR X [px]	 '0.00',
        # [24] R POR Y [px]	 '0.00',
        timestamp, x_cord, y_cord = sample

        # filter (skip) coordinates outside of the screen 1920×1080 px
        if x_cord < 0 or y_cord < 0 or x_cord > 1680 or y_cord > 1050:
            continue

        # adding sample if it appears to be valid
        window_x.append(x_cord)
        window_y.append(y_cord)

        # calculate dispersion = [max(x) - min(x)] + [max(y) - min(y)]
        dispersion = (max(window_x) - min(window_x)) + \
                     (max(window_y) - min(window_y))

        # if dispersion is above maximum_dispersion
        if dispersion > maximum_dispersion:

            # then the window does not represent a fixation
            # pop last item in window
            window_x.pop()
            window_y.pop()

            # add fixation to fixations if window is not empty (size >= window_size)
            if len(window_x) == len(window_y) and len(window_x) > window_size:
                # print("duration", len(window_x)*4, "is average of", len(window_x), "samples")

                # the fixation is registered at the centroid of the window points
                # trial, participant, timestamp, duration, x_cord, y_cord, token
                order += 2
                fixations[order] = tk.Fixation(1,
                                               '001',
                                               timestamp,
                                               len(window_x),
                                               statistics.mean(window_x),
                                               statistics.mean(window_y),
                                               "",
                                               0)

            # clear window
            window_x = []
            window_y = []

    for saccade_order in range(2, order, 2):
        timestamp = fixations[saccade_order - 1].timestamp
        x_cord = fixations[saccade_order - 1].x_cord
        y_cord = fixations[saccade_order - 1].y_cord

        end_time = fixations[saccade_order + 1].timestamp
        x1_cord = fixations[saccade_order + 1].x_cord
        y1_cord = fixations[saccade_order + 1].y_cord

        duration = end_time - timestamp

        saccades[saccade_order] = tk.Saccade(1,
                                             "001",
                                             timestamp,
                                             duration,
                                             x_cord,
                                             y_cord,
                                             x1_cord,
                                             y1_cord,
                                             0,
                                             0)

    return fixations, saccades


def original_filter_fixations(self, minimum_duration=50, sample_duration=4, maxmimum_dispersion=25):
    '''based on page 296 of eye tracker manual:
    https://psychologie.unibas.ch/fileadmin/user_upload/psychologie/Forschung/N-Lab/SMI_iView_X_Manual.pdf

            minimum_duration : int (optional)
            minimum duration for a fixation in milliseconds, less than minimum is considered noise.
            set to 50 milliseconds by default.

            sample_duration : int (optional)
            Sample duration in milliseconds, this is 4 milliseconds based on this eye tracker.

            maxmimum_dispersion : int (optional)
            maximum distance from a group of samples to be considered a single fixation.
            Set to 25 pixels by default.


    notes:
    remember that some data is MSG for mouse clicks.
    some records are invalid with value -1.
    read right eye data only.
    '''

    # clears fixations in case called multiple times
    self.fixations.clear()

    # create moving window based on minimum_duration
    window_size = int(math.ceil(minimum_duration / sample_duration))

    window_x = []
    window_y = []

    # go over all SMPs in trial data
    for sample in self.trial_data:

        # filter MSG samples if any exist, or R eye is inValid
        if sample[1] != "SMP" or sample[27] == "-1":
            continue

        # get x and y for each sample (right eye only)
        # [23] R POR X [px]	 '0.00',
        # [24] R POR Y [px]	 '0.00',
        x_cord, y_cord = float(sample[23]), float(sample[24])

        # filter (skip) cordinates outside of the screen 1920×1080 px
        if x_cord < 0 or y_cord < 0 or x_cord > 1920 or y_cord > 1080:
            continue

        # adding sample if it appears to be valid
        window_x.append(x_cord)
        window_y.append(y_cord)

        # calculate dispersion = [max(x) - min(x)] + [max(y) - min(y)]
        dispersion = (max(window_x) - min(window_x)) + \
                     (max(window_y) - min(window_y))

        # if dispersion is above maxmimum_dispersion
        if dispersion > maxmimum_dispersion:

            # then the window does not represent a fixation
            # pop last item in window
            window_x.pop()
            window_y.pop()

            # add fixation to fixations if window is not empty (size >= window_size)
            if len(window_x) == len(window_y) and len(window_x) > window_size:
                # print("duration", len(window_x)*4, "is average of", len(window_x), "samples")

                # the fixation is registered at the centroid of the wondow points
                # trial, participant, timestamp, duration, x_cord, y_cord, token
                self.fixations.append(Fixation(self.trial_ID,
                                               self.trial_participant,
                                               sample[0],
                                               len(window_x) * 4,
                                               statistics.mean(window_x),
                                               statistics.mean(window_y),
                                               "na"))

            # clear window
            window_x.clear()
            window_y.clear()

        # if dispersion is below maxmimum_dispersion
        # then the window represents a fixation
        # in this case the window expands to the right until
        # the wondow dispersion is above threshold


data = process_GazeBase('S_1001_S1_TEX.csv')
filter_fixations(data)
