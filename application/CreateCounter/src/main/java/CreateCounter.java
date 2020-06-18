
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

public class CreateCounter {

    public static void main(String args[]) throws Exception {

        Path walletPath = Paths.get("wallet");
        System.out.println(walletPath);
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.json");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);

        // create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("FabCounter");

            //byte[] result;
            //if (args.length != 3) {
            //    String errorMessage = String.format("The argument length is wrong. It should have three arguments," +
            //            "but is given %d!", args.length);
            //    System.out.println(errorMessage);
            //    Throw new Exception(errorMessage);
            //}
            contract.submitTransaction("createCounter", args[0], args[1], args[2]);

            //result = contract.evaluateTransaction("queryCar", "CAR10");
            //System.out.println(new String(result));
        }
    }
}