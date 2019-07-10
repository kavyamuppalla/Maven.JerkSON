package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GroceryReporter {
    private final String originalFileText;

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
    }

    @Override
    public String toString() {

        ItemParser itemParser = new ItemParser();
        List<Item> items = itemParser.parseItemList(this.originalFileText);

        Map<String, Map<String, Integer>> itemMap = new LinkedHashMap<>();
        int errorCount = 0;
        for(Item item: items) {

            if(item.getName() == null) {
                errorCount++;
            } else {
                Map<String, Integer> countMap = itemMap.containsKey(item.getName()) ? itemMap.get(item.getName()) : new LinkedHashMap<>();
                int itemCount = countMap.containsKey("ITEM_COUNT") ? countMap.get("ITEM_COUNT") : 0;

                if (item.getPrice() != null) {
                    int priceCount = countMap.containsKey(item.getPrice().toString()) ? countMap.get(item.getPrice().toString()) : 0;
                    countMap.put(item.getPrice().toString(), ++priceCount);
                    countMap.put("ITEM_COUNT", ++itemCount);
                } else {
                    errorCount++;
                }

                itemMap.put(item.getName(), countMap);
            }

        }

        StringBuilder sb = new StringBuilder();
        for(String key : itemMap.keySet()) {
            sb.append("name:\t" + key.substring(0, 1).toUpperCase() + key.substring(1) +" \t\t seen: " + itemMap.get(key).get("ITEM_COUNT") + " times");
            sb.append("\n=================\n");
            itemMap.get(key).remove("ITEM_COUNT");
            for(String price: itemMap.get(key).keySet()) {
                sb.append("Price:\t" + price + "\t\t seen: " + itemMap.get(key).get(price) + " times");
                sb.append("\n---------------\n");
            }

        }

        if (errorCount > 0) {
            sb.append("Errors \t seen: " + errorCount + " times");
        }

        return sb.toString();
    }
}
