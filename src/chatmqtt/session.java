package chatmqtt;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;//Grupos
import java.util.Map;


public class session {
	// Gerencia as requisições de conversas
	private Set<String> solicitacoesPendentes;
	private List<String> logSolicitacoes;
	private DateTimeFormatter formatter;// para a data da conversa
	// Gerencia as requisições de participação em grupos
	private Map<String, String> solicitacoesGrupo;
	private Map<String, String> conversasAtivas;

	public session() {
		this.solicitacoesPendentes = new HashSet<>();
		this.logSolicitacoes = new ArrayList<>();
		this.formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm", Locale.ENGLISH);// para o time do log
		this.solicitacoesGrupo = new HashMap<>();
		this.conversasAtivas = new HashMap<>();

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

	// Métodos de gerenciamento de grupos
	public void solicitarParticipacao(String membro, String grupo){
		solicitacoesGrupo.put(membro,grupo);
		System.out.println("\r\n[NOTIFICAÇÃO] Nova solicitação de participação ao grupo '"+grupo+"' vindo de "+membro );
		System.out.print("Escolha uma opção: ");
	}

	public void removerSolicitacaoGrupo(String membro){
		solicitacoesGrupo.remove(membro);
	}
	
	public boolean possuiSolicitacaoGrupo(String membro){
		return solicitacoesGrupo.containsKey(membro);
	}

	public String getGrupoSolicitado(String membro){
		// Trocar a palavra "membro" por "solicitante"
		// nesses métodos de grupo
		return solicitacoesGrupo.get(membro);
	}
	public void listarSolicitacoesPendentesGrupo(){
		System.out.println("Solicitações de participação Pendentes:");
		if (solicitacoesGrupo.isEmpty()){
			System.out.println("Nenhuma solicitação no momento");
			return;
		}

		for (Map.Entry<String, String> entry : solicitacoesGrupo.entrySet()){
			System.out.println("Usuário '"+entry.getKey()+"' solicitou participação no grupo '"+entry.getValue()+"'");
		}
	}

	// Métodos de gerenciamento de conversas
	public void adicionarConversa(String utilizador, String topico) {
		conversasAtivas.put(utilizador, topico);
	}
	public String getTopicoConversa(String utilizador) {
		return conversasAtivas.get(utilizador);
	}
	public void listarConversasAtivas() {
		System.out.println("Conversas Ativas: ");
		if (conversasAtivas.isEmpty()) {
			System.out.println("Nenhuma conversa.");
		}
		else{
			for(String user : conversasAtivas.keySet()){
				System.out.println("- " + user);
			}
		}
	}
}
