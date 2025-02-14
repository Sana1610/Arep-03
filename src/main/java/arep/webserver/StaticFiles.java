package arep.webserver;

import java.io.*;
import java.util.Base64;

public class StaticFiles {
    private static String staticFilesLocation;

    public static void setLocation(String location) {
        staticFilesLocation = location;
    }

    public static String serveStaticFile(String filePath) throws IOException {
        File file = new File(staticFilesLocation, filePath);
        if (file.exists()) {
            String contentType = getContentType(filePath);
            int fileLength = (int) file.length();

            if (contentType.startsWith("image")) {
                byte[] fileData = readFileData(file, fileLength);
                String base64Image = Base64.getEncoder().encodeToString(fileData);

                return "<!DOCTYPE html>\r\n"
                        + "<html>\r\n"
                        + "    <head>\r\n"
                        + "        <title>Image</title>\r\n"
                        + "    </head>\r\n"
                        + "    <body>\r\n"
                        + "        <center><img src=\"data:" + contentType + ";base64," + base64Image + "\" alt=\"image\"></center>\r\n"
                        + "    </body>\r\n"
                        + "</html>";
            } else {
                // Para otros tipos de archivos, devolver el contenido directamente
                FileInputStream fileIn = new FileInputStream(file);
                byte[] fileData = readFileData(file, fileLength);
                return new String(fileData);
            }
        } else {
            throw new FileNotFoundException("File not found: " + filePath);
        }
    }

    private static byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = new FileInputStream(file);
        byte[] fileData = new byte[fileLength];
        fileIn.read(fileData);
        fileIn.close();
        return fileData;
    }

    private static String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".html")) return "text/html";
        else if (fileRequested.endsWith(".css")) return "text/css";
        else if (fileRequested.endsWith(".js")) return "application/javascript";
        else if (fileRequested.endsWith(".png")) return "image/png";
        else if (fileRequested.endsWith(".jpg") || fileRequested.endsWith(".jpeg")) return "image/jpeg";
        return "text/plain";
    }
}