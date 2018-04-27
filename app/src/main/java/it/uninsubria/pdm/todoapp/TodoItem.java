package it.uninsubria.pdm.todoapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ignazio on 3/15/18.
 */

public class TodoItem {
    private String todo;  // il task
    private GregorianCalendar createOn; // la data di creazione del task
    private int id;

    public TodoItem(String todo) {
        super();
        this.todo = todo;
        this.createOn = new GregorianCalendar();
    }

    public TodoItem() {
        super();
    }

    @Override
    public String toString() {
        String currentDate = new
                SimpleDateFormat("dd/MM/yyyy").format(createOn.getTime());
        return currentDate + ":\n >> " + todo;
    }

    public Calendar getDate() {
        return createOn;
    }

    public String getTask() {
        return todo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTask(String task) {
        this.todo = task;
    }

    public void setDate(GregorianCalendar date) {
        this.createOn = date;
    }

    public int getId() {
        return id;
    }
}
