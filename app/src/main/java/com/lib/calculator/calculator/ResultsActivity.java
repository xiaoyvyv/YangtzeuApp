package com.lib.calculator.calculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yangtzeu.R;


public class ResultsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_results);
        TextView resultsText = findViewById(R.id.text_results);
        resultsText.setText(getIntent().getStringExtra("results"));
    }

    public static void actionStart(Context context, String results) {
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra("results", results);
        context.startActivity(intent);

    }
}
