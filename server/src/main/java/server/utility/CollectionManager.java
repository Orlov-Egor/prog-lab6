package server.utility;

import java.time.LocalDateTime;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.Comparator; 

import data.SpaceMarine;
import data.Weapon;
import exceptions.CollectionIsEmptyException;
import data.MeleeWeapon;

/**
 * Operates the collection itself.
 */
public class CollectionManager {
    private NavigableSet<SpaceMarine> marinesCollection =  new TreeSet<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private FileManager fileManager;

    public CollectionManager(FileManager fileManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.fileManager = fileManager;
        
        loadCollection();
    }
    
    /**
     * @return The collecton itself.
     */
    public NavigableSet<SpaceMarine> getCollection() {
        return marinesCollection;
    }

    /**
     * @return Last initialization time or null if there wasn't initialization.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Last save time or null if there wasn't saving.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        return marinesCollection.getClass().getName();
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize() {
        return marinesCollection.size();
    }

    /**
     * @return The first element of the collection or null if collection is empty.
     */
    public SpaceMarine getFirst() {
        return marinesCollection.stream().findFirst().orElse(null);
    }

    /**
     * @param id ID of the marine.
     * @return A marine by his ID or null if marine isn't found.
     */
    // public SpaceMarine getById(Long id) {
    //     return marinesCollection.stream().filter(marine -> marine.getId()==id);
    // }

    public SpaceMarine getById(Long id) {
        return marinesCollection.stream().filter(marine -> marine.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * @param marineToFind A marine who's value will be found.
     * @return A marine by his value or null if marine isn't found.
     */
    public SpaceMarine getByValue(SpaceMarine marineToFind) {
        return marinesCollection.stream().filter(marine -> marine.equals(marineToFind)).findFirst().orElse(null);
    }

    /**
     * @return Sum of all marines' health or 0 if collection is empty.
     */
    public double getSumOfHealth() {

        double sumOfHealth = marinesCollection.stream()
        .reduce(0.0, (sum,p) -> sum+=p.getHealth(), (sum1,sum2) -> sum1+sum2);
        return sumOfHealth;
    }

    /**
     * @return Marine, who has max melee weapon.
     * @throws CollectionIsEmptyException If collection is empty.
     */
    public String maxByMeleeWeapon() throws CollectionIsEmptyException {
        if (marinesCollection.isEmpty()) throw new CollectionIsEmptyException();
        MeleeWeapon maxMelleWeapon = marinesCollection.stream().map(marine -> marine.getMeleeWeapon())
            .max(Comparator.comparing(String::valueOf)).get();
        return marinesCollection.stream()
            .filter(marine -> marine.getMeleeWeapon().equals(maxMelleWeapon)).findFirst().toString();
    }

    /**
     * @param weaponToFilter Weapon to filter by.
     * @return Information about valid marines or empty string, if there's no such marines.
     */
    public String weaponFilteredInfo(Weapon weaponToFilter) {
        String info = marinesCollection.stream().filter(marine -> marine.getWeaponType().equals(weaponToFilter))
        .reduce("", (sum,m) -> sum+= m +"\n\n", (sum1,sum2) -> sum1+sum2);
        return info;
    }

    /**
     * Remove marines greater than the selected one.
     * @param marineToCompare A marine to compare with.
     */
    public void removeGreater(SpaceMarine marineToCompare) {
        marinesCollection.removeIf(marine -> marine.compareTo(marineToCompare) > 0);
    }

    /**
     * Adds a new marine to collection.
     * @param marine A marine to add.
     */
    public void addToCollection(SpaceMarine marine) {
        marinesCollection.add(marine);
    }

    /**
     * Removes a new marine to collection.
     * @param marine A marine to remove.
     */
    public void removeFromCollection(SpaceMarine marine) {
        marinesCollection.remove(marine);
    }

    /**
     * Clears the collection.
     */
    public void clearCollection() {
        marinesCollection.clear();
    }

    /**
     * Generates next ID. It will be (the bigger one + 1).
     * @return Next ID.
     */
    public Long generateNextId() {
        if (marinesCollection.isEmpty()) return 1L;
        return marinesCollection.last().getId() + 1;
    }

    /**
     * Saves the collection to file.
     */
    public void saveCollection() {
            fileManager.writeCollection(marinesCollection);
            lastSaveTime = LocalDateTime.now();
    }

    /**
     * Loads the collection from file.
     */
    private void loadCollection() {
        marinesCollection = fileManager.readCollection();
        lastInitTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        if (marinesCollection.isEmpty()) return "Коллекция пуста!";

        String info = "";
        for (SpaceMarine marine : marinesCollection) {
            info += marine;
            if (marine != marinesCollection.last()) info += "\n\n";
        }
        return info;
    }
}
