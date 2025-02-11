package com.example.financeplayapi.commons.persistence

import java.util.*

/**
 * Representação do caminho de uma demografia na árvore de demografias. Um caminho é um conjunto de inteiros separados por ".". A
 * representação string do path é algo como
 * .1.3.2.4.10.
 *
 *
 * Se a representação string de um path P1 começa com o valor integral de um path P2, considera-se que P2 é ancestral de P1.
 */
class NodePath {
    private var parts: IntArray
    private var path: String

    /**
     * Construtor.
     *
     * @param path array de inteiros do path
     */
    constructor(vararg path: Int) {
        parts = IntArray(path.size)
        System.arraycopy(path, 0, parts, 0, path.size)
        val sb = StringBuilder(path.size * 3)
        sb.append(".")
        for (i in path) {
            sb.append(i)
            sb.append(".")
        }
        this.path = sb.toString()
    }

    /**
     * Construtor.
     *
     * @param path representacao string do path no formato ".X1.X2.X3.....Xn.", onde Xi sao inteiros
     */
    constructor(path: String?) {
        require(!(path == null || !path.matches(PATH))) { "Invalid path: $path" }
        this.path = path
        val ps = path.replace("[.]".toRegex(), " ").trim { it <= ' ' }.split(" ").toTypedArray()
        parts = IntArray(ps.size)
        for (i in ps.indices) {
            parts[i] = ps[i].toInt()
        }
    }

    /**
     * Obtem um trecho inicial do path.
     *
     * @param lastIndex indice do ultimo elemento (exclusive).
     *
     * @return novo path contendo o trecho selecionado
     */
    fun subPath(lastIndex: Int): NodePath {
        return this.subPath(0, lastIndex)
    }

    /**
     * Obtem um trecho do path.
     *
     * @param firstIndex indice do primeiro elemento (inclusive).
     * @param lastIndex  indice do ultimo elemento (exclusive).
     *
     * @return novo path contendo o trecho selecionado
     */
    fun subPath(firstIndex: Int, lastIndex: Int): NodePath {
        val arr = IntArray(lastIndex - firstIndex)
        for (i in firstIndex until lastIndex) {
            arr[i - firstIndex] = parts[i]
        }
        return NodePath(*arr)
    }

    /**
     * Gera um path filho.
     *
     * @param i valor do novo nivel do path
     *
     * @return novo path
     */
    fun child(i: Int): NodePath {
        val newArr = Arrays.copyOf(parts, parts.size + 1)
        newArr[newArr.size - 1] = i
        return NodePath(*newArr)
    }

    /**
     * Verifica se this é um path ancestral ou igual a um outro path.
     *
     * @param path path verificado se é descendente ou igual
     *
     * @return true caso this seja ancestral ou igual ao path; false caso contrário
     */
    fun isChildOrEqualOf(path: NodePath): Boolean {
        return this.path.matches("$path.*".toRegex())
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that: NodePath = o as NodePath
        return path == that.path
    }

    override fun hashCode(): Int {
        return Objects.hash(path)
    }

    /**
     * Tamanho do path, que é dado pelo número de partes inteiras na composição do path.
     *
     * @return tamanho
     */
    fun size(): Int {
        return parts.size
    }

    override fun toString(): String {
        return path
    }

    companion object {
        private val PATH = "\\.(\\d+\\.)+".toRegex()
    }
}
