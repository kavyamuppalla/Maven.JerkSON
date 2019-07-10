package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ItemParser {

    private int errorCount = 0;

    public List<Item> parseItemList(String valueToParse) {

        List<Item> items = new ArrayList<>();
        Pattern itemPattern = Pattern.compile("##", Pattern.CASE_INSENSITIVE);
        String[] itemStrings = itemPattern.split(valueToParse);

        for(String itemString : itemStrings) {
            try {
                items.add(parseSingleItem(itemString+"##"));
            } catch (ItemParseException itemParseException) {
                System.out.println("Failed to parse === " + itemString);
                itemParseException.printStackTrace();
                errorCount++;
            }
        }

        return items;
    }

    public Item parseSingleItem(String singleItem) throws ItemParseException {

        String name = null;
        String type = null;
        String expiration = null;
        Double price = null;

        Pattern fieldPattern = Pattern.compile(";");
        Pattern nameValuePattern = Pattern.compile("[@:\\^*%]");
        singleItem = singleItem.substring(0, singleItem.length()-2);

        String[] fieldStrings = fieldPattern.split(singleItem);
        for(String fieldString: fieldStrings) {
            String[] nameValues = nameValuePattern.split(fieldString);
            if (nameValues[0].equalsIgnoreCase("name")) {
                name = (nameValues.length > 1 && nameValues[1] != null) ? nameValues[1].toLowerCase() : null;
            }
            else if (nameValues[0].equalsIgnoreCase("type")) {
                type = (nameValues.length > 1 && nameValues[1] != null) ? nameValues[1].toLowerCase() : null;
            }
            else if (nameValues[0].equalsIgnoreCase("expiration")) {
                expiration = (nameValues.length > 1 && nameValues[1] != null) ? nameValues[1].toLowerCase() : null;
            }
            else if (nameValues[0].equalsIgnoreCase("price")) {
                price = (nameValues.length > 1 && nameValues[1] != null) ? Double.parseDouble(nameValues[1]) : null;
            }
        }
        if (name == null || type == null || expiration == null || price == null) {
            throw new ItemParseException();
        }
        return new Item(name, price, type, expiration);

    }

    public int getErrorCount() {
        return errorCount;
    }
}
