package bank.server;

import bank.bankieren.CentraleBank;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.util.Properties;

/**
 * Created by Nekkyou on 17-1-2016.
 */
public class CentraleBankServer extends Application
{
    private CentraleBank centraleBank;
    private BalieServerForCentralBank balieServer;
    private BalieServerForCentralBank secondBalieServer;


    @Override
    public void start(Stage primaryStage) throws Exception
    {
        centraleBank = new CentraleBank();

        createRMI();

        balieServer = new BalieServerForCentralBank();
        balieServer.setCentraleBank(centraleBank);
        balieServer.start(new Stage());

        secondBalieServer = new BalieServerForCentralBank();
        secondBalieServer.setCentraleBank(centraleBank);
        secondBalieServer.start(new Stage());


    }

    public void createRMI() {
        FileOutputStream out = null;
        try {
            String address = java.net.InetAddress.getLocalHost().getHostAddress();
            int port = 1099;
            Properties properties = new Properties();
            String rmiCentraleBank = address + ":" + port + "/" + "cb";
            properties.setProperty("cb", rmiCentraleBank);
            out = new FileOutputStream("cb" + ".props");
            properties.store(out, null);
            out.close();
            java.rmi.registry.LocateRegistry.createRegistry(port);

            Naming.rebind("cb", centraleBank);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {launch(args);}
}
