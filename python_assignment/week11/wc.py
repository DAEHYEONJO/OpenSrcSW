import sys

# fileName = sys.argv[1]
f = open('text1.txt','r')
lines = f.readlines()

f.close()

print("file line# : ",len(lines))

count = 0
for i in range(0,len(lines)):
    temp = lines[i].split()
    count += len(temp)

print("file word# : ",count)
