import java.text.NumberFormat;

public abstract class Produto {
	
	private static final double MARGEM_PADRAO = 0.2;
	protected String descricao;
	protected double precoCusto;
	protected double margemLucro;
	protected int quantidadeEmEstoque;
	
	private void init(String desc, double precoCusto, double margemLucro) {
		
		if ((desc.length() >= 3) && (precoCusto > 0.0) && (margemLucro > 0.0)) {
			descricao = desc;
			this.precoCusto = precoCusto;
			this.margemLucro = margemLucro;
		} else {
			throw new IllegalArgumentException("Valores inválidos para os dados do produto.");
		}
	}

	protected Produto(String desc, double precoCusto, double margemLucro) {
		init(desc, precoCusto, margemLucro);
	}
	
	protected Produto(String desc, double precoCusto) {
		init(desc, precoCusto, MARGEM_PADRAO);
	}
	
	public double valorVenda() {
		return (precoCusto * (1.0 + margemLucro));
	}

	public String getDescricao() {
		return descricao;
	}

	public double getPrecoCusto() {
		return precoCusto;
	}

	public double getMargemLucro() {
		return margemLucro;
	}

	public int getQuantidadeEmEstoque() {
		return quantidadeEmEstoque;
	}

	public int reduzirEstoque (int quantidadeEmEstoque) {
		if (pedido(quantidadeEmEstoque) == true) {
			quantidadeEmEstoque -= quantidadeEmEstoque;
		} else {
			throw new IllegalArgumentException("O estoque do produto acabou");
		}
	}

    @Override
	public String toString() {
    	
    	NumberFormat moeda = NumberFormat.getCurrencyInstance();
    	
		return String.format("NOME: " + descricao + ": " + moeda.format(valorVenda()) + "Em estoque:" + quantidadeEmEstoque);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Produto outro = (Produto) obj;
		return this.descricao.toLowerCase().equals(outro.descricao.toLowerCase());
	}

	public abstract String gerarDadosTexto();

	public static Produto criarDoTexto(String linha) {
		if (linha == null || linha.isBlank()) {
			throw new IllegalArgumentException("Linha de dados inválida.");
		}
		String[] partes = linha.split(";");
		if (partes.length < 4) {
			throw new IllegalArgumentException("Linha com dados insuficientes.");
		}
		String tipo = partes[0].trim();
		String descricao = partes[1].trim();
		double precoCusto = Double.parseDouble(partes[2].trim());
		double margemLucro = Double.parseDouble(partes[3].trim());

		if ("1".equals(tipo)) {
			return new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
		} else if ("2".equals(tipo)) {
			if (partes.length < 5) {
				throw new IllegalArgumentException("Produto perecível exige data de validade.");
			}
			String dataValidade = partes[4].trim();
			return ProdutoPerecivel.criarDoArquivo(descricao, precoCusto, margemLucro, dataValidade);
		} else {
			throw new IllegalArgumentException("Tipo de produto inválido: " + tipo);
		}
	}
}