package interview.google.onsite;

import io.vertx.core.json.Json;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * AOOO
 * AOOO
 * OOBB
 *
 * 求A和B的最短哈密顿距离，这个题目就是3，最短是第二行的第一个A和第三行的第一个B
 *
 * LeetCode 286
 */
public class LeastManhantonDistance {

    int[][] dirs = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};


    @Test
    public void test() {
        int[][] board = new int[][]{{1, 0, 0, 0}, {1, 0, 0, 0}, {0, 0, -1, -1}};
        int[][] res = solve(board);
        System.out.println(Json.encode(res));
    }

    /**
     * Return the closest position of 1 and -1
     * @param board
     * @return
     */
    private int[][] solve(int[][] board) {
        int m = board.length, n = board[0].length;
        Queue<int[]> q = new LinkedList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 1) {
                    // [cur row, cur col, distance, orig row, orig col]
                    q.offer(new int[]{i, j, 0, i, j});
                }
            }
        }
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            for (int[] d : dirs) {
                int nr = cur[0] + d[0], nc = cur[1] + d[1], nCost = cur[2] + 1;
                if (nr >= 0 && nr < m && nc >=0 && nc < n) {
                    if (board[nr][nc] == -1) {
                        // [[1 row, 1 col], [-1 row, -1 col]]
                        return new int[][]{{cur[3], cur[4]}, {nr, nc}};
                    }
                    if (board[nr][nc] == 0) {
                        // visited
                        board[nr][nc] = 1;
                        q.offer(new int[]{nr, nc, nCost, cur[3], cur[4]});
                    }
                }
            }
        }
        return null;
    }
}
