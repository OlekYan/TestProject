package Things;

import org.springframework.stereotype.Component;

@Component
public class AlienP {
    private int age;
    public AlienP() {
        System.out.println("I'm private alien");

    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
