package ca.bcit.comp2522.termproject.idk.ui;

import ca.bcit.comp2522.termproject.idk.sound.Sound;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.view.KeyView;
import com.almasb.fxgl.input.view.MouseButtonView;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.scene.input.KeyCode.*;


/**
 * Represents the class for the factory of tiles.
 *
 * @author Nikolay Rozanov
 * @author Prince Chabveka
 * @version 2022
 */
public class GameMainMenu extends FXGLMenu {
    /**
     * Shows the game main menu.
     */

    private final VBox scoresRoot = new VBox(10); //to be implemented later
    private Node highScores; //to be implemented later
    private boolean isLoadedScore = false;
    private int userId;
    private boolean hasLoggedIn = false;


    /**
     * Constructs a main menu.
     */
    public GameMainMenu() {
        super(MenuType.MAIN_MENU);
        getContentRoot().getChildren().setAll(new Rectangle(getAppWidth(), getAppHeight()));
        Text title = constructTitle();
        getContentRoot().getChildren().addAll(title);
        VBox menuBox = constructMenuBox();
        constructHighScores(menuBox);
        Group loginRoot = constructLogin();
        getContentRoot().getChildren().addAll(menuBox, highScores, loginRoot);
    }


    private static class ColorBlock extends Rectangle {

        /**
         * Construct colorblock class.
         * @param size an int
         * @param color an object of type color
         */
        ColorBlock(final int size, final Color color) {
            super(size, size, color);
            setArcWidth(8);
            setArcHeight(8);
            setStrokeType(StrokeType.INSIDE);
            setStrokeWidth(2.5);
            setStroke(Color.color(0.138, 0.138, 0.375, 0.66));
        }
    }

    private void showCredits() {
        getDialogService().showMessageBox("Prince Chabveka\nNikolay Rozanov");
    }


    @Override
    public void onCreate() {
        if (isLoadedScore)
            return;
        isLoadedScore = true;
    }

    //TO BE CHANGED LATER
    private void instructions() {
        GridPane pane = new GridPane();
        if (!FXGL.isMobile()) {
            pane.setEffect(new DropShadow(5, 3.5, 3.5, Color.BLUE));
        }
        pane.setHgap(25);
        pane.setVgap(10);
        pane.addRow(0, getUIFactoryService().newText("Movement"), new HBox(4, new KeyView(W), new KeyView(S),
                new KeyView(A), new KeyView(D)));
        pane.addRow(1, getUIFactoryService().newText("Attack"), new MouseButtonView(MouseButton.PRIMARY));

        getDialogService().showBox("How to Play", pane, getUIFactoryService().newButton("OK"));
    }

    private static class MenuButton extends Parent {
        MenuButton(final String name, final Runnable action) {
            var text = getUIFactoryService().newText(name, Color.WHITE, 36.0);
            text.setStrokeWidth(1.5);
            text.strokeProperty().bind(text.fillProperty());

            text.fillProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(Color.BLUE)
                            .otherwise(Color.WHITE)
            );

            setOnMouseClicked(e -> action.run());

            setPickOnBounds(true);

            getChildren().add(text);
        }
    }

    private void startGame() {
        if (hasLoggedIn) {
            fireNewGame();
        } else {
            GridPane pane = new GridPane();
            if (!FXGL.isMobile()) {
                pane.setEffect(new DropShadow(5, 3.5, 3.5, Color.BLUE));
            }
            pane.setHgap(25);
            pane.setVgap(10);
            getDialogService().showBox("You must log in first.", pane, getUIFactoryService().newButton("OK"));
        }
    }

    private VBox constructMenuBox() {
        VBox menuBox = new VBox(
                5,
                new MenuButton("New Game", this::startGame), // startGame()
                new MenuButton("How to Play", this::instructions),
                new MenuButton("Credits", this::showCredits),
                new MenuButton("Exit", this::fireExit)
        );
        menuBox.setAlignment(Pos.TOP_CENTER);

        menuBox.setTranslateX(getAppWidth() / 2.0 - 125);
        menuBox.setTranslateY(getAppHeight() / 2.0 + 125);

        return menuBox;
    }

    private Text constructTitle() {
        Text title = getUIFactoryService().newText(getSettings().getTitle(), Color.WHITE, 46.0);

        title.setStroke(Color.WHITESMOKE);
        title.setStrokeWidth(1.5);
        centerTextBind(title, getAppWidth() / 2.0, 225);
        return title;
    }

    private void constructHighScores(VBox menuBox) {
        StackPane hsRoot = new StackPane(new Rectangle(450, 250,
                Color.color(0, 0, 0.2, 0.8)), scoresRoot);
        hsRoot.setAlignment(Pos.TOP_CENTER);
        hsRoot.setCache(true);
        hsRoot.setCacheHint(CacheHint.SPEED);
        hsRoot.setTranslateX(getAppWidth());
        hsRoot.setTranslateY(menuBox.getTranslateY());

        highScores = hsRoot;
    }

    private Group constructLogin() {
        Group root = new Group();
        TextField username = constructUsernameField();
        PasswordField password = constructPasswordField();
        Rectangle background = constructBackground();
        VBox loginBox = constructLoginBox();
        Label label = constructLoginLabel();
        constructAnimatedBlocks();
        constructCenterLine();
        Button btn = constructLoginButton(username, password, loginBox, label, background);
        loginBox.getChildren().addAll(label, username, password, btn);
        root.getChildren().addAll(background, loginBox);
        return root;
    }

    private void constructAnimatedBlocks() {
        Color color = Color.DARKBLUE;
        ArrayList<ColorBlock> blocks = new ArrayList<ColorBlock>();
        double blockStartX = getAppWidth() / 2.0 - 380;

        for (int i = 0; i < 15; i++) {
            ColorBlock upperBlock = new ColorBlock(40, color);
            upperBlock.setTranslateX(blockStartX + i * 50);
            upperBlock.setTranslateY(100);
            ColorBlock block = new ColorBlock(40, color);
            block.setTranslateX(blockStartX + i * 50);
            block.setTranslateY(220);
            blocks.add(upperBlock);
            getContentRoot().getChildren().add(upperBlock);
            blocks.add(block);
            getContentRoot().getChildren().add(block);
        }

        for (int i = 0; i < blocks.size(); i++) {
            ColorBlock block = blocks.get(i);
            animationBuilder()
                    .delay(Duration.seconds(i * 0.05))
                    .duration(Duration.seconds(0.5))
                    .repeatInfinitely()
                    .autoReverse(true)
                    .animate(block.fillProperty())
                    .from(color)
                    .to(color.brighter().brighter())
                    .buildAndPlay(this);
        }
    }

    private void constructCenterLine() {
        Line centeringLine = new Line(getAppWidth() / 2.0, 0, getAppWidth() / 2.0, getAppHeight());
        centeringLine.setStroke(Color.WHITE);
        scoresRoot.setPadding(new Insets(10));
        scoresRoot.setAlignment(Pos.TOP_LEFT);
    }



    private Rectangle constructBackground() {


        Rectangle background = new Rectangle(320, 250);
        background.setX(50);
        background.setY(getAppHeight() - 280);
        background.setArcHeight(15);
        background.setArcWidth(15);
        background.setFill(Color.rgb(0 , 0 , 0, 0.01));
//        background.setStroke(foreground);
        background.setStrokeWidth(1.5);

        return background;
    }

    private VBox constructLoginBox() {
        VBox vbox = new VBox(25);
        vbox.setTranslateX(50);
        vbox.setTranslateY(getAppHeight() - 280);
        vbox.setPadding(new Insets(10, 0, 0, 10));

        return vbox;
    }

    private Label constructLoginLabel() {
        Label label = new Label("Log in to play");
        label.setTextFill(Color.WHITESMOKE);
        label.setFont(new Font("SanSerif", 20));
        return label;
    }

    private TextField constructUsernameField() {
        TextField username = new TextField();
        username.setFont(Font.font("SanSerif", 20));
        username.setPromptText("Username");
        username.getStyleClass().add("field-background");
        return username;
    }

    private PasswordField constructPasswordField() {
        PasswordField password = new PasswordField();
        password.setFont(Font.font("SanSerif", 20));
        password.setPromptText("Password");
        password.getStyleClass().add("field-background");

        return password;
    }

    private Button constructLoginButton(final TextField username, final PasswordField password, final VBox loginBox,
                                        final Label label, final Rectangle background) {

        Button btn = new Button("Login");
        btn.setFont(Font.font("SanSerif", 15));
        btn.setOnAction(e -> {
            registerDriver();
            final Properties connectionProperties = setUpConnectionProperties();
            final String url = "jdbc:mysql://localhost:3306/comp2522";
            final Connection connection = establishConnection(url, connectionProperties);
            final Statement statement = establishStatement(connection);
            login(statement, username, password, loginBox, label, background, btn);
        });
        return btn;
    }

    private void registerDriver() {
        // We register the Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private Connection establishConnection(final String url, final Properties connectionProperties) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, connectionProperties);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (connection != null) {
            System.out.println("Successfully connected to MySQL database test");
        }

        return connection;
    }

    private Properties setUpConnectionProperties() {
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "root");
        connectionProperties.put("password", "12345");

        return connectionProperties;
    }

    private Statement establishStatement(final Connection connection) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            statement.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return statement;
    }

    private void login(final Statement statement, final TextField username, final PasswordField password,
                      final VBox loginBox, final Label label, final Rectangle background, final Button btn) {
        try {
            boolean userExists = false;
            String user = username.getText();
            String pass = password.getText();
            ResultSet rs = statement.executeQuery("SELECT * FROM adventuregamers");
            while (rs.next() && !userExists) {
                String userID = rs.getString("userID");
                String gamerName = rs.getString("UserName");
                String gamerPassword = rs.getString("UserPassword");
                //check if gamer is in database
                if ((gamerName.equals(user) && gamerPassword.equals(gamerPassword))) {
                    System.out.println("User exists");
                    getNotificationService().pushNotification("Hello " + user);
                    this.userId = Integer.parseInt(userID);
                    String audioFile = "src/main/resources/assets/Sounds/notification.mp3";
                    Sound.playSound(audioFile, false);
                    loginBox.getChildren().remove(btn);
                    loginBox.getChildren().remove(label);
                    loginBox.getChildren().remove(username);
                    loginBox.getChildren().remove(password);
                    loginBox.getChildren().remove(background);
                    loginBox.getChildren().remove(loginBox);
                    hasLoggedIn = true;
                }
                System.out.println(userID + "\t\t" + gamerPassword + "\t\t" + gamerName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }




}
