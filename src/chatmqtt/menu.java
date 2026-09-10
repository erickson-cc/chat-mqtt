package chatmqtt;

import java.util.Scanner;

public class menu {
	private Scanner scanner;
	private users gerenciadorUsuarios;

	public menu(users usuarios) {
		this.scanner = new Scanner(System.in);
		this.gerenciadorUsuarios = usuarios;
	}

	public menu() {
	}

	public void exibir() {
		boolean onscreen = true;
		while (onscreen) {
			System.out.println("1. Listar usuários");
			System.out.println("2. Solicitar conversa");
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
					System.out.println("Chamar metodos solicitar conversa");
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
}
