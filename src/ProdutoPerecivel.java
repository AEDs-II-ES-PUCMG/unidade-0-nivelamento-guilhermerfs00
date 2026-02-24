import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProdutoPerecivel extends Produto {

    private static final int DIAS_DESCONTO = 7;
    private static final double FATOR_DESCONTO = 0.75;

    private LocalDate dataValidade;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate dataValidade) {
        super(desc, precoCusto, margemLucro);
        if (dataValidade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de validade não pode ser anterior ao dia atual.");
        }
        this.dataValidade = dataValidade;
    }

    public ProdutoPerecivel(String desc, double precoCusto, LocalDate dataValidade) {
        super(desc, precoCusto);
        if (dataValidade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de validade não pode ser anterior ao dia atual.");
        }
        this.dataValidade = dataValidade;
    }

    @Override
    public double valorDeVenda() {
        LocalDate hoje = LocalDate.now();
        if (dataValidade.isBefore(hoje)) {
            throw new IllegalStateException("Produto fora da data de validade: " + dataValidade);
        }
        double precoBase = precoCusto * (1.0 + margemLucro);
        if (!dataValidade.isAfter(hoje.plusDays(DIAS_DESCONTO))) {
            return precoBase * FATOR_DESCONTO;
        }
        return precoBase;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return super.toString() + " (val: " + dataValidade.format(fmt) + ")";
    }
}
