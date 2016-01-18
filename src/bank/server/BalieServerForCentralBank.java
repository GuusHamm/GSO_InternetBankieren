package bank.server;

import bank.bankieren.Bank;
import bank.gui.BankierClient;
import bank.internettoegang.Balie;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Nekkyou on 17-1-2016.
 * This class is there so i won't have to edit BalieServer, because BalieServer uses RMI
 */
public class BalieServerForCentralBank extends Application
{
    private Stage stage;
    private final double MINIMUM_WINDOW_WIDTH = 600.0;
    private final double MINIMUM_WINDOW_HEIGHT = 200.0;
    private String nameBank;
    private CentraleBank centraleBank;
    private Balie balie;


    @Override
    public void start(Stage primaryStage) throws Exception
    {
        try {
            stage = primaryStage;
            stage.setTitle("Bankieren");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            gotoBankSelect();

            primaryStage.show();
            //centraleBank = new CentraleBank();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoBankSelect() {
        try {
            BalieControllerForCentralBank bankSelect = (BalieControllerForCentralBank) replaceSceneContent("Balie.fxml");
            bankSelect.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(BankierClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = BalieServerForCentralBank.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(BalieServerForCentralBank.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page, 800, 600);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    public boolean startBalie(String bankName) {
        nameBank = bankName;
        try
        {
            System.out.println("Starting Balie");
            Bank bank = new Bank(nameBank, centraleBank);
            balie = new Balie(bank);
            System.out.println("Setting Balie at Bank: " + bank.getName());
            bank.setBalie(balie);
            centraleBank.addBank(bank);

            System.out.println("Started Balie");
            return true;
        }
        catch (RemoteException e)
        {
            System.out.println("Failed to start Balie");
            e.printStackTrace();
            return false;
        }
    }

    public void setCentraleBank(CentraleBank cb) {
        this.centraleBank = cb;
    }
}
