package project.devmob.tripcount.utils;

/**
 * Created by MicroStop on 28/06/2016.
 */
public abstract class TaskComplete<T> {

    public T result;

    public abstract void run();
}
