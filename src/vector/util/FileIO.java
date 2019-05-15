package vector.util;

import vector.VectorCanvas;
import vector.exception.UnknownCommandException;
import vector.shape.Rectangle;
import vector.shape.VectorShape;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileIO {

    public static String getString(VectorCanvas canvas) {
        StringBuilder output = new StringBuilder();
        VectorColor penColor, fillColor;
        boolean includePen, includeFill;
        penColor = new VectorColor(0);
        fillColor = new VectorColor(0, false);
        for (VectorShape shape: canvas.getShapes() ) {
            includePen = !penColor.equals(shape.getPen());
            includeFill = !fillColor.equals(shape.getFill());
            penColor = shape.getPen();
            fillColor = shape.getFill();
            output.append(shape.getVec(includePen, includeFill));
            output.append('\n');
        }
        return output.toString();
    }

    private static List<VectorPoint> parseShape(String[] parts) {
        ArrayList<VectorPoint> output = new ArrayList<>();
        for (int i = 1; i < parts.length; i+=2) {
            try {
                output.add(new VectorPoint(Double.parseDouble(parts[i]), Double.parseDouble(parts[i+1])));
            } catch (IllegalArgumentException e) { System.out.println("Error"); }
        }
        return output;
    }

    public static VectorCanvas parseString(List<String> input) throws UnknownCommandException {
        VectorColor penColor = new VectorColor(0);
        VectorColor fillColor = new VectorColor(0, false);
        VectorCanvas output = new VectorCanvas();
        VectorShape shape;
        for (String line: input) {
            String[] l = line.strip().split(" ");
            String command = l[0];
            switch (command) {
                case "PEN":
                    penColor.setRgb(l[1]);
                    break;
                case "FILL":
                    if (l[1].equals("OFF")) { fillColor.setActive(false); }
                    else { fillColor.setRgb(l[1]); }
                    break;
                case "RECTANGLE":
                    output.addShape(new Rectangle(parseShape(l)));
                    break;
//                case "Ellipse":
//                    output.addShape(new Ellipse(parseShape(l)));
//                    break;
//                case "Line":
//                    output.addShape(new Line(parseShape(l)));
//                    break;
                case "\n":
                    break;
                default:
                    throw new UnknownCommandException("Unknown command:" + command);

            }
        }
        return output;
    }

    static public void toImage(VectorCanvas canvas, File file) throws IOException {
        BufferedImage i = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        canvas.paintComponent(i.getGraphics());
        FileOutputStream out = new FileOutputStream(file);
        ImageIO.write(i, "BMP", out);
        out.flush();
        out.close();
    }
}
