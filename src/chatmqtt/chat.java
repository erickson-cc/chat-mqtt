package chatmqtt;

import java.util.Scanner;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class chat {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		String broker       = "tcp://test.mosquitto.org:1883";
		//String broker       = "tcp://localhost:1883"; //rodar mosquitto
		MemoryPersistence persistence = new MemoryPersistence();
		
		System.out.println("=== CHATMQTT ===");
		System.out.print("Login: ");
		String userId = scanner.nextLine();
		
		
		try {
			MqttClient client = new MqttClient(broker, userId, persistence);
			//Holds the set of options that control how the client connects to a server.
			MqttConnectOptions connOpts = new MqttConnectOptions();
			//Sets whether the client and server should remember state across restarts and reconnects.
			connOpts.setCleanSession(false);// Retém mensagens enviadas para usuarios offline
			//controller MQTT = new controller(broker userId);
			//users gerenciadorUsuarios = new users();
			//session sessoes = new session(MQTT, userId);


			System.out.println("Conectando ao broker...");
			client.connect(connOpts);
			System.out.println("Conectado com sucesso!");

			// Comunicar Estado do Usuário
			String messageStatus = userId + " está online";
			MqttMessage statusUser = new MqttMessage(messageStatus.getBytes());
			statusUser.setQos(1);
			client.publish("USERS", statusUser);// Publica no tópico USERS
			
			// Assinar próprio tópico controle
			String topicoControleUser = userId + "_Control";
			client.subscribe(topicoControleUser);
			System.out.println(userId + " inscrito no tópico de controle: " + topicoControleUser);

			//menu menu = new menu(gerenciadorUsuarios, sessoes, MQTT);// ?
			menu menu = new menu();// ?
			menu.exibir();
		}
		catch(MqttException me){
			System.out.println("Erro MQTT: "+ me.getMessage());
			me.printStackTrace();
		}
		

	}

}

