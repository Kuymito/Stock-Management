package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
public class Validate {
    private static final int MAX_NAME_LENGTH = 50;
    private static final double MAX_PRICE = 10000.0;
    private static final int MAX_STOCK = 100000;
    private static final String NAME_REGEX = "^[a-zA-Z0-9\\s-]+$";
    private static Pattern namePattern;
    static {
        try {
            namePattern = Pattern.compile(NAME_REGEX);
        } catch (PatternSyntaxException e) {
            throw new IllegalStateException("Failed to compile name validation regex: " + NAME_REGEX, e);
        }
    }
    public static void validateName(String name) throws ValidationException {
        List<String> errors = new ArrayList<>();
            String trimmedName = name.trim();
                if (trimmedName.length() > MAX_NAME_LENGTH) {
                }
                if (!namePattern.matcher(trimmedName).matches()) {
                }
            }
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
    public static void validatePrice(double price) throws ValidationException {
        List<String> errors = new ArrayList<>();
        } else {
            if (price <= 0) {
                errors.add("Price must be greater than 0. Got: " + price);
            }
            if (price > MAX_PRICE) {
                errors.add("Price cannot exceed " + MAX_PRICE + ". Got: " + price);
            }
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
    public static void validateStock(int stock) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (stock < 0) {
            errors.add("Stock quantity cannot be negative. Got: " + stock);
        }
        if (stock > MAX_STOCK) {
            errors.add("Stock quantity cannot exceed " + MAX_STOCK + ". Got: " + stock);
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
    public static void validateId(int id) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (id <= 0) {
            errors.add("Product ID must be a positive number. Got: " + id);
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
    public static void validateProduct(int id, String name, double price, int stock) throws ValidationException {
        List<String> errors = new ArrayList<>();
        try {
            validateId(id);
        } catch (ValidationException e) {
            errors.addAll(e.getErrors());
        }

        try {
            validateName(name);
        } catch (ValidationException e) {
            errors.addAll(e.getErrors());
        }

        try {
            validatePrice(price);
        } catch (ValidationException e) {
            errors.addAll(e.getErrors());
        }
        try {
            validateStock(stock);
        } catch (ValidationException e) {
            errors.addAll(e.getErrors());
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
    public static void validateProduct(String name, double price, int stock) throws ValidationException {
        List<String> errors = new ArrayList<>();
        try {
            validateName(name);
        } catch (ValidationException e) {
            errors.addAll(e.getErrors());
        }
        try {
            validatePrice(price);
        } catch (ValidationException e) {
            errors.addAll(e.getErrors());
        }
        try {
            validateStock(stock);
        } catch (ValidationException e) {
            errors.addAll(e.getErrors());
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
    public static boolean isValidName(String name) {
        try {
            validateName(name);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }
    public static boolean isValidPrice(double price) {
        try {
            validatePrice(price);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }
    public static boolean isValidStock(int stock) {
        try {
            validateStock(stock);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }
    public static boolean isValidId(int id) {
        try {
            validateId(id);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }
    public static boolean isValidProduct(int id, String name, double price, int stock) {
        try {
            validateProduct(id, name, price, stock);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }
    public static boolean isValidProduct(String name, double price, int stock) {
        try {
            validateProduct(name, price, stock);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }
}