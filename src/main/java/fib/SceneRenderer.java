package fib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.jmathanim.Utils.PathUtils;
import com.jmathanim.jmathanim.JMathAnimScene;
import com.jmathanim.jmathanim.Scene2D;
import com.jmathanim.mathobjects.MathObject.Align;
import com.jmathanim.mathobjects.Text.LaTeXMathObject;
import com.jmathanim.mathobjects.Point;
import com.jmathanim.mathobjects.Shape;
import com.jmathanim.mathobjects.Axes.Axes;
public class SceneRenderer extends Scene2D {

    private List<FibBasedSequence> seq;
    private List<String> colors = List.of("blue", "red", "orange", "yellow", "green");

    public static void main(String[] args) {
        List<FibBasedSequence> seq = Arrays.stream(args)
            .map(name -> FibManager.getInstance().getByName(name))
            .collect(Collectors.toList());

        SceneRenderer renderer = new SceneRenderer(seq);
        renderer.execute();
    }

    public void setSeq(List<FibBasedSequence> seq) {
        this.seq = seq;
    }

    public SceneRenderer(List<FibBasedSequence> seq) {
        this.seq = seq; 
    }

    public static void run(List<FibBasedSequence> seq) {
        JMathAnimScene demoScene = new SceneRenderer(seq);
        demoScene.execute();
    }

    @Override
    public void setupSketch() {
        config.parseFile("#light.xml");
        config.setCreateMovie(true);
        config.setLowQuality();
    }

    @Override
    public void runSketch() throws Exception {
        if(seq == null) {
            throw new IllegalStateException("Sequence(s) must be provided!");
        }

        if(seq.size() == 1) {
            singleFib();
        } else {
            multiFib();
        }

    }

    private void multiFib() {
        int n = seq.get(0).getMem().length;
        camera.centerAtObjects(Point.at(n / 2, 0));    

        Axes axes = new Axes();
        add(axes);

        Shape newShape = null;
        
        Point[][] points = new Point[seq.size()][n];
        int[] mem; 
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < seq.size(); j++) {
                mem = seq.get(j).getMem();
                newShape =  Shape.rectangle(Point.at(i, 0), Point.at(i + 1, mem[i]))
                .drawColor("black")
                .fillColor(colors.get(j));
    
                var bounding = newShape.getBoundingBox();
                Point point = Point.at(bounding.xmin, bounding.ymax)
                                            .fillColor("red");
                Point boundary = Point.at(bounding.xmax, bounding.ymax + bounding.ymax / 4)
                                            .visible(false);
    
                points[j][i] = point;
                
                add(
                    newShape, 
                    boundary,
                    point
                );
    
                play.adjustCameraToAllObjects(1);
            }
        }
    
        
        int curr = 0;
        for(Point[] subPoints : points) {
            Shape pol = Shape.polyLine(subPoints).thickness(20).drawColor(colors.get(curr));
            play.fadeIn(pol);
            PathUtils.generateControlPointsBySimpleSlopes(pol.getPath(),.7);
            curr++;
        }


        waitSeconds(5);
    }

    private void singleFib() {
        int[] mem = seq.get(0).getMem();
        camera.centerAtObjects(Point.at(mem.length / 2, 0));    

        Axes axes = new Axes();
        add(axes);

        Shape newShape = null;
        
        Point[] points = new Point[mem.length];


        for(int i = 0; i < mem.length; i++) {
            newShape =  Shape.rectangle(Point.at(i, 0), Point.at(i + 1, mem[i]))
            .drawColor("black")
            .fillColor("orange");

            var bounding = newShape.getBoundingBox();
            Point point = Point.at(bounding.xmin, bounding.ymax)
                                        .fillColor("red");
            Point boundary = Point.at(bounding.xmax, bounding.ymax + bounding.ymax / 4)
                                        .visible(false);

            points[i] = point;
            
            add(
                newShape, 
                boundary,
                point
            );

            play.adjustCameraToAllObjects(1);
        }

        Shape pol = Shape.polyLine(points).thickness(20).drawColor("tomato");
        play.fadeIn(pol);
        PathUtils.generateControlPointsBySimpleSlopes(pol.getPath(),.7);

        waitSeconds(5);
    }
    

}
