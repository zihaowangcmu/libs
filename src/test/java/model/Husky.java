package model;

public class Husky implements Dog {

    String name;
    int age;
    Owner owner;
    Mode mode = Mode.NORMAL;

    public Husky(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Husky(String name, int age, Owner owner) {
        this.name = name;
        this.age = age;
        this.owner = owner;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Owner getOwner() {
        return this.owner;
    }

    @Override
    public void introduceOwner() {
        this.owner.introduceMyself();
    }

    @Override
    public void learnWord(String word) {
        System.out.println("I learnt: " + word);
    }

    @Override
    public void makeOwnerSpeak(String sentence) {
        this.owner.speak(sentence);
    }

    @Override
    public void bark() {
        System.out.println("I am a husky. Wang!");
    }

    @Override
    public void grow(int years) {
        this.age += years;
    }

    /**
     * Grow and get the age
     *
     * @param years
     * @return
     */
    @Override
    public int growAndGetAge(int years) {
        this.age += years;
        return this.age;
    }

    @Override
    public void setOwner(Owner owner) {
        this.owner = owner;
        System.out.println(owner.age);
    }

    @Override
    public Owner setAndGetOwner(Owner owner) {
        this.owner = owner;
        System.out.println(owner.age);
        return this.owner;
    }

    @Override
    public Mode getMode() {
        return this.mode;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    /**
     * Destroy the house make a Husky happy.
     */
    public void destroyHouse() {
        this.mode = Mode.HAPPY;
    }

}
