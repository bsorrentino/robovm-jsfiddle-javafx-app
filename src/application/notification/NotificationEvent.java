package application.notification;


public class NotificationEvent  {

	final Object info;

	public NotificationEvent(Object info) {
		super();
		this.info = info;
	}

	public Object getInfo() {
		return info;
	}
	
	
}
