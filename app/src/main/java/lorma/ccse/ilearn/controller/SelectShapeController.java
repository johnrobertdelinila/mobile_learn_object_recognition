package lorma.ccse.ilearn.controller;

import lorma.ccse.ilearn.model.Shape;

import java.util.ArrayList;

public class SelectShapeController {

    public static ArrayList<Shape> getShapesFromLevel(String shapeLevel){
        ArrayList<Shape> shapes = new ArrayList<>();
        shapes.addAll(Shape.find(Shape.class, "level = ?", shapeLevel));
        return shapes;
    }
}
