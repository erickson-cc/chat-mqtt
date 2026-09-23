package chatmqtt;

import java.util.HashSet;
import java.util.Set;
//import java.time.Clock;


public class session {
	private Set<String> solicitacoesPendentes;

	public session() {
		this.solicitacoesPendentes = new HashSet<>();
		//Clock systemClock = Clock.systemDefaultZone();

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
}
