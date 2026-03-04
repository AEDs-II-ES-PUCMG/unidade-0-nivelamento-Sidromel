import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Comercio {

	static final int MAX_NOVOS_PRODUTOS = 10;
	static String nomeArquivoDados;
	static Scanner teclado;
	static Produto[] produtosCadastrados;
	static int quantosProdutos;

	static void pausa() {
		System.out.println("Digite enter para continuar...");
		teclado.nextLine();
	}

	static void cabecalho() {
		System.out.println("AEDII COMÉRCIO DE COISINHAS");
		System.out.println("===========================");
	}

	static int menu() {
		cabecalho();
		System.out.println("1 - Listar todos os produtos");
		System.out.println("2 - Procurar e listar um produto");
		System.out.println("3 - Cadastrar novo produto");
		System.out.println("0 - Sair");
		System.out.print("Digite sua opção: ");
		return Integer.parseInt(teclado.nextLine());
	}

	// Lê o arquivo de dados e devolve o vetor de produtos, já com espaço para novos cadastros.
	static Produto[] lerProdutos(String nomeArquivoDados) {
		Produto[] vetorProdutos;
		Scanner arqDados = null;
		try {
			arqDados = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));
			int n = Integer.parseInt(arqDados.nextLine().trim());
			quantosProdutos = n;
			vetorProdutos = new Produto[quantosProdutos + MAX_NOVOS_PRODUTOS];
			for (int i = 0; i < quantosProdutos; i++) {
				String linha = arqDados.nextLine();
				vetorProdutos[i] = Produto.criarDoTexto(linha);
			}
		} catch (FileNotFoundException e) {
			vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS];
			quantosProdutos = 0;
		} catch (Exception e) {
			vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS];
			quantosProdutos = 0;
		} finally {
			if (arqDados != null) {
				arqDados.close();
			}
		}
		return vetorProdutos;
	}

	/** Lista todos os produtos cadastrados, numerados, um por linha */
	static void listarTodosOsProdutos() {
		cabecalho();
		if (quantosProdutos == 0) {
			System.out.println("Nenhum produto cadastrado.");
			return;
		}
		for (int i = 0; i < quantosProdutos; i++) {
			System.out.println((i + 1) + " - " + produtosCadastrados[i].toString());
		}
	}

	static void localizarProdutos() {
		System.out.print("Digite a descrição do produto a localizar: ");
		String descricao = teclado.nextLine().trim();
		if (descricao.length() < 3) {
			System.out.println("Descrição deve ter pelo menos 3 caracteres.");
			return;
		}
		Produto busca = new ProdutoNaoPerecivel(descricao, 0.01, 0.01);
		boolean encontrado = false;
		for (int i = 0; i < quantosProdutos; i++) {
			if (produtosCadastrados[i].equals(busca)) {
				System.out.println(produtosCadastrados[i].toString());
				encontrado = true;
				break;
			}
		}
		if (!encontrado) {
			System.out.println("Produto não encontrado.");
		}
	}

	static void cadastrarProduto() {
		if (quantosProdutos >= produtosCadastrados.length) {
			System.out.println("Não há espaço para mais produtos.");
			return;
		}
		System.out.println("Tipo: 1 - Não perecível | 2 - Perecível");
		System.out.print("Digite o tipo: ");
		int tipo = Integer.parseInt(teclado.nextLine().trim());
		System.out.print("Descrição (mín. 3 caracteres): ");
		String descricao = teclado.nextLine().trim();
		System.out.print("Preço de custo: ");
		double precoCusto = Double.parseDouble(teclado.nextLine().trim().replace(",", "."));
		System.out.print("Margem de lucro (ex: 0,20): ");
		double margemLucro = Double.parseDouble(teclado.nextLine().trim().replace(",", "."));

		try {
			if (tipo == 1) {
				produtosCadastrados[quantosProdutos++] = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
				System.out.println("Produto não perecível cadastrado.");
			} else if (tipo == 2) {
				System.out.print("Data de validade (dd/mm/aaaa): ");
				String dataValidade = teclado.nextLine().trim();
				produtosCadastrados[quantosProdutos++] = new ProdutoPerecivel(descricao, precoCusto, margemLucro, dataValidade);
				System.out.println("Produto perecível cadastrado.");
			} else {
				System.out.println("Tipo inválido.");
			}
		} catch (IllegalArgumentException e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

	public static void salvarProdutos(String nomeArquivo) {
		try (PrintWriter arquivo = new PrintWriter(new OutputStreamWriter(new FileOutputStream(nomeArquivo), Charset.forName("ISO-8859-2")))) {
			arquivo.println(quantosProdutos);
			for (int i = 0; i < quantosProdutos; i++) {
				arquivo.println(produtosCadastrados[i].gerarDadosTexto());
			}
		} catch (Exception e) {
			System.err.println("Erro ao salvar: " + e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
		nomeArquivoDados = "dadosProdutos.csv";
		produtosCadastrados = lerProdutos(nomeArquivoDados);
		int opcao = -1;
		do {
			opcao = menu();
			switch (opcao) {
				case 1 -> listarTodosOsProdutos();
				case 2 -> localizarProdutos();
				case 3 -> cadastrarProduto();
			}
			if (opcao != 0) {
				pausa();
			}
		} while (opcao != 0);
		salvarProdutos(nomeArquivoDados);
		teclado.close();
	}
}
