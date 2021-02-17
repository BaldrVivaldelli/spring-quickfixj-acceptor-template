package com.tecval.negociacion.backoffice.entities;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration("configUserProp")
@ConfigurationProperties(prefix = "local.property")
public class AcceptorUserProperties {

	String username;
	String password;
}
