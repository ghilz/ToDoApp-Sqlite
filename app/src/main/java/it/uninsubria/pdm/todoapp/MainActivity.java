package it.uninsubria.pdm.todoapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<TodoItem> todoItems;

    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar myToolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);

        // Display icon in the toolbar
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        dbHelper = new DatabaseHelper(this);

        // the array list containing the todo items
        todoItems = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recyclerView);
        //mRecyclerView.setLayoutManager(mLinearLayoutManager);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
        }
        else{
            mGridLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
        }

        mAdapter = new RecyclerAdapter(todoItems);
        mRecyclerView.setAdapter(mAdapter);

        // This will attach the ItemTouchListener to the RecyclerView
        setRecyclerViewItemTouchListener();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    static final int NEW_ITEM_REQUEST = 1;  // The request code

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(),
                    "Not yet implemented!",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_new_item) {
            Intent i = new Intent(getApplicationContext(), AddNewItemActivity.class);
            startActivityForResult(i, NEW_ITEM_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == NEW_ITEM_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String returnValue = data.getStringExtra("TODO_TASK");
                Log.d(MainActivity.class.getName(), "onActivityResult() -> " + returnValue);
                addNewItem(returnValue);
                return;
            }
        }
    }

    private void addNewItem(String todo) {
        if (todo.length() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Empty ToDo string",
                    Toast.LENGTH_LONG).show();
            return;
        }
        TodoItem newTodo = new TodoItem(todo);
        // add to the DB and set the item idx
        long idx = dbHelper.insertItem(newTodo);
        newTodo.setId((int)idx);

        todoItems.add(0, newTodo);
        mAdapter.notifyDataSetChanged();


    }

    protected void onResume() {
        super.onResume();
        Log.v(MainActivity.class.getName(), "onResume()");
        // open the DB and update the listview
        this.todoItems.clear();
        todoItems.addAll(dbHelper.getAllItems());
        mAdapter.notifyDataSetChanged();
    }

    private void setRecyclerViewItemTouchListener() {
        // 1 - You create the callback and tell it what events to listen for.
        // It takes two parameters, one for drag directions and one for swipe directions, but
        // you’re only interested in swipe, so you pass 0 to inform the callback not to respond to drag events.
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
        {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder viewHolder1)      {
                // 2 - You return false in onMove because you don’t want to perform any special behavior here.
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // 3 - onSwiped is called when you swipe an item in the direction specified in the
                // ItemTouchHelper. Here, you request the viewHolder parameter passed for the
                // position of the item view, then you remove that item from your list of photos.
                // Finally, you inform the RecyclerView adapter that an item has been removed at a
                // specific position.
                int position = viewHolder.getAdapterPosition();
                TodoItem item = todoItems.remove(position);
                // delete the item from the DB
                dbHelper.deleteItem(item);
                mRecyclerView.getAdapter().notifyItemRemoved(position);

            }
        };

        // 4 - You initialize the ItemTouchHelper with the callback behavior you defined, and
        // then attach it to the RecyclerView.
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

}
