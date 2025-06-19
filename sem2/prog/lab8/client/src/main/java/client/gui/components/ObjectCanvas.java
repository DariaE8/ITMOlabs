package client.gui.components;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;

import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import models.Ticket;

import java.util.*;
import java.util.function.Consumer;

public class ObjectCanvas {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private Collection<Ticket> tickets = Collections.emptyList();
    private final Map<String, Color> userColors = new HashMap<>();

    private static final float MIN_X = -180.0f;
    private static final float MAX_X = 180.0f;
    private static final long MIN_Y = -90;
    private static final long MAX_Y = 90;
    private Consumer<Ticket> onTicketClicked;
    private final List<DrawnTicket> drawnTickets = new ArrayList<>();
    private record DrawnTicket(Ticket ticket, double x, double y, double width, double height) {
        boolean contains(double px, double py) {
            return px >= x && px <= x + width && py >= y && py <= y + height;
        }
    }

    public ObjectCanvas(Canvas canvas) {
            this.canvas = canvas;
            this.gc = canvas.getGraphicsContext2D();

            canvas.setOnMouseClicked(e -> {
            double px = e.getX();
            double py = e.getY();

            for (DrawnTicket dt : drawnTickets) {
                if (dt.contains(px, py)) {
                    if (onTicketClicked != null) {
                        onTicketClicked.accept(dt.ticket());
                    }
                    break;
                }
            }
        });
    }

    public void setTickets(Collection<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void setOnTicketClicked(Consumer<Ticket> handler) {
        this.onTicketClicked = handler;
    }

    private Color getColorForOwner(String owner) {
        return userColors.computeIfAbsent(owner,
                k -> Color.hsb(new Random(k.hashCode()).nextInt(360), 0.7, 0.9));
    }

    public void drawObjects() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawnTickets.clear();

        drawAxes(); // ← рисуем оси

        if (tickets == null || tickets.isEmpty()) return;

        for (Ticket ticket : tickets) {
            float rawX = ticket.getCoordinates().getX();
            long rawY = ticket.getCoordinates().getY();
            double x = mapX(rawX);
            double y = mapY(rawY);
            double size = 30;

            String owner = Integer.toString(ticket.getOwnerId());
            Color color = getColorForOwner(owner);

            gc.setFill(color);
            drawnTickets.add(new DrawnTicket(ticket, x, y, size, size));
            gc.fillRect(x, y, size, size);

            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("Arial", 10));
            gc.fillText(ticket.getName(), x + 5, y + size / 2);
        }
    }

public void addTicketAnimated(Ticket ticket) {
    if (tickets instanceof List<Ticket> list) {
        list.add(ticket);
    } else {
        tickets = new ArrayList<>(tickets);
        ((List<Ticket>) tickets).add(ticket);
    }

    DoubleProperty scale = new SimpleDoubleProperty(0.0);

    Timeline timeline = new Timeline(
        new KeyFrame(Duration.seconds(1),
            new KeyValue(scale, 1.0))
    );

    Timeline renderLoop = new Timeline(
        new KeyFrame(Duration.millis(16), e -> drawObjectsWithScale(ticket, scale.get()))
    );
    renderLoop.setCycleCount(Animation.INDEFINITE);

    timeline.setOnFinished(e -> {
        renderLoop.stop();
        drawObjects(); // обычный рендер
    });

    renderLoop.play();
    timeline.play();
}

private void drawObjectsWithScale(Ticket animatingTicket, double scale) {
    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    drawnTickets.clear();
    drawAxes();

    for (Ticket ticket : tickets) {
        float rawX = ticket.getCoordinates().getX();
        long rawY = ticket.getCoordinates().getY();
        double x = mapX(rawX);
        double y = mapY(rawY);
        double size = 30;

        if (ticket.equals(animatingTicket)) {
            size *= scale;
        }

        String owner = Integer.toString(ticket.getOwnerId());
        Color color = getColorForOwner(owner);

        gc.setFill(color);
        drawnTickets.add(new DrawnTicket(ticket, x, y, size, size));
        gc.fillRect(x, y, size, size);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", 10));
        gc.fillText(ticket.getName(), x + 5, y + size / 2);
    }
}


public void removeTicketAnimated(Ticket ticket) {
    DoubleProperty scale = new SimpleDoubleProperty(1.0);

    Timeline timeline = new Timeline(
        new KeyFrame(Duration.seconds(1),
            new KeyValue(scale, 0.0))
    );

    Timeline renderLoop = new Timeline(
        new KeyFrame(Duration.millis(16), e -> drawObjectsWithScale(ticket, scale.get()))
    );
    renderLoop.setCycleCount(Animation.INDEFINITE);

    timeline.setOnFinished(e -> {
        renderLoop.stop();
        // Удаляем билет из коллекции и перерисовываем без него
        if (tickets instanceof List<Ticket> list) {
            list.remove(ticket);
        } else {
            tickets = tickets.stream().filter(t -> !t.equals(ticket)).toList();
        }
        drawObjects();
    });

    renderLoop.play();
    timeline.play();
}



    private void drawAxes() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);

        // Ось X
        double zeroY = mapY(0);
        gc.strokeLine(0, zeroY, width, zeroY);

        // Ось Y
        double zeroX = mapX(0);
        gc.strokeLine(zeroX, 0, zeroX, height);

        gc.setFont(Font.font("Arial", 9));
        gc.setFill(Color.DARKGRAY);

        // Деления по X каждые 60°
        for (int x = -180; x <= 180; x += 60) {
            double screenX = mapX(x);
            gc.strokeLine(screenX, zeroY - 5, screenX, zeroY + 5);
            gc.fillText(String.valueOf(x), screenX + 2, zeroY + 12);
        }

        // Деления по Y каждые 30°
        for (int y = -90; y <= 90; y += 30) {
            double screenY = mapY(y);
            gc.strokeLine(zeroX - 5, screenY, zeroX + 5, screenY);
            gc.fillText(String.valueOf(y), zeroX + 6, screenY - 2);
        }
    }



    private double mapX(float x) {
        return ((x - MIN_X) / (MAX_X - MIN_X)) * canvas.getWidth();
    }

    private double mapY(long y) {
        return canvas.getHeight() - ((y - MIN_Y) / (double)(MAX_Y - MIN_Y)) * canvas.getHeight();
    }
}

