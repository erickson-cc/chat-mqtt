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

	public controller(
			String broker, String userId, users gerenciadorUsuarios, session sessoes,
			groups gerenciadorGrupos
			) throws MqttException {
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
				System.out.println("\nError MQTT: CoonnectionLost");
				// PARA CORRIGIR POSTERIORMENTE
				// A mensagem "Outro usuario logou com seu ID" aparece para qualquer
				// cas de perda de conexão. Criar ifs que interpretam os erros do mqtt
				// System.println.out(cause);
				System.exit(1);
			}

			// called when message delivered + ACK received
			public void deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken token) {
			}

			// called when message arrived from server
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				String payload = new String(message.getPayload());

				if (topic.startsWith("USERS/")) {// Criar subtópicos para evitar o problema de
									// Guardar apenas uma mensagem
					gerenciadorUsuarios.atualizarStatus(payload);
				}
				else if (topic.startsWith("GROUPS/")){ // Cria subtópico GROUPS/
					gerenciadorGrupos.atualizarGrupo(topic, payload);
				}
				// Ler mensagens que chegam em user_Control
				else if(topic.equals(userTopicoControle)){
					if(payload.startsWith("REQ:")){
						String remetente = payload.split(":")[1];
						//String dataehora = payload.split(" ")[0];
						sessoes.adicionarSolicitacao(remetente);
						sessoes.registrarLogSolicitacoes("SOLICITADO", remetente, userId, null);
					}
					else if(payload.startsWith("ACCEPT:")){
						String mensagemPartes[] = payload.split(":");
						String destinatario = mensagemPartes[1];
						String topicoSessao = mensagemPartes[2];

						sessoes.registrarLogSolicitacoes("ACEITO", userId, destinatario, topicoSessao);
						sessoes.adicionarConversa(destinatario,topicoSessao);
						client.subscribe(topicoSessao);

						System.out.println("\r\n Canal iniciado. O usuário aceitou sua solicitação.\nTópico: "+topicoSessao);
						System.out.println("Escolha uma opção:");
					}
					else if(payload.startsWith("REJECT:")){
						String destinatario = payload.split(":")[1];
						sessoes.registrarLogSolicitacoes("RECUSADO", userId, destinatario, null);

						System.out.println("\r\nAviso. O usuário "+destinatario+" recusou sua solicitação.");
						System.out.println("Escolha uma opção:");
					}
					// Grupos
					else if(payload.startsWith("GROUP_REQ:")){
						String mensagemPartes[] = payload.split(":");
						String grupo = mensagemPartes[1];
						String membro = mensagemPartes[2];
						sessoes.solicitarParticipacao(membro, grupo);
					}
					else if(payload.startsWith("GROUP_ACCEPT:")){
						String grupo = payload.split(":")[1];
						System.out.println("A sua solicitação para entrar no grupo '" + grupo + "' foi aceita");
						System.out.println("Escolha uma opção:");
					}
					else if(payload.startsWith("GROUP_REJECT:")){
						String grupo = payload.split(":")[1];
						System.out.println("A sua solicitação para entrar no grupo '" + grupo + "' foi rejeitada");
						System.out.println("Escolha uma opção:");
					}

				}
				else if (topic.contains("_"+userId+"_")&& !topic.endsWith("_Control")){
					// Não é USERS nem GROUPS nem _Control, logo é chat privado
					String remetente = payload.split(":")[0];

					if (!remetente.equals(userId)){ // não imprime mensagens que o user enviou
						System.out.println("\r\nMensagem de " + remetente +":" +payload.substring(remetente.length()+2));
						System.out.print("Escolha uma opção:");
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

	public void enviarMensagemRetida(String topicoDestino, String mensagem) throws MqttException{
		MqttMessage msg = new MqttMessage(mensagem.getBytes());
		msg.setQos(1);
		msg.setRetained(true);
		client.publish(topicoDestino,msg);
	}

}
