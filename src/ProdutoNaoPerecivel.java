public class ProdutoNaoPerecivel extends Produto {

	public ProdutoNaoPerecivel(String desc, double precoCusto, double margemLucro) {
		super(desc, precoCusto, margemLucro);
	}

	public ProdutoNaoPerecivel(String desc, double precoCusto) {
		super(desc, precoCusto);
	}

	@Override
	public double valorVenda() {
		return super.valorVenda();
	}

	@Override
	public String gerarDadosTexto() {
		return String.format("1; %s; %.2f; %.2f",
				getDescricao(),
				getPrecoCusto(),
				getMargemLucro());
	}
}
