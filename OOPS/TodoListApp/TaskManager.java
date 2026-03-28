package todolist;
import java.util.*;

public class TaskManager implements TaskOperations {
    private List<Task>tasks;
    private int nextId;

    // constructor
    public TaskManager(){
        this.tasks = new ArrayList<>();
        this.nextId = 1;
    }

    @Override
    public void addTask(Task task){
        tasks.add(task);
        System.out.println("Tasks added successfully");
    }



    @Override
    public void removeTask(int id){
        tasks.removeIf(task->task.getId()==id);
        System.out.println("Task removed successfully !");
    }
    @Override
    public void updateTask(int id){
        for(Task task : tasks){
            if(task.getId()==id){
                Scanner sc = new Scanner(System.in);
                System.out.print("New Title  : ");
                task.setTitle(sc.nextLine());
                System.out.println("New Description :  ");
                task.setDescription(sc.nextLine());
                System.out.println("New Priority  (High/Medium/Low) : ");
                task.setPriority(sc.nextLine().toUpperCase());
                System.out.println("Task Updated Successfully");
                return;
            }
        }
        System.out.println("Task not found!");
    }



    @Override
    public void markComplete(int id){
        for(Task task : tasks){
            if(task.getId()==id){
                task.markCompleted();
                System.out.println("Task Marked Successfully");
                return;
            }
            System.out.println("Task not found !");
        }
    }
    @Override
    public void listTasks(String filter){
        List<Task>filtered = new ArrayList<>();
        switch(filter.toLowerCase()){
            case "all":
                filtered=tasks;
                break;
            case "pending":
                filtered = tasks.stream().filter(t-> !t.isCompleted()).toList();
                break;
            case "completed":
                filtered = tasks.stream().filter(Task::isCompleted).toList();
                break;
            case "high":
                filtered = tasks.stream().filter(t->t.getPriority().equals("HIGH")).toList();
                break;
            case "medium":
                filtered = tasks.stream().filter(t->t.getPriority().equals("MEDIUM")).toList();
                break;
            case "low":
                filtered = tasks.stream().filter(t -> t.getPriority().equals("LOW")).toList();
                break;
            default:
                System.out.println("Invalid filter");
                return;
        }
        if(filtered.isEmpty()){
            System.out.println("No task found !");
            return;
        }
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         📋 TASK LIST (" + filtered.size() + ")              ║");
        System.out.println("╚══════════════════════════════════════╝");
        filtered.forEach(t-> System.out.println(t));
    }



    public int getNextId(){ return nextId++;}
    public int getTaskCount(){ return tasks.size();}
    public int getPendingCount(){ return (int)tasks.stream().filter(t->!t.isCompleted()).count();}
    public int getCompletedCount(){return (int) tasks.stream().filter(t->t.isCompleted()).count();}
}
