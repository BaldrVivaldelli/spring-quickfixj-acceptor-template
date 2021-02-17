package com.tecval.negociacion.backoffice.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tecval.negociacion.backoffice.core.ServerApplicationAdapter;

import lombok.extern.slf4j.Slf4j;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FieldConvertError;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.ThreadedSocketAcceptor;
import quickfix.mina.acceptor.DynamicAcceptorSessionProvider;

@Slf4j
@Configuration
public class FixAcceptorConfig {

	@Value("${quickfixj.server.config}")
	private String fileName;

	@Autowired
	private ServerApplicationAdapter application;

	@Bean
	public ThreadedSocketAcceptor threadedSocketAcceptor() {
		ThreadedSocketAcceptor acceptor = null;
		try {
			SessionSettings settings = new SessionSettings(new FileInputStream(fileName));
			MessageStoreFactory storeFactory = new FileStoreFactory(settings);
			LogFactory logFactory = new FileLogFactory(settings);
			MessageFactory messageFactory = new DefaultMessageFactory();
			final ThreadedSocketAcceptor threadedSocketacceptor = new ThreadedSocketAcceptor(application, storeFactory,
					settings, logFactory, messageFactory);

			settings.sectionIterator().forEachRemaining(s -> {
				InetSocketAddress address;
				try {
					address = getAcceptorSocketAddress(settings, s);
					DynamicAcceptorSessionProvider provider = new DynamicAcceptorSessionProvider(settings, s, application,
							storeFactory, logFactory, messageFactory);
					threadedSocketacceptor.setSessionProvider(address, provider);
				} catch (ConfigError | FieldConvertError e) {
					e.printStackTrace();
				}

			});

			acceptor = threadedSocketacceptor;
			
		} catch (ConfigError configError) {
			configError.printStackTrace();
		} catch (FileNotFoundException e) {
			log.error("archivo no encontrado");
		}
		return acceptor;
	}

	private InetSocketAddress getAcceptorSocketAddress(SessionSettings settings, SessionID sessionID)
			throws ConfigError, FieldConvertError {
		String acceptorHost = "0.0.0.0";
		if (settings.isSetting(sessionID, quickfix.Acceptor.SETTING_SOCKET_ACCEPT_ADDRESS)) {
			acceptorHost = settings.getString(sessionID, quickfix.Acceptor.SETTING_SOCKET_ACCEPT_ADDRESS);
		}
		int acceptorPort = (int) settings.getLong(sessionID, quickfix.Acceptor.SETTING_SOCKET_ACCEPT_PORT);

		InetSocketAddress address = new InetSocketAddress(acceptorHost, acceptorPort);
		return address;
	}

}
