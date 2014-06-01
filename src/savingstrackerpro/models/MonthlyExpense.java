package savingstrackerpro.models;

/**
 * Represents an expense for a specific month.
 * 
 * @author Agustin Muniz
 */
public class MonthlyExpense {
    
    private int month;
    private int year;
    private String name;
    private double amount;
    private boolean active;
    private String note;
    
    public MonthlyExpense() {}
    
    public MonthlyExpense(int month, int year, String name) {
        this.month = month;
        this.year = year;
        this.name = name;
    }
    
    public int getMonth() {
        return month;
    }
    
    public int getYear() {
        return year;
    }
    
    public String getName() {
        return name;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public String getNote() {
        return note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    
    public String toString() {
        return "(" + getName() + ", " + getMonth() + ", " + getYear() + ", " + getAmount() + ")";
    }
    
}
