package addition;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileLoader {

    public static String getFileName(String fileName) {
        return "./" + fileName;
    }

    public static List<String> getFilesNames(String path) {
        try {
            return Files.walk(Paths.get(getFileName(path)))
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.getStackTrace();
            return null;
        }
    }

    public static void clearFolder(String path) {
        try {
            Files.walk(Paths.get(getFileName(path)))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
    public static List<List<String>> getFilesNamesGroup(String path, List<String> finished) {
        List<String> filesNames = Objects.requireNonNull(getFilesNames(path)).stream()
                .filter(fileName-> !finished.contains(getSingleName(fileName)))
                .toList();
        return makeListOfList(filesNames, AppProperties.NUMBER_PHOTO_IN_GROUP, true);
    }

    public static String getSingleName(String fullName) {
        if(fullName ==  null) return "";
        return fullName.substring(fullName.lastIndexOf("\\") + 1, fullName.lastIndexOf("."))
                .substring(fullName.lastIndexOf("/") + 1);
    }

    public static List<String> getAllSingleNames(List<String> fullNames) {
        return fullNames.stream()
                .map(name -> name.substring(name.lastIndexOf("\\") + 1, name.lastIndexOf("."))
                        .substring(name.lastIndexOf("/") + 1))
                .toList();
    }

    public static List<List<String>> makeListOfList(List<String> buttonsName, int numberInRow, boolean remFirst) {
        List<List<String>> buttonsNameRows = new ArrayList<>();
        int rem = buttonsName.size() % numberInRow;
        int i = 0;
        while (i < buttonsName.size()) {
            List<String> row = new ArrayList<>();
            for (int j = 0; (j < numberInRow && i < buttonsName.size()); j++) {
                row.add(buttonsName.get(i));
                i++;
                if (remFirst && rem != 0 && i == rem) break;
            }
            buttonsNameRows.add(row);
        }
        return buttonsNameRows;
    }

    public static void writeGoods() {
        try(ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream("goods.bin"))) {
            oos.writeObject(AppProperties.get().goods);
        } catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void readGoods() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("goods.bin"))) {
            System.out.println( AppProperties.get().goods);
            AppProperties.get().goods = (Goods) ois.readObject();
            System.out.println( AppProperties.get().goods);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void writeFirstDay() {
        try(ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream("first_day.bin"))) {
            oos.writeObject(AppProperties.get().firstDay);
        } catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void readFirstDay() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("first_day.bin"))) {
            AppProperties.get().setFirstDay((LocalDate) ois.readObject());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void writeLastDay() {
        try(ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream("last_day.bin"))) {
            oos.writeObject(AppProperties.get().lastDay);
        } catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void readLastDay() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("last_day.bin"))) {
            AppProperties.get().setLastDay((LocalDate) ois.readObject());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static String upLoadFile(Long chatId, String fileId, String folder) {
        try {
            URL url = new URL("https://api.telegram.org/bot" + AppProperties.BOT_TOKEN + "/getFile?file_id=" + fileId);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

            String fileResponse = bufferedReader.readLine();
            JSONObject response = new JSONObject(fileResponse);
            JSONObject result = response.getJSONObject("result");
            String path = result.getString("file_path");

            String fileName = chatId.toString() + "_" + path.substring(path.lastIndexOf("/") + 1);
            File localFile = new File(getFileName(folder + "/" + fileName));
            System.out.println(localFile.getAbsolutePath());
            InputStream inputStream = new URL("https://api.telegram.org/file/bot"
                    + AppProperties.BOT_TOKEN + "/" + path).openStream();

            FileUtils.copyInputStreamToFile(inputStream, localFile);
            bufferedReader.close();
            inputStream.close();
            return "Файл отправлен";
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return "Ошибка отправки файла. Возможно файл слишком большой.";
        }
    }
}
