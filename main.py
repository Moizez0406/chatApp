mySettup = input("Enter your name: ")
myUser = list()

while(mySettup != "exit"):
    print("Hello, " + mySettup + "!")
    mySettup = input("Enter your name: ")
    myUser.append(mySettup)
