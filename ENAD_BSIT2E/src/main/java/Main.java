import dao.DatabaseConnection;
import dao.UserDao;
import ui.MainMenu;

/** App entry point. */
public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initialize();   // create the users table if needed
        new UserDao().seedAdmin();         // default admin: admin / admin123
        new MainMenu().show();
    }
}
