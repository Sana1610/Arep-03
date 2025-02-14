package arep.webserver;

import arep.webserver.annotations.*;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class HelloService {

    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator
            + "src" + File.separator
            + "main" + File.separator
            + "resources" + File.separator;

    static {
        StaticFiles.setLocation(WEB_ROOT); // Configura la ubicación de los archivos estáticos
    }

    @GetMapping("/hello")
    public static String hello() {
        return "Hello World";
    }

    @GetMapping("/time")
    public static String time() {
        return "The current time is: " + java.time.LocalTime.now();
    }

    @GetMapping("/greeting")
    public static String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }

    @GetMapping("/uuid")
    public static String uuid() {
        return "Your unique identifier is: " + UUID.randomUUID().toString();
    }

    @GetMapping("/bye")
    public static String bye() {
        return "Bye!";
    }

    @GetMapping("/dayofweek")
    public static String dayOfWeek() {
        return "Today is: " + java.time.LocalDate.now().getDayOfWeek();
    }

    @GetMapping("/")
    public static String serveIndex() {
        try {
            return StaticFiles.serveStaticFile("index.html");
        } catch (IOException e) {
            e.printStackTrace();
            return "HTTP/1.1 500 Internal Server Error\r\n\r\n";
        }
    }

    @GetMapping("/staticfile")
    public static String serveStatic(@RequestParam(value = "file", defaultValue = "index.html") String fileName) {
        try {
            return StaticFiles.serveStaticFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return "HTTP/1.1 500 Internal Server Error\r\n\r\n";
        }
    }
}