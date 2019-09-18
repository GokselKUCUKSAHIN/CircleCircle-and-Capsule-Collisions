import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Vector;

public class Main extends Application
{

    public static ObservableList<Node> child;
    //
    private static final String title = "JellyBeanci";
    public static final int width = 1200;
    public static final int height = 800;
    private static Color backcolor = Color.rgb(51, 51, 51);
    public static final Point2D GRAVITY = new Point2D(0, 0.4);
    private boolean isGravityOn = false;

    private static Timeline update;
    //
    Ball selecetedBall = null;
    Vector<Ball> vecFakeBalls = new Vector<>();
    Vector<BallPairs> vecCollidingPairs = new Vector<>();
    Line force = new Line(300, 400, 600, 400);

    //
    @Override
    public void start(Stage stage) throws Exception
    {
        Pane root = new Pane();
        child = root.getChildren();

        //
        //Ball b1 = new Ball(width * 0.25, height * 0.5);
        //Ball b2 = new Ball(width * 0.75, height * 0.5);
        for (int i = 0; i < 25; i++)
        {
            Ball a = new Ball(Math.random() * width, Math.random() * height);
            a.setScale(Math.random() + 0.1);
        }
        for (int i = 0; i < Ball.balls.size(); i++)
        {
            child.add(Ball.balls.get(i).getNode());
        }
        //
        for (int i = 0; i < 5; i++)
        {
            new Capsule(Math.random() * width, Math.random() * height, Math.random() * width, Math.random() * height);
        }
        for (int i = 0; i < Capsule.capsules.size(); i++)
        {
            child.add(Capsule.capsules.get(i).getBody());
        }
        //
        force.setStrokeWidth(4);//default
        force.setVisible(false);//default
        child.add(force);
        //
        root.setOnDragDetected(e->root.startFullDrag());
        //SELECT EVENT
        for (Ball ball : Ball.balls)
        {
            ball.getNode().setOnMouseClicked(e -> {
                this.selecetedBall = ball;
            });
        }

        //RELEASE EVENT
        for (Ball ball : Ball.balls)
        {
            ball.getNode().setOnMouseReleased(e -> {
                if (e.getButton() == MouseButton.SECONDARY)
                {
                    double mX = e.getSceneX();
                    double mY = e.getSceneY();
                    ball.setVel((ball.getX() - mX) * 0.15, (ball.getY() - mY) * 0.15);
                    force.setVisible(false);
                }
            });
        }

        //DRAG EVENT
        for (Ball ball : Ball.balls)
        {
            ball.getNode().setOnMouseDragged(e -> {
                if (e.getButton() == MouseButton.SECONDARY)
                {
                    force.setStartX(ball.getX());
                    force.setStartY(ball.getY());
                    force.setEndX(e.getSceneX());
                    force.setEndY(e.getSceneY());

                    // Calculate Distance
                    double clr = Utils.distance(force.getStartX(), force.getStartY(), force.getEndX(), force.getEndY());
                    clr = Utils.map(clr, 0, 1200, 160, 0);
                    force.setStroke(Color.hsb(clr, 1, 1));
                    force.setVisible(true);
                } else if (e.getButton() == MouseButton.PRIMARY)
                {
                    ball.setPos(e.getSceneX(), e.getSceneY());
                }
            });
            ball.getNode().setOnMouseDragEntered(e->{
                ball.setFallable(false);
                ball.setVel(0,0);
            });
            ball.getNode().setOnMouseDragExited(e->{
                ball.setFallable(isGravityOn);
            });
        }

        //Capsule DRAG EVENTS
        for (Capsule capsule : Capsule.capsules)
        {
            capsule.getStart().setOnMouseDragged(e -> {
                if (e.getButton() == MouseButton.PRIMARY)
                {
                    capsule.setStart(e.getX(), e.getY());
                }
            });
            capsule.getEnd().setOnMouseDragged(e -> {
                if (e.getButton() == MouseButton.PRIMARY)
                {
                    capsule.setEnd(e.getX(), e.getY());
                }
            });
        }

        //
        root.setOnKeyPressed(e -> {
            switch (e.getCode())
            {
                case F1:
                {
                    //PLAY
                    update.play();
                    break;
                }
                case F2:
                {
                    //PAUSE
                    update.pause();
                    break;
                }
                case F3:
                {
                    update.setRate(update.getRate() - 1);
                    System.out.println(update.getRate());
                    break;
                }
                case F4:
                {
                    update.setRate(update.getRate() + 1);
                    System.out.println(update.getRate());
                    break;
                }
                case F9:
                {
                    gravityOn();
                    break;
                }
                case F10:
                {
                    gravityOff();
                    break;
                }
                default:
                {
                    break;
                }
            }
        });

        update = new Timeline(new KeyFrame(Duration.millis(1000), e -> {

            // Remove Collisions
            vecCollidingPairs.clear();
            // Static Collision
            for (Ball ball : Ball.balls)
            {
                ball.update();
            }
            for (Ball ball : Ball.balls)
            {
                // Against Edges
                for (Capsule edge : Capsule.capsules)
                {
                    double x1 = edge.getEndX() - edge.getStartX();
                    double y1 = edge.getEndY() - edge.getStartY();

                    double x2 = ball.getX() - edge.getStartX();
                    double y2 = ball.getY() - edge.getStartY();

                    double edgeLength = Math.pow(x1, 2) + Math.pow(y1, 2);

                    double t = Math.max(0, Math.min(edgeLength, (x1 * x2 + y1 * y2))) / edgeLength;

                    double closestPointX = edge.getStartX() + t * x1;
                    double closestPointY = edge.getStartY() + t * y1;

                    double distEdge = Math.sqrt((ball.getX() - closestPointX) * (ball.getX() - closestPointX) + (ball.getY() - closestPointY) * (ball.getY() - closestPointY));

                    if (distEdge <= (ball.getRadius() + edge.getRadius()))
                    {
                        // Static collision has occured
                        Ball fakeball = new Ball(false);
                        fakeball.setRadius(edge.getRadius());
                        fakeball.setMass(ball.getMass() * 1.0);
                        fakeball.setX(closestPointX);
                        fakeball.setY(closestPointY);
                        fakeball.setVel(-ball.getVelX(), -ball.getVelY());

                        vecFakeBalls.add(fakeball);
                        vecCollidingPairs.add(new BallPairs(ball, fakeball));

                        double overl = 1.0 * (distEdge - ball.getRadius() - fakeball.getRadius());

                        // Displace Current Ball away from collision
                        ball.setX(ball.getX() - (overl * (ball.getX() - fakeball.getX()) / distEdge));
                        ball.setY(ball.getY() - (overl * (ball.getY() - fakeball.getY()) / distEdge));
                    }
                }

                // Against Other Balls
                for (Ball target : Ball.balls)
                {
                    if (ball.getId() != target.getId())
                    {
                        if (Utils.isHit(ball, target))
                        {
                            // Collision has occured
                            vecCollidingPairs.add(new BallPairs(ball, target));
                            // Distance between ball centers
                            double distance = Utils.distance(ball, target);
                            double overlap = 0.5 * (distance - ball.getRadius() - target.getRadius());

                            // Displace Current Ball
                            ball.setX(ball.getX() - ((overlap * (ball.getX() - target.getX()) / distance)));
                            ball.setY(ball.getY() - ((overlap * (ball.getY() - target.getY()) / distance)));

                            // Displace Targer Ball
                            target.setX(target.getX() + ((overlap * (ball.getX() - target.getX()) / distance)));
                            target.setY(target.getY() + ((overlap * (ball.getY() - target.getY()) / distance)));
                        }
                    }
                }
            }
            // Now work out dynamic collisions
            for (BallPairs c : vecCollidingPairs)
            {
                Ball b1 = c.getB1();
                Ball b2 = c.getB2();

                // Distance between balls
                double dist = Utils.distance(b1, b2);

                // Normal
                double nx = (b2.getX() - b1.getX()) / dist;
                double ny = (b2.getY() - b1.getY()) / dist;

                // Tangent
                double tx = -ny;
                double ty = nx;

                // Dot Product Tangent
                double dpTan1 = b1.getVelX() * tx + b1.getVelY() * ty;
                double dpTan2 = b2.getVelX() * tx + b2.getVelY() * ty;

                // Dot Product Normal
                double dpNorm1 = b1.getVelX() * nx + b1.getVelY() * ny;
                double dpNorm2 = b2.getVelX() * nx + b2.getVelY() * ny;

                // Conservation of momentum in 1D
                double m1 = (dpNorm1 * (b1.getMass() - b2.getMass()) + 2.0 * b2.getMass() * dpNorm2) / (b1.getMass() + b2.getMass());
                double m2 = (dpNorm2 * (b2.getMass() - b1.getMass()) + 2.0 * b1.getMass() * dpNorm1) / (b1.getMass() + b2.getMass());

                // Update ball velocities

                b1.setVel(tx * dpTan1 + nx * m1, ty * dpTan1 + ny * m1);
                b2.setVel(tx * dpTan2 + nx * m2, ty * dpTan2 + ny * m2);
            }

            // Remove fake balls
            vecFakeBalls.clear();

        }));

        update.setCycleCount(Timeline.INDEFINITE);
        update.setRate(60);
        update.setAutoReverse(false);
        update.play(); //uncomment for play when start
        //
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(new Scene(root, width - 10, height - 10, backcolor));
        stage.show();
        root.requestFocus();
    }

    private void gravityOn()
    {
        isGravityOn = true;
        for (Ball ball : Ball.balls)
        {
            ball.setFallable(true);
        }
    }

    private void gravityOff()
    {
        isGravityOn = false;
        for (Ball ball : Ball.balls)
        {
            ball.setFallable(false);
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
