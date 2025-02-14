package arep.webserver;

import java.io.*;
import java.lang.reflect.Parameter;
import java.net.*;
import arep.webserver.annotations.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SimpleWebServer {
    private static Object serviceInstance;
    private static final Map<String, Method> services = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Class<?> c = Class.forName("arep.webserver.HelloService");
        if (c.isAnnotationPresent(RestController.class)) {
            serviceInstance = c.getDeclaredConstructor().newInstance();
            Method[] methods = c.getDeclaredMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(GetMapping.class)) {
                    String key = m.getAnnotation(GetMapping.class).value();
                    services.put(key, m);
                }
            }
        }

        try (ServerSocket serverSocket = new ServerSocket(35000)) {
            System.out.println("Server is running on port 35000...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleRequest(clientSocket);
                }
            }
        }
    }

    private static void handleRequest(Socket clientSocket) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String requestLine = reader.readLine();
            if (requestLine == null) return;

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) return;

            String path = requestParts[1];
            System.out.println("Requested Path: " + path);

            // Redirigir la ruta raíz a index.html
            if (path.equals("/")) {
                path = "/index.html";
            }

            // Extraer parámetros de la consulta (query parameters)
            Map<String, String> queryParams = new HashMap<>();
            if (path.contains("?")) {
                String[] pathAndQuery = path.split("\\?", 2);
                path = pathAndQuery[0];
                String query = pathAndQuery[1];
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        queryParams.put(keyValue[0], keyValue[1]);
                    }
                }
            }

            // Buscar el método correspondiente en HelloService
            Method serviceMethod = services.get(path);
            if (serviceMethod != null) {
                try {
                    // Extraer los argumentos del método
                    Object[] methodParams = extractArguments(serviceMethod, queryParams);
                    // Invocar el método con los argumentos
                    Object response = serviceMethod.invoke(serviceInstance, methodParams);
                    // Construir la respuesta HTTP
                    String httpResponse = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: text/plain\r\n"
                            + "Content-Length: " + response.toString().length() + "\r\n"
                            + "\r\n"
                            + response;
                    outputStream.write(httpResponse.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                    outputStream.write("HTTP/1.1 500 Internal Server Error\r\n\r\n".getBytes());
                }
            } else {
                // Intentar servir un archivo estático
                try {
                    String filePath = path.substring(1); // Elimina la barra inicial
                    String fileContent = StaticFiles.serveStaticFile(filePath);
                    String contentType = getContentType(filePath); // Obtener el tipo de contenido
                    String httpResponse = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: " + contentType + "\r\n"
                            + "Content-Length: " + fileContent.length() + "\r\n"
                            + "\r\n"
                            + fileContent;
                    outputStream.write(httpResponse.getBytes());
                } catch (IOException e) {
                    // Archivo no encontrado
                    String httpResponse = "HTTP/1.1 404 Not Found\r\n"
                            + "Content-Type: text/html\r\n"
                            + "\r\n"
                            + "<html><body><h1>File Not Found</h1></body></html>";
                    outputStream.write(httpResponse.getBytes());
                }
            }
        } finally {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
        }
    }

    private static String getContentType(String filePath) {
        if (filePath.endsWith(".html")) return "text/html";
        else if (filePath.endsWith(".css")) return "text/css";
        else if (filePath.endsWith(".js")) return "application/javascript";
        else if (filePath.endsWith(".png")) return "image/png";
        else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) return "image/jpeg";
        return "text/plain";
    }

    private static Object[] extractArguments(Method method, Map<String, String> queryParams) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                RequestParam annotation = parameters[i].getAnnotation(RequestParam.class);
                String paramName = annotation.value();
                String value = queryParams.getOrDefault(paramName, annotation.defaultValue());
                args[i] = value;
            }
        }
        return args;
    }
}