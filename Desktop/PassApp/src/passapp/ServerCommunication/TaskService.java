package passapp.ServerCommunication;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import passapp.ServerCommunication.ServerTask;

/**
 * Created by Brad on 2/11/2016.
 *
 * Service for executing Task thread to capture output
 */
public class TaskService extends Service<String> {

    String message;

    public TaskService(String message) {
        this.message = message;
    }

    @Override
    protected Task<String> createTask() {
        return new ServerTask(message);
    }

    @Override
    protected void failed() {
        super.failed();
        System.out.print("In TaskService failed");
    }


}
