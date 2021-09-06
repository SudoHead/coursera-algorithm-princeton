class QuickFind(object):
    def __init__(self, n):
        self.count = n
        self.id = [i for i in range(n)]
    
    def connected(self, p, q):
        """ O(1) """
        return self.id[p] == self.id[q]

    def union(self, p, q):
        """ O(n). Quadratic complexity when union * N. Too slow."""
        pid = self.id[p]
        for i in range(self.count):
            if self.id[i] == pid:
                self.id[i] = self.id[q]


class QuickUnion(object):
    def __init__(self, n):
        self.count = n
        self.id = [i for i in range(n)]

    def root(self, i):
        """ O(n) """
        while i != self.id[i]:
            i = self.id[i]
        return i
    
    def connected(self, p, q):
        """ O(n) """
        return self.root(p) == self.root(q)

    def union(self, p, q):
        """ O(n+). Faster than before, but still too slow (tall tree)."""
        i = self.root(p)
        j = self.root(q)
        self.id[i] = j


class QuickUnionBalanced(object):
    def __init__(self, n):
        self.count = n
        self.id = [i for i in range(n)]
        self.sz = [1] * n

    def root(self, i):
        """ O(lg*n). Path compression. Point to root. Almost linear."""
        while i != self.id[i]:
            self.id[i] = self.id[self.id[i]]
            i = self.id[i]
        return i

    def connected(self, p, q):
        """ O(lg*n) """
        return self.root(p) == self.root(q)

    def union(self, p, q):
        """ O(lg*n) """
        i = self.root(p)
        j = self.root(q)
        if i == j:
            return
        if self.sz[i] < self.sz[j]:
            self.id[i] = j
            self.sz[j] += self.sz[i]
        else:
            self.id[j] = i
            self.sz[i] += self.sz[j]


import time, random

def benchmark(algo, n):
    """
    Benchmark the given algorithm.
    """
    start = time.time()
    for i in range(n):
        rand = random.randint(0, n-1)
        algo.connected(i, rand)
    end = time.time()
    return end - start


n = 10000000
# qf1 = QuickFind(n)
qf2 = QuickUnion(n)
qf3 = QuickUnionBalanced(n)

for algo in [qf2, qf3]:
    print(algo.__class__.__name__, benchmark(algo, n))