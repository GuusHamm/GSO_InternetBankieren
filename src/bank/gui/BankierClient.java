




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bank.gui;

import bank.bankieren.IBank;
import bank.internettoegang.IBalie;
import bank.internettoegang.IBankiersessie;
import bank.bankieren.ICentraleBank;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frankcoenen
 */
public class BankierClient extends Application {
    
    private Stage stage;
    private final double MINIMUM_WINDOW_WIDTH = 390.0;
    private final double MINIMUM_WINDOW_HEIGHT = 500.0;
   // 

    @Override
    public void start(Stage primaryStage) throws IOException {
        
         try {
            stage = primaryStage;
            stage.setTitle("Bankieren");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            gotoBankSelect();
            
            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
     public IBalie connectToBalie(String bankName) {
//        try {
//            FileInputStream in = new FileInputStream(bankName+".props");
//            Properties props = new Properties();
//            props.load(in);
//            String rmiBalie = props.getProperty("balie");
//            in.close();
//
//            IBalie balie = (IBalie) Naming.lookup("rmi://" + rmiBalie);
//                        return balie;
//
//            } catch (Exception exc) {
//                exc.printStackTrace();
//                return null;
//            }

         //Because Central bank is being used and not a normal BalieServer, this needs to be edited
             try
             {
                 FileInputStream in = new FileInputStream("cb" + ".props");
                 Properties properties = new Properties();
                 properties.load(in);
                 String centralBankRmi = properties.getProperty("cb");
                 in.close();

                 //Get the Central bank.
                 ICentraleBank cb = (ICentraleBank) Naming.lookup("rmi://" + centralBankRmi);

                 for (IBank b : cb.getBanken()) {
                     if (b.getBalie() == null) {
                         System.out.println("Bank: " + b.getName() + " does not have a balie");
                     }
                     else {
                         System.out.println("Bank: " + b.getName() + " has a balie");
                     }
                 }

                 //Get the bank with the bankname
                 IBank bank = cb.getBank(bankName);

                 if (bank != null) {
                     IBalie balie = bank.getBalie();
                     return balie;
                 } else {
                     System.out.println("Bank is null");

                 }
             }
             catch (FileNotFoundException e)
             {
                 e.printStackTrace();
             }
             catch (IOException e)
             {
                 e.printStackTrace();
             }
             catch (NotBoundException e)
             {
                 e.printStackTrace();
             }
            //Bank has not been found or there was an error.
             return null;
         }
    

     protected void gotoBankSelect() {
        try {
            BankSelectController bankSelect = (BankSelectController) replaceSceneContent("BankSelect.fxml");
            bankSelect.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(BankierClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

     protected void gotoLogin(IBalie balie,String accountNaam) {
        try {
            LoginController login = (LoginController) replaceSceneContent("Login.fxml");
            login.setApp(this, balie, accountNaam);
        } catch (Exception ex) {
            Logger.getLogger(BankierClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
      protected void gotoOpenRekening(IBalie balie) {
        try {
            OpenRekeningController openRekeningController = (OpenRekeningController) replaceSceneContent("OpenRekening.fxml");
            openRekeningController.setApp(this,balie);
        } catch (Exception ex) {
            Logger.getLogger(BankierClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
      protected void gotoBankierSessie(IBalie balie, IBankiersessie sessie) {
        try {
            BankierSessieController sessionController = (BankierSessieController) replaceSceneContent("BankierSessie.fxml");
            sessionController.setApp(this, balie, sessie);
        } catch (Exception ex) {
            Logger.getLogger(BankierClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = BankierClient.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(BankierClient.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        } 
        Scene scene = new Scene(page, 800, 600);
       // scene.getStylesheets().add("bank/gui/ING.css");
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
