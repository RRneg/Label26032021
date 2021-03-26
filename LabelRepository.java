package main.java.com.minaev.crud.repository;

import main.java.com.minaev.crud.AppRunner;
import main.java.com.minaev.crud.model.Label;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LabelRepository {

    public static Path path = Paths.get("src/main/resources/files/Lables.txt");

    public static void createNewFileNIO() {
        try {
            if (!Files.exists(path))
                Files.createFile(path);
        } catch (IOException e) {
            System.out.println("Не удалось создать файл-репозиторий :" + e);
        }
    }

    public static Label getById(int id) {
        List<Label> allLabel = LabelRepository.getAll();
        Optional<Label> label = allLabel.stream().filter((a) -> a.getId() == id).findFirst();
        Label searchingLabel = label.orElse(new Label());
        return searchingLabel;
    }

    public static Label update(Label label, String category) {
        Label label1 = label;
        label1.setCategory(category);
        LabelRepository.deleteById(label.getId());
        LabelRepository.saveLabel(label1);
        LabelRepository.reSaveListLabel(LabelRepository.getAll());
        return label1;
    }

    public static void reSaveListLabel(ArrayList<Label> labels) {
        try {
            Files.delete(path);
            LabelRepository.createNewFileNIO();
        } catch (IOException e) {
            System.out.println("Не удалось обновить файл :" + e);
        }
        List<Label> forSave = labels.stream().distinct().
                sorted((a, b) -> Integer.compare(a.getId(), b.getId())).
                collect(Collectors.toList());
        for (Label label : forSave) {
            LabelRepository.saveLabel(label);
        }
    }


    public static void deleteById(int id) {
        ArrayList<Label> allLabel = LabelRepository.getAll();
        Optional<Label> label = allLabel.stream().filter((a) -> a.getId() == id).findFirst();
        if (label.isPresent()) {
            Label delLabel = label.get();
            allLabel.remove(delLabel);
            LabelRepository.reSaveListLabel(allLabel);
        } else {
            System.out.println("Элемента с таким id не существует");
        }


    }

    public static ArrayList<Label> getAll() {
        ArrayList<Label> listLabel = new ArrayList<>();
        try {
            List<String> list = Files.readAllLines(path);
            for (int i = 0; i < list.size(); i++) {
                listLabel.add(Label.stringToLabel(list.get(i)));
            }
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл-хранилище: " + e);
        }
        return listLabel;
    }

    public static void saveLabel(Label label) {
        String lableForSave = Label.labelToString(label) + System.getProperty("line.separator");
        try {
            Files.writeString(path, lableForSave, StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.out.println("Не удалось записать в файл(((: " + e);
        }

    }

}


