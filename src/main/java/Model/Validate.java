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

    private Validate() {}  // Private constructor to prevent instantiation

    // Validate that an object is not null
    public static void notNull(Object obj, String message) throws ValidationException {
        if (obj == null) {
            throw new ValidationException(List.of(message));
        }
    }

    static {
        try {
            namePattern = Pattern.compile(NAME_REGEX);
        } catch (PatternSyntaxException e) {
            throw new IllegalStateException("Failed to compile name validation regex: " + NAME_REGEX, e);
        }
    }

    public static void validateName(String name) throws ValidationException {
        List<String> errors = new ArrayList<>();

        try {
            notNull(name, "Product name cannot be null.");
        } catch (ValidationException e) {
            errors.addAll(e.getErrors());
        }

        if (name != null) {
            String trimmedName = name.trim();
            if (trimmedName.isEmpty()) {
                errors.add("Product name cannot be empty or whitespace only.");
            } else {
                if (trimmedName.length() > MAX_NAME_LENGTH) {
                    errors.add("Product name is too long. Maximum length is " + MAX_NAME_LENGTH + " characters.");
                }
                if (!namePattern.matcher(trimmedName).matches()) {
                    errors.add("Product name contains invalid characters. Only letters, numbers, spaces, and hyphens are allowed.");
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    // Validate product price
    public static void validatePrice(double price) throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (Double.isNaN(price)) {
            errors.add("Price must be a valid number. Got: NaN");
        } else if (Double.isInfinite(price)) {
            errors.add("Price cannot be infinite. Got: " + price);
        } else {
            if (price <= 0) {
                errors.add("Price must be greater than 0. Got: " + price);
            }
            if (price > MAX_PRICE) {
                errors.add("Price cannot exceed " + MAX_PRICE + ". Got: " + price);
            }

            // Check for more than 2 decimal places
            if (Math.round(price * 100) != price * 100) {
                errors.add("Price can have maximum 2 decimal places. Got: " + price);
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    // Validate product stock
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

    // Validate product ID
    public static void validateId(int id) throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (id <= 0) {
            errors.add("Product ID must be a positive number. Got: " + id);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    // Validate complete product (with ID)
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

    // Validate complete product (without ID)
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