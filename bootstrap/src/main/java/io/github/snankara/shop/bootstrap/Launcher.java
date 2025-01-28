package io.github.snankara.shop.bootstrap;

import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;

public class Launcher {

    private static final int PORT = 8080;
    private UndertowJaxrsServer server;

    public static void main(String[] args) {
        //new Launcher().startOnPort(PORT); for e2e tests

        new Launcher().startOnDefaultPort();
    }

    public void startOnDefaultPort(){
        server = new UndertowJaxrsServer();
        startServer();
    }

    public void startOnPort(int port){
        server = new UndertowJaxrsServer().setPort(port);
        startServer();
    }

    public void stopServer(){
        server.stop();
    }

    private void startServer() {
        server.start();
        server.deploy(RestEasyUndertowShopApplication.class);
    }
}
