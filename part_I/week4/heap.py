class heap:

    def __init__(self) -> None:
        self.h = [0]
        self.n = 0

    def is_empty(self) -> bool:
        return self.n == 0

    def push(self, item):
        self.h += [item]
        self.n += 1
        self._promote(self.n)

    def pop(self):
        # exchange root with last leaf and then sink (demote)
        maxItem = self.h[1]
        self.h[1], self.h[self.n] = self.h[self.n], self.h[1]
        self.n -= 1
        self.h.pop()
        self._demote(1)
        return maxItem

    def _promote(self, k):
        while (k > 1 and self.h[k//2] < self.h[k]):
            self.h[k//2], self.h[k] = self.h[k], self.h[k//2]
            k = k // 2
    
    def _demote(self, k):
        while k * 2 <= self.n:
            j = 2 * k
            if j < self.n and self.h[j] < self.h[j + 1]:
                j += 1
            if self.h[k] > self.h[j]:
                break
            self.h[k], self.h[j] = self.h[j], self.h[k]
            k = j


if __name__ == '__main__':
    h = heap()

    from random import randrange

    for _ in range(10):
        r = randrange(26)
        h.push(chr(r + ord('A')))

    print(h.h)

    while not h.is_empty():
        print(h.pop())