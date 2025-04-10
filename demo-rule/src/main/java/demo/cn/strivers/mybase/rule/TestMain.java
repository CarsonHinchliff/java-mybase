package demo.cn.strivers.mybase.rule;

import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Carson
 * @created 2025/4/10 星期四 下午 04:46
 */
public class TestMain {
    public static void main(String[] args) {
        // 创建一个变量上下文
        Map<String, Object> vars = new HashMap<>();
        vars.put("System", System.class); // 添加 System 类
        vars.put("a", "abc");
        vars.put("testMain", new TestMain());
        vars.put("testMain1", TestMain.class);

        // 编译表达式
        Serializable compiledExpression = MVEL.compileExpression(
                "System.out.println('Hello, MVEL!');" +
                "testMain.sayHello(a);" +
                        "testMain1.sayHello1(a);");

        // 执行编译后的表达式
        MVEL.executeExpression(compiledExpression, vars);
    }

    public void sayHello(String name){
        System.out.println("Hello World!" + name);
    }

    public static void sayHello1(String name){
        System.out.println("Hello1 World!" + name);
    }
}
