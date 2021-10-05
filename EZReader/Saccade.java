// This class instantiates the random- and systematic-error components of saccades.

import java.util.*;

public class Saccade {

    // Initialize random # generator:
    Random r = new Random();

    //**************************************************************************

    double randomError(double intendedSaccade) {

        double result = r.nextGaussian() * (EZReader10.Eta1 + (Math.abs(intendedSaccade) * EZReader10.Eta2));
        return result;
    }

    //**************************************************************************

    double systematicError(double intendedSaccade, double launchSiteFixDur) {

        double result = (EZReader10.Psi - Math.abs(intendedSaccade)) * ((EZReader10.Omega1 - Math.log(launchSiteFixDur)) /  EZReader10.Omega2);
        if (intendedSaccade < 0) result *= -1.0;
        return result;
    }
}
