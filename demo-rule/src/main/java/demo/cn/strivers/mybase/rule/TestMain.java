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

        // 编译表达式
        Serializable compiledExpression = MVEL.compileExpression("System.out.println('Hello, MVEL!');");

        // 执行编译后的表达式
        MVEL.executeExpression(compiledExpression, vars);
    }
}
