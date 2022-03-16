package interview.google.phone;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * https://www.1point3acres.com/bbs/forum.php?mod=viewthread&tid=698370&extra=&highlight=google%2B%C3%E6&page=1
 *
 * Input:
 * - num_states：int
 * - delegates: int[]
 * - votes_president_A: int[]
 * - votes_president_B: int[]
 * - votes_Undecided: int[]
 *
 * output: the minimum number of people vote A will lead A win, output -1 if not valid (int)
 *
 * example:
 *
 * input1:
 * num_states = 3
 * [delegate, vote_A, vote_B, undecided)
 * state 1: 5 0 0 20
 * state 2: 4 0 0 19
 * state 3: 2 0 0 10
 *
 * output1:
 * 16
 *
 * Input 2:
 * 3
 * 7 100 200 200
 * 8 100 300 200
 * 9 100 400 200
 * Output 2: -1
 *
 * public int solution(int num_states, int[] delegates, int[] votes_president_A, int[]
 * votes_president_B, int[] votes_Undecided){
 * // return min number people vote A
 * }
 */
public class MinVoteToWinElection {
    @Test
    public void test() {
//        // test case 1 => 16, win 4 + 2
//        int[] delegates = new int[]{5, 4, 2};
//        int[] votes_president_A = new int[]{0, 0, 0};
//        int[] votes_president_B = new int[]{0, 0, 0};
//        int[] votes_Undecided = new int[]{20, 19, 10};

        // test case 2 => 16, winn 5 draw 2
        int[] delegates = new int[]{5, 4, 2};
        int[] votes_president_A = new int[]{0, 0, 0};
        int[] votes_president_B = new int[]{0, 10, 0};
        int[] votes_Undecided = new int[]{20, 9, 10};
        int res = vote(delegates, votes_president_A, votes_president_B, votes_Undecided);
    }

    private int vote(int[] delegates, int[] voteA, int[] voteB, int[] voteUndecided) {
        int aGot = 0, sum = 0, bGot = 0;
        List<int[]> votes = new ArrayList<>();
        for (int i = 0; i < delegates.length; i++) {
            int va = voteA[i], vb = voteB[i], undecided = voteUndecided[i];
            if (va + undecided < vb) {
                bGot += delegates[i];
            } else if (vb + undecided < va) {
                aGot += delegates[i];
            } else {
                int toWin = (va + vb + undecided) / 2 + 1 - va;
                int draw = (va + vb + undecided) % 2;
                votes.add(new int[]{delegates[i], toWin, draw});
                sum += delegates[i];
            }
        }
        if (aGot + sum <= bGot) return -1;
        int need = (aGot + bGot + sum) / 2 + 1 - aGot;
        if (need <= 0) return 0;

        int n = votes.size();
        // dp[i][j]: when only given the first i(start from 1) delegates, to win j votes in total, the effort needs for A.
        int[][] dp = new int[n + 1][sum + 1];
        for (int i = 0; i < n + 1; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
            dp[i][0] = 0;
        }

        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < sum + 1; j++) {
                // Do not take this state
                dp[i][j] = Math.min(dp[i][j], dp[i - 1][j]);
                // Win this state
                if (j - votes.get(i - 1)[0] >= 0
                        && dp[i - 1][j - votes.get(i - 1)[0]] != Integer.MAX_VALUE) {
                    dp[i][j] = Math.min(dp[i][j],
                            dp[i - 1][j - votes.get(i - 1)[0]] + votes.get(i - 1)[1]);
                }
                // Draw this state
                if (j - votes.get(i - 1)[0] + 1 >= 0
                        && votes.get(i - 1)[2] == 0
                        && dp[i - 1][j - votes.get(i - 1)[0] + 1] != Integer.MAX_VALUE) {
                    dp[i][j] = Math.min(dp[i][j],
                            dp[i - 1][j - votes.get(i - 1)[0] + 1] + votes.get(i - 1)[1] - 1);
                }
            }
        }
        int minVotes = Integer.MAX_VALUE;
        for (int i = need; i < sum + 1; i++) minVotes = Math.min(minVotes, dp[n][i]);
        return minVotes == Integer.MAX_VALUE ? -1 : minVotes;
    }


//    {
//    // return minumber number people vote A
//    var target = delegates.Sum();
//    var dp = new int[target+1];
//    Array.Fill(dp, int.MaxValue);
//    dp[0] = 0;
//    var votes = new int[num_states];
//    for (var i = 0; i < num_states; ++i) {
//        // total number of votes
//        var total = votes_president_A[i] + votes_president_B[i] + votes_Undecided[i];
//        // votes required to win for A
//        votes[i] = total / 2 + 1 - votes_president_A[i];
//    }
//     
//    // dp[j] = min(dp[j-delegates[i]] + votes[i], dp[j])   0 <= i < num_states
//    for (var i = 0; i < num_states; ++i) {
//        for (var j = target; j >= 0; --j) {
//            if (j >= delegates[i] &&
//                dp[j - delegates[i]] != int.MaxValue &&
//                dp[j - delegates[i]] + votes[i] < dp[j]) {
//                dp[j] = dp[j - delegates[i]] + votes[i];
//            }
//        }
//    }
//    // Find the minimum votes for delegates larger than half
//    var result = int.MaxValue;
//    for (var i = target/2+1; i <= target; ++i) {
//        result = Math.Min(result, dp[i]);
//    }
//    return result == int.MaxValue ? -1 : result;
//    }
}
