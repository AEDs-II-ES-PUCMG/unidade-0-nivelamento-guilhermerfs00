import java.text.NumberFormat;

public abstract class Produto {

	private static final double MARGEM_PADRAO = 0.2;

	protected String descricao;
	protected double precoCusto;
	protected double margemLucro;

	public Produto(String desc, double precoCusto, double margemLucro) {
			this.descricao = desc;
			this.precoCusto = precoCusto;
			this.margemLucro = margemLucro;
	}

	public Produto(String desc, double precoCusto) {
		this(desc, precoCusto, MARGEM_PADRAO);
	}

	public abstract double valorDeVenda();

	@Override
	public String toString() {
		NumberFormat moeda = NumberFormat.getCurrencyInstance();
		return "NOME: " + descricao + ": " + moeda.format(valorDeVenda());
	}
}