package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Validate {
    private static final Logger LOGGER = Logger.getLogger(Validate.class.getName());

    public static class ValidationException extends Exception {
        private final List<String> errors;
        public ValidationException(List<String> errors) {
            super("Validation failed: " + String.join("; ", errors));
            this.errors = errors;
        }
        public List<String> getErrors() {
            return errors;
        }
    }
    private static final int maxNameLen = 50;
    private static final double minPrice = 0.01;
    private static final double MAX_PRICE = 10000;
    private static final int stockLimit = 100000;
    private static final LocalDate MIN_DATE = LocalDate.of(2000, 1, 1);

    public static void checkProductName(String productName) throws ValidationException {
        if (productName == null || productName.trim().isEmpty()) {
            LOGGER.warning("Product name is empty! Can't do that.");
            throw new ValidationException(List.of("Hey, the product name can't be empty! Please give it a proper name."));
        }

        String cleanedName = productName.trim();

        if (cleanedName.length() > maxNameLen) {
            String errorMsg = "Whoa, the name '" + cleanedName + "' is too long! Keep it under " + maxNameLen + " characters.";
            LOGGER.warning(errorMsg);
            throw new ValidationException(List.of(errorMsg));
        }

        for (char c : cleanedName.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != ' ' && c != '-') {
                throw new ValidationException(List.of("Oops, the name '" + cleanedName + "' has invalid characters. Stick to letters, numbers, spaces, and hyphens."));
            }
        }

        if (cleanedName.matches("\\d+")) {
            throw new ValidationException(List.of("A product name that's just numbers ('" + cleanedName + "')? That doesn't make sense. Give it a real name!"));
        }


        if (cleanedName.length() < 2) {
            LOGGER.warning("Product name '" + cleanedName + "' is too short. Needs to be at least 2 characters.");
            throw new ValidationException(List.of("The name '" + cleanedName + "' is too short. Needs to be at least 2 characters."));
        }
        if (cleanedName.trim().isEmpty()) {
            throw new ValidationException(List.of("Somehow the name is just spaces after trimming? That's not right."));
        }
    }

    public static void checkProductPrice(double priceValue) throws ValidationException {
        if (Double.isNaN(priceValue) || Double.isInfinite(priceValue)) {
            throw new ValidationException(List.of("Uh-oh, the price '" + priceValue + "' isn't a valid number. Let's fix that."));
        }

        if (priceValue < minPrice) {
            throw new ValidationException(List.of("The price " + priceValue + " is too low! It needs to be at least " + minPrice + "."));
        }

        if (priceValue > MAX_PRICE) {
            throw new ValidationException(List.of("Whoa, " + priceValue + " is way too expensive! Keep it under " + MAX_PRICE + ", please."));
        }

        String priceStr = String.format("%.4f", priceValue);
        int decimals = 0;
        if (priceStr.contains(".")) {
            decimals = priceStr.split("\\.")[1].length();
        }
        if (decimals > 2) {
            LOGGER.warning("Price " + priceValue + " has too many decimals (" + decimals + ").");
            throw new ValidationException(List.of("The price " + priceValue + " has too many decimal places (" + decimals + "). Let's keep it to 2 max."));
        }
    }

    public static void checkStockQty(int stockQty) throws ValidationException {
        if (stockQty < 0) {
            LOGGER.warning("Stock quantity " + stockQty + " is negative!");
            throw new ValidationException(List.of("Stock quantity can't be negative! You entered " + stockQty + "."));
        }

        if (stockQty > stockLimit) {
            throw new ValidationException(List.of("That's a lot of stock! " + stockQty + " is more than the max (" + stockLimit + "). Let's keep it reasonable."));
        }
    }

    public static void checkProductId(int productId) throws ValidationException {
        if (productId <= 0) {
            throw new ValidationException(List.of("The product ID needs to be a positive number, not " + productId + "."));
        }
    }

    public static void checkImportedDate(LocalDate date) throws ValidationException {
        if (date == null) {
            throw new ValidationException(List.of("Imported date cannot be null! Please provide a date."));
        }

        if (date.isBefore(MIN_DATE)) {
            throw new ValidationException(List.of("Imported date " + date + " is too old! Must be after " + MIN_DATE + "."));
        }

        if (date.isAfter(LocalDate.now())) {
            throw new ValidationException(List.of("Imported date " + date + " is in the future! That doesn't make sense."));
        }
    }

    public static void validateProductWithId(int productId, String name, double price, int stock, LocalDate date) throws ValidationException {
        checkProductId(productId);
        checkProductName(name);
        checkProductPrice(price);
        checkStockQty(stock);
        checkImportedDate(date);

        if (price > 500 && stock < 5) {
            LOGGER.warning("High price (" + price + ") but low stock (" + stock + ") for product ID " + productId + ".");
            throw new ValidationException(List.of("A high-priced product (" + price + ") should have at least 5 units in stock, not " + stock + "."));
        }
    }

    public static void validateProductWithoutId(String productName, double priceValue, int stockQty, LocalDate date) throws ValidationException {
        checkProductName(productName);
        checkProductPrice(priceValue);
        checkStockQty(stockQty);
        checkImportedDate(date);

        if (priceValue < 1.0 && stockQty < 20) {
            LOGGER.info("Low price (" + priceValue + ") but stock (" + stockQty + ") is also low. Might want to stock up!");
        }
    }

    public static boolean isValidName(String name) {
        try {
            checkProductName(name);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    public static boolean isValidPrice(double price) {
        try {
            checkProductPrice(price);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    public static boolean isValidStock(int stock) {
        try {
            checkStockQty(stock);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    public static boolean isValidId(int id) {
        try {
            checkProductId(id);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    public static boolean isValidImportedDate(LocalDate date) {
        try {
            checkImportedDate(date);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    public static boolean isValidProduct(int id, String name, double price, int stock, LocalDate date) {
        try {
            validateProductWithId(id, name, price, stock, date);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    public static boolean isValidProduct(String name, double price, int stock, LocalDate date) {
        try {
            validateProductWithoutId(name, price, stock, date);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }
}