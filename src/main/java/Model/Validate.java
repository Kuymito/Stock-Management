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
    // ពិនិត្យឈ្មោះ
    public static void validateName(String name) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) {
            errors.add("Product name cannot be empty or null.");
        } else {
            String trimmedName = name.trim();
            if (trimmedName.length() > MAX_NAME_LENGTH) {
                errors.add("Product name '" + trimmedName + "' is too long. Maximum length is " + MAX_NAME_LENGTH + " characters.");
            }
            try {
                if (!namePattern.matcher(trimmedName).matches()) {
                    errors.add("Product name '" + trimmedName + "' contains invalid characters. Only letters, numbers, spaces, and hyphens are allowed.");
                }
            } catch (Exception e) {
                throw new ValidationException(List.of("Error validating product name due to regex issue: " + e.getMessage()));
            }
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
    // ពិនិត្យតម្លៃ
    public static void validatePrice(double price) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (Double.isNaN(price) || Double.isInfinite(price)) {
            errors.add("Price must be a valid number. Got: " + price);
        } else {
            if (price <= 0) {
                errors.add("Price must be greater than 0. Got: " + price);
            }
            if (price > MAX_PRICE) {
                errors.add("Price cannot exceed " + MAX_PRICE + ". Got: " + price);
            }
            try {
                String priceStr = Double.toString(price);
                int decimalPlaces = priceStr.contains(".") ? priceStr.split("\\.")[1].length() : 0;
                if (decimalPlaces > 2) {
                    errors.add("Price has too many decimal places. Maximum is 2. Got: " + price);
                }
            } catch (Exception e) {
                throw new ValidationException(List.of("Error validating price due to formatting issue: " + e.getMessage()));
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
    // ពិនិត្យស្តុក
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
    // ពិនិត្យ ID
    public static void validateId(int id) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (id <= 0) {
            errors.add("Product ID must be a positive number. Got: " + id);
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
    // ពិនិត្យផលិតផលពេញលេញ (ជាមួយ ID)
    public static void validateProduct(int id, String name, double price, int stock) throws ValidationException {
        List<String> errors = new ArrayList<>();
        // ប្រមូលកំហុសពីរាល់ការពិនិត្យ
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
    // ពិនិត្យផលិតផលពេញលេញ (ដោយគ្មាន ID)
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