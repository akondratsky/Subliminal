package subliminal.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import subliminal.model.Referented;
import subliminal.model.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Statistics {

    @FXML
    TextArea textArea;

    public void initialize() {
        textArea.setText(getStatisticsString(MainAppController.getTests()));
    }

    public static String getStatisticsString(ArrayList<Test> tests) {

        if (tests==null||tests.size()==0) return "Нет результатов для подсчета статистики";

        HashMap<Referented, Result> results = new HashMap<>(Referented.values().length-1);
        for (int i=0; i<Referented.values().length-1; i++)
            results.put(Referented.values()[i], new Result());

        for (Test test : tests) {
            Referented enigmated = test.getEnigmated();
            Result result = results.get(enigmated);
            result.tests++;
            if (enigmated.equals(test.getNamed())) {
                result.guessed++;
            }
        }

        StringBuilder str = new StringBuilder();

        for (Map.Entry<Referented, Result> pair : results.entrySet()) {
            str.append(String.format("%-10s", pair.getKey().getName()));
            str.append("\t Загадано: " + pair.getValue().tests + "\t Угадано: " + pair.getValue().guessed);
            int percents = 0;
            try {
                percents = Math.round((float)pair.getValue().guessed/pair.getValue().tests*100);
            } catch (ArithmeticException ex) {}

            str.append("\t Процент угадывания: " + percents);
            str.append("\r\n");
        }

        int enigmated = 0;
        int guessed = 0;
        for (Map.Entry<Referented, Result> pair : results.entrySet()) {
            enigmated += pair.getValue().tests;
            guessed  += pair.getValue().guessed;
        }
        int percents = 0;
        try {
            percents = Math.round((float)guessed/enigmated*100);
        } catch (ArithmeticException ex) {}

        str.append("\r\nОбщая статистика:\r\n");
        str.append("Загадано: " + enigmated + "\t Угадано: " + guessed);
        str.append("\t Процент угадывания: " + percents);


        return str.toString();
    }

}

class Result {
    public int tests = 0;
    public int guessed = 0;
}
