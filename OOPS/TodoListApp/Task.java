package todolist;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Task {
    private int id;
    private String title;
    private String description;
    private String priority;
    private boolean isCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;


    // constructor
    public Task(int id,String title,String description,String priority){
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.isCompleted = false;
        this.createdAt = LocalDateTime.now();
        this.completedAt = null;
    }

    //getters
    public int getId() {return id;}
    public String getTitle(){return this.title;}
    public String getDescription(){ return this.description;}
    public String getPriority(){ return this.priority;}
    public boolean isCompleted(){ return this.isCompleted;}
    public LocalDateTime getCreatedAt(){ return this.createdAt;}
    public LocalDateTime getCompletedAt(){ return this.completedAt;}


    // Setters
    public void setTitle(String title){this.title = title;}
    public void setDescription(String description){ this.description = description;}
    public void setPriority(String priority){ this.priority = priority;}

    // functions
    public void markCompleted(){
        this.isCompleted = true;
        this.completedAt = LocalDateTime.now();
    }

    public void markPending(){
        this.isCompleted = false;
        this.completedAt = null;
    }

    @Override public String toString(){
        String status = isCompleted? "Done" : "Pending";
        String completed = isCompleted ? " | Completed: " + completedAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) : "";
        return String.format(
                "--------------------------------------------------------------------------------------------------------\n"+
                "| ID                         :             %-25d  | \n "+
                "| Title                      :             %-25s  | \n " +
                "| Priority                   :             %-25s  | \n "+
                "| Status                     :             %-25s  | \n "+
                "| Created                    :             %-25s  | \n "+
                "-----------------------------------------------------------------------------------------------------------",
                id,title,priority,status,createdAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), completed
        );
    }
}
