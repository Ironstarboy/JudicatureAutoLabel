from queue import Queue
from threading import Thread
import time

class Student:
    def __init__(self, name):
        self.name = name

    def speak(self):
        print("{}：到！".format(self.name))


class Teacher:
    def __init__(self, queue):
        super().__init__()
        self.queue=queue

    def call(self, student_name):
        if student_name == "exit":
            print("点名结束，开始上课..")
        else:
            print("老师：{}来了没？".format(student_name))
            # 发送消息，要点谁的名
        self.queue.put(student_name)

class CallManager(Thread):
    def __init__(self, queue):
        super().__init__()
        self.students = {}
        self.queue = queue

    def put(self, student):
        self.students.setdefault(student.name, student)

    def run(self):
        while True:
            # 阻塞程序，时刻监听老师，接收消息
            student_name = queue.get()
            if student_name == "exit":
                break
            elif student_name in self.students:
                self.students[student_name].speak()
            else:
                print("老师，咱班，没有 {} 这个人".format(student_name))

queue = Queue()
teacher = Teacher(queue=queue)

s1 = Student(name="小明")
s2 = Student(name="小亮")

cm = CallManager(queue)
cm.put(s1)
cm.put(s2)
cm.start()

print('开始点名~')
teacher.call('小明')
time.sleep(1)
teacher.call('小亮')
time.sleep(1)
teacher.call("exit")
