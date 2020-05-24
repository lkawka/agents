package com.pw.agents;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import lombok.SneakyThrows;

public class App {

	@SneakyThrows
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.setProperty(Profile.GUI, Boolean.TRUE.toString());
		AgentContainer mainContainer = Runtime.instance().createMainContainer(new ProfileImpl(properties));

		//		mainContainer.acceptNewAgent(DIRECTORY_FACILITATOR, directoryFacilitatorAgent).start();
	}
}
