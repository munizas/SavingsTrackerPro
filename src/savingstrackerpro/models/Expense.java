package savingstrackerpro.models;

import savingstrackerpro.SavingsTrackerPro;

/**
 * Expense objects represent any given expense for the month.
 * 
 * @author Agustin Muniz
 */
public class Expense {
    
    private String name;
    private double defaultAmount;
    
    public Expense(String name, double defaultAmount) {
        this.name = name;
        this.defaultAmount = defaultAmount;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getDefaultAmount() {
        return defaultAmount;
    }
    
    public void setDefaultAmount(double defaultAmount) {
        this.defaultAmount = defaultAmount;
    }
    
    public String toString() {
        return "(" + getName() + ", " + getDefaultAmount() + ")";
    }
    
    public void delete() {
        SavingsTrackerPro.getExpenseMap().remove(getName());
    }
    
}
