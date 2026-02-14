import java.util.concurrent.*;
import java.util.Set;
import java.util.HashSet;

public class InstanceConfinement {

    public static void main(String[] args) {
        Person gbenga = new Person("Gbenga Opeyemi");
        Person ifeoluwa = new Person("Oluwagbemiga Ifeoluwa");
        PersonSet myFamily = new PersonSet();

        myFamily.addPerson(gbenga);
        boolean boole = myFamily.containsPerson(ifeoluwa);

        System.out.println(boole);
    }
    
    static class Person {
        private final String name;

        public Person(String name) {
            this.name = name;
        }
    }

    public static class PersonSet{
        private final Set<Person> mySet = new HashSet<Person>();

        public synchronized void addPerson(Person p){
            mySet.add(p);
        }
        public synchronized boolean containsPerson(Person p){
            return mySet.contains(p);
        }
    }
}
