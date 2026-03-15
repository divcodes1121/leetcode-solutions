/*
================================================================
LeetCode 207. Course Schedule
Difficulty: Medium
Topics: Graph · DFS · Cycle Detection · Topological Sort
================================================================

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
PROBLEM SUMMARY
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
You have numCourses courses (0 to numCourses-1).
prerequisites[i] = [a, b] means: take b before a.

Return true if you can finish all courses, false if impossible.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
EXAMPLES
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Example 1: numCourses=2, prerequisites=[[1,0]]
  0 → 1: take 0 first, then 1. No cycle → true ✓

Example 2: numCourses=2, prerequisites=[[1,0],[0,1]]
  0 → 1 → 0: cycle detected → false ✓

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
INTUITION
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
This is purely a cycle detection problem in a directed graph.
If there's a cycle in prerequisites → impossible to finish.
If no cycle → always possible.

Model it as: each course = node, each prerequisite = directed edge.
Run DFS and track if we ever visit a node that's currently being
processed — that means we found a cycle.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
APPROACH
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Step 1 — Build adjacency list
  For each [a, b]: add edge b → a
  (b must come before a, so b points to a)

Step 2 — DFS with 3-state tracking
  Each node has one of three states:
    0 = unvisited
    1 = currently being visited (in current DFS path)
    2 = fully processed (safe, no cycle through here)

  During DFS:
    → Hit state 1: we're back on the current path = CYCLE found
    → Hit state 2: already confirmed safe, skip
    → Hit state 0: explore it, mark as 1 first

Step 3 — Run DFS from every node
  Some nodes may be disconnected, so we try all.
  If any DFS returns true (cycle found) → return false.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
COMPLEXITY
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Time  : O(V + E) — V = numCourses, E = prerequisites.length
  Space : O(V + E) — graph + state array + recursion stack

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
WALKTHROUGH — Example 2
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
numCourses=2, prerequisites=[[1,0],[0,1]]

Graph: 0→1, 1→0

DFS from node 0:
  state[0] = 1 (visiting)
  → visit neighbor 1:
      state[1] = 1 (visiting)
      → visit neighbor 0:
          state[0] == 1 → CYCLE! return true
  → cycle detected → canFinish returns false ✓

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
EDGE CASES
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  • No prerequisites at all   → no edges, no cycle → true
  • Single course             → always true
  • Disconnected components   → DFS from every node handles this
  • Self loop [a, a]          → immediate cycle → false
================================================================
*/

class Solution {

    public boolean canFinish(int numCourses, int[][] prerequisites) {

        // Build adjacency list
        HashMap<Integer, List<Integer>> graph = new HashMap<>();

        for(int i = 0; i < numCourses; i++)
            graph.put(i, new ArrayList<>());

        for(int[] p : prerequisites) {
            graph.get(p[1]).add(p[0]);
        }

        // 0 = unvisited, 1 = visiting, 2 = fully processed
        int[] state = new int[numCourses];

        for(int i = 0; i < numCourses; i++) {
            if(dfs(graph, state, i))
                return false;
        }

        return true;
    }

    private boolean dfs(HashMap<Integer, List<Integer>> graph, int[] state, int node) {

        // Currently in DFS path = cycle found
        if(state[node] == 1)
            return true;

        // Already fully processed = safe, skip
        if(state[node] == 2)
            return false;

        // Mark as currently visiting
        state[node] = 1;

        for(int neighbor : graph.get(node)) {
            if(dfs(graph, state, neighbor))
                return true;
        }

        // Mark as fully processed
        state[node] = 2;

        return false;
    }
}