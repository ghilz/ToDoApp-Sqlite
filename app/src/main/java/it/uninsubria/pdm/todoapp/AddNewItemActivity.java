package it.uninsubria.pdm.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

public class AddNewItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        final EditText editText = findViewById(R.id.editNewTodo);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("TODO_TASK", editText.getText().toString());
        Log.d("NewTodoActivity", "onSupportNavigateUp() -> " + editText.getText());
        setResult(RESULT_OK, resultIntent);
        finish();
        return true;
    }
}
