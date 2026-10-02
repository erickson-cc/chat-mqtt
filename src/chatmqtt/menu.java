package chatmqtt;

import java.util.Scanner;
import org.eclipse.paho.client.mqttv3.MqttException;

public class menu {
	private Scanner scanner;
	private users gerenciadorUsuarios;
	private session sessoes;
	private controller mqttController;
	private groups gerenciadorGrupos;
	private String userId;

	public menu(users usuarios, session sessoes, controller mqttController, String userId, groups gerenciadorGrupos) {
		this.scanner = new Scanner(System.in);
		this.gerenciadorUsuarios = usuarios;
		this.sessoes = sessoes;
		this.mqttController = mqttController;
		this.userId = userId;
		this.gerenciadorGrupos = gerenciadorGrupos;
	}

	public void exibir() {
		boolean onscreen = true;
		while (onscreen) {
			System.out.println("1. Listar usuários");
			System.out.println("2. Conversas");
			System.out.println("3. Grupos");
			System.out.println("4. Histórico"); // Tem mais coisa aqui, abrir outro menu
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
					//gerenciadorGrupos.listarGrupos();
					subMenuGrupos();
					break;
				case "4":
					System.out.print("Chamar classe histórico");
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

	private void subMenuConversas(){
		sessoes.listarPendentes();
		System.out.println("1. Solicitar nova conversa");
		System.out.println("2. Aceitar solicitação pendente");
		System.out.println("3. Recusar solicitação pendente");
		System.out.println("4. Mostrar registros de solicitações");
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
					sessoes.registrarLogSolicitacoes("SOLICITADO",userId,destinatario,null);
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
					mqttController.enviarMensagem(solicitante+"_Control", "ACCEPT:"+userId+":"+topicoDaSessao);
					mqttController.assinarTopico(topicoDaSessao);
					sessoes.registrarLogSolicitacoes("ACEITO", solicitante, userId, topicoDaSessao);
					sessoes.removerSolicitacao(solicitante);
					System.out.println("Conversa estabelecida no tópico: "+topicoDaSessao);
				}
				else{
					System.out.println("Não existe solicitação pendente para esse usuário");
				}
			}
			else if (subOpcao.equals("3")){
				System.out.println("Digite o ID do usuário que deseja recusar a conversa");
				String solicitante = scanner.nextLine();
				if(sessoes.possuiSolicitacao(solicitante)){
					// Enviar mensagem REJECT para o tópico de controle
					mqttController.enviarMensagem(solicitante+"_Control", "REJECT:"+userId);
					sessoes.registrarLogSolicitacoes("RECUSADO",solicitante, userId, null);
					sessoes.removerSolicitacao(solicitante);
					System.out.println("Solicitação de " + solicitante +" recusada");
				}
				else{
					System.out.println("Não existe solicitação pendente para esse usuário");
				}
			}
			else if (subOpcao.equals("4")){// Mostrar logSolicitacoes
				sessoes.listarLogSolicitacoes();
			}
				
		}
		catch (MqttException e){
			System.out.println("Erro: "+e.getMessage());
		}
	}

	private void subMenuGrupos() {
		System.out.println("1. Listar grupos cadastrados");
		System.out.println("2. Criar novo grupo");
		System.out.println("3. Solicitar entrada num grupo");
		System.out.println("4. Gerenciar pedidos de entrada");
		System.out.println("0. Voltar");
		System.out.print("Escolha: ");

		String sub = scanner.nextLine();

		try{
			if (sub.equals("1")){
				gerenciadorGrupos.listarGrupos();
			}
			else if (sub.equals("2")){
				System.out.print("Digite o nome do novo grupo: ");
				String nomeGrupo = scanner.nextLine();
				if (gerenciadorGrupos.esseGrupoExiste(nomeGrupo)){
					System.out.println("Erro: esse grupo já existe");
				}
				else{
					//try{
					mqttController.enviarMensagemRetida("GROUPS/" + nomeGrupo +"/"+userId, "LEADER");
					System.out.println("Grupo '"+nomeGrupo+"' criado com sucesso.");
					//} catch (MqttException e) {
					//	System.out.println("Erro ao criar o grupo" + e.getMessage());
				}
			}
			else if (sub.equals("3")){
				System.out.print("Digite o nome do grupo que deseja entrar: ");
				String nomeGrupo= scanner.nextLine();
				if (!gerenciadorGrupos.esseGrupoExiste(nomeGrupo)){
					System.out.println("Erro: Esse grupo não existe");
				}
				else{
					String liderId = gerenciadorGrupos.retornaLider(nomeGrupo);
					if (liderId.equals(userId)){
						System.out.println("Você ja é o líder desse grupo");
					}
					else{
						//Envia GROUP_REQ:NomeGRUPO:userId para o canal do lider
						mqttController.enviarMensagem(liderId+"_Control", "GROUP_REQ:"+nomeGrupo+":"+userId);
						System.out.println("Solicitação enviada para o líder: "+liderId);
					}
				}

			}
			else if (sub.equals("4")){
				sessoes.listarSolicitacoesPendentesGrupo();
				System.out.print("\nDigite o ID do usuário solicitante: ");
				String solicitante = scanner.nextLine();

				if (sessoes.possuiSolicitacaoGrupo(solicitante)){
					String grupoAlvo = sessoes.getGrupoSolicitado(solicitante);
					System.out.print("Deseja (A)ceitar ou (R)ecusar?: ");
					String acao = scanner.nextLine().toUpperCase();
					if (acao.equals("A")){
						mqttController.enviarMensagemRetida("GROUPS/"+ grupoAlvo+"/"+solicitante,"MEMBER");
						mqttController.enviarMensagem(solicitante+"_Control","GROUP_ACCEPT:"+grupoAlvo);
						System.out.println("Usuário "+solicitante+" adicionado ao grupo");
						sessoes.removerSolicitacaoGrupo(solicitante);
					}
					else if (acao.equals("R")){
						mqttController.enviarMensagem(solicitante+"_Control","GROUP_REJECT:"+grupoAlvo);
						System.out.println("Solicitação recusada");
						sessoes.removerSolicitacaoGrupo(solicitante);
					}
				}

			}
			//else if (sub.equals("0")){
			//}
		}
		catch (MqttException e) {
			System.out.println("Erro no gerenciamento de grupos");
		}
	}
}
