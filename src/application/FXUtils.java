package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXUtils {

	/**
	 * 
	 * @param service
	 * @param owner
	 * @return
	 */
	public static Stage createProgressDialog(final Service<Void> service, Stage owner) {
	    final Stage stage = new Stage();
	    stage.initModality(Modality.WINDOW_MODAL);
	    stage.initOwner(owner);
	    final BorderPane root = new BorderPane();
	    final ProgressIndicator indicator = new ProgressIndicator();
	    // have the indicator display the progress of the service:
	    indicator.progressProperty().bind(service.progressProperty());
	    // hide the stage when the service stops running:
	    service.stateProperty().addListener(new ChangeListener<State>() {
	      @Override
	      public void changed(ObservableValue<? extends State> observable,
	          State oldValue, State newValue) {
	        if (newValue == State.CANCELLED || newValue == State.FAILED
	            || newValue == State.SUCCEEDED) {
	          stage.hide();
	        }
	      }
	    });
	    // A cancel button:
	    Button cancel = new Button("Cancel");
	    cancel.setOnAction(new EventHandler<ActionEvent>() {
	      @Override
	      public void handle(ActionEvent event) {
	        service.cancel();
	      }
	    });
	    root.setCenter(indicator);
	    root.setBottom(cancel);
	    Scene scene = new Scene(root, 200, 200);
	    stage.setScene(scene);
	    return stage;
	  }
	
		/**
		 * Find a {@link Node} within a {@link Parent} by it's ID.
		 * <p>
		 * This might not cover all possible {@link Parent} implementations but it's
		 * a decent crack. {@link Control} implementations all seem to have their
		 * own method of storing children along side the usual
		 * {@link Parent#getChildrenUnmodifiable()} method.
		 * 
		 * @param parent
		 *            The parent of the node you're looking for.
		 * @param id
		 *            The ID of node you're looking for.
		 * @return The {@link Node} with a matching ID or {@code null}.
		 */
		@SuppressWarnings("unchecked")
		static <T> T getChildByID(Parent parent, String id) {
			
			String nodeId = null;
			
			if(parent instanceof TitledPane) {
				TitledPane titledPane = (TitledPane) parent;
				Node content = titledPane.getContent();
				nodeId = content.idProperty().get();
				
				if(nodeId != null && nodeId.equals(id)) {
					return (T) content;
				}
				
				if(content instanceof Parent) {
					T child = getChildByID((Parent) content, id);
					
					if(child != null) {
						return child;
					}
				}
			}
			
			for (Node node : parent.getChildrenUnmodifiable()) {
				nodeId = node.idProperty().get();
				if(nodeId != null && nodeId.equals(id)) {
					return (T) node;
				}
				
				if(node instanceof SplitPane) {
					SplitPane splitPane = (SplitPane) node;
					for (Node itemNode : splitPane.getItems()) {
						nodeId = itemNode.idProperty().get();
						
						if(nodeId != null && nodeId.equals(id)) {
							return (T) itemNode;
						}
						
						if(itemNode instanceof Parent) {
							T child = getChildByID((Parent) itemNode, id);
							
							if(child != null) {
								return child;
							}
						}
					}
				}
				else if(node instanceof Accordion) {
					Accordion accordion = (Accordion) node;
					for (TitledPane titledPane : accordion.getPanes()) {
						nodeId = titledPane.idProperty().get();
						
						if(nodeId != null && nodeId.equals(id)) {
							return (T) titledPane;
						}
						
						T child = getChildByID(titledPane, id);
						
						if(child != null) {
							return child;
						}
					}
				}
				else   if(node instanceof Parent) {
					T child = getChildByID((Parent) node, id);
					
					if(child != null) {
						return child;
					}
				}
			}
			return null;
		}
	}
