import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartServer {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:spring_server.xml");
        System.out.println("Server up and running!");
    }
}
