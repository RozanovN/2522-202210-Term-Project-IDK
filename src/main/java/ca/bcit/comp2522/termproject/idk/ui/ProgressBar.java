package ca.bcit.comp2522.termproject.idk.ui;


import com.almasb.fxgl.logging.Logger;
import javafx.animation.*;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

/**
 * Represents the class for the factory of tiles.
 *
 * @author Prince Chabveka
 * @author Nikolay Rozanov
 * @version 2022
 */
public final class ProgressBar extends Parent {
    private static final Logger log = Logger.get("FXGL.ProgressBar");
    private final DoubleProperty minValue = new SimpleDoubleProperty(0.0);
    private final DoubleProperty currentValue = new SimpleDoubleProperty(0.0);
    private final  DoubleProperty maxValue = new SimpleDoubleProperty(100.0);
    private final DoubleProperty width = new SimpleDoubleProperty(200.0);
    private final DoubleProperty height = new SimpleDoubleProperty(10.0);
    private final Rectangle backgroundBar = new Rectangle();
    private final Rectangle innerBar = new Rectangle();
    private final Group barGroup = new Group();
    private final Label label = new Label();
    private Position labelPosition = Position.BOTTOM;
    private Paint traceFill = Color.WHITE;
    private final Timeline timeline = new Timeline();
    private final ChangeListener<Number> update;

    /**
     * Constructs a Progress Bar that shows changes.
     */
    public ProgressBar() {
        this(true);
    }

    /**
     * Constructs a Progress Bar.
     */
    public ProgressBar(final boolean showChanges) {
        innerBar.setTranslateX(15);
        innerBar.setTranslateY(3);

        backgroundBar.setFill(new ImagePattern(image("Medieval_Castle_Asset_Pack/HUD/bar.png")));
        backgroundBar.widthProperty().bind(width);
        backgroundBar.heightProperty().bind(height);

        innerBar.heightProperty().bind(height.subtract(10));

        DropShadow ds = new DropShadow(10, Color.WHITE);
        ds.setInput(new Glow(0.3));
        ds.setWidth(50);
        backgroundBar.setEffect(ds);

        ds = new DropShadow(5, Color.GOLD);
        ds.setInput(new Glow(0.1));
        innerBar.setEffect(ds);

        label.setFont(Font.font(16));
        label.setTextFill(Color.RED);
        label.textProperty().bind(currentValue.asString("%.0f").concat("/").concat(maxValue.asString("%.0f")));

        update = (obs, oldValue, newValue) -> {
            if (!showChanges)
                return;

            double newWidth = (width.get() - 10)
                    * (currentValue.get() - minValue.get()) / (maxValue.get() - minValue.get());
            int diff = newValue.intValue() - oldValue.intValue();

            // text value animation

            if (diff != 0) { //allows reacting to width changes without showing a label
                Text text = new Text((diff > 0 ? "+" : "") + diff);
                text.setTranslateX(newWidth + (diff > 0 ? 5 : 25));
                text.setTranslateY(height.get() / 2);
                text.setFill(traceFill);
                text.setFont(Font.font(14));

                barGroup.getChildren().add(text);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.66), text);
                tt.setToY(0);

                FadeTransition ft = new FadeTransition(Duration.seconds(0.66), text);
                ft.setToValue(0);

                ParallelTransition pt = new ParallelTransition(ft);
                pt.setOnFinished(e -> barGroup.getChildren().remove(text));
                pt.play();
            }

            // trace shown as a flash

            Rectangle trace = new Rectangle(Math.abs(newWidth - innerBar.getWidth()), height.get() - 6);
            trace.setArcWidth(innerBar.getArcWidth());
            trace.setArcHeight(innerBar.getArcHeight());
            trace.setTranslateX(Math.min(innerBar.getWidth(), newWidth));
            trace.setTranslateY(7);
            trace.setFill(traceFill);
            trace.setOpacity(0.55);
            trace.setEffect(new Glow(0.5));

            if (trace.getWidth() > 50) {
                barGroup.getChildren().add(trace);

                FadeTransition ft2 = new FadeTransition(Duration.seconds(0.5), trace);
                ft2.setToValue(0);
                ft2.setOnFinished(e -> barGroup.getChildren().remove(trace));
                ft2.play();
            }

            // smooth fill animation
            timeline.stop();
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.66),
                    new KeyValue(innerBar.widthProperty(), newWidth)));
            timeline.play();
        };

        currentValue.addListener(update);

        barGroup.getChildren().addAll(innerBar, backgroundBar);
        getChildren().addAll(barGroup, label);
        setLabelPosition(labelPosition);
        setLabelVisible(false);

        if (!showChanges) {
            innerBar.widthProperty().bind(width.subtract(10).multiply(new DoubleBinding() {
                {
                    super.bind(minValue, currentValue, maxValue);
                }

                @Override
                protected double computeValue() {
                    return (currentValue.get() - minValue.get())
                            / (maxValue.get() - minValue.get());
                }
            }));
        }
    }

    /**
     * Returns the inner bar.
     *
     * @return Rectangle representation of inner bar
     */
    public Rectangle getInnerBar() {
        return innerBar;
    }
    
    /**
     * Sets the fill color for inner bar.
     *
     * @param color color type
     */
    public void setFill(final Color color) {
        innerBar.setFill(color);
        DropShadow ds = new DropShadow(15, color);
        ds.setInput(new Glow(0.5));
        innerBar.setEffect(ds);
    }

    /**
     * Sets the fill color for labels.
     *
     * @param color color type
     */
    public void setLabelFill(final Paint color) {
        label.setTextFill(color);
    }

    /**
     * Sets the trace color.
     *
     * @param color color type
     */
    public void setTraceFill(final Paint color) {
        traceFill = color;
    }

    /**
     * Makes labels visible.
     *
     * @param b no idea, ask Prince.
     */
    public void setLabelVisible(final boolean b) {
        if (!b) {
            getChildren().remove(label);
            barGroup.translateXProperty().unbind();
            barGroup.translateYProperty().unbind();
            barGroup.setTranslateX(0);
            barGroup.setTranslateY(0);
        } else if (!getChildren().contains(label)) {
            getChildren().add(label);
            setLabelPosition(labelPosition);
        }
    }

    /**
     * Checks if label is visible.
     *
     * @return true if label is visible, otherwise false
     */
    public boolean isLabelVisible() {
        return getChildren().contains(label);
    }

    /**
     * Sets label position.
     *
     * @param pos a Position representing new position
     */
    public void setLabelPosition(final Position pos) {
        labelPosition = pos;
        if (!isLabelVisible())
            return;

        barGroup.translateXProperty().unbind();
        barGroup.translateYProperty().unbind();
        label.translateXProperty().unbind();
        label.translateYProperty().unbind();
        switch (pos) {
            case BOTTOM:
                barGroup.setTranslateX(0);
                barGroup.setTranslateY(0);
                label.translateXProperty().bind(width.divide(2).subtract(label.widthProperty().divide(2)));
                label.translateYProperty().bind(height);
                break;
            case LEFT:
                barGroup.translateXProperty().bind(label.widthProperty().add(10));
                barGroup.setTranslateY(0);
                label.setTranslateX(0);
                label.translateYProperty().bind(height.divide(2).subtract(label.heightProperty().divide(2)));
                break;
            case RIGHT:
                barGroup.setTranslateX(0);
                barGroup.setTranslateY(0);
                label.translateXProperty().bind(width.add(10));
                label.translateYProperty().bind(height.divide(2).subtract(label.heightProperty().divide(2)));
                break;
            case TOP:
                barGroup.setTranslateX(0);
                barGroup.translateYProperty().bind(label.heightProperty());
                label.translateXProperty().bind(width.divide(2).subtract(label.widthProperty().divide(2)));
                label.setTranslateY(0);
                break;
            default:
                log.warning("Unknown position: " + pos);
                break;
        }
    }


    /**
     * Sets width.
     *
     * @param value a double representing width
     */
    public void setWidth(final double value) {
        if (value <= 0)
            throw new IllegalArgumentException("Width must be > 0");

        width.set(value);
        update.changed(currentValue, currentValue.get(), currentValue.get());
    }


    /**
     * Sets height.
     *
     * @param value a double representing height.
     */
    public void setHeight(final double value) {
        if (value <= 0)
            throw new IllegalArgumentException("Height must be > 0");

        height.set(value);
    }

    /**
     * Sets minimum value of the bar.
     *
     * @param value a double representing new minimum value
     */
    public void setMinValue(final double value) {
        if (value > currentValue.get()) {
            log.warning("Current value < min value. Setting min value as current");
            currentValue.set(value);
        }

        if (value >= maxValue.get()) {
            log.warning("Min value >= max value. Setting max value to min value + 1");
            maxValue.set(value + 1);
        }

        minValue.set(value);
    }

    /**
     * Sets current value for the inner bar.
     *
     * @param value a double representing new value of the bar.
     */
    public void setCurrentValue(final double value) {
        double newValue = value;

        if (value < minValue.get()) {
            log.warning("Current value < min value. Setting min value as current");
            newValue = minValue.get();
        } else if (value > maxValue.get()) {
            log.warning("Current value > max value. Setting max value as current");
            newValue = maxValue.get();
        }

        currentValue.set(newValue);
    }

    /**
     * Sets max value of the bar.
     *
     * @param value an int representing new maximum value
     */
    public void setMaxValue(final int value) {
        if (value <= minValue.get()) {
            log.warning("Max value <= min value. Setting min value to max value - 1");
            minValue.set(value - 1);
        }

        if (value < currentValue.get()) {
            log.warning("Max value < current value. Setting current value to max");
            currentValue.set(value);
        }

        maxValue.set(value);
    }
}
