
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

public class CreateCounter {

    public static void main(String args[]) throws Exception {

        Path walletPath = Paths.get(System.getProperty("user.dir"));
        Path applicationPath = walletPath.getParent();
        Path rootPath = applicationPath.getParent();
        walletPath = Paths.get(applicationPath.toString(), "wallet");

        System.out.println(walletPath);
        System.out.println(applicationPath);
        System.out.println(rootPath);

        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP

        Path networkConfigPath = Paths.get(rootPath.toString(), "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.json");
        System.out.println(networkConfigPath);

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);

        // create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("FabCounter");

            contract.submitTransaction("createCounter", args[0], args[1], args[2]);
        }

    }
}