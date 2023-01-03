import sys
import gzip

def main(arg1, arg2):
  if arg1 == "decompress":
    data = str(gzip.decompress(open(arg2, "rb").read()), 'utf-8')
    with open(f'{arg2}_new', 'w') as f:
      f.write(data)
      

if __name__ == "__main__":
  main(sys.argv[1], sys.argv[2])