import sys

# args = sys.argv[1:]

f = open('text1.txt','r')
lines = f.readlines()
f.close()

f2 = open('text2.txt','w')
for i in lines:
    f2.write(i)

f.close()