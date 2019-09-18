import javafx.geometry.Point2D;

public class Utils
{

    public static boolean isHit(Ball b1, Ball b2)
    {

        double difX = b2.getX() - b1.getX();
        double difY = b2.getY() - b1.getY();
        return ((difX * difX) + (difY * difY)) <= (Math.pow(b1.getRadius() + b2.getRadius(), 2));
    }

    public static double distance(Ball b1, Ball b2)
    {
        return Math.sqrt(Math.pow(b2.getX() - b1.getX(), 2) + Math.pow(b2.getY() - b1.getY(), 2));
    }

    public static double distance(Point2D p1, Point2D p2)
    {
        return distance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public static double distance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    static double span(Point2D vector)
    {
        return Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2));
    }

    static Point2D endPoint(Point2D point, double angle, double size)
    {
        return endPoint(point.getX(), point.getY(), angle, size);
    }

    static Point2D endPoint(double x, double y, double angle, double size)
    {
        x += (size * Math.cos(angleToRadian(angle)));
        y -= (size * Math.sin(angleToRadian(angle)));
        return new Point2D(x, y);
    }

    static double calculateAngle(double p1X, double p1Y, double p2X, double p2Y)
    {
        double dx = Math.abs(p1X - p2X);
        double dy = Math.abs(p1Y - p2Y);
        //System.out.println(dx+": "+dy);
        double a = radianToAngle(Math.atan(dy / dx));
        if (p1X - p2X >= 0)
        {
            //II - III
            if (p1Y - p2Y >= 0)
            {
                //II Region
                return 180 - a;
            } else
            {
                //III Region
                return 180 + a;
            }
        } else
        {
            // I - IV
            if (p1Y - p2Y >= 0)
            {
                //I Region
                return a;
            } else
            {
                //IV Region
                return 360 - a;
            }
        }
    }

    static double angleToRadian(double angle)
    {
        return (Math.PI / 180.0) * angle;
    }

    static double radianToAngle(double radian)
    {
        return radian * (180.0 / Math.PI);
    }

    static double map(double x, double in_min, double in_max, double out_min, double out_max)
    {
        //double map
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

}
