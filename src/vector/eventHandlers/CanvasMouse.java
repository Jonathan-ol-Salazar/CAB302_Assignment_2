package vector.eventHandlers;

import vector.exception.CanvasException;
import vector.shape.Line;
import vector.shape.VectorShape;
import vector.uiComponents.VectorCanvas;
import vector.util.Point;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.List;

public class CanvasMouse implements MouseListener, MouseMotionListener, Point {

    private VectorCanvas vectorCanvas;
    public boolean shapeCreating = false;
    public boolean polygon = false;
    public boolean clicked = false;
    private int x;
    private int y;

    public void attachCanvas(VectorCanvas c) {
        vectorCanvas = c;
    }

    public void mouseClicked(MouseEvent mouseEvent) { }

    public void mousePressed(MouseEvent mouseEvent) {
        clicked = true;
        if (!shapeCreating) {
            if(!polygon){
                try {
                    VectorShape s = vectorCanvas.createShape();
                    shapeCreating = true;
                    vectorCanvas.repaint();
                    Thread createShape = new Thread(() -> s.drag(vectorCanvas));
                    createShape.start();
                } catch (CanvasException e) {
                    JOptionPane.showMessageDialog(null,
                            "An error has occurred",
                            "Unhandled error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } else {
            shapeCreating = false;
        }
        System.out.println("Creating: " + vectorCanvas.getSelectedTool().name());
    }

    public boolean getShapeCreating(){
        return this.shapeCreating;
    }

    public void mouseReleased(MouseEvent mouseEvent) { }

    public void mouseEntered(MouseEvent mouseEvent) { }

    public void mouseExited(MouseEvent mouseEvent) { }

    public void mouseDragged(MouseEvent mouseEvent) { }

    public void mouseMoved(MouseEvent mouseEvent) { }

    public double getX() {
        try { x = vectorCanvas.getMousePosition().x; }
        catch (NullPointerException e) { }
        if(vectorCanvas.gridToggle){
            return mouseSnap((double) x / vectorCanvas.getHeight());
        }
        else {
            return (double) x / vectorCanvas.getWidth();
        }
    }

    public double mouseSnap(double snap){
        double buffer = 1.0/vectorCanvas.nLines;
        double snapRight =  snap + buffer;
        double snapLeft = snap - buffer;
        double snapPosition = 1;
        for (Line line : vectorCanvas.Grid) {
            double position = line.getPoint(1).getX();
            if(position<snapRight && position>snapLeft){
                double test = (snap-position);
                if(test<0){
                    test = test*(-1);
                }
                if (test < snapPosition) {
                    snapPosition = position;
                }
            }
        }
        return  snapPosition;
    }
    public double getY() {
        try {y = vectorCanvas.getMousePosition().y; }
        catch (NullPointerException e) { }
        if(vectorCanvas.gridToggle){
           return mouseSnap((double) y / vectorCanvas.getHeight());
        }
        else {
            return (double) y / vectorCanvas.getHeight();
        }
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