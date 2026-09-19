package chatmqtt;

import java.util.HashSet;
import java.util.Set;

public class users {
	private Set<String> onlineUsers;

	public users() {
		this.onlineUsers = new HashSet<>();
	}

	public void atualizarStatus(String mensagem) {
		String[] partes = mensagem.split(":");
		if (partes.length == 2) {
			String id = partes[0];
			String status = partes[1];
			String novaEntrada = id + ":" + status;

			String registroAntigo = null; // Serve para encontrar se o registro antigo já
							// estava lá antes.
			for (String user : onlineUsers) { 
				if (user.startsWith(id + ":")) {
					registroAntigo = user; // 'user_1 : ONLINE'
					break;
				}
			}

			if (registroAntigo != null) {
				onlineUsers.remove(registroAntigo);
			}
			onlineUsers.add(novaEntrada);
			// if (status.equals("ONLINE")) {
			// onlineUsers.add(id);
			// } else if (status.equals("OFFLINE")) {
			// onlineUsers.remove(id);
			// }
		}
	}

	public void listarUsers() {
		System.out.print("\nUsuários online:\n");
		if (onlineUsers.isEmpty()) {
			System.out.println("Nenhum usuário disponível");
		} else {
			for (String user : onlineUsers) {
				// System.out.println("- " + user + ": ONLINE");
				System.out.println("- " + user);
			}

		}
		System.out.println("------------------------------");
		return;
	}
}
