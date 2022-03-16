package model;

public interface Dog {
    String getName();
    Owner getOwner();
    int getAge();
    void introduceOwner();
    void learnWord(String word);
    void makeOwnerSpeak(String sentence);
    void bark();

    /**
     * Grow by some years
     * @param years
     */
    void grow(int years);

    /**
     * Grow and get the age
     * @param years
     * @return
     */
    int growAndGetAge(int years);

    void setOwner(Owner owner);

    Owner setAndGetOwner(Owner owner);

    Mode getMode();
}
