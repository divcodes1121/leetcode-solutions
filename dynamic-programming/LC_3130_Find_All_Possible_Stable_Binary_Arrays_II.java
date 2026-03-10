/*
---------------------------------------------------------------------
LeetCode 3130. Find All Possible Stable Binary Arrays II
Difficulty: Hard 🔴
Topic: Dynamic Programming
---------------------------------------------------------------------

Problem
-------

You are given three positive integers: zero, one, and limit.

A binary array arr is called stable if:

1. The number of occurrences of 0 in arr is exactly 'zero'.
2. The number of occurrences of 1 in arr is exactly 'one'.
3. Every subarray of length greater than 'limit' must contain both 0 and 1.

In other words, we cannot have more than 'limit' consecutive 0s or 1s.

Return the number of stable binary arrays.

Since the answer can be very large, return it modulo 10^9 + 7.

---------------------------------------------------------------------

Examples
--------

Example 1:
Input:  zero = 1, one = 1, limit = 2
Output: 2
Explanation:
Possible arrays:
[1,0], [0,1]

Example 2:
Input:  zero = 1, one = 2, limit = 1
Output: 1
Explanation:
Only valid array:
[1,0,1]

Example 3:
Input:  zero = 3, one = 3, limit = 2
Output: 14

---------------------------------------------------------------------

Constraints
-----------

1 <= zero, one, limit <= 1000

---------------------------------------------------------------------

Approach
--------

Key Observation:
A subarray larger than 'limit' must contain both 0 and 1.

Therefore:
We cannot have more than 'limit' consecutive identical numbers.

Dynamic Programming State:

dp[i][j][0] -> number of arrays using
               i zeros and j ones
               ending with 0

dp[i][j][1] -> number of arrays using
               i zeros and j ones
               ending with 1

Transition Idea:

To end with 0:
We append a block of k zeros (1 ≤ k ≤ limit)
to an array that previously ended with 1.

dp[i][j][0] = sum(dp[i-k][j][1])  for k in [1..limit]

To end with 1:
We append a block of k ones (1 ≤ k ≤ limit)
to an array that previously ended with 0.

dp[i][j][1] = sum(dp[i][j-k][0])  for k in [1..limit]

To compute these sums efficiently, we maintain prefix sums:

pzo -> prefix sums of dp[*][j][1]
poo -> prefix sums of dp[i][*][0]

This allows O(1) range queries.

---------------------------------------------------------------------

Time Complexity
---------------

O(zero × one)

---------------------------------------------------------------------

Space Complexity
----------------

O(zero × one)

---------------------------------------------------------------------
*/

class Solution {

    public int numberOfStableArrays(int zero, int one, int limit) {

        final int MOD = 1_000_000_007;

        // dp[i][j][0] = arrays with i zeros, j ones ending with 0
        // dp[i][j][1] = arrays with i zeros, j ones ending with 1
        long[][][] dp = new long[zero + 1][one + 1][2];

        // prefix sums
        long[][] pzo = new long[zero + 1][one + 1];
        long[][] poo = new long[zero + 1][one + 1];

        for (int i = 0; i <= zero; i++) {
            for (int j = 0; j <= one; j++) {

                // ---- compute dp[i][j][0] ----
                if (i > 0) {
                    long sum = pzo[i - 1][j];

                    if (i - 1 - limit >= 0)
                        sum -= pzo[i - 1 - limit][j];

                    sum = ((sum % MOD) + MOD) % MOD;

                    // pure zero case
                    if (j == 0 && i <= limit)
                        sum = (sum + 1) % MOD;

                    dp[i][j][0] = sum;
                }

                // ---- compute dp[i][j][1] ----
                if (j > 0) {
                    long sum = poo[i][j - 1];

                    if (j - 1 - limit >= 0)
                        sum -= poo[i][j - 1 - limit];

                    sum = ((sum % MOD) + MOD) % MOD;

                    // pure one case
                    if (i == 0 && j <= limit)
                        sum = (sum + 1) % MOD;

                    dp[i][j][1] = sum;
                }

                // ---- update prefix sums ----
                pzo[i][j] = ((i > 0 ? pzo[i - 1][j] : 0) + dp[i][j][1]) % MOD;
                poo[i][j] = ((j > 0 ? poo[i][j - 1] : 0) + dp[i][j][0]) % MOD;
            }
        }

        return (int)((dp[zero][one][0] + dp[zero][one][1]) % MOD);
    }
}