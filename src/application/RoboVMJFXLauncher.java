package application;

import javafx.application.Application;

import org.robovm.cocoatouch.foundation.NSAutoreleasePool;
import org.robovm.cocoatouch.foundation.NSDictionary;
import org.robovm.cocoatouch.uikit.UIApplication;
import org.robovm.cocoatouch.uikit.UIApplicationDelegate;
import org.robovm.cocoatouch.uikit.UIPasteboard;

public class RoboVMJFXLauncher extends UIApplicationDelegate.Adapter {

    @Override
    public boolean didFinishLaunching(UIApplication application, NSDictionary launchOptions) {

        Thread launchThread = new Thread() {
            @Override
            public void run() {
                Application.launch(Main.class);
            }
        };
        launchThread.setDaemon(true);
        launchThread.start();

        return true;
    }
    
    

    @Override
	public void willEnterForeground(UIApplication application) {
    	System.err.printf( "willEnterForeground\n") ;

    	final String code = UIPasteboard.getGeneral().getString();
    	
    	System.err.printf( "get string [%s]\n", code) ;
    	
    	Main.notificationCenter.fire( code );
	}



	@Override
	public void didBecomeActive(UIApplication application) {

    	System.err.printf( "didBecomeActive\n") ;
    	
    
    }



	public static void main(String[] args) throws Exception {
        System.setProperty("glass.platform", "ios");
        System.setProperty("prism.text", "native");

        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(args, null, RoboVMJFXLauncher.class);
        pool.drain();
    }
}