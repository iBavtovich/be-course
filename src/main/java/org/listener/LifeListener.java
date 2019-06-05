package org.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class LifeListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("App: " + servletContextEvent.getServletContext().getServletContextName() + " just started");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
