public class ItemDePedido {

    // Atributos encapsulados
    private Produto produto;
    private int quantidade;
    private double precoVenda;

    /**
     * Construtor da classe ItemDePedido.
     * O precoVenda deve ser capturado do produto no momento da criação do item,
     * garantindo que alterações futuras no preço do produto não afetem este pedido.
     */
    public ItemDePedido(Produto produto, int quantidade, double precoVenda) {
        if (produto == null) {
            throw new IllegalArgumentException("O produto precisa ser criado primeiramente");
        }
        if (quantidade < 0) {
            throw new IllegalArgumentException("A quantidade de itens deve ser pelo menos uma");
        }
        if (precoVenda < 0) {
            throw new IllegalArgumentException("O produto precisa de um valor para ser vendido");
        }
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoVenda = precoVenda;
    }

    public double calcularSubtotal() {
        return 0;
    }

    public Produto getProduto() {
		return produto;
	}

    public int getQuantidade() {
		return quantidade;
	}

    public double getPrecoVenda() {
		return precoVenda;
	}

    @Override
    public boolean equals(Object obj) {
        Pedido outro = (Pedido)obj;
        return this.produto.equals(outro.produto);
    }

    /**
     * Compara a igualdade entre dois itens de pedido.
     * A regra de negócio define que dois itens são iguais se possuírem o mesmo Produto.
     */
    @Override
    public boolean equals(Object obj) {

        return false;
    }
}
