package ru.itmo.websocket;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class Notifier {

    private final WebSocketHandler handler;

    public Notifier(WebSocketHandler handler) {
        this.handler = handler;
    }

    public void changed(String entity) {
        Runnable send = () -> handler.broadcast("{\"changed\":\"" + entity + "\"}");

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    send.run();
                }
            });
        } else {
            send.run();
        }
    }
}