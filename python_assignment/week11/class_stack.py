#-*- coding: utf-8 -*-
# -*- coding: cp949 -*-
#week.10
class Stack:
    def __init__(self):
        self.a = []
    def push(self,x):
        self.a.append(x)
    def pop(self):
        self.a.pop()
    def top(self):
        if(len(self.a)>0):
            return self.a[-1]
    def size(self):
        return len(self.a)

def calc(op,first,second):
    if(op == "-"): 
        return float(first) - float(second)
    if(op == "+"): 
        return float(first) + float(second)
    if(op == "/"):
        return float(first) / float(second)
    if(op == "*"):
        return float(first) * float(second)


def getPriority(op):
    return {'-': 1, '+': 1, '*':2, '/':2}[op]

def is_digit(str): 
    try:
        tmp = float(str)
        return True
    except ValueError:
        return False

if __name__ == "__main__":
    arr = input("input : ")
    #arr = "1 + 2 - 3 * 4 + 5"
    #arr = "2 * 3 - 4 / 2 * 5 - 18 / 9 + 3 * 5 - 16 / 4 "
    # 6 - 10 -2 + 15 - 4
    arr = arr.split()
    num = Stack()
    oper = Stack()


    for i in range(0,len(arr)):
        if(is_digit(arr[i])):
            print("1//숫자 : i : {0:d} arr[i] : {1:s}".format(i, arr[i]))
            if(num.size() == 0 or i == len(arr)-1):
                num.push(float(arr[i]))
                print("1//숫자 push : ", num.top())
            else:
                if(getPriority(arr[i+1]) == getPriority(oper.top())):
                    print("2//현재 숫자 : {0:f} 내다음 기호 : {1:s} oper top : {2:s}".format(float(arr[i]), arr[i+1], oper.top()))
                    # 현재 내 다음 위치한 기호가 oper top 보다 우선순위가 같은 경우
                    # 숫자탑 오펄탑 현재 숫자 -> 연산 실시 -> 숫자 팝 -> 문자 팝 -> 연산결과 숫자에 푸쉬 
                    for j in range(0,len(num.a)):
                        print("num출력 : ",num.a[j])
                    first = num.top() # 숫자스택 top
                    op = oper.top() # 연산자 스택 top
                    second = float(arr[i]) # 현자 숫자
                    result = calc(op, first, second)
                    num.pop()
                    oper.pop()
                    num.push(result)
                    print("2//{0:f} {1:s} {2:f} = {3:f}".format(first,op,second,result))
                    print("2//결과 {0:f} push / num.top() : {1:f}".format(result,num.top()))

                elif(getPriority(arr[i+1]) > getPriority(oper.top())):
                    # 현재 내 다음 위치한 기호가 oper top 보다 우선순위가 높은 경우
                    # 그냥 숫자만 푸쉬 하면 됨
                    num.push(float(arr[i]))
                    print("3//숫자 push else : ", num.top())
                    print("3//내 다음 기호 : {0:s} oper top : {1:s}".format(arr[i+1],oper.top()))
                else:
                    # 현재 내 다음 위치한 기호가 oper top 보다 우선순위가 낮은 경우
                    num.push(float(arr[i]))
                    while(num.size()!=1):
                        second = num.top()
                        num.pop()
                        first = num.top()
                        num.pop()
                        op = oper.top()
                        oper.pop()
                        print("4//numtop1 : {0:f} numtop1밑 : {1:f} op : {2:s}".format(second,first,op))
                        result = calc(op,first,second)
                        num.push(result)
                        print("4//{0:f} {1:s} {2:f} = {3:f}".format(first, op, second, result))
                        print("4//결과 {0:f} push / num.top() : {1:f}".format(result, num.top()))
        else:
            oper.push(arr[i])
            print("문자 : i : {0:d} arr[i] : {1:s} <=> oper.top() : {2:s}".format(i,arr[i], oper.top()))
            print("문자 push : ", oper.top())
            for j in range(0, len(oper.a)):
                print("oper출력 : ", oper.a[j])

    if(num.size() > 1):
        while(num.size() != 1):
            print("나머지계산")
            second = num.top()
            num.pop()
            first = num.top()
            num.pop()
            result = calc(oper.top(), first, second)
            print("{0:f} {1:s} {2:f} = {3:f}".format(first, oper.top(), second, result))
            oper.pop()
            num.push(result)
    if(oper.size() == 0):
        print("답 : {0:f}".format(num.top()))
