import execjs, os

ctx = execjs.compile("""

const jsdom = require("jsdom");
const { JSDOM } = jsdom;
const { window } = new JSDOM('<!doctype html><html><body></body></html>').window;
const $ = require('jQuery')(window);


       function add(x, y) {
               return x + y;
          }
""", cwd=r'D:\nodejs\node_global\node_modules')  # 获取代码编译完成后的对象
print(ctx.call("add", 1, 2))  # 3
# print(ctx.eval("add({0},{1})").format(1,2)) # 报错
print(ctx.eval('add("{0}", "{1}")').format("1", "2"))  # 12

print(execjs.get().name)

os.environ["EXECJS_RUNTIME"] = "Node"
print(execjs.get().name)
execjs.eval("1 + 2")
