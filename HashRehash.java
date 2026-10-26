import java.util.Random;

/*
 * Atividade 2 - Implementação do Re Hash
 *
 * Tabela hash (matriz) de 100 posições com endereçamento aberto.
 * Função hash:  h(k) = k % 100
 * Re hash:      rh(p) = (p + 1) % 100   (re hash linear)
 *
 * Tentamos inserir 100 elementos aleatórios (seed = 1090). Quando a matriz
 * atinge 90% de ocupação, o re hash bloqueia a inserção de novos elementos.
 *
 * Restrição: sem comandos prontos da API do Java (apenas if, else, for,
 * while e Random). System.out.println é usado somente para exibir resultados.
 */
public class HashRehash {

    static final int TAMANHO = 100;
    static final int QTD_ELEMENTOS = 100;
    static final int LIMITE_OCUPACAO = 90; // 90% de 100 posições
    static final int SEED = 1090;
    static final int VALOR_MAXIMO = 1000;  // números gerados entre 0 e 999
    static final int VAZIO = -1;

    static int[] tabela = new int[TAMANHO];
    static int ocupados = 0;

    // Função hash
    static int hash(int chave) {
        return chave % TAMANHO;
    }

    // Função de re hash: próxima posição a ser testada após uma colisão
    static int rehash(int posicao) {
        return (posicao + 1) % TAMANHO;
    }

    // Insere a chave e retorna quantas colisões ocorreram até achar posição livre.
    // Retorna -1 se o re hash bloqueou a inserção (matriz com 90% de ocupação).
    static int inserir(int chave) {
        if (ocupados >= LIMITE_OCUPACAO) {
            return -1;
        }

        int posicao = hash(chave);
        int colisoes = 0;

        while (tabela[posicao] != VAZIO) {
            colisoes++;
            posicao = rehash(posicao);
        }

        tabela[posicao] = chave;
        ocupados++;
        return colisoes;
    }

    // Busca a chave e retorna a posição onde está, ou -1 se não encontrada.
    // Guarda em tentativasBusca quantas posições foram visitadas.
    static int tentativasBusca = 0;

    static int buscar(int chave) {
        int posicao = hash(chave);
        tentativasBusca = 0;

        while (tentativasBusca < TAMANHO && tabela[posicao] != VAZIO) {
            tentativasBusca++;
            if (tabela[posicao] == chave) {
                return posicao;
            }
            posicao = rehash(posicao);
        }
        return -1;
    }

    public static void main(String[] args) {
        Random random = new Random(SEED);

        for (int i = 0; i < TAMANHO; i++) {
            tabela[i] = VAZIO;
        }

        // Gera 100 números aleatórios distintos
        int[] elementos = new int[QTD_ELEMENTOS];
        int gerados = 0;
        while (gerados < QTD_ELEMENTOS) {
            int numero = random.nextInt(VALOR_MAXIMO);
            boolean repetido = false;
            for (int i = 0; i < gerados; i++) {
                if (elementos[i] == numero) {
                    repetido = true;
                }
            }
            if (!repetido) {
                elementos[gerados] = numero;
                gerados++;
            }
        }

        // Inserção
        int[] colisoesPorElemento = new int[QTD_ELEMENTOS];
        int totalColisoes = 0;
        int inseridos = 0;
        int bloqueados = 0;

        System.out.println("===== INSERÇÃO (seed = " + SEED + ") =====");
        for (int i = 0; i < QTD_ELEMENTOS; i++) {
            int colisoes = inserir(elementos[i]);
            if (colisoes == -1) {
                colisoesPorElemento[i] = -1;
                bloqueados++;
                System.out.println("Elemento " + elementos[i]
                        + " -> BLOQUEADO (matriz com 90% de ocupação)");
            } else {
                colisoesPorElemento[i] = colisoes;
                totalColisoes = totalColisoes + colisoes;
                inseridos++;
                System.out.println("Elemento " + elementos[i]
                        + " -> hash " + hash(elementos[i])
                        + ", colisões: " + colisoes);
            }
        }

        // Matriz final
        System.out.println();
        System.out.println("===== MATRIZ FINAL =====");
        for (int i = 0; i < TAMANHO; i++) {
            if (tabela[i] == VAZIO) {
                System.out.println("[" + i + "] = livre");
            } else {
                System.out.println("[" + i + "] = " + tabela[i]);
            }
        }

        // Resumo
        System.out.println();
        System.out.println("===== RESULTADOS =====");
        System.out.println("Elementos inseridos: " + inseridos);
        System.out.println("Elementos bloqueados: " + bloqueados);
        System.out.println("Total de colisões na inserção: " + totalColisoes);

        // 3 elementos que mais colidiram (seleção manual, sem ordenação pronta).
        // Em caso de empate, fica o que foi inserido primeiro.
        int[] top = new int[3];
        boolean[] escolhido = new boolean[QTD_ELEMENTOS];
        for (int t = 0; t < 3; t++) {
            int maior = -1;
            for (int i = 0; i < QTD_ELEMENTOS; i++) {
                if (!escolhido[i] && colisoesPorElemento[i] >= 0) {
                    if (maior == -1 || colisoesPorElemento[i] > colisoesPorElemento[maior]) {
                        maior = i;
                    }
                }
            }
            escolhido[maior] = true;
            top[t] = maior;
        }

        System.out.println();
        System.out.println("3 elementos que mais colidiram:");
        for (int t = 0; t < 3; t++) {
            System.out.println("  " + (t + 1) + "º) elemento " + elementos[top[t]]
                    + " com " + colisoesPorElemento[top[t]] + " colisões");
        }

        // Posições livres
        System.out.println();
        System.out.print("Posições livres na matriz: ");
        int livres = 0;
        for (int i = 0; i < TAMANHO; i++) {
            if (tabela[i] == VAZIO) {
                if (livres > 0) {
                    System.out.print(", ");
                }
                System.out.print(i);
                livres++;
            }
        }
        System.out.println();
        System.out.println("Total de posições livres: " + livres);

        // Teste de busca com os 3 elementos que mais colidiram
        System.out.println();
        System.out.println("===== TESTE DE BUSCA =====");
        for (int t = 0; t < 3; t++) {
            int chave = elementos[top[t]];
            int posicao = buscar(chave);
            if (posicao == -1) {
                System.out.println("Elemento " + chave + " NÃO encontrado ("
                        + tentativasBusca + " tentativas)");
            } else {
                System.out.println("Elemento " + chave + " encontrado na posição "
                        + posicao + " (hash " + hash(chave) + ", "
                        + tentativasBusca + " tentativas)");
            }
        }
    }
}
