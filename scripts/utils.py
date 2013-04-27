import sys

class LibSVMPredReader:
    def __init__(self, fname):
        self.stream = open(fname)

        header = self.stream.readline().strip()
        if header.startswith("labels"):
            self.has_probs = True
            self.labels = [int(x) for x in header.split()[1:]]
        else:
            self.has_probs = False
            self.stream.close()
            self.stream = open(fname)

    def next_line(self):
        line = self.stream.readline()
        if line == "":
            return None, None

        lexemes = line.strip().split()
        pred = int(lexemes[0])
        predictions = None
        if self.has_probs:
            predictions = [0] * (len(self.labels) + 1)
            for i in range(1, len(lexemes)):
                predictions[self.labels[i - 1]] = float(lexemes[i])

        return pred, predictions

def read_file(fname):
    print >>sys.stderr, "Reading %s" % fname
    ret = []
    for line in open(fname):
        line = line.strip()
        t = line.split()[0]
        if t == "labels":
            continue
        ret.append(int(t))

    return ret 
