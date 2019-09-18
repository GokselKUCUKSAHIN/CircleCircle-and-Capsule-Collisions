import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Capsule
{

    public static ArrayList<Capsule> capsules = new ArrayList<>();

    private Color borderColor = Color.LIGHTCORAL;
    private Color transparent = Color.gray(0, 0);
    private Group body = new Group();
    //
    private double radius = 10;
    private double strWidth = 4;
    private Circle start = new Circle(0, 0, radius);
    private Circle end = new Circle(0, 0, radius);
    private Line testLine = new Line();
    private Line edgeA = new Line();
    private Line edgeB = new Line();
    //
    private double nx = 0;
    private double ny = 0;
    private double d = 0;
    //
    private int id;

    public Capsule()
    {
        start.setFill(transparent);
        end.setFill(transparent);
        //
        start.setStrokeWidth(strWidth);
        end.setStrokeWidth(strWidth);
        testLine.setStrokeWidth(strWidth);
        edgeA.setStrokeWidth(strWidth);
        edgeB.setStrokeWidth(strWidth);
        //
        start.setStroke(borderColor);
        end.setStroke(borderColor);
        testLine.setStroke(borderColor);
        edgeA.setStroke(borderColor);
        edgeB.setStroke(borderColor);
        //
        body.getChildren().addAll(edgeA, edgeB,start, end);
        capsules.add(this);
        this.id = capsules.size();
    }

    public Capsule(Point2D start, Point2D end)
    {
        this();
        setStart(start);
        setEnd(end);
        calculate();
    }

    public Capsule(double sx, double sy, double ex, double ey)
    {
        this();
        setStart(sx, sy);
        setEnd(ex, ey);
        calculate();
    }

    public void setStart(Point2D start)
    {
        setStart(start.getX(), start.getY());
    }

    public void setEnd(Point2D end)
    {
        setStart(end.getX(), end.getY());
    }

    public void setStart(double x, double y)
    {
        setStartX(x);
        setStartY(y);
    }

    public void setEnd(double x, double y)
    {
        setEndX(x);
        setEndY(y);
    }

    public void setStartX(double sx)
    {
        this.start.setCenterX(sx);
        testLine.setStartX(sx);
        calculate();
    }

    public void setStartY(double sy)
    {
        this.start.setCenterY(sy);
        testLine.setStartY(sy);
        calculate();
    }

    public void setEndX(double ex)
    {
        this.end.setCenterX(ex);
        testLine.setEndX(ex);
        calculate();
    }

    public void setEndY(double ey)
    {
        this.end.setCenterY(ey);
        testLine.setEndY(ey);
        calculate();
    }

    public double getStartX()
    {
        return this.start.getCenterX();
    }

    public double getStartY()
    {
        return this.start.getCenterY();
    }

    public double getEndX()
    {
        return this.end.getCenterX();
    }

    public double getEndY()
    {
        return this.end.getCenterY();
    }

    public double getRadius()
    {
        return this.radius;
    }

    public int getId()
    {
        return this.id;
    }

    public Group getBody()
    {
        return this.body;
    }

    private void calculate()
    {
        nx = -(getEndY() - getStartY());
        ny = (getEndX() - getStartX());
        d = Math.sqrt(nx * nx + ny * ny);
        nx /= d;
        ny /= d;

        edgeA.setStartX(getStartX() + nx * getRadius());
        edgeA.setStartY(getStartY() + ny * getRadius());
        edgeA.setEndX(getEndX() + nx * getRadius());
        edgeA.setEndY(getEndY() + ny * getRadius());

        edgeB.setStartX(getStartX() - nx * getRadius());
        edgeB.setStartY(getStartY() - ny * getRadius());
        edgeB.setEndX(getEndX() - nx * getRadius());
        edgeB.setEndY(getEndY() - ny * getRadius());
    }

    public Circle getStart()
    {
        return this.start;
    }

    public Circle getEnd()
    {
        return this.end;
    }

}
