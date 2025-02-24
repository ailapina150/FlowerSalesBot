package addition;

import java.util.List;

public final class FlowerLoader {
    private static List<List<String>> filesNameGroup;

    private FlowerLoader() {
        filesNameGroup = FileLoader.getFilesNamesGroup(AppProperties.PEONY, AppProperties.get().goods.getFinished());
        filesNameGroup.addAll(FileLoader.getFilesNamesGroup(AppProperties.TULIP, AppProperties.get().goods.getFinished()));
    }

    public static synchronized List<List<String>> get() {
        if (filesNameGroup == null) {
            new FlowerLoader();
        }
        return filesNameGroup;
    }

    public static void remake() {
        filesNameGroup = FileLoader.getFilesNamesGroup(AppProperties.PEONY, AppProperties.get().goods.getFinished());
        filesNameGroup.addAll(FileLoader.getFilesNamesGroup(AppProperties.TULIP, AppProperties.get().goods.getFinished()));
    }

}