package chatmqtt;
import java.util.Scanner;

public class menu{
	private Scanner scanner;

	public menu(){
		this.scanner = new Scanner(System.in);
	}

	public void exibir() {
		boolean onscreen = true;
		while(onscreen){
			System.out.println("1. Listar usuários");
			System.out.println("2. Solicitar conversa");
			System.out.println("3. Listar grupos");
			System.out.println("4. Criar novo grupo");
			System.out.println("5. Histórico"); // Tem mais coisa aqui, abrir outro menu
			System.out.println("0. Sair");

			System.out.print("Escolha uma opção: ");
			String opcao = scanner.nextLine();

			switch(opcao){
				case "1":
					System.out.println("Chamar classe lista de usuários");
					break;
				case "2":
					System.out.println("Chamar metodos solicitar conversa");
					break;
				case "3":
					System.out.println("Chamar classe listar grupos");
					break;
				case "4":
					System.out.println("Chamar metodo criar novo grupo");
					break;
				case "5":
					System.out.println("Chamar classe histórico");
					break;
				case "0":
					System.out.println("Dar um exit da classe anterior");
					break;
			}
		}
	}
}
