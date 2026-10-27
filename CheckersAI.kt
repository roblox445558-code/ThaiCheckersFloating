package com.example.thaicheckersfloating

object CheckersAI {
    // Prototype engine: men move forward and capture in all diagonal directions;
    // kings move one diagonal square. Confirm your app's exact Thai-checkers rules before relying on it.
    fun bestMove(board: Array<IntArray>, red: Boolean): Move? {
        val moves = legalMoves(board, red)
        if (moves.isEmpty()) return null
        return moves.maxByOrNull { scoreMove(board, it, red) }
    }

    private fun scoreMove(b: Array<IntArray>, m: Move, red: Boolean): Int {
        var score = if (m.capture) 100 else 0
        val piece = b[m.fromR][m.fromC]
        if ((piece > 0 && m.toR == 0) || (piece < 0 && m.toR == 7)) score += 30
        score += 3 - kotlin.math.abs(3.5 - m.toC).toInt()
        return score
    }

    fun legalMoves(board: Array<IntArray>, red: Boolean): List<Move> {
        val moves = mutableListOf<Move>()
        val captures = mutableListOf<Move>()
        for (r in 0..7) for (c in 0..7) {
            val piece = board[r][c]
            if (piece == 0 || ((piece > 0) != red) || (r+c)%2 == 0) continue
            val dirs = if (piece > 0) arrayOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1)
                       else arrayOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1)
            for ((dr,dc) in dirs) {
                val nr=r+dr; val nc=c+dc
                if (nr !in 0..7 || nc !in 0..7) continue
                if (board[nr][nc] == 0) {
                    // For men, normal moves only forward.
                    val forward = (piece > 0 && dr < 0) || (piece < 0 && dr > 0)
                    if (forward) moves.add(Move(r,c,nr,nc,false))
                } else if ((board[nr][nc] > 0) != red) {
                    val tr=r+2*dr; val tc=c+2*dc
                    if (tr in 0..7 && tc in 0..7 && board[tr][tc] == 0)
                        captures.add(Move(r,c,tr,tc,true))
                }
            }
        }
        return if (captures.isNotEmpty()) captures else moves
    }
}
