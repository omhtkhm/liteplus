package daemon;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class GatherListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {

        Thread th = new Thread() {
            public void run() {
                // implement daemon logic here.
                SGAStatTask.start();
            }
        };
        th.setDaemon(true);
        th.start();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        // you could notify your thread you're shutting down if
        // you need it to clean up after itself
    }
}