package todolist;

import java.util.Scanner;

public class TodoApp {
    private static TaskManager manager = new TaskManager();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║     📌 WELCOME TO TODO MANAGER       ║");
        System.out.println("║        Built with Java OOPS          ║");
        System.out.println("╚══════════════════════════════════════╝");
        while(true){
            showMenu();
            int choice = sc.nextInt();
            sc.nextLine();
            switch(choice){
                case 1 -> addTask();
                case 2 -> listTask();
                case 3 -> removeTask();
                case 4 -> updateTask();
                case 5 -> markComplete();
                case 6 -> showStatus();
                case 7 -> {
                    System.out.println("\n Good Bye stay productive ");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Invalid choice");
                }
            }
        }
    }


    public static void showMenu(){
        System.out.println("\n┌─────────────── MENU ───────────────┐");
        System.out.println("│  1. ➕  Add New Task               │");
        System.out.println("│  2. 📋  List Tasks                 │");
        System.out.println("│  3. 🗑️   Remove Task               │");
        System.out.println("│  4. ✏️   Update Task               │");
        System.out.println("│  5. ✅  Mark Complete               │");
        System.out.println("│  6. 📊  Show Statistics             │");
        System.out.println("│  7. 🚪  Exit                        │");
        System.out.println("└────────────────────────────────────┘");
        System.out.print("Enter choice: ");
    }

    private static void addTask(){
        System.out.println("\n Add New Task \n");
        System.out.println("Title : ");
        String title = sc.nextLine();
        System.out.println("Description : ");
        String description = sc.nextLine();
        System.out.println("Priority (Low/Medium/High)  :  ");
        String priority = sc.nextLine();

        if(!priority.equals("LOW") && !priority.equals("MEDIUM") && !priority.equals("HIGH")){
            priority = "MEDIUM";
        }
        Task task = new Task(manager.getNextId(),title,description,priority);
        manager.addTask(task);
    }

    public static void listTask(){
        System.out.println("Filtered Tasks \n");
        System.out.println("all / pending / completed / high / medium / low");
        System.out.print("Filter: ");
        String filter = sc.nextLine();
        manager.listTasks(filter);
    }

    public static void removeTask(){
        System.out.println("Enter Task id to Remove :  ");
        int id =sc.nextInt();
        sc.nextLine();
        manager.removeTask(id);
    }

    public static void updateTask(){
        System.out.println("Enter Task id to Update :  ");
        int id = sc.nextInt();
        sc.nextLine();
        manager.updateTask(id);
    }

    public static void markComplete(){
        System.out.println("Enter Task to Complete");
        int id = sc.nextInt();
        manager.markComplete(id);
    }

    private static void showStatus() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║          📊 STATISTICS               ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf("║  Total Tasks  : %-20d │\n", manager.getTaskCount());
        System.out.printf("║  Pending      : %-20d │\n", manager.getPendingCount());
        System.out.printf("║  Completed    : %-20d │\n", manager.getCompletedCount());
        System.out.println("╚══════════════════════════════════════╝");
    }
}
