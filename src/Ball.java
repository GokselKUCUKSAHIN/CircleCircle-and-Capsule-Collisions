import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;


public class Ball
{

    public static ArrayList<Ball> balls = new ArrayList<>();

    private Color borderColor = Color.SNOW;
    private Color transparent = Color.gray(0, 0);
    private Circle body = new Circle();

    private Point2D vel = new Point2D(0, 0);
    private Point2D acc = new Point2D(0, 0);
    private double radius = 40;
    private double scale = 1;
    private double mass;
    private int id;
    private boolean isFallable = false;

    public Ball()
    {
        body.setStroke(borderColor);
        body.setFill(transparent);
        body.setStrokeWidth(1.5);
        body.setRadius(radius * scale);
        mass = getRadius() * 5;
        balls.add(this);
        this.id = balls.size();
        //System.out.println("non fake");
    }

    public Ball(boolean fake)
    {
        body.setStroke(borderColor);
        body.setFill(transparent);
        body.setStrokeWidth(1.5);
        body.setRadius(radius * scale);
        mass = getRadius() * 5;
        this.id = balls.size();
    }

    public Ball(Point2D pos)
    {
        this();
        setPos(pos);
    }

    public Ball(double x, double y)
    {
        this();
        setPos(x, y);
    }

    public Circle getNode()
    {
        return this.body;
    }

    public void setScale(double scale)
    {
        this.scale = scale;
        body.setRadius(radius * scale);
        mass = getRadius() * 5;
    }

    public int getId()
    {
        return id;
    }

    public Point2D getPos()
    {
        return new Point2D(body.getCenterX(), body.getCenterY());
    }

    public double getX()
    {
        return this.body.getCenterX();
    }

    public double getY()
    {
        return this.body.getCenterY();
    }

    public void setX(double x)
    {
        this.body.setCenterX(x);
    }

    public void setY(double y)
    {
        this.body.setCenterY(y);
    }

    public double getRadius()
    {
        return this.body.getRadius();
    }

    public void setPos(Point2D pos)
    {
        setX(pos.getX());
        setY(pos.getY());
    }

    public void setPos(double x, double y)
    {
        setX(x);
        setY(y);
    }

    public void setVel(Point2D vel)
    {
        this.vel = vel;
    }

    public void setVel(double vx, double vy)
    {
        setVel(new Point2D(vx, vy));
    }

    public double getVelX()
    {
        return this.vel.getX();
    }

    public double getVelY()
    {
        return this.vel.getY();
    }

    private void edges()
    {
        if (getX() < 0)
        {
            setX(getX() + Main.width);
        }
        if (getX() >= Main.width)
        {
            setX(getX() - Main.width);
        }
        if (getY() < 0)
        {
            setY(getY() + Main.height);
        }
        if (getY() >= Main.height)
        {
            setY(getY() - Main.height);
        }
    }

    private void checkVel()
    {
        if (Utils.span(vel) < 0.1)
        {
            vel = vel.multiply(0);
        }
    }

    public void update()
    {
        acc = (vel.multiply(-0.035));
        if (isFallable)
        {
            acc = acc.add(Main.GRAVITY);
        }
        vel = vel.add(acc);
        setPos(getPos().add(vel));
        edges();
        checkVel();
    }

    public double getMass()
    {
        return this.mass;
    }

    public void setRadius(double radius)
    {
        this.body.setRadius(radius);
        this.radius = radius;
    }

    public void setMass(double mass)
    {
        this.mass = mass;
    }

    public void setAcc(Point2D gravity)
    {
        this.acc = this.acc.add(gravity);
    }

    public void setFallable(boolean statement)
    {
        isFallable = statement;
    }

    public boolean isFallable()
    {
        return this.isFallable;
    }
}
