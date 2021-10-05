// This class implements gamma distributions.

import java.util.*;

public class Gamma {

    // Initialize classes:
    Random r = new Random();

    public double nextDouble(double mean, double stdDev) {
        double a, b, c, d, q, t, u1, u2, v, w, yy, z;
        double Beta;
        boolean done = false;
        double xx = 0;
        a = (2.0 * stdDev) - 1.0;
        a = Math.sqrt(1.0 / a);
        b = stdDev - Math.log(4.0);
        q = stdDev + (1.0 / a);
        t = 4.5;
        d = 1.0f + Math.log(t);
        Beta = mean / stdDev;
        while (!done) {
            u1 = r.nextDouble();
            u2 = r.nextDouble();
            v = a * Math.log(u1 / (1.0001 - u1));
            yy = stdDev * Math.exp(v);
            z = u1 * u1 * u2;
            w = b + (q * v) - yy;
            if ((w + d - (t * z)) >= 0) {
                xx = yy;
                done = true;
            }
            else if (w >= Math.log(z)) {
                xx = yy;
                done = true;
            }
            else done = false;
        }
        return(Beta * xx);
    }
}
