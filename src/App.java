import java.time.LocalDate;
import java.util.List;

public class App {

	public static void main(String[] args) {

		// ── Produtos ──────────────────────────────────────────────────────────
		Produto caneta  = new ProdutoNaoPerecivel("Caneta Azul", 2.50, 0.30);
		Produto caderno = new ProdutoNaoPerecivel("Caderno 100 folhas", 15.00);
		Produto iogurte = new ProdutoPerecivel("Iogurte Natural", 3.00, 0.25, LocalDate.now().plusDays(10));
		Produto pao     = new ProdutoPerecivel("Pão de Forma", 5.00, 0.20, LocalDate.now().plusDays(3));

		System.out.println("\n--- Lista de produtos ---");
		List.of(caneta, caderno, iogurte, pao).forEach(p -> System.out.println(p));

		// ── Tarefa 1: criação de ItemDePedido ─────────────────────────────────
		System.out.println("\n--- Itens de pedido ---");
		ItemDePedido itemCaneta  = new ItemDePedido(caneta,  3);
		ItemDePedido itemCaderno = new ItemDePedido(caderno, 2);
		ItemDePedido itemIogurte = new ItemDePedido(iogurte, 5);
		ItemDePedido itemPao     = new ItemDePedido(pao, 12); // qty > 10 → desconto 5%

		System.out.println(itemCaneta);
		System.out.println(itemCaderno);
		System.out.println(itemIogurte);
		System.out.println(itemPao);

		// ── Tarefa 2.3: recibo de um único pedido ─────────────────────────────
		System.out.println("\n--- Pedido 1 (à vista) ---");
		Pedido pedido1 = new Pedido(LocalDate.now(), 1);
		pedido1.incluirProduto(itemCaneta);
		pedido1.incluirProduto(itemCaderno);
		pedido1.incluirProduto(itemPao);
		pedido1.imprimirRecibo();

		// ── Tarefa 2.1 + 2.2: mesclagem de pedidos ───────────────────────────
		// Caneta com preço promocional para testar conflito de preços (menor deve vencer)
		ItemDePedido itemCanetaPromo = new ItemDePedido(caneta, 2, 2.00);
		ItemDePedido itemIogurte2    = new ItemDePedido(iogurte, 3);
		Pedido pedido2 = new Pedido(LocalDate.now(), 2);
		pedido2.incluirProduto(itemCanetaPromo);
		pedido2.incluirProduto(itemIogurte2);

		System.out.println("\n--- Pedido 2 (parcelado, será mesclado no pedido 1) ---");
		System.out.println(pedido2);

		System.out.println("\n--- Mesclando pedido2 no pedido1 ---");
		pedido1.mesclarPedido(pedido2);
		System.out.println("Itens no pedido2 após mesclagem: " + pedido2.getQuantProdutos()); // deve ser 0

		System.out.println("\n--- Recibo final após mesclagem ---");
		pedido1.imprimirRecibo();

		// ── Teste de exceção por capacidade ──────────────────────────────────
		System.out.println("\n--- Teste: capacidade excedida ---");
		Produto biscoito = new ProdutoNaoPerecivel("Biscoito", 3.00);
		Pedido pedidoCheio = new Pedido(LocalDate.now(), 2, 2); // capacidade 2, já cheia
		pedidoCheio.incluirProduto(new ItemDePedido(biscoito, 1));
		pedidoCheio.incluirProduto(new ItemDePedido(caderno, 1));

		Pedido pedidoExtra = new Pedido(LocalDate.now(), 2);
		pedidoExtra.incluirProduto(new ItemDePedido(caneta, 1));

		try {
			pedidoCheio.mesclarPedido(pedidoExtra);
		} catch (IllegalStateException e) {
			System.out.println("Exceção capturada corretamente: " + e.getMessage());
		}
	}
}
