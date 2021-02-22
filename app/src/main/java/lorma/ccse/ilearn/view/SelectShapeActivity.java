package lorma.ccse.ilearn.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import lorma.ccse.ilearn.Config;
import lorma.ccse.ilearn.R;
import lorma.ccse.ilearn.controller.SelectShapeController;
import lorma.ccse.ilearn.controller.ShapesGridViewAdapter;
import lorma.ccse.ilearn.model.Shape;

import java.util.ArrayList;

public class SelectShapeActivity extends AppCompatActivity {

    private String mShapeLevel;
    private ArrayList<Shape> mShapes;
    private GridView mGridView;
    private ShapesGridViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shape);
        mGridView = (GridView) findViewById(R.id.gridViewShapes);
        //noinspection ConstantConditions
//        getSupportActionBar().setTitle("Select Shape");
        // Get the shape level
        mShapeLevel = getIntent().getStringExtra(Config.KEY_LEVEL);
        mAdapter = new ShapesGridViewAdapter(getApplicationContext(), mShapes);
        mGridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(SelectShapeActivity.this, DrawingActivity.class);
            i.putExtra("shapeId", mShapes.get(position).getId());
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShapes = SelectShapeController.getShapesFromLevel(mShapeLevel);
        mAdapter = new ShapesGridViewAdapter(getApplicationContext(), mShapes);
        mGridView.setAdapter(mAdapter);
    }
}
