
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

public class CreateCounter {

    public static void main(String args[]) throws Exception {

        Path applicationPath = Paths.get(System.getProperty("user.dir"));
        Path rootPath = applicationPath.getParent();
        Path walletPath = Paths.get(applicationPath.toString(), "wallet");

        System.out.println(walletPath);
        System.out.println(applicationPath);
        System.out.println(rootPath);

        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP

        Path networkConfigPath = Paths.get(rootPath.toString(), "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.json");
        System.out.println(networkConfigPath);

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);

        System.out.println("builder.identity success");
        // create a gateway connection
        try (Gateway gateway = builder.connect()) {
            System.out.println("connect success");
            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            System.out.println("channel access success");
            Contract contract = network.getContract("FabCounter");
            System.out.println("contract access success");

            contract.submitTransaction("createCounter", args[0], args[1], args[2]);
        }

    }
}