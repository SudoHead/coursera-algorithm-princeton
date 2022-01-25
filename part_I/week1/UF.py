class UF:
    def __init__(self, n: int) -> None:
        self.n = n
        self.id = [i for i in range(n)]
        self.sz = [1] * n # number of items for its tree
        self.num_sets = n

    def check_index(self, i: int) -> bool:
        if (i < 0 or i >= self.n):
            raise ValueError(f"Index {i} out of bounds")
    
    def root(self, p: int) -> int:
        self.check_index(p)
        while (self.id[p] != p):
            self.id[p] = self.id[self.id[p]] # sets all id of all children to the root | lg*N (log star)
            p = self.id[p]
        return p

    def union(self, p: int, q: int) -> None:
        i = self.root(p)
        j = self.root(q)
        if i == j: # same root == already connected
            return
        if self.sz[i] < self.sz[j]:
            self.id[i] = j
            self.sz[j] += self.sz[i]
        else:
            self.id[j] = i
            self.sz[i] += self.sz[j]
        self.num_sets -= 1

    def connected(self, p: int, q: int) -> bool:
        return self.root(p) == self.root(q)

    def find(self, p: int) -> int:
        return self.root(p)

    def count(self) -> int:
        return self.num_sets

if __name__ == '__main__':
    import sys
    if len(sys.argv) != 2:
        print("Requires size argument n: str.")
        sys.exit(0)
    size = int(sys.argv[1])
    print(f'Creating a WeightedUnionFind object of size {size}.')
    uf = UF(size)
    while (True):
        input_str = input("Enter union, two numbers separeted by space:")
        try:
            p, q = [int(s) for s in input_str.split()]
            uf.union(p, q)
        except Exception:
            print("Input not valid. Try again.")
            continue
        print(f"id[]: {uf.id}")
        print(f"sz[]: {uf.sz}")
        print(f"count: {uf.count()}\n")