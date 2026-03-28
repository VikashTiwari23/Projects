package todolist;

public interface TaskOperations {
    void addTask(Task task);

    void removeTask(int id);

    void updateTask(int id);

    void markComplete(int id);

    void listTasks(String filter);
}
