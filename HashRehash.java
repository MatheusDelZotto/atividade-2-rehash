import java.util.Random;

public class HashRehash {
    static final int TAMANHO = 100;
    static final int QTD_ELEMENTOS = 100;
    static final int LIMITE_OCUPACAO = 90;
    static final int SEED = 1090;
    static final int VALOR_MAXIMO = 1000;
    static final int VAZIO = -1;

    static int[] tabela = new int[TAMANHO];
    static int ocupados = 0;

    static int hash(int chave) {
        return chave % TAMANHO;
    }

    static int rehash(int posicao) {
        return (posicao + 1) % TAMANHO;
    }

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

        System.out.println();
        System.out.println("===== MATRIZ FINAL =====");
        for (int i = 0; i < TAMANHO; i++) {
            if (tabela[i] == VAZIO) {
                System.out.println("[" + i + "] = livre");
            } else {
                System.out.println("[" + i + "] = " + tabela[i]);
            }
        }

        System.out.println();
        System.out.println("===== RESULTADOS =====");
        System.out.println("Elementos inseridos: " + inseridos);
        System.out.println("Elementos bloqueados: " + bloqueados);
        System.out.println("Total de colisões na inserção: " + totalColisoes);

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
