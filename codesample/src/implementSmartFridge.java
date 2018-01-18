/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codesample;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author RemyValery
 */
public class implementSmartFridge implements SmartFridgeManager {

    List<oneItem> allFridgeItems = new ArrayList<>(); // Let this array hold all fridge contents
    Set<Long> forgottenItems = new HashSet<>(); // List of forgotten Item types
    Set<Long> allItemTypes = new HashSet<>();
    Boolean allItemsEmpty = false;


    @Override
    public void handleItemRemoved( String itemUUID ){
        System.out.println("Item Removed");

        for(oneItem item: this.allFridgeItems){
            if(item.itemUUID.equals(itemUUID)){
                allFridgeItems.remove(item);
                break; // assuming only one item per itemUUID
            }
        }
        this.updateData();
    }

    @Override
    public void handleItemAdded( long itemType, String itemUUID, String name, Double fillFactor ){
        System.out.println("Item added " + name);
        this.allFridgeItems.add(new oneItem(itemType, itemUUID, name, fillFactor));
        this.updateData();
    }

    @Override
    public Object[] getItems( Double fillFactor ){
        List<Object> selectedItems = new ArrayList<>();
        //oneItem[] allItems = this.allItems();

        for(long type: this.allItemTypes){
            Double averageFillFactor = this.getFillFactor(type);
            // ad type to list only if average fill factor is less than specified value, and if type is not in list of forgotten items.
            if((averageFillFactor < fillFactor) && (!forgottenItems.contains(type))) selectedItems.add(new Object[]{type, averageFillFactor});
        }
        Object[] fill = new Object[selectedItems.size()];
        for(int i=0; i<selectedItems.size(); i++){
            fill[i] = selectedItems.get(i);
        }
        return (Object[]) fill;
    }

    @Override
    public Double getFillFactor( long itemType ){
        Double averageFillFactor = 0.0, totalFillFactor = 0.0;
        Integer numOfItems = 0;

        //oneItem[] allItems = this.allItems();
        for( oneItem item: this.allFridgeItems){
            if((item.itemType == itemType) && (item.fillFactor > 0) && !this.allItemsEmpty ){// consider non-Empty containers for this type when all containers are not empty
                totalFillFactor += item.fillFactor;
                numOfItems++;

            } else if(this.allItemsEmpty){
                // TODO when all containers are empty.
                System.out.println("All containers are empty, or No containers at all.");
            }
        }
        averageFillFactor = (numOfItems>0)? totalFillFactor / numOfItems : 0; // avoid dividing by zero

        return averageFillFactor;
    }

    @Override
    public void forgetItem( long itemType ){
        this.forgottenItems.add(itemType); // add forgoten item Type to a Set.
    }
    /**
     * Assume the fridge is made up of many oneItems. This method gets all items in the fridge.
     */
    public void updateData(){

        Integer numberOfEmptyItems = 0;
        List<Object> emptyItems = new ArrayList<>();

        for(oneItem item: this.allFridgeItems){
            if(item.fillFactor == 0 ){
                emptyItems.add(item);
                numberOfEmptyItems++;
            }
            allItemTypes.add(item.itemType);
        }
        this.allItemsEmpty = (allFridgeItems.size() == numberOfEmptyItems ); // if true, that means either All containers are empty or there are No containers at all in fridge.

        System.out.println("No of fridgeitems :  "+ allFridgeItems.size());
    }
    /**
     * Assume the structure of an item class
     */
    public class oneItem{
        long itemType;
        String itemUUID;
        String name;
        Double fillFactor;
        oneItem(long itemType, String itemUUID, String name, Double fillFactor){
            this.fillFactor = fillFactor;
            this.name = name;
            this.itemUUID = itemUUID;
            this.itemType = itemType;
        }
    }

    public static void main(String[] args){
        System.out.println("Welcome to this fridge implementation..");
        implementSmartFridge fridge = new implementSmartFridge();

        fridge.handleItemAdded(1122,"firstUID", "milk", 0.8);
        fridge.handleItemAdded(1123,"SecUID", "soda", 0.4);
        fridge.handleItemAdded(1124,"TrdUID", "ketchup", 0.5);
        fridge.handleItemAdded(1122,"FthUID", "milk", 0.4);
        System.out.println("frigde remove uid:firstUID " );
        fridge.getItems(0.7);
        fridge.updateData();
    }
}