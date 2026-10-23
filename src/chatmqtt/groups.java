package chatmqtt;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class groups {
	class infoGroup { // Para organizar os dados do grupo
		String lider = "";
		Set<String> membros = new HashSet<>();
	}

	private Map<String, infoGroup> listaGroups;

	public groups() {
		this.listaGroups = new HashMap<>();
	}

	public void atualizarGrupo(String topico, String payload){
		String[] partes = topico.split("/");
		if (partes.length == 3){
			String nomeGrupo = partes[1];
			String membroId = partes[2];
			//infoGroup info = listaGroups.get(nomeGrupo);

			if (payload.isEmpty()){ // Sair do grupo
				// Se o payload estiver vazio, elimina mensagem retida
				if (listaGroups.containsKey(nomeGrupo)){
					infoGroup info = listaGroups.get(nomeGrupo); // evita problema assinc Map
					if (membroId.equals(info.lider)){
						// Se for o líder, remove o grupo
						listaGroups.remove(nomeGrupo);
					}
					else{
						// se foi um membro, retira do grupo
						info.membros.remove(membroId);
					}
				}
				return;
			}

			listaGroups.putIfAbsent(nomeGrupo, new infoGroup()); // Se o grupo não existe no map, cria.
			infoGroup info = listaGroups.get(nomeGrupo);

			if (payload.equals("LEADER")){
				info.lider = membroId;
			}
			else if (payload.equals("MEMBER")){
				info.membros.add(membroId);
			}
		}
	}

	public Set<String> listarMembros(String nomeGrupo){
		if (listaGroups.containsKey(nomeGrupo)){
			return listaGroups.get(nomeGrupo).membros;
		}
		return new HashSet<>();
	}

	public boolean esseGrupoExiste(String nomeGrupo) {
		return listaGroups.containsKey(nomeGrupo);
	}

	public void listarGrupos() {
		System.out.println("\nGrupos Cadastrados:");
		if (listaGroups.isEmpty()){
			System.out.println("Nenhum grupo cadastrado");
			System.out.println("--------------------------");
			return;
		}
		//else {
		for (Map.Entry<String, infoGroup> entry : listaGroups.entrySet()){
			String nomeGrupo = entry.getKey();
			infoGroup info = entry.getValue();

			System.out.println("Grupo: "+nomeGrupo);
			System.out.println("Líder: "+info.lider);// Testar quando o lider sai do grupo
			System.out.print("Membros:" );
			if (info.membros.isEmpty()){
				System.out.println("Nenhum membro");
			}
			else{
				System.out.println(String.join(", ", info.membros));
			}
			System.out.println("----");

		}
		//}
		System.out.println("--------------------------");
	}

	public String retornaLider(String nomeGrupo){
		if (listaGroups.containsKey(nomeGrupo)) {
			return listaGroups.get(nomeGrupo).lider;
		}
		return null;
	}

}
