"""
Climbing Grid
==============

Your task is to implement the function, that given an "n" x "m" climbing grid,
find the maximum score such that your total summed energy cost is under some
budget B.

Your constraints:
    * Your budget B is an integer, and your path must result in a cost less
      than or equal to B.
    * You may only move RIGHT and UP in direction along the grid to form a
      path.
    * Optimise the maximum score gained.
    * You _MUST_ start from the bottom left vertex.

=== Example ===

Budget = 10
Cost Right = 3
Cost up = 2

Grid:
    1 1 2
    1 2 2
    1 2 1

Path for maximum reward:

        2
      2 2
    1 2

Return: 9
"""

from typing import List


def set_grid(height, width, grid):
    dp = [[0 for i in range(width)] for j in range(height)]
    dp[0][0] = grid[0][0]
    for i in range(1, height):
        dp[i][0] = dp[i - 1][0] + grid[i][0]
    for j in range(1, width):
        dp[0][j] = dp[0][j - 1] + grid[0][j]
    for i in range(1, height):
        for j in range(1, width):
            dp[i][j] = max(dp[i - 1][j], dp[i][j - 1]) + grid[i][j]
    return dp


def get_opt(dp, height, width, cost_up, cost_right, budget):
    best = 0
    for i in range(0, width):
        if (budget - cost_right * i) < 0:
            break
        highest = (budget - cost_right * i) // cost_up
        cur = 0
        if highest >= height - 1:
            cur = dp[height - 1][i]
        else:
            cur = dp[highest][i]
        if cur > best:
            best = cur
    return best


def max_score(
        height: int,
        width: int,
        grid: List[List[int]],
        cost_right: int,
        cost_up: int,
        budget: int) -> int:
    """
    Get the max score by traversing the `grid`.

    ***IMPORTANT NOTE***: Your GRID is represented in coordinates, so:
    grid[0][0] is the bottom, leftmost vertex, and
    grid[height-1][width-1] is the top, rightmost.

    Example: The Grid

    1 2 3
    4 5 6
    7 8 9

    Is represented as

    grid = [
      [7, 8, 9],
      [4, 5, 6],
      [1, 2, 3]
    ]

    so grid[0][0] == 7 and grid[3-1][3-1] == 3.

    :param cost_up - the cost of moving upwards
    :param cost_right - the cost of moving right
    :param height - the height of the grid (n)
    :param width - the width of the grid (m)
    :param grid - The nxm matrix of vertices.
    :param budget - The maximum budget you can use.
    :returns The maximum score obtained by your path.
    """
    dp = set_grid(height, width, grid)
    best = get_opt(dp, height, width, cost_up, cost_right, budget)
    return best
    # TODO implement me please
