package networking.utils;


import controller.IController;
import networking.objectprotocol.ClientWorker;

import java.net.Socket;

public class ObjectConcurrentServer extends AbsConcurrentServer {
    private IController server;

    public ObjectConcurrentServer(int port, IController server) {
        super(port);
        this.server = server;
        System.out.println("Object - ObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientWorker worker = new ClientWorker(server, client);
        Thread tw = new Thread(worker);
        return tw;
    }


}
