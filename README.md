# Atividade 2 - Implementação do Re Hash

Tabela hash (matriz) de **100 posições** com endereçamento aberto em Java.

- Função hash: `h(k) = k % 100`
- Re hash (linear): `rh(p) = (p + 1) % 100`
- 100 números aleatórios distintos (0 a 999) gerados com `Random` e **seed = 1090**
- Ao atingir **90% de ocupação** (90 elementos), o re hash bloqueia novas inserções
- Implementado apenas com `if`, `else`, `for`, `while` e `Random` (sem estruturas/métodos prontos da API)

## Como executar

```bash
javac HashRehash.java
java HashRehash
```

## Resultados

| Item | Valor |
|---|---|
| Elementos inseridos | 90 |
| Elementos bloqueados (90% de ocupação) | 10 |
| **Total de colisões na inserção** | **197** |

**3 elementos que mais colidiram:**

1. `950` (hash 50) com 38 colisões
2. `774` (hash 74) com 21 colisões
3. `163` (hash 63) com 17 colisões

**Posições livres na matriz:** 1, 2, 3, 4, 5, 27, 34, 37, 38, 97

**Teste de busca:**

| Elemento | Hash | Posição encontrada | Tentativas |
|---|---|---|---|
| 950 | 50 | 88 | 39 |
| 774 | 74 | 95 | 22 |
| 163 | 63 | 80 | 18 |
