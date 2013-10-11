package application.notification;

public class NotificationController {

	final java.util.Set<NotificationListener> listeners = new java.util.HashSet<NotificationListener>();
	
	
	public <T> void addNotificationListener( NotificationListener<T> l ) {
		listeners.add(l);
	}
	
	public <T> void removeNotificationListener( NotificationListener<T> l ) {
		listeners.remove(l);
	}

	public <T> void fire( T event ) {
		
		for( NotificationListener l : listeners ) {
			l.onNotification(event);
		}
	}
}
