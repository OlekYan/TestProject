package Things;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Alien {
    public int age;
    public Alien (){
        System.out.println("I'm created");
    }
    @Autowired
    Laptop laptop;
    public void code(){
        System.out.println("I'm here.");
        laptop.compile();
    }
}
