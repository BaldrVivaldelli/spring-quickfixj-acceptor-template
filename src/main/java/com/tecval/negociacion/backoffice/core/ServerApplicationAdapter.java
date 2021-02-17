package com.tecval.negociacion.backoffice.core;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix50.SecurityListRequest;

@Slf4j
@Component
public class ServerApplicationAdapter extends MessageCracker implements Application {

	@Override
	public void onCreate(SessionID sessionId) {
		   log.info("--------- onCreate ---------");
	}

	@Override
	public void onLogon(SessionID sessionId) {
		   log.info("--------- onLogon ---------");
	}

	@Override
	public void onLogout(SessionID sessionId) {
		  log.info("--------- onLogout ---------");
	}

	@Override
	public void toAdmin(Message message, SessionID sessionID) {
		  log.info("--------- toAdmin ---------");
		  log.info(message.toString());
	}

	@Override
	public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		 log.info("--------- fromAdmin ---------");
		 log.info(message.toString());
	}

	@Override
	public void toApp(Message message, SessionID sessionID) throws DoNotSend {
		 log.info("--------- toApp ---------");
		 log.info(message.toString());
	}

	@Override
	public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		 log.info("--------- fromApp ---------");
		 log.info(message.toString());
		 crack(message, sessionID);
	}
	
	@Handler
	public void securityListRequestHandler(SecurityListRequest message, SessionID sessionID) {
		log.info("--------- securityListRequestHandler ---------");
		log.info(message.toString());		
	}


}
