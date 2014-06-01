package savingstrackerpro.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import savingstrackerpro.controllers.MonthPaneController;
import savingstrackerpro.models.MonthlyData;
import savingstrackerpro.models.MonthlyExpense;

/**
 *
 * @author Agustin Muniz
 */
public class MonthlyExpenseActiveCheckBoxCell extends TableCell<MonthlyExpense, Boolean> implements EventHandler<ActionEvent> {
    private CheckBox checkBox;
    private MonthPaneController controller;
    private MonthlyData md;

    public MonthlyExpenseActiveCheckBoxCell(MonthPaneController controller) {
        this.controller = controller;
        setAlignment(Pos.CENTER);
        createCheckBox();
    }
    @Override
    public void updateItem(Boolean item, boolean empty) {
        if (((MonthlyExpense)getTableRow().getItem()) != null) {
            super.updateItem(item, empty);
            if(!isEmpty()) {
                if(checkBox != null)
                    checkBox.setSelected(item);
                setGraphic(checkBox);
            }
        } else {
            setGraphic(null);
        }
    }
    private void createCheckBox() {
        checkBox = new CheckBox();
        checkBox.setOnAction(this);
    }
    @Override
    public void handle(ActionEvent ae) {
        MonthlyExpense me = ((MonthlyExpense)getTableRow().getItem());
        me.setActive(checkBox.isSelected());
        controller.refreshPane();
    }
}
