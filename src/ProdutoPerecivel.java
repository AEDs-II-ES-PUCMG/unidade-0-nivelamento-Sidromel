import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ProdutoPerecivel extends Produto {

	private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final double DESCONTO = 0.25;
	private static final int PRAZO_DESCONTO = 7;

	private LocalDate dataDeValidade;

	public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade) {
		super(desc, precoCusto, margemLucro);
		if (validade == null) {
			throw new IllegalArgumentException("Data de validade não pode ser nula.");
		}
		if (validade.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Data de validade não pode ser anterior ao dia atual.");
		}
		this.dataDeValidade = validade;
	}

	public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, String dataValidadeStr) {
		this(desc, precoCusto, margemLucro, dataValidadeStr, true);
	}

	ProdutoPerecivel(String desc, double precoCusto, double margemLucro, String dataValidadeStr, boolean validarDataSeFutura) {
		super(desc, precoCusto, margemLucro);
		LocalDate data = parseDataValidade(dataValidadeStr);
		if (validarDataSeFutura && data.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Data de validade não pode ser anterior ao dia atual.");
		}
		this.dataDeValidade = data;
	}

	public static ProdutoPerecivel criarDoArquivo(String desc, double precoCusto, double margemLucro, String dataValidadeStr) {
		return new ProdutoPerecivel(desc, precoCusto, margemLucro, dataValidadeStr, false);
	}

	private static LocalDate parseDataValidade(String dataStr) {
		try {
			return LocalDate.parse(dataStr.trim(), FORMATO_DATA);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Data de validade inválida. Use o formato dd/mm/aaaa.", e);
		}
	}

	@Override
	public double valorVenda() {
		LocalDate hoje = LocalDate.now();
		if (dataDeValidade.isBefore(hoje)) {
			throw new IllegalStateException("Produto fora da data de validade. Venda não permitida.");
		}
		double valorBase = super.valorVenda();
		if (!dataDeValidade.isAfter(hoje.plusDays(PRAZO_DESCONTO))) {
			valorBase *= (1.0 - DESCONTO);
		}
		return valorBase;
	}

	@Override
	public String toString() {
		NumberFormat moeda = NumberFormat.getCurrencyInstance();
		return String.format("NOME: %s: %s (validade: %s)", getDescricao(), moeda.format(valorVenda()), dataDeValidade.format(FORMATO_DATA));
	}

	@Override
	public String gerarDadosTexto() {
		String precoFormatado = String.format("%.2f", getPrecoCusto()).replace(',', '.');
		String margemFormatado = String.format("%.2f", getMargemLucro()).replace(',', '.');
		return String.format(
				"2; %s; %s; %s; %s",
				getDescricao(),
				precoFormatado,
				margemFormatado,
				dataDeValidade.format(FORMATO_DATA));
	}
}
