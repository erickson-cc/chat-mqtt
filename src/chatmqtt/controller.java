package chatmqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class controller {
	private MqttClient client;
	private String userId;

	public controller(String broker, String userId, users gerenciadorUsuarios) throws MqttException{
		this.userId = userId;
		MemoryPersistence persistence = new MemoryPersistence();
		this.client = new MqttClient(broker, userId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(false);// Retém mensagens enviadas para usuarios offline
						//
		client.setCallback(new org.eclipse.paho.client.mqttv3.MqttCallback() {
			// Escutador de Mensagens assíncronas
			// called when the connection to the server is lost.
			public void connectionLost(Throwable cause) {
			}

			// called when message delivered + ACK received
			public void deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken token) {
			}

			// called when message arrived from server
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				String payload = new String(message.getPayload());

				if (topic.equals("USERS")) {
					gerenciadorUsuarios.atualizarStatus(payload);
				}
			}
		});
		System.out.println("Conectando ao broker...");
		client.connect(connOpts);
		System.out.println("Conectado com sucesso!");
	}

	public void assinarTopico(String topico) throws MqttException {
		client.subscribe(topico); 
	}

	public void publicarStatus(String status) throws MqttException{
		String messageStatus = userId + ":" + status;
		MqttMessage statusUser = new MqttMessage(messageStatus.getBytes());
		statusUser.setQos(1);
		statusUser.setRetained(true);// para quem logar depois
		client.publish("USERS", statusUser);
	}

	public void desconectar() throws MqttException{
		client.disconnect(); 
	}
}
