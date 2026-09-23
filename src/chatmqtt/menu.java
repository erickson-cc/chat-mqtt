package chatmqtt;

import java.util.Scanner;
import org.eclipse.paho.client.mqttv3.MqttException;

public class menu {
	private Scanner scanner;
	private users gerenciadorUsuarios;
	private session sessoes;
	private controller mqttController;
	private String userId;

	public menu(users usuarios, session sessoes, controller mqttController, String userId) {
		this.scanner = new Scanner(System.in);
		this.gerenciadorUsuarios = usuarios;
		this.sessoes = sessoes;
		this.mqttController = mqttController;
		this.userId = userId;
	}

	public void exibir() {
		boolean onscreen = true;
		while (onscreen) {
			System.out.println("1. Listar usuários");
			System.out.println("2. Solicitar/Aceitar conversa");
			System.out.println("3. Listar grupos");
			System.out.println("4. Criar novo grupo");
			System.out.println("5. Histórico"); // Tem mais coisa aqui, abrir outro menu
			System.out.println("0. Sair");

			System.out.print("Escolha uma opção: ");
			String opcao = scanner.nextLine();

			switch (opcao) {
				case "1":
					gerenciadorUsuarios.listarUsers();
					break;
				case "2":
					subMenuConversas();
					// String alvo = scanner.nextLine();
					// sessoes.solicitarConversa(alvo);
					break;
				case "3":
					this.listarGroups();
					break;
				case "4":
					System.out.println("Chamar metodo criar novo grupo");
					break;
				case "5":
					System.out.println("Chamar classe histórico");
					break;
				case "0":
					System.out.println("Dar um exit da classe anterior");
					onscreen = false;
					break;
			}
		}
	}

	public void listarGroups() {
		System.out.println("Chamar classe listar grupos");
		return;
	}

	private void subMenuConversas(){
		sessoes.listarPendentes();
		System.out.println("1. Solicitar nova conversa");
		System.out.println("2. Aceitar solicitação pendente");
		System.out.println("0. Voltar");
		System.out.print("Escolha: ");

		String subOpcao = scanner.nextLine();

		try{
			if(subOpcao.equals("1")){ // Solicitar conversa
				System.out.println("Digite o ID do usuário que deseja iniciar a conversa");
				String destinatario = scanner.nextLine(); 
				if (sessoes.possuiSolicitacao(destinatario)){
					//Impede solicitar para quem já solicitou
					System.out.println("Esse usuário já solicitou uma conversa contigo");
				}
				else{
					// Envia a mensagem REQ para o tópçico de controle
					mqttController.enviarMensagem(destinatario + "_Control", "REQ:" + userId);
					System.out.println("Solicitação enviada para "+destinatario);
				}

			}
			else if (subOpcao.equals("2")){ // Aceitar Solicitação
				System.out.println("Digite o ID do usuário que deseja aceitar a conversa");
				String solicitante = scanner.nextLine(); 
				if(sessoes.possuiSolicitacao(solicitante)){
					long timestamp = System.currentTimeMillis();
					String topicoDaSessao = solicitante + "_" + userId + "_" + timestamp;
					// Enviar mensagem ACCEPT para o tópico de controle
					mqttController.enviarMensagem(solicitante+"_Control", "ACCEPT:"+topicoDaSessao);
					mqttController.assinarTopico(topicoDaSessao);
					sessoes.removerSolicitacao(solicitante);
					System.out.println("Conversa estabelecida no tópico: "+topicoDaSessao);
				}
				else{
					System.out.println("Não existe solicitação pendente para esse usuário");
				}
			}
		}
		catch (MqttException e){
			System.out.println("Erro: "+e.getMessage());
		}
	}
}
