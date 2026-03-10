import java.text.NumberFormat;
import java.util.Objects;

public class ItemDePedido {

    private Produto produto;
    private int quantidade;
    private double precoVenda;

    public ItemDePedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoVenda = produto.valorDeVenda();
    }

    public ItemDePedido(Produto produto, int quantidade, Double precoVenda) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoVenda = precoVenda;
    }
    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemDePedido)) return false;
        ItemDePedido outro = (ItemDePedido) obj;
        return Objects.equals(this.produto, outro.produto);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(produto);
    }

    @Override
    public String toString() {
        NumberFormat moeda = NumberFormat.getCurrencyInstance();
        return produto.getDescricao() + " | Qtd: " + quantidade
                + " | Preço: " + moeda.format(precoVenda);
    }
}
