package chatmqtt;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class session {
	// Gerencia as requisições de conversas
	private Set<String> solicitacoesPendentes;
	private List<String> logSolicitacoes;
	private DateTimeFormatter formatter;

	public session() {
		this.solicitacoesPendentes = new HashSet<>();
		this.logSolicitacoes = new ArrayList<>();
		this.formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm", Locale.ENGLISH);// para o time do log

	}

	public void adicionarSolicitacao(String idSol) {
		solicitacoesPendentes.add(idSol);
		System.out.println("\r\n[NOTIFICAÇÃO] Nova solicitação de conversa de: " + idSol);
		System.out.print("Escolha uma opção: ");
	}

	public void removerSolicitacao(String idSol) {
		solicitacoesPendentes.remove(idSol);
	}

	public boolean possuiSolicitacao(String idSol) {
		return solicitacoesPendentes.contains(idSol);
	}

	public void listarPendentes() {
		System.out.println("\n--- Solicitações de Conversa ---");
		if (solicitacoesPendentes.isEmpty()) {
			System.out.println("Nenhuma solicitação pendente");
		}
		else{
			for (String solicitante : solicitacoesPendentes){
				System.out.println("- " + solicitante);
			}
		}
	}

	public void listarLogSolicitacoes() {
		System.out.println("Registros de solicitações em ordem cronológica");
		if (logSolicitacoes.isEmpty()){
			System.out.println("Nenhuma solicitação registrada");
		}
		else{
			for (String registro : logSolicitacoes){
				System.out.println(registro);
			}
		}
	}

	public void registrarLogSolicitacoes(String tipo, String remetente, String destinatario, String canal){
		// se for enviado: "[SOLICITADO]" + Horário + remetente + "->" + destinatario
		// se for aceito: "[ACEITO]" + Horário + remetente + "->" + destinatario + (canal da conversa)
		// se for recusado: "[RECUSADO]" + Horário + remetente + "->" + destinatario 
		String timestamp = LocalDateTime.now().format(formatter);
		String log = "["+tipo+"]"+timestamp+" " + remetente + "->" + destinatario;
		if (tipo.equals("ACEITO")){
			log += " ("+canal+")";
		}
		logSolicitacoes.add(log);
	}
}
