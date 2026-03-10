import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Pedido {

	private static final int MAX_PRODUTOS = 10;
	private static final double DESCONTO_PG_A_VISTA = 0.15;
	private static final double DESCONTO_QUANTIDADE = 0.05;
	private static final int LIMITE_DESCONTO_QTD = 10;

	private ItemDePedido[] itemDePedidos;
	private LocalDate dataPedido;
	private int quantProdutos = 0;
	private int formaDePagamento;

	public Pedido(LocalDate dataPedido, int formaDePagamento) {
		itemDePedidos = new ItemDePedido[MAX_PRODUTOS];
		quantProdutos = 0;
		this.dataPedido = dataPedido;
		this.formaDePagamento = formaDePagamento;
	}

	// Construtor extra com capacidade customizada (útil nos testes)
	public Pedido(LocalDate dataPedido, int formaDePagamento, int capacidade) {
		itemDePedidos = new ItemDePedido[capacidade];
		quantProdutos = 0;
		this.dataPedido = dataPedido;
		this.formaDePagamento = formaDePagamento;
	}

	public boolean incluirProduto(ItemDePedido novo) {
		// Agrupa se produto já existir no pedido
		for (int i = 0; i < quantProdutos; i++) {
			if (itemDePedidos[i].equals(novo)) {
				itemDePedidos[i].setQuantidade(itemDePedidos[i].getQuantidade() + novo.getQuantidade());
				return true;
			}
		}
		if (quantProdutos < itemDePedidos.length) {
			itemDePedidos[quantProdutos++] = novo;
			return true;
		}
		return false;
	}

	public double valorFinal() {
		double valorPedido = 0;
		for (int i = 0; i < quantProdutos; i++) {
			valorPedido += itemDePedidos[i].getPrecoVenda() * itemDePedidos[i].getQuantidade();
		}
		if (formaDePagamento == 1) {
			valorPedido = valorPedido * (1.0 - DESCONTO_PG_A_VISTA);
		}
		return valorPedido;
	}

	/**
	 * Mescla o outroPedido neste pedido de forma atômica:
	 * – Valida capacidade antes de qualquer alteração;
	 * – Agrupa itens iguais (soma quantidades, mantém menor preço);
	 * – Esvazia o pedido secundário após a mesclagem.
	 */
	public void mesclarPedido(Pedido outroPedido) {
		// Conta apenas os itens do pedido secundário que ainda não existem aqui
		int novosItens = 0;
		for (int i = 0; i < outroPedido.quantProdutos; i++) {
			if (buscarItem(outroPedido.itemDePedidos[i]) == null) {
				novosItens++;
			}
		}

		if (quantProdutos + novosItens > itemDePedidos.length) {
			throw new IllegalStateException(
				"Capacidade insuficiente para mesclar os pedidos. " +
				"Espaço disponível: " + (itemDePedidos.length - quantProdutos) +
				", itens novos: " + novosItens + "."
			);
		}

		// Realiza a mesclagem
		for (int i = 0; i < outroPedido.quantProdutos; i++) {
			ItemDePedido itemSec = outroPedido.itemDePedidos[i];
			ItemDePedido itemPrinc = buscarItem(itemSec);

			if (itemPrinc != null) {
				// Produto já existe: soma quantidade e aplica o menor preço
				itemPrinc.setQuantidade(itemPrinc.getQuantidade() + itemSec.getQuantidade());
				if (itemSec.getPrecoVenda() < itemPrinc.getPrecoVenda()) {
					itemPrinc.setPrecoVenda(itemSec.getPrecoVenda());
				}
			} else {
				// Produto novo: insere na próxima posição livre
				itemDePedidos[quantProdutos++] = itemSec;
			}
		}

		// Esvazia logicamente o pedido secundário
		for (int i = 0; i < outroPedido.quantProdutos; i++) {
			outroPedido.itemDePedidos[i] = null;
		}
		outroPedido.quantProdutos = 0;
	}

	/**
	 * Imprime o recibo do pedido no terminal.
	 * Aplica 5% de desconto no subtotal de itens com quantidade > 10.
	 */
	public void imprimirRecibo() {
		NumberFormat moeda = NumberFormat.getCurrencyInstance();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		System.out.println("============================================");
		System.out.println("               RECIBO DE VENDA              ");
		System.out.println("Data: " + dataPedido.format(fmt));
		System.out.println("Pagamento: " + (formaDePagamento == 1 ? "À vista (-15%)" : "Parcelado"));
		System.out.println("============================================");
		System.out.printf("%-22s %4s %10s %12s%n", "Produto", "Qtd", "Preço Unit.", "Subtotal");
		System.out.println("--------------------------------------------");

		double totalGeral = 0;

		for (int i = 0; i < quantProdutos; i++) {
			if (itemDePedidos[i] == null) continue;

			ItemDePedido item = itemDePedidos[i];
			double subtotal = item.getPrecoVenda() * item.getQuantidade();
			boolean temDesconto = item.getQuantidade() > LIMITE_DESCONTO_QTD;

			if (temDesconto) {
				subtotal *= (1.0 - DESCONTO_QUANTIDADE);
			}

			System.out.printf("%-22s %4d %10s %12s%s%n",
				item.getProduto().getDescricao(),
				item.getQuantidade(),
				moeda.format(item.getPrecoVenda()),
				moeda.format(subtotal),
				temDesconto ? " (-5%)" : ""
			);

			totalGeral += subtotal;
		}

		if (formaDePagamento == 1) {
			totalGeral *= (1.0 - DESCONTO_PG_A_VISTA);
		}

		System.out.println("============================================");
		System.out.printf("%-38s %12s%n", "TOTAL GERAL:", moeda.format(totalGeral));
		System.out.println("============================================");
	}

	// ── Métodos auxiliares ────────────────────────────────────────────────────

	private ItemDePedido buscarItem(ItemDePedido alvo) {
		for (int i = 0; i < quantProdutos; i++) {
			if (itemDePedidos[i] != null && itemDePedidos[i].equals(alvo)) {
				return itemDePedidos[i];
			}
		}
		return null;
	}

	public int getQuantProdutos() {
		return quantProdutos;
	}

	public int getCapacidade() {
		return itemDePedidos.length;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		sb.append("Data do pedido: ").append(formatoData.format(dataPedido)).append("\n");
		sb.append("Pedido com ").append(quantProdutos).append(" item(ns).\n");
		sb.append("Produtos no pedido:\n");

		for (int i = 0; i < quantProdutos; i++) {
			sb.append("  ").append(itemDePedidos[i].toString()).append("\n");
		}

		sb.append("Pedido pago ");
		if (formaDePagamento == 1) {
			sb.append("à vista. Desconto: ").append(String.format("%.0f%%", DESCONTO_PG_A_VISTA * 100)).append("\n");
		} else {
			sb.append("parcelado.\n");
		}
		sb.append("Valor total: R$ ").append(String.format("%.2f", valorFinal()));
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Pedido)) return false;
		Pedido outro = (Pedido) obj;
		return this.dataPedido.equals(outro.dataPedido);
	}
}