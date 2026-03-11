/*
 * ============================================================
 * 1009. Complement of Base 10 Integer
 * Difficulty: Easy
 * ============================================================
 *
 * PROBLEM:
 * The complement of an integer is the integer you get when you
 * flip all 0's to 1's and all 1's to 0's in its binary representation.
 * Given an integer n, return its complement.
 *
 * EXAMPLES:
 *   n = 5  → "101"  → complement "010"  → 2
 *   n = 7  → "111"  → complement "000"  → 0
 *   n = 10 → "1010" → complement "0101" → 5
 *
 * CONSTRAINTS:
 *   0 <= n < 10^9
 *
 * ============================================================
 * INTUITION:
 * If we XOR any bit with 1, it flips (0→1, 1→0).
 * So if we XOR n with a mask of all 1's of the same bit-length,
 * every bit in n gets flipped — which is exactly the complement.
 *
 * Example:
 *   n      =  5  →  1 0 1
 *   mask   =  7  →  1 1 1
 *   n^mask =  2  →  0 1 0  ✓
 *
 * ============================================================
 * APPROACH:
 * 1. Handle edge case: n = 0 → complement is 1
 * 2. Build a mask with all 1's matching the bit-length of n
 *    e.g. n = 5 (3 bits) → mask = 111 in binary = 7
 * 3. Return n ^ mask
 *
 * ============================================================
 * COMPLEXITY:
 *   Time  : O(log n) — mask building loop runs once per bit
 *   Space : O(1)     — no extra data structures used
 * ============================================================
 */

class Solution {
    public int bitwiseComplement(int n) {
        if (n == 0) return 1;
        int mask = 1;
        while (mask < n) {
            mask = (mask << 1) | 1;
        }
        return n ^ mask;
    }
}