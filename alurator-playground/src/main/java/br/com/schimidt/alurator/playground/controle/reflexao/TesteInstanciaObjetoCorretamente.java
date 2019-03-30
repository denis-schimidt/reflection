package br.com.schimidt.alurator.playground.controle.reflexao;

import br.com.schimidt.alurator.playground.controle.SubControle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TesteInstanciaObjetoCorretamente {

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
		Class<SubControle> subControle1 = SubControle.class;

		Class<?> subControle2 = Class.forName("br.com.schimidt.alurator.playground.controle.SubControle");

		Class<?> controle = Class.forName("br.com.schimidt.alurator.playground.controle.Controle");

		Constructor<SubControle> constructor = subControle1.getDeclaredConstructor();

//		constructor.setAccessible(true);

		SubControle subControle = null;
		try {
			subControle = constructor.newInstance();
		} catch (InvocationTargetException e) {
			System.out.println(e.getTargetException());
		}

		System.out.println(subControle);
	}
}
