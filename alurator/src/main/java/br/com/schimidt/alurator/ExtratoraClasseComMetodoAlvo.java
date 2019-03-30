package br.com.schimidt.alurator;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Optional.empty;
import static java.util.Optional.of;

class ExtratoraClasseComMetodoAlvo {
	private static String DELIMITADOR = "/";
	private String nomePacote;

	ExtratoraClasseComMetodoAlvo(String nomePacote) {
		this.nomePacote = nomePacote;
	}

	Optional<ClasseComMetodoAlvo> obterDe(String url) {
		String[] partesNomeClasse = url.replaceFirst("^/", "")
				.split(DELIMITADOR);

		if (partesNomeClasse.length > 0) {
			return of(new ClasseComMetodoAlvo(nomePacote, partesNomeClasse[0], partesNomeClasse[1]));
		}

		return empty();
	}

	static class ClasseComMetodoAlvo {
		private static final String SUFIXO_CLASSE_CONTROLLER = "Controller";
		private final String nomeTotalmenteQualificadoClasse;
		private final String metodoAlvo;

		ClasseComMetodoAlvo(String nomePacote, String nomeSimplesClasse, String metodoAlvo) {
			this.nomeTotalmenteQualificadoClasse = new StringBuilder(nomePacote)
					.append(".")
					.append(Character.toUpperCase(nomeSimplesClasse.charAt(0)))
					.append(nomeSimplesClasse.substring(1))
					.append(SUFIXO_CLASSE_CONTROLLER)
					.toString();
			this.metodoAlvo = metodoAlvo;
		}

		String getNomeClasseTotalmenteQualificada() {
			return nomeTotalmenteQualificadoClasse;
		}

		<T> T newInstance(Object... params) {

			Class<?>[] tiposParametros = null;

			try {
				if (params.length > 0) {
					tiposParametros = Stream.of(params)
							.map(c -> c.getClass())
							.toArray(Class[]::new);
				}

				return (T) Class.forName(nomeTotalmenteQualificadoClasse).getDeclaredConstructor(tiposParametros).newInstance(params);

			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
				throw new RuntimeException(e);

			} catch (InvocationTargetException e) {
				throw new RuntimeException("Erro no construtor", e.getTargetException());
			}
		}

		String getMetodoAlvo() {
			return metodoAlvo;
		}
	}
}

