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

			if (status.equals("ONLINE")) {
				onlineUsers.add(id);
			} else if (status.equals("OFFLINE")) {
				onlineUsers.remove(id);
			}
		}
	}

	public void listarUsers() {
		System.out.print("\nUsuários online:\n");
		if (onlineUsers.isEmpty()) {
			System.out.println("Nenhum usuário disponível");
		} else {
			for (String user : onlineUsers) {
				System.out.println("- " + user + ": ONLINE");
			}

		}
		System.out.println("------------------------------");
		return;
	}
}
