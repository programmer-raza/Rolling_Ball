
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author HP
 */
public class Rolling_ball extends Application {

    @Override
    public void start(Stage primaryStage) {
        Ball ball_1 = new Ball();

        HBox hb1 = new HBox();
        hb1.setSpacing(
                5);

        //buttons
        Button Inc = new Button("Increase Obs");

        Inc.setOnAction(e
                -> {
            ball_1.increase();
        }
        );
        Button Dec = new Button("Decrease Obs");

        Dec.setOnAction(e
                -> {
            ball_1.decrease();
        }
        );
        Button IncUltra = new Button("Increase  10+ Obs");

        IncUltra.setOnAction(e
                -> {
            ball_1.increaseUltra();
        }
        );

        Inc.setFocusTraversable(false);
        Dec.setFocusTraversable(false);
        IncUltra.setFocusTraversable(false);

        //keyboards
        ball_1.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                ball_1.moveLeft();
            } else if (e.getCode() == KeyCode.RIGHT) {
                ball_1.moveRight();
            } else if (e.getCode() == KeyCode.SPACE) { // Use the SPACE key for jumping
                ball_1.jump();

            }
        }
        );

        hb1.setAlignment(Pos.CENTER);

        hb1.getChildren()
                .addAll(Inc, Dec, IncUltra);
        // Create a VBox for gravity controls

        ball_1.getChildren()
                .add(hb1);

        ball_1.setOnMouseClicked(event -> {
            // Request focus for the pane when it's clicked
            ball_1.requestFocus();
        });

        Scene scene = new Scene(ball_1, 800, 550);
        ball_1.requestFocus(); // Request focus on the Ball pane
        ball_1.setFocusTraversable(true);
        primaryStage.setTitle(
                "Ball Collision");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }

}

class Obstacles extends Pane {

    private int highScore = 0;
    boolean round = true;

    int radius = 40;
    int score = 0;
    boolean gameOver = false;
    Line line;
    double GravityY = 0.5;
    double GravityX = 0.1;
    final int RectH = 50;
    final int RectW = 10;

    int rectSpeed = 3;
    double ball_x = 800;
    double ball_y = 300;
    int Cx = 200;
    int Cy = 308;

    Circle c1 = new Circle(Cx, Cy, radius);
    Rectangle r1 = new Rectangle(ball_x, ball_y, RectW, RectH);

    Timeline t1;
    Timeline t2;
    Label ScoreLabel;

    public Obstacles() {

        line = new Line(0, ball_y + RectH + 2, 800, ball_y + RectH + 2);
        line.setStroke(Color.BLACK);

        ScoreLabel = new Label("Score: " + score);

        VBox ScoreBox = new VBox();
        ScoreBox.setAlignment(Pos.TOP_RIGHT);
        ScoreBox.setSpacing(10);

        ScoreBox.setLayoutX(600);
        ScoreBox.setLayoutY(50);

        ScoreBox.getChildren().addAll(ScoreLabel);

        c1.setFill(Color.BLUE);
        c1.setStroke(Color.BLACK);

        r1.setFill(Color.GREEN);

        getChildren().addAll(c1, ScoreBox, r1, line);

        t1 = new Timeline(new KeyFrame(Duration.millis(16), e -> moveRect()));
        t1.setCycleCount(Timeline.INDEFINITE);
        t1.play();

    }

    public void moveRect() {

        if (r1.getX() <= 0) {
            newObstacle();

        } else {
            ball_x -= rectSpeed;

            r1.setX(ball_x);
        }
        checkColision();

    }

    public void increase() {
        if (rectSpeed < 8) {
            rectSpeed += 1;
        }
        if (GravityY > 0.4) {

            setGravityY(-0.01);
        }
        if (GravityX < 0.2) {

            setGravityX(0.05);
        }

    }

    public void decrease() {
        if (rectSpeed > 1) {
            rectSpeed -= 1;
        }

        if (GravityX > 0.1) {

            setGravityX(-0.01);
        }

        if (GravityY < 0.5) {
            setGravityY(0.1);
        }

    }

    public void increaseUltra() {
        rectSpeed += 10;
    }

    public void setGravityY(double val) {
        GravityY += val;

    }

    public void setGravityX(double val) {
        GravityX += val;

    }

    private void showGameOverScreen() {
        // Create a game over scene
        t1.pause();
        t2.pause();
        setHighScore();
        VBox gameOverPane = new VBox(10);
        gameOverPane.setAlignment(Pos.CENTER);

        Label gameOverLabel = new Label("Game Over!\nHighest score : " + highScore + "\n your Score : " + score);
        Button retryButton = new Button("Retry");
        Button quitButton = new Button("Quit");

        retryButton.setOnAction(event -> {
            // Close the game over stage
            Stage gameOverStage = (Stage) retryButton.getScene().getWindow();
            gameOverStage.close();

            // Reset the game
            resetGame();
        });
        quitButton.setOnAction(event -> {
            System.exit(0);
        });

        gameOverPane.getChildren().addAll(gameOverLabel, retryButton, quitButton);

        Scene gameOverScene = new Scene(gameOverPane, 300, 200);
        Stage gameOverStage = new Stage();
        gameOverStage.setTitle("Game Over");
        gameOverStage.setScene(gameOverScene);
        gameOverStage.show();
    }

    private void handleCollision() {
        // Game over logic
        gameOver = true;
    }

    private void resetGame() {
        // Reset game variables and restart the animation
        gameOver = false;
        Cy = 308;
        Cx = 200;
        c1.setCenterX(Cx);
        c1.setCenterY(Cy);
        newObstacle();
        score = 0;
        ScoreLabel.setText("Score : " + score);

        t2.play();
        t1.play();
    }

    public void checkColision() {
        if (c1.getBoundsInParent()
                .intersects(r1.getBoundsInParent())) {
            // Handle the collision here

            handleCollision();

        }

        if (gameOver) {
            System.out.println("screen");
            showGameOverScreen();

        }

    }

    public void newObstacle() {
        ball_x = getWidth();
        r1.setX(ball_x);
        round = true;
    }

    public void setHighScore() {
        if (score > highScore) {
            highScore = score;
        }
    }
}

/// ball class
class Ball extends Obstacles {

    private double velocityY = 0; // Initial y-velocity
    private double velocityX = 0;

    public Ball() {
        super();

        t2 = new Timeline(new KeyFrame(Duration.millis(16), e -> jumpPos()));
        t2.setCycleCount(Timeline.INDEFINITE);

    }

    public void jump() {

        if (Cy == 308) {
            velocityY = -10; // Set the initial y-velocity for the jump
            velocityX = 2;

            t2.play();
        }
    }

    public void jumpPos() {

        // Update the ball's position based on velocity
        if (!gameOver) {
            Cy += velocityY;
            c1.setCenterY(Cy);
            Cx += velocityX;
            c1.setCenterX(Cx);

            // Apply gravity to simulate falling
            velocityY += GravityY;
            velocityX += GravityX; //initial velocity
            checkColision();
            if ((c1.getCenterX() - radius) >= (r1.getX() + RectW / 2 + 1) && round == true) {

                score++;
                round = false;
                ScoreLabel.setText("Score : " + score);
                if (score % 10 == 0) {
                    increase();
                }

            }
            // Stop the animation when the ball hits the ground
            if (Cy >= 308) {
                Cy = 308; // Ensure the ball stays on the ground
                velocityY = 0; // Stop vertical velocity

                stopJumping();
            }

        }
    }

    public void moveLeft() {
        if (c1.getCenterX() > 20) {
            Cx -= 20;

            c1.setCenterX(Cx);
            checkColision();
        }

    }

    public void moveRight() {
        if (c1.getCenterX() < getWidth() - 20) {
            Cx += 20;

            c1.setCenterX(Cx);
            checkColision();
        }

    }

    public void stopJumping() {
        t2.pause();

    }

}
