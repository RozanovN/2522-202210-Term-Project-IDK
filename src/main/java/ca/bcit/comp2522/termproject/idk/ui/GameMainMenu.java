package ca.bcit.comp2522.termproject.idk.ui;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.view.KeyView;
import com.almasb.fxgl.input.view.MouseButtonView;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.scene.Scene;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.MenuButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

import java.util.ArrayList;

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
    private final VBox scoresRoot = new VBox(10);
    private Node highScores;

    public GameMainMenu() {
        super(MenuType.MAIN_MENU);

        getContentRoot().getChildren().setAll(new Rectangle(getAppWidth(), getAppHeight()));
        var title = getUIFactoryService().newText(getSettings().getTitle(), Color.WHITE, 46.0);
        title.setStroke(Color.WHITESMOKE);
        title.setStrokeWidth(1.5);
        centerTextBind(title, getAppWidth() / 2.0, 200);

        var version = getUIFactoryService().newText(getSettings().getVersion(), Color.WHITE, 22.0);
        centerTextBind(version, getAppWidth() / 2.0, 220);

        getContentRoot().getChildren().addAll(title, version);

        var color = Color.DARKBLUE;

        var blocks = new ArrayList<ColorBlock>();

        var blockStartX = getAppWidth() / 2.0 - 380;

        for (int i = 0; i < 15; i++) {
            var block = new ColorBlock(40, color);
            block.setTranslateX(blockStartX + i*50);
            block.setTranslateY(100);

            blocks.add(block);
            getContentRoot().getChildren().add(block);
        }

        for (int i = 0; i < 15; i++) {
            var block = new ColorBlock(40, color);
            block.setTranslateX(blockStartX + i*50);
            block.setTranslateY(220);

            blocks.add(block);
            getContentRoot().getChildren().add(block);
        }

        for (int i = 0; i < blocks.size(); i++) {
            var block = blocks.get(i);

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

        var menuBox = new VBox(
                5,
                new MenuButton("New Game", () -> fireNewGame()),
                new MenuButton("How to Play", () -> instructions()),
                new MenuButton("Credits", () -> showCredits()),
                new MenuButton("Exit", () -> fireExit())
        );
        menuBox.setAlignment(Pos.TOP_CENTER);

        menuBox.setTranslateX(getAppWidth() / 2.0 - 125);
        menuBox.setTranslateY(getAppHeight() / 2.0 + 125);

        // useful for checking if nodes are properly centered
        var centeringLine = new Line(getAppWidth() / 2.0, 0, getAppWidth() / 2.0, getAppHeight());
        centeringLine.setStroke(Color.WHITE);

        scoresRoot.setPadding(new Insets(10));
        scoresRoot.setAlignment(Pos.TOP_LEFT);

        StackPane hsRoot = new StackPane(new Rectangle(450, 250, Color.color(0, 0, 0.2, 0.8)), scoresRoot);
        hsRoot.setAlignment(Pos.TOP_CENTER);
        hsRoot.setCache(true);
        hsRoot.setCacheHint(CacheHint.SPEED);
        hsRoot.setTranslateX(getAppWidth());
        hsRoot.setTranslateY(menuBox.getTranslateY());

        highScores = hsRoot;

        getContentRoot().getChildren().addAll(menuBox, hsRoot);
    }

    public static class ColorBlock extends Rectangle {

        public ColorBlock(int size, Color color) {
            super(size, size, color);
            setArcWidth(8);
            setArcHeight(8);
            setStrokeType(StrokeType.INSIDE);
            setStrokeWidth(2.5);
            setStroke(Color.color(0.138, 0.138, 0.375, 0.66));
        }
    }

    private void showCredits() {
        getDialogService().showMessageBox("Add something here later");
    }

    private boolean isLoadedScore = false;

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
        MenuButton(String name, Runnable action) {
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
}
