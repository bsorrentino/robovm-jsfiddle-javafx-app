package application.notification;

public interface NotificationListener<T> {

	public void onNotification( T event );
}
