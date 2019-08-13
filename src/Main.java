import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    private String RESULT = null;

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(setGrid(primaryStage), 800, 500);
        scene.getStylesheets().add(this.getClass().getResource("javafx.css").toExternalForm());

        primaryStage.setTitle("Renaming");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane setGrid(Stage stage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Programme de renaming");
        scenetitle.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Button btnFolder = new Button("Dossier de départ");
        btnFolder.setMinWidth(200);
        grid.add(btnFolder, 0, 1);
        Label labelFolderAffich = new Label();
        grid.add(labelFolderAffich, 1, 1);

        Label nFact = new Label();
        nFact.setMaxWidth(200);
        grid.add(nFact, 0, 2);

        Button btnValider = new Button("Valider");
        btnValider.setMinWidth(200);
        btnValider.setId("btnValider");
        grid.add(btnValider, 1, 2);

        btnFolder.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Dossier de destination");
            File selectedDirectory = directoryChooser.showDialog(stage);
            if (selectedDirectory != null) {
                labelFolderAffich.setText(selectedDirectory.getName());
                RESULT = selectedDirectory.getAbsolutePath();
            }
        }
        );

        btnValider.setOnAction(event -> {
            nFact.setText("");
            String res = rename(new File(RESULT));
            nFact.setText(res);
        });
        return grid;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private String rename(File folder){
        File newFolder = new File(folder.getPath()+"-pieces");
        if (!newFolder.mkdir())
            return "Le dossier destination existe déjà";

        for (File file : folder.listFiles()){
            try {
                copyFileUsingStream(file,new File(newFolder.getAbsolutePath()+"\\"+file.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (File file : newFolder.listFiles()){
            String str = newFolder.getAbsolutePath()+"\\"+file.getName().substring(0,file.getName().indexOf("-"))+file.getName().substring(file.getName().lastIndexOf("."));
            file.renameTo(new File(str));
        }
        return "Effectué";
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }
}

