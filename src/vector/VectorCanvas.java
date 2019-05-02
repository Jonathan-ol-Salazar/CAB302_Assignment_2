package vector;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class VectorCanvas extends Canvas{
    private List<VectorShape> shapes;  // List of all shapes on the vectorCanvas
    private Type selectedTool;
    private boolean leftMouse;  // True if the left mouse key is down
    private CanvasMouse MouseObserver;
    private int sideWidth;

    public void paint(Graphics g) {
        for (VectorShape shape : shapes) {
            shape.draw(g, sideWidth);
            System.out.println("hello");
        }
    }

    public VectorCanvas() {
        shapes = new LinkedList<>();
        selectedTool = Type.LINE;
        MouseObserver = new CanvasMouse();
        MouseObserver.attachCanvas(this);
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
    }

    public int getSideWith() {
        return sideWidth;
    }

    public void setSideWidth(int sideWidth) {
        this.sideWidth = sideWidth;
    }

    public VectorShape createShape() {
        VectorShape s = new VectorShape(selectedTool, MouseObserver, getBackground(), getForeground());
        addShape(s);
        return s;
    }

    public VectorShape addShape(VectorShape shape) {
        shapes.add(shape);
        return shape;
    }

    public void drag(VectorShape shape, int point) {
        while (leftMouse) {
            shape.editPoint(point, MouseObserver);
            try {
                Thread.sleep((long) 60);
            } catch (InterruptedException error) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class CanvasMouse implements MouseListener, MouseMotionListener, Point {

    VectorCanvas vectorCanvas;

    public void attachCanvas(VectorCanvas c) {
        vectorCanvas = c;
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        vectorCanvas.createShape();
    }

    public void mousePressed(MouseEvent mouseEvent) { }

    public void mouseReleased(MouseEvent mouseEvent) { }

    public void mouseEntered(MouseEvent mouseEvent) { }

    public void mouseExited(MouseEvent mouseEvent) { }

    public void mouseDragged(MouseEvent mouseEvent) { }

    public void mouseMoved(MouseEvent mouseEvent) { }

    public double getX() {
        return MouseInfo.getPointerInfo().getLocation().x / vectorCanvas.getSideWith();
    }
    public double getY() {
        return MouseInfo.getPointerInfo().getLocation().y / vectorCanvas.getSideWith();
    }
    public List<Double> asList() {
        return Arrays.asList(getX(), getY());
    }
    public java.awt.Point getAbsPoint(int canvasSideLength) {
        return new java.awt.Point((int) getX() * canvasSideLength, (int) getY() * canvasSideLength);
    }
    public java.awt.Point asPoint() {
        return getAbsPoint(vectorCanvas.getSideWith());
    }
}