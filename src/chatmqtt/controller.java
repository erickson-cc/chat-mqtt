package chatmqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class controller {
	private MqttClient client;
	private String userId;
	private String userTopicoControle;

	public controller(String broker, String userId, users gerenciadorUsuarios, session sessoes) throws MqttException {
		this.userId = userId;
		this.userTopicoControle = userId+"_Control";
		MemoryPersistence persistence = new MemoryPersistence();
		this.client = new MqttClient(broker, userId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(false);// Retém mensagens enviadas para usuarios offline
						//
		client.setCallback(new org.eclipse.paho.client.mqttv3.MqttCallback() {
			// Escutador de Mensagens assíncronas
			// called when the connection to the server is lost.
			public void connectionLost(Throwable cause) {
				System.out.println("\nError MQTT: CoonnectionLost\nOutro usuário logou com seu ID");
				System.exit(1);
			}

			// called when message delivered + ACK received
			public void deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken token) {
			}

			// called when message arrived from server
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				String payload = new String(message.getPayload());

				// if (topic.equals("USERS")) {
				// gerenciadorUsuarios.atualizarStatus(payload);
				// }
				if (topic.startsWith("USERS/")) {// Criar subtópicos para evitar o problema de
									// Guardar apenas uma mensagem
					gerenciadorUsuarios.atualizarStatus(payload);
				}
				// Ler mensagens que chegam em user_Control
				else if(topic.equals(userTopicoControle)){
					if(payload.startsWith("REQ:")){
						String remetente = payload.split(":")[1];
						//String dataehora = payload.split(" ")[0];
						sessoes.adicionarSolicitacao(remetente);
					}
					else if(payload.startsWith("ACCEPT:")){
						String topicoSessao = payload.split(":")[1];
						System.out.println("\r\nSessão iniciada. O usuário aceitou sua solicitação.\nTópico: "+topicoSessao);
						System.out.println("Escolha uma opção:");
					}

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

	public void publicarStatus(String status) throws MqttException {
		String messageStatus = userId + ":" + status;
		MqttMessage statusUser = new MqttMessage(messageStatus.getBytes());
		statusUser.setQos(1);
		statusUser.setRetained(true);// para quem logar depois
		// client.publish("USERS", statusUser);
		client.publish("USERS/" + userId, statusUser); // Publica no subtópico
	}

	public void desconectar() throws MqttException {
		client.disconnect();
	}

	public void enviarMensagem(String topicoDestino, String mensagem) throws MqttException{
		MqttMessage msg = new MqttMessage(mensagem.getBytes());
		msg.setQos(1);
		client.publish(topicoDestino,msg);
	}
}
