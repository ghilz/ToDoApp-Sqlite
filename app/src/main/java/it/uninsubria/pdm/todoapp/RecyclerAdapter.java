package it.uninsubria.pdm.todoapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TodoItemHolder> {
    private ArrayList<TodoItem> mTodoItems;

    public RecyclerAdapter(ArrayList<TodoItem> items) {
        mTodoItems = items;
    }

    @Override
    public RecyclerAdapter.TodoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_row, parent, false);
        return new TodoItemHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.TodoItemHolder holder, int position) {
        TodoItem todoItem = mTodoItems.get(position);
        holder.bindTodoItem(todoItem);
    }

    @Override
    public int getItemCount() {
        return mTodoItems.size();
    }

    // 1 - Made the class extend RecyclerView.ViewHolder, allowing it to be used as a ViewHolder for the adapter.
    public static class TodoItemHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {
        // 2 - Add a list of references to the lifecycle of the object to allow the ViewHolder
        // to hang on to your ImageView and TextView, so it doesn’t have to repeatedly query the same information.
        private ImageView mItemImage;
        private TextView mItemDate;
        private TextView mItemDescription;
        private TodoItem mTodoItem;

        // 3 - Set up a constructor to handle grabbing references to various subviews of the layout.
        public TodoItemHolder(View v) {
            super(v);
            mItemImage = (ImageView) v.findViewById(R.id.item_image);
            mItemDate = (TextView) v.findViewById(R.id.item_date);
            mItemDescription = (TextView) v.findViewById(R.id.item_description);
            v.setOnClickListener(this);
        }

        // 4 - Implemented the required method for View.OnClickListener since ViewHolders are responsible for their own event handling.
        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!");
        }

        // 5 - This binds the TodoItem to the TodoItemHolder,
        // giving your item the data it needs to work out what it should show.
        public void bindTodoItem(TodoItem item) {
            mTodoItem = item;
            mItemImage.setImageResource(R.mipmap.ic_todo_item);
            String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(item.getDate().getTime());
            mItemDate.setText(currentDate);
            mItemDescription.setText(item.getTask());
        }
    }
}
