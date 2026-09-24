package chatmqtt;

import java.util.Scanner;
import org.eclipse.paho.client.mqttv3.MqttException;

public class chat {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		//String broker = "tcp://test.mosquitto.org:1883";
		String broker = "tcp://localhost:1883"; // rodar mosquitto
		// MemoryPersistence persistence = new MemoryPersistence();

		System.out.println("=== CHATMQTT ===");
		System.out.print("Login: ");
		String userId = scanner.nextLine();

		try {
			// MqttClient client = new MqttClient(broker, userId, persistence);
			// Holds the set of options that control how the client connects to a server.
			// Sets whether the client and server should remember state across restarts and
			// reconnects.
			// session sessoes = new session(MQTT, userId);

			users gerenciadorUsuarios = new users();
			session sessoes = new session();
			controller mqttController = new controller(broker, userId, gerenciadorUsuarios, sessoes);
			//mqttController.assinarTopico("USERS");
			mqttController.assinarTopico("USERS/#");// subtópcios de cada usuario
			String topicoControleUser = userId + "_Control";
			mqttController.assinarTopico(topicoControleUser);
			System.out.println(userId + " inscrito no tópico de controle: " + topicoControleUser);
			mqttController.publicarStatus("ONLINE");

			// menu menu = new menu(gerenciadorUsuarios, sessoes, MQTT);// ?
			menu menu = new menu(gerenciadorUsuarios, sessoes, mqttController, userId);// ?
			menu.exibir();// fica num loop

			// A partir daqui o usuário sai do sistema
			System.out.println("Encerrando a conexão...");
			mqttController.publicarStatus("OFFLINE");
			mqttController.desconectar();
		} catch (MqttException e) {
			System.out.println("Erro MQTT: " + e.getMessage());
			e.printStackTrace();
		}
		scanner.close();

	}

}
