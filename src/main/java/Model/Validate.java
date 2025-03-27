package Model;

public class Validate {
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Oops! The product name can't be empty.");
            return false;
        }
        return true;
    }
    public static boolean isValidPrice(double price) {
        if (price <= 0) {
            System.out.println("The price needs to be more than 0.");
            return false;
        }
        return true;
    }
    public static boolean isValidStock(int stock) {
        if (stock < 0) {
            System.out.println("Stock can't be negative. Please enter 0 or more.");
            return false;
        }
        return true;
    }
    public static boolean isValidId(int id) {
        if (id <= 0) {
            System.out.println("The product ID must be a positive number.");
            return false;
        }
        return true;
    }
    public static boolean isValidProduct(int id, String name, double price, int stock) {
        return isValidId(id) && isValidName(name) && isValidPrice(price) && isValidStock(stock);
    }
}