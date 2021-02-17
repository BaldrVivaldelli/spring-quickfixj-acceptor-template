package com.tecval.negociacion.backoffice.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import quickfix.ConfigError;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.ThreadedSocketAcceptor;

@Slf4j
@Component
public class AppLogonListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ThreadedSocketAcceptor threadedSocketAcceptor;

    private boolean acceptorStarted = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent refreshedEvent) {
        startFixAcceptor();
    }

    private void startFixAcceptor (){
        if(!acceptorStarted) {
            try {
            	threadedSocketAcceptor.start();
                log.info("--------- ThreadedSocketAcceptor started ---------");
                acceptorStarted = true;
            } catch (ConfigError configError) {
                configError.printStackTrace();
                log.error("--------- ThreadedSocketAcceptor ran into an error ---------");
            }
        } else {
            logon();
        }
    }

    private void logon (){
        if(threadedSocketAcceptor.getSessions() != null && threadedSocketAcceptor.getSessions().size() > 0) {
            for (SessionID sessionID: threadedSocketAcceptor.getSessions()) {
                Session.lookupSession(sessionID).logon();
            }
            log.info("--------- ThreadedSocketAcceptor logged on to sessions. Size: " + threadedSocketAcceptor.getSessions().size() + " ---------");
        }
    }

    @Scheduled(fixedRate = 5000)
    public void serverStatus(){
        log.info("Server Status | Logged on: {}. Current Time: {}", threadedSocketAcceptor.isLoggedOn(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
    }
}