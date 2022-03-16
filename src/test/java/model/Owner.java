package model;

public class Owner {

    String name;
    int age;

    public Owner(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void introduceMyself() {
        System.out.println(String.format("Hi I am %s I am %i years old.", this.name, this.age));
    }

    public void speak(String sentence) {
        System.out.println(sentence);
    }
}
