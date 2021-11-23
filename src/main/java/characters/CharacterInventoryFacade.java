package characters;

import constants.Constants;
import constants.Observer;
import items.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A facade class that provides a unified interface for Characters and Inventory interactions.
 * It is assumed that the Character and Inventory instances passed into CharacterInventoryFacade are linked:
 * the Inventory instance being passed in is the inventory of the Character instance.
 */
public class CharacterInventoryFacade implements ItemCheckable {

    private final InventoryManager inventory;
    private CharacterManager character;
    private final ItemList itemList = new ItemList();
    private List<Observer> observers;

    public CharacterInventoryFacade(Inventory inventory, GameCharacter character, List<Observer> observers){
        this.inventory = new InventoryManager(inventory);
        this.character = new CharacterManager(character);
        this.observers = observers;
    }

    /**
     * Remove the specified item from the inventory and call the corresponding method in CharacterManager.
     * @param itemName the item being consumed
     */
    public void consumeItem(String itemName){
        inventory.removeItem(itemName);
        Item item = itemList.getItem(itemName);
        if (item instanceof HealingItem){
            character.consumeHeal(itemName);
        }
        else if(item instanceof StatusItem) {
            character.consumeStatus(itemName);
        }
    }

    public PlayerCharacter getCharacter(){
        if (this.character.getCharacter() instanceof PlayerCharacter) {
            return (PlayerCharacter) this.character.getCharacter();
        }
        return null;
    }

    /**
     * Add an WeaponItem or ArmorItem back to the inventory and modify character accordingly.
     * @param itemName the item being unequipped
     */
    public void unequipItem(String itemName){
        Item item = itemList.getItem(itemName);

        if(item != Constants.DEFAULT_ARMOR && item != Constants.DEFAULT_WEAPON) {
            inventory.addItem(itemName, 1);
            if (item instanceof ArmorItem) {
                character.unequipArmor();
            } else if (item instanceof WeaponItem) {
                character.unequipWeapon();
            }
        }
    }

    /**
     * Remove the specified item from the inventory and call an equip method.
     * @param itemName the name of the item
     */
    public void equipItem(String itemName){
        Item item = itemList.getItem(itemName);
        inventory.removeItem(itemName);
        if (item instanceof ArmorItem){
            character.equipArmor(itemName);
        } else if(item instanceof WeaponItem){
            character.equipWeapon(itemName);
        }
    }

    public void addItem(String itemName, int quantity){
        inventory.addItem(itemName, quantity);
        for(Observer observer: this.observers){
            observer.update(this.itemList.getItem(itemName));
        }
    }

    @Override
    public boolean checkItem(String itemName){
        return inventory.checkItem(itemName);
    }

}
