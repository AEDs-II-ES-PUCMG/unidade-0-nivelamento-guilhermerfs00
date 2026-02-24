import java.time.LocalDate;
import java.util.List;

public class App {

	public static void main(String[] args) {

		Produto caneta = new ProdutoNaoPerecivel("Caneta Azul", 2.50, 0.30);
		Produto caderno = new ProdutoNaoPerecivel("Caderno 100 folhas", 15.00);
		Produto iogurte = new ProdutoPerecivel("Iogurte Natural", 3.00, 0.25, LocalDate.now().plusDays(10));

		Produto pao = new ProdutoPerecivel("Pão de Forma", 5.00, 0.20, LocalDate.now().plusDays(3));

		System.out.println("\n--- Lista de produtos ---");
		List.of(caneta, caderno, iogurte, pao).forEach(p -> System.out.println(p));
		;

	}
}
