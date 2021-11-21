import js2py
context=js2py.EvalJs()
c=open('strToBinary.js',encoding='utf-8').read()
d="""
function add(num1, num2) {
    return num1 + num2;
}
function add(num1) {
    return num1;
}
"""
# print(c)
context.execute(c)
res=context.cipher()
print(res)


