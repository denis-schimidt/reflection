package br.com.schimidt.alurator.reflexao;

import br.com.schimidt.alurator.reflexao.Reflexao.ManipuladorClasse.ManipuladorConstrutor.ManipuladorInstancia;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

public class Reflexao {

	public ManipuladorClasse refletirClasse(String nomePacote, String nomeController) {
		return new ManipuladorClasse(nomePacote, nomeController);
	}

	public static class ManipuladorClasse {
		private final Class<?> classeController;

		public ManipuladorClasse(String nomePacote, String nomeController) {
			try {
				String nomeFinalClasseController = Character.toUpperCase(nomeController.charAt(0)) +
						nomeController.substring(1);

				String nomeQualificadoClasse = nomePacote.concat(".").concat(nomeFinalClasseController);

				this.classeController = Class.forName(nomeQualificadoClasse);

			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}

		public ManipuladorInstancia criarInstancia() {

			try {
				ManipuladorConstrutor construtor = new ManipuladorConstrutor(classeController.getDeclaredConstructor());
				return new ManipuladorInstancia(construtor.invocar());

			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}

		public static class ManipuladorConstrutor {
			private final Constructor<?> constructor;

			public ManipuladorConstrutor(Constructor<?> constructor) {
				this.constructor = constructor;
			}

			public Object invocar() {

				try {
					return constructor.newInstance();

				} catch (InstantiationException | IllegalAccessException e) {
					throw new RuntimeException(e);

				} catch (InvocationTargetException e) {
					throw new RuntimeException("Erro no construtor!", e.getTargetException());
				}
			}

			public static class ManipuladorInstancia {
				private final Object instancia;

				public ManipuladorInstancia(Object instancia) {
					this.instancia = instancia;
				}

				public ManipuladorMetodo getMetodo(String nomeMetodoAlvo, Map<String, Object> argumentosDoRequest) {
					Method metodoAlvo;

					if (argumentosDoRequest.isEmpty()) {
						metodoAlvo = getDeclaredMethod(nomeMetodoAlvo);

					} else {
						metodoAlvo = Stream.of(instancia.getClass().getDeclaredMethods())
								.filter(metodoDeclarado -> isMetodoComParametrosFoiEncontrado(metodoDeclarado, nomeMetodoAlvo, argumentosDoRequest))
								.findFirst()
								.orElseThrow(() -> new RuntimeException("Nenhum método foi encontrado com base nos parâmetros informados!"));
					}

					return new ManipuladorMetodo(instancia, metodoAlvo, argumentosDoRequest);
				}

				private boolean isMetodoComParametrosFoiEncontrado(Method metodoDeclarado, String nomeMetodoAlvo, Map<String, Object> parametrosDaUri) {
					return metodoDeclarado.getName().equals(nomeMetodoAlvo) &&
							metodoDeclarado.getParameterCount() == parametrosDaUri.size() &&
							isMetodoTemTodosOsParametros(metodoDeclarado, parametrosDaUri);
				}

				private boolean isMetodoTemTodosOsParametros(Method metodo, Map<String, Object> parametrosDaUri) {
					return Stream.of(metodo.getParameters())
							.allMatch(parameter -> ofNullable(parametrosDaUri.get(parameter.getName()))
									.filter(valorParametro -> valorParametro.getClass().equals(parameter.getType()))
									.isPresent());
				}

				private Method getDeclaredMethod(String nomeMetodoAlvo) {
					try {
						return instancia.getClass().getDeclaredMethod(nomeMetodoAlvo);

					} catch (NoSuchMethodException e) {
						throw new RuntimeException(e);
					}
				}

				public static class ManipuladorMetodo {
					private final Object instancia;
					private final Method metodoAlvo;
					private final Map<String, Object> parametros;
					private BiConsumer<Method, InvocationTargetException> tratamentoexcecao;

					public ManipuladorMetodo(Object instancia, Method metodoAlvo, Map<String, Object> parametros) {
						this.instancia = instancia;
						this.metodoAlvo = metodoAlvo;
						this.parametros = parametros;
					}

					public ManipuladorMetodo comTratamentoEspecialExcecao(BiConsumer<Method, InvocationTargetException> tratamentoexcecao) {
						this.tratamentoexcecao = tratamentoexcecao;

						return this;
					}

					public Object invocar() {

						try {
							List<Object> argumentos = Stream.of(metodoAlvo.getParameters())
									.map(parametroDoMetodo -> parametros.get(parametroDoMetodo.getName()))
									.collect(Collectors.toList());

							return metodoAlvo.invoke(instancia, argumentos.toArray());

						} catch (IllegalAccessException e) {
							throw new RuntimeException(e);

						} catch (InvocationTargetException e) {
							ofNullable(tratamentoexcecao)
								.ifPresent(tratamentoexcecao -> tratamentoexcecao.accept(metodoAlvo, e));

							throw new RuntimeException("Erro ao executar método!", e);
						}
					}
				}
			}
		}
	}
}


